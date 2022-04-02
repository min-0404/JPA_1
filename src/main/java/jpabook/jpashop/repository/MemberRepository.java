package jpabook.jpashop.repository;


import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository // 빈 등록
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em; // 선언만 해줘도 @RequiredArgsConstructor 가 알아서 생성자만들면서 빈 주입 해줌

    public void save(Member member){
         em.persist(member);
    }
    public Member findOne(Long id){
        return em.find(Member.class, id);
    }
    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }
    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name= :name", Member.class).setParameter("name", name).getResultList();
        // 주의 ) 쿼리 파라미터로 받은 name 을 쿼리에서 :name 으로 사용했으므로 "쿼리 바인딩" 꼭 해줘야함 !!
    }

}
