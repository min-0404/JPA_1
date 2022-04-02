package jpabook.jpashop.web;

import lombok.Data;

// 컨트롤러와 뷰 간에 데이터 전달만을 위해 임시적으로 사용하는 객체
@Data
public class BookForm {

    private Long id;
    private String name;
    private int price;
    private int stockQuantity;
    private String author;
    private String isbn;
}
