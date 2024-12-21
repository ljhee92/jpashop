package jpabook.jpashop.domain.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional // Test 에서는 Rollback 처리 함
class MemberServiceTest {
    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
//    @Rollback(value = false) // DB insert 확인 가능
    public void 회원가입() throws Exception {
        // given
        Member member = new Member();
        member.setName("juhee");
        
        // when
        Long savedId = memberService.join(member);

        // then
        assertThat(member).isEqualTo(memberRepository.findOne(savedId));
    }
    
    @Test
    public void 중복_회원_예외() throws Exception {
        // given
        Member member1 = new Member();
        member1.setName("juhee");

        Member member2 = new Member();
        member2.setName("juhee");
        
        // when
        memberService.join(member1);

        // then
        assertThatThrownBy(() -> {
            memberService.join(member2); // 예외가 발생해야 한다.
        }).isInstanceOf(IllegalStateException.class);
    }
}