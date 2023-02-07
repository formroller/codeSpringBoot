package org.zerock.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.board.entity.Board;
import org.zerock.board.repository.search.SearchBoardRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>, SearchBoardRepository {
    @Query("select b, w from Board b left join b.writer w where b.bno =:bno")
    Object getBoardWithWriter(@Param("bno") Long bno); // board 사용하지만 member 같이 조회하는 상황

    @Query("SELECT b, r FROM Board b LEFT JOIN Reply r ON r.board = b WHERE b.bno = :bno") // 연관 관계 없을 경우 left join 쿼리에 on 추가
    List<Object[]> getBoardWithReply(@Param("bno") Long bno);

    @Query(value = "select b, w, count(r)" +
    " from Board b" +
    " left join b.writer w" +
    " left join Reply r on r.board = b" +
    " group by b",
    countQuery = "select count(b) from Board b"
    )
    Page<Object[]> getBoardWithReplyCount(Pageable pageable); // 목록 화면에 필요한 데이터

    @Query("select b, w, count(r) " +
            " from Board b left join b.writer w"+
            " left outer join Reply r on r.board = b" +
            " where b.bno = :bno"
    )
    Object getBoardByBno(@Param("bno") Long bno);

}

