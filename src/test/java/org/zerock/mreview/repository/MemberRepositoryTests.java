package org.zerock.mreview.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mreview.entity.Member;

import java.util.stream.IntStream;

@SpringBootTest
public class MemberRepositoryTests {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    public void insertMembers(){
        IntStream.rangeClosed(1,100).forEach(i->{
            Member member = Member.builder()
                    .email("r"+i+"@zerock.org")
                    .pw("1111")
                    .nickname("review"+i).build();

            memberRepository.save(member);
        });
    }

    @Test // 회원 삭제 문제와 트랜잭션 처리
    @Transactional // 리뷰(FK) 삭제 후 멤버(PK) 삭제 처리
    @Commit
    public void testDeleteMember(){
        Long mid = 3L; // member의 mid
        Member member = Member.builder().mid(mid).build();

        // 기존 - session error
        //memberRepository.deleteById(mid);
        //reviewRepository.deleteByMember(member);

        reviewRepository.deleteByMember(member);
        memberRepository.deleteById(mid);
    }
}
