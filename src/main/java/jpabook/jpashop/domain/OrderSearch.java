package jpabook.jpashop.domain;

import lombok.Data;


// 검색 기능을 위해 만들어준 클래스
// 검색 조건 : "회원 이름" + "주문 상태" -> 이 두가지이므로 Order 테이블과 Member 테이블을 조인해야함

// OrderRepository 에서 구현한 동적쿼리의 4가지 옵션
// 1. 회원 이름 o + 주문 상태 o (즉, 해당 회원이 주문한 주문들 중 해당 상태의 주문만 조회)
// 2. 회원 이름 o + 주문 상태 x (즉, 해당 회원이 주문한 모든 주문들 조회)
// 3. 회원 이름 x + 주문 상태 o (즉, 해당 주문 상태의 모든 주문들 조회)
// 4. 회원 이름 x + 주문 상태 x (즉, 모든 주문 다 검색)
@Data
public class OrderSearch {

    private String memberName; // 회원 이름
    private OrderStatus orderStatus; // 주문 상태
}
