package jpabook.jpashop.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "order_item_id")
@Data
public class OrderItem {

    @Id @GeneratedValue
    @Column(name= "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order;


    private Item item;

    private int orderPrice;

    private int count;
}
