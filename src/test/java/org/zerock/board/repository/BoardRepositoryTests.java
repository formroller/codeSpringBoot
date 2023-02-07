package org.zerock.board.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class BoardRepositoryTests {
    @Autowired
    private BoardRepository boardRepository;

    @Test // 객체 추가 - 한 명의 사용자가 하나의 게시물 등록
    public void insertBoard(){
        IntStream.rangeClosed(1,100).forEach(i->{
            Member member = Member.builder().email("user"+i+"@aa.com").build();

            Board board = Board.builder()
                    .title("Title..."+i)
                    .content("Content......"+i)
                    .writer(member)
                    .build();

            boardRepository.save(board);
        });
    }

    @Transactional // board 테이블 연결 후 member 테이블 로딩 위해 추가
    @Test // - Member를 @ManyToOne으로 참조하는 Board 조회
    public void testRead1(){
        Optional<Board> result = boardRepository.findById(100L); // DB에 존재하는 번호

        Board board = result.get();

        System.out.println(board);
        System.out.println(board.getWriter());
    }

    @Test // 연관 관계 있는 지연 로딩 테스트
    public void testReadWithWriter(){
        Object result = boardRepository.getBoardWithWriter(100L);

        Object [] arr = (Object [])result;

        System.out.println("-".repeat(50));
        System.out.println(Arrays.toString(arr));
    }

    @Test // 연관 관계 없는 지연 로딩 테스트
    public void testGetBoardWithReply(){
        List<Object[]> result = boardRepository.getBoardWithReply(100L);

        for(Object[] arr: result){
            System.out.println(Arrays.toString(arr));
        }
    }

    @Test // board 기준 groupby 처리
    public void testWithReplyCount(){
        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());
        Page<Object[]> result = boardRepository.getBoardWithReplyCount(pageable);
        result.get().forEach(row ->{
            Object[] arr = (Object[])row;
            System.out.println(Arrays.toString(arr));
        });
    }

    @Test // 조회 화면 구성(화면 댓글 갯수)
    public void testRead3(){
        Object result = boardRepository.getBoardByBno(100L);
        Object[] arr = (Object[]) result;
        System.out.println(Arrays.toString(arr));
    }

    @Test
    public void testSearch1(){
        boardRepository.search1();
    }

    @Test
    public void testSearch2(){
        boardRepository.search2();
    }

    @Test
    public void testSearchPage(){
        Pageable pageable = PageRequest.of(0,10,Sort.by("bno").descending()
                .and(Sort.by("title").ascending()));

        Page<Object[]> result = boardRepository.searchPage("t", "1", pageable);
    }
}
