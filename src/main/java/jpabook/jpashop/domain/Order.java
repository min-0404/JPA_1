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

    // Order 생성 매서드 : 주문은 워낙 복잡하기 때문에 생성 매서드 자체를 만들어주는 것이 좋다 -> 구현해놓은 연관관계 매서드 적극 활용
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){ // static 인 것이 핵심 !!
        Order order = new Order();
        // 1. Member 설정
        order.setMember(member);
        // 2. Delivery 설정
        order.setDelivery(delivery);
        // 3. OrderItem 설정
        for(OrderItem x : orderItems){
            order.addOrderItem(x);
        }
        // 4. OrderStatus 설정 : 일단 ORDER 를 기본으로
        order.setStatus(OrderStatus.ORDER);
        // 5. OrderDate 설정 : 현재 날짜로
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    // <비즈니스 로직>
    // "주문 취소" : Order 객체의 orderItems 배열의 OrderItem 객체들을 하나하나 취소해주고 재고를 원상태로 돌려놓는 것이 핵심
    public void cancel(){
        if(delivery.getStatus() == DeliveryStatus.COMP){ // case1 : 이미 배송완료된 경우
            throw new IllegalStateException("이미 배송된 상품은 취소가 불가능합니다");
        }

        this.setStatus(OrderStatus.CANCEL); // case2 : 아직 주문 상태인 경우 -> OrderItem 리스트의 상품들 취소해줌
        for(OrderItem x : orderItems){
            x.cancel(); // OrderItem 클래스에서 정의한 cancel 함수 활용
        }
    }

    // 조회 로직
    // "총 가격 조회" : Order 객체의 orderItems 배열에 있는 OrderItem 객체들의 가격을 모두 더해주는 것이 핵심
    public int getTotalPrice(){
        int totalPrice = 0;
        for(OrderItem x : orderItems){
            totalPrice += x.getTotalPrice(); // OrderItem 클래스에서 정의한 getTotalPrice 함수 활용
        }
        return totalPrice;
    }




}
