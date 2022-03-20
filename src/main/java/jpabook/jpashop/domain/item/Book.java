package jpabook.jpashop.domain.item;

import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Data
@DiscriminatorValue("B") // dtype 이용한 구분을 위해서
public class Book extends Item{

    private String author;

    private String isbn;
}
