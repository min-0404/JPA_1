package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 3가지 상속 전략 중에 "single table" 전략 사용하자(한 개 테이블에 다 때려넣기)
@DiscriminatorColumn(name = "dtype") // 다 때려넣은 구현체들은 dtype 이라는 키워드로 구분하자
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<Category>();

    // <비즈니스 로직>
    // "재고 증가"
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    // "재고 감소"
    public void removeStock(int quantity){
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0){ // 만약 해당 아이템의 재고가 0 미만이 되어버리면
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
