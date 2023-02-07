package org.zerock.board.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "writer")
public class Board extends BaseEntity{
    @Id // member-email (PK) - bno(FK)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;
    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY) // 연관관계 지정(다대일) {fetch=FetchType.Lazy (지연 로딩)}
    private Member writer; // 연관 관계에서는 FK 먼저 해석하면 편하다

    public void changeTitle(String title) { this.title =title; }
    public void changeContent(String content){ this.content = content; }

}
