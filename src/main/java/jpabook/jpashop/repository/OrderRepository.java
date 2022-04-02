package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Repository // 빈 등록
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order){
        em.persist(order);
    }

    public Order findOne(Long id){
        return em.find(Order.class, id);
    }

    public List<Order> findAllByString(OrderSearch orderSearch){ // 동적 쿼리로 구현(검색 조건 : 회원 이름 + 주문 상태)

        String jpql = "select o From Order o join o.member m"; // Order 테이블과 Member 테이블의 조인
        boolean isFirstCondition = true;

        if(orderSearch.getOrderStatus() != null){ // 조건 1 : 만약 검색 조건에 주문 상태가 선택되었다면
            if(isFirstCondition){
                jpql += "where";
                isFirstCondition = false;
            }
            else{
                jpql += "and";
            }
            jpql += "o.status = :status";
        }

        if(StringUtils.hasText(orderSearch.getMemberName())){ // 조건 2 : 만약 검색 조건에 회원 이름이 선택되었다면
            if(isFirstCondition){
                jpql += "where";
                isFirstCondition = false;
            }
            else{
                jpql += "and";
            }
            jpql += "m.name like :name";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class).setMaxResults(1000);

        if(orderSearch.getOrderStatus() != null){ // 쿼리 바인딩 처리 필수 (쿼리에 :status 사용했으니깐)
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if(StringUtils.hasText(orderSearch.getMemberName())){ // 쿼리 바인딩 처리 필수 (쿼리에 :name 사용했으니깐)
            query = query.setParameter("name", orderSearch.getMemberName());
        }
        return query.getResultList();
    }
}
