package jpabook.jpashop.controller;


import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.web.MemberForm;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Data
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model){
        model.addAttribute("memberForm", new MemberForm()); // 텅 빈 MemberForm 객체 전달
        return "members/createMemberForm";
    }

    @PostMapping("/members/new") // createMemberForm.html 에서 입력한 데이터를 받아주는 컨트롤러
    public String create(@Valid MemberForm memberForm, BindingResult result){

        if(result.hasErrors()){ // 만약 데이터 입력에 오류 있으면 다시 페이지 리로딩해줌
            return "members/createMemberForm";
        }

        Address address = new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipcode()); // 전달받은 MemberForm 에서 데이터 쏙 뺴먹기
        Member member = new Member();
        //member.setId() 는 필요없음 : @GeneratedValue 로 설정해놔서 자동 생성 됨
        member.setName(memberForm.getName()); // 전달받은 MembeForm 에서 데이터 쏙 빼먹기
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model){
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }

}
