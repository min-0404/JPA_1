package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional // 데이터에 변경사항 생기므로 꼭 있어야함 + 롤백 기능도 지원해줌
public class MemberServiceTest {

    @Autowired MemberRepository memberRepository; // 필드 주입 (테스트 할 땐 써도 됨)
    @Autowired MemberService memberService; // 필드 주입

    @Test
    public void 회원가입() throws Exception{
        // Given
        Member member = new Member();
        member.setName("minseok");
        // When
        Long savedId = memberService.join(member);

        // Then
        Assertions.assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test
    public void 중복_회원_예외() throws Exception{
        // Given
        Member member1 = new Member();
        member1.setName("kim");
        Member member2 = new Member();
        member2.setName("kim");

        // When
        memberService.join(member1);
        try {
            memberService.join(member2); // 예외가 발생해야 함 !!!
        } catch (IllegalStateException e){
            return;
        }

        // Then
        Assertions.fail("예외가 발생해야 한다");

    }

}
