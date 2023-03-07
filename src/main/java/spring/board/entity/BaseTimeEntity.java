package spring.board.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 공통 정보
 * - craetedDate, modifiedDate 만 사용하는 User 테이블을 만족시키기 위해 생성
 */
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass //진짜 상속이 아닌 속성값(createDate, updateDate)만 테이블에서 같이 쓸 수 있게끔!
@Getter
public class BaseTimeEntity {
    @CreatedDate
    @Column(updatable = false, name = "created_date")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        modifiedDate = now;
    }

    @PreUpdate
    public void preUpdate() {
        modifiedDate = LocalDateTime.now();
    }
}
