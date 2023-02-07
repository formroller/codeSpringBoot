package org.zerock.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO {
    /*화면에서 전달되는 목록 관련된 데이터 DTO
    * > JPA쪽에서 사용하는 Pageable 타입의 객체 생성이 목적
    * */

    private int page;
    private int size;
    /*서버측 검색 처리*/
    private String type;
    private String keyword;

    public PageRequestDTO(){
        this.page=1;
        this.size=50;
    }

    public Pageable getPageable(Sort sort){
        return PageRequest.of(page-1, size, sort);
    }
}
