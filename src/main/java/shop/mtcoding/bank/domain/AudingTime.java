package shop.mtcoding.bank.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

@Getter
@MappedSuperclass // 자식이 이친구를 상속할건데, 자식은 이 친구를 테이블에 컬럼으로 만들어라.
@EntityListeners(AuditingEntityListener.class)
public abstract class AudingTime {

    @LastModifiedDate // Insert, Update 시에 현재 시간 들어감
    @Column(nullable = false)
    protected LocalDateTime updatedAt;

    @CreatedDate // Insert 시에 현재 시간 들어감
    @Column(nullable = false)
    protected LocalDateTime createdAt;

}
