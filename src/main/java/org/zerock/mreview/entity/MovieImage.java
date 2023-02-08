package org.zerock.mreview.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(exclude = "movie") // 연관 관계 주의
public class MovieImage {
    // 단방향 참조 처리(@ManyToOne)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inum;

    private String uuid;  // java.util.UUID 사용해 고유 번호 생성

    private String imgName;

    private String path;  // 이미지 저장 경로

    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;  // FK

}
