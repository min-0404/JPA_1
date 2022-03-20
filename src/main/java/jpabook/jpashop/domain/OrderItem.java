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

    // 생성 매서드
    public static OrderItem createOrderItem(Item item, int orderPrice, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count); // 해당 Item 의 재고에서 count 수량 만큼 감소시켜줌
        return orderItem;
    }

    // 비즈니스 로직
    public void cancel(){
        getItem().addStock(count); // 해당 Item 의 재고에서 count 수량 만큼 다시 증가시켜줌
    }

    // 조회 로직
    public int getTotalPrice(){
        return getOrderPrice() * getCount();
    }
}
