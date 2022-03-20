package jpabook.jpashop.domain.item;

import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Data
@DiscriminatorValue("A") // dtype 이용한 구분을 위해서
public class Album extends Item{

    private String artist;

    private String etc;
}
