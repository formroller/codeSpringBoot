package org.zerock.board.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Reply;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class ReplyRepositoryTests {
    @Autowired
    private ReplyRepository replyRepository;

    @Test // 객체 생성 - 임의의 게시글 대상으로 댓글(300개) 추가
    public void insertReply(){

        IntStream.rangeClosed(1,300).forEach(i->{
            long bno = (long)(Math.random()*100)+1; // 1~100 사이의 번호 추가

            Board board = Board.builder().bno(bno).build();

            Reply reply = Reply.builder()
                    .text("Reply is...."+i)
                    .board(board)
                    .replyer("guest")
                    .build();

            replyRepository.save(reply);
        });
    }

    @Test // ManyToOne 관계 조인 테스트
    public void readReply(){
        Optional<Reply> result = replyRepository.findById(1L);

        Reply reply = result.get();

        System.out.println(reply);
        System.out.println(reply.getBoard());
    }
    @Test // 게시글에 달린 댓글 번호 및 내용 등
    public void testListByBoard(){
        List<Reply> replyList = replyRepository.getRepliesByBoardOrderByRno(Board.builder().bno(97L).build());

        replyList.forEach(reply -> System.out.println(reply));
    }
}
