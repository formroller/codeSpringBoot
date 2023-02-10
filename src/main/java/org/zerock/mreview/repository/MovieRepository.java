package org.zerock.mreview.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.mreview.entity.Movie;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    // N + 1 문제 발생(1번의 쿼리로 N개 데이터 가져왔으나, 이를 처리하기 위해 추가적인 쿼리 N개 수행되는 상황)
//    @Query("select m, max(mi), avg(coalesce(r.grade,0)), count(distinct r) from "+
//            " Movie m "
//            + " left join MovieImage mi on mi.movie = m "
//            + " left join Review r on r.movie = m group by m ")

    // JPQL 활용한 N+1 처리
    @Query("select m, min(mi.inum), min(mi.imgName) , avg(coalesce(r.grade,0)), count(distinct r.reviewnum) from Movie m " +
            "left outer join MovieImage mi on mi.movie = m "+
            "left outer join Review r on r.movie = m " +
            "where mi.inum in (select min(mi2.inum) from MovieImage mi2 group by mi2.movie) " +
            "group by m.mno, m.title, m.regDate, m.modDate, mi.movie , r.movie")
    Page<Object[]> getListPage(Pageable pageable); // 페이지 처리


    @Query("select m, mi ,avg(coalesce(r.grade,0)),  count(r)" +
            " from Movie m left outer join MovieImage mi on mi.movie = m " +
            " left outer join Review  r on r.movie = m "+
            " where m.mno = :mno group by mi")
    List<Object[]> getMovieWithAll(Long mno); // 특정 영화 조회
}
