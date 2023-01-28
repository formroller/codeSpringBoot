package org.zerock.guestbook.dto;
/* 화면에서 필요한 결과 처리
* Page<Entity>의 엔티티 객체들을 DTO 객체로 변환해 자료구조로 담아야 한다.
* 화면 출력에 필요한 페이지 정보 구성
 */

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResultDTO<DTO, EN> {
    private List<DTO> dtoList; // DTO 리스트
    private int totalPage; // 총 페이지 번호
    private int page; // 현재 페이지 번호
    private int size; // 사이즈
    private int start, end; // 페이지 시작,끝 번호
    private boolean prev, next; // 이전,다음
    private List<Integer> pageList; // 페이지 번호 목록

    public PageResultDTO(Page<EN> result, Function<EN, DTO> fn) {
        // Page<EN> - 해당 타입 생성자 (제네릭 타입)
        // Function<EN,DTO> - 엔티티 객체들을 DTO 변환

        dtoList = result.stream().map(fn).collect(Collectors.toList());
        totalPage = result.getTotalPages();
        makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable) {
        this.page = pageable.getPageNumber() + 1; // 0부터 시작하므로 1 추가
        this.size = pageable.getPageSize();

        //temp end page
        int tempEnd = (int) (Math.ceil(page/10.0)) * 10;

        start = tempEnd - 9;

        prev = start>1;

        end = totalPage > tempEnd ? tempEnd : totalPage;

        next = totalPage > tempEnd;

        pageList = IntStream.rangeClosed(start,end).boxed().collect(Collectors.toList());
    }
}
