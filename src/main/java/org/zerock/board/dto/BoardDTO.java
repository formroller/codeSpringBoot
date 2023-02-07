package org.zerock.board.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/*DTO는 데이터를 기준으로 작성되므로 엔티티 클래스 구성과 일치하지 않는 경우가 많다.*/
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {
    private Long bno;
    private String title;
    private String content;
    private String writerEmail; // 작성자 이메일
    private String writerName; // 작성자 이름
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private int replyCount; // 댓글 수
}
