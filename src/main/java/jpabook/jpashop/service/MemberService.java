package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // 빈 등록
public class MemberService {

    private final MemberRepository memberRepository; // @RequiredArgsConstructor 쓰면 사실 굳이 생성자 안만들어줘도 알아서 해줌 : 귀찮지만 그냥 예시로 생성자 주입 해봄
    @Autowired
    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = false) // 단순 조회가 아닌 저장이므로 readOnly = true 절대 설정하면 안됨
    public Long join(Member member){
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member){
        List<Member> findMembers = memberRepository.findByName(member.getName()); // 해당 이름의 멤버가 있는지 조회해봄
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }
    }
    @Transactional(readOnly = true)  // 회원 조회 함수의 성능 최적화를 위해 readOnly = true 로 설정
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }

    @Transactional(readOnly = true)  // 회원 조회 함수의 성능 최적화를 위해 readOnly = true 로 설정
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }


}
