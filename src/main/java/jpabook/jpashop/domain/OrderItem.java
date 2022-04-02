package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Data;
import org.aspectj.weaver.ast.Or;

import javax.persistence.*;

@Entity
@Data
@Table(name = "order_item")
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int orderPrice;

    private int count;

    // 생성 매서드 : OrderItem 은 워낙 복잡하기 때문에 생성 매서드 자체를 만들어주는 것이 좋다
    public static OrderItem createOrderItem(Item item, int orderPrice, int count){ // static 인 것이 핵심 !!
        OrderItem orderItem = new OrderItem();
        // 1. Item 설정
        orderItem.setItem(item);
        // 2. orderPrice 설정
        orderItem.setOrderPrice(orderPrice);
        // 3. count 설정
        orderItem.setCount(count);
        // 4. 재고 감소 설정
        item.removeStock(count); // 중요!! : 해당 Item 의 재고에서 count 수량 만큼 감소시켜줌
        return orderItem;
    }

    // <비즈니스 로직>
    // "취소 로직" : OrderItem 객체와 연결된 Item 의 재고를 늘려주는 것이 핵심
    public void cancel(){
        getItem().addStock(count); // 중요!! : 해당 Item 의 재고에서 count 수량 만큼 다시 증가시켜줌
    }

    // "조회 로직" : 단순히 OrderItem 의 총 액수 계산
    public int getTotalPrice(){
        return getOrderPrice() * getCount();
    }
}
