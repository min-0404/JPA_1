package jpabook.jpashop.domain;


import lombok.Data;
import org.aspectj.weaver.ast.Or;
import org.hibernate.annotations.LazyToOne;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // cascade : Order 에 persist 해주면 자동으로 OrderItem 도 persist 됨
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY) // cascade : Order 에 persist 해주면 자동으로 Delivery 도 persist 됨
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;


    // 연관 관계 매서드 (Order 와 관계 맺고 있는 엔티티들도 설정해줘야함)
    public void setMember(Member member){ // Order 와 Member 는 양방향이므로, 둘 다 셋팅해주는 식으로...
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem){ // Order 와 OrderItem 은 양방향이므로, 둘 다 셋팅해주는 식으로...
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){ // Order 와 Delivery 는 양방향이므로, 둘 다 셋팅해주는 식으로...
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    // Order 생성 매서드
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem x : orderItems){
            order.addOrderItem(x);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    // 비즈니스 로직
    // 주문 취소
    public void cancel(){
        if(delivery.getStatus() == DeliveryStatus.COMP){ // case1 : 이미 배송완료된 경우
            throw new IllegalStateException("이미 배송된 상품은 취소가 불가능합니다");
        }

        this.setStatus(OrderStatus.CANCEL); // case2 : 아직 주문 상태인 경우 -> OrderItem 리스트의 상품들 취소해줌
        for(OrderItem x : orderItems){
            x.cancel();
        }
    }

    // 조회 로직
    // 총 가격 조회
    public int getTotalPrice(){
        int totalPrice = 0;
        for(OrderItem x : orderItems){
            totalPrice += x.getTotalPrice();
        }
        return totalPrice;
    }




}
