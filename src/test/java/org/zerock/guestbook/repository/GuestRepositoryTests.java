package org.zerock.guestbook.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.QGuestbook;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class GuestRepositoryTests {
    @Autowired
    private GuestbookRepository guestbookRepository;

    @Test
    public void insertDummies() {
        IntStream.rangeClosed(1, 300).forEach(i -> {
            Guestbook guestbook = Guestbook.builder()
                    .title("Title......" + i)
                    .content("Content....." + i)
                    .writer("user" + (i % 10))
                    .build();
            System.out.println(guestbookRepository.save(guestbook));
        });
    }

    @Test // 수정시간 테스트
    public void updateTest() {
        Optional<Guestbook> result = guestbookRepository.findById(601L); // 존재하는 번호로 테스트

        if (result.isPresent()) {
            Guestbook guestbook = result.get();

            guestbook.changeTitle("Changed Title!!!!");
            guestbook.changeContent("Changed Content!!!");

            guestbookRepository.save(guestbook);
        }
    }

    @Test //301-600번 더미 데이터 삭제(실패)
    public void deleteTest() {
        IntStream.rangeClosed(301, 601).forEach(i -> {
            Guestbook guestbook = new Guestbook();
            guestbookRepository.deleteById(guestbook.getGno());
        });
    }

    @Test // 단일 항목 검색 테스트
    public void testQuery1(){
        Pageable pageable = PageRequest.of(0,10, Sort.by("gno").descending());
        QGuestbook qGuestbook = QGuestbook.guestbook; // 동적 처리 위해 Q도메인 클래스 얻어온다.(title, content 같은 필드를 변수로 활용 가능)
        String keyword = "1";
        BooleanBuilder builder = new BooleanBuilder(); // where문에 넣을 조건이 담길 컨테이너
        BooleanExpression expression = qGuestbook.title.contains(keyword); // 원하는 조건은 필드 값과 같이 결합해 생성
        builder.and(expression); // 만들어진 조건은 where문에 and 또는 or 키워드와 결합
        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable); // repository에 추가된 QuerydslPredicateExcutor 인터페이스의 findall() 사용 가능
    }

    @Test // 다중 항목 검색 테스트
    public void testQuery2(){
        Pageable pageable = PageRequest.of(0,10,Sort.by("gno").descending());
        QGuestbook qGuestbook = QGuestbook.guestbook;
        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder();
        BooleanExpression exTitle = qGuestbook.title.contains(keyword);
        BooleanExpression exContent = qGuestbook.content.contains(keyword);
        BooleanExpression exAll = exTitle.or(exContent); // 조건 결합
        builder.and(exAll); // 조건 결합 포함
        builder.and(qGuestbook.gno.gt(0L)); // gno가 0보다 크다(gt) 조건 추가

        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);
        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });
    }

}
