package com.concertmania.domain.concert.repository;

import com.concertmania.domain.concert.model.Concert;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ConcertRepository extends JpaRepository<Concert, Long>, JpaSpecificationExecutor<Concert> {

    /**
     * 콘서트 조회할 때 비관적 쓰기 락을 겁니다. (Row Lock)
     * 이 트랜잭션이 커밋되거나 롤백될 때까지 다른 트랜잭션은 이 레코드에 접근할 수 없습니다.
     * * JPA에게 SELECT ... FOR UPDATE   SQL을 생성하도록 지시하는 1차 핵심 비즈니스 로직.
     * @param id 조회할 콘서트의 ID
     * @return 락이 걸린 콘서트 Optional 객체
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Concert c where c.id = :id")
    Optional<Concert> findByIdWithPessimisticLock(Long id);

    Page<Concert> findByOpenAtBefore(LocalDateTime openAtBefore, Pageable pageable);
    Page<Concert> findByOpenAtAfter(LocalDateTime openAtBefore, Pageable pageable);

}
