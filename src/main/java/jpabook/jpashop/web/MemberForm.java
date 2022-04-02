package jpabook.jpashop.web;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

// 컨트롤러와 뷰 간에 데이터 전달만을 위해 임시적으로 사용하는 객체
@Data
public class MemberForm {

    @NotEmpty(message = "회원 이름은 필수입니다")
    private String name;

    private String city;
    private String street;
    private String zipcode;
}
