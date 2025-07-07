package com.concertmania.domain.concert.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SeatService {


    // 기타 좌석 관련 비즈니스 로직 추가
}
