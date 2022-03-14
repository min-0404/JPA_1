package jpabook.jpashop.domain;

import java.util.ArrayList;
import java.util.List;

public class Category {

    private Long id;

    private String name;

    private List<Item> items = new ArrayList<>();

    private Category parent;

    private List<Category> child = new ArrayList<>();

    public void addChildCategory(Category child){
        this.child.add(child);
        child.setParent(child);
    }



}
