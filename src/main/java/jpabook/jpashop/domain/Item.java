package jpabook.jpashop.domain;

import jdk.jfr.Category;

import java.util.ArrayList;
import java.util.List;

public abstract class Item {

    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    private List<Category> categories = new ArrayList<Category>();
}
