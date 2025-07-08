package com.concertmania.domain.concert.controller;

import com.concertmania.domain.concert.dto.ConcertDto;
import com.concertmania.domain.concert.service.ConcertService;
import com.concertmania.global.dto.ApiResult;
import com.concertmania.global.dto.PageResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/concerts")
public class ConcertController {

    private final ConcertService concertService;

    public ConcertController(ConcertService concertService) {
        this.concertService = concertService;
    }

    @GetMapping
    public ResponseEntity<ApiResult<PageResponse<ConcertDto.Response>>> getAllConcerts(
            @Valid @RequestBody ConcertDto.Request request,
            Pageable pageable
    ) {
        Page<ConcertDto.Response> concerts = concertService.listConcerts(request, pageable);
        return ResponseEntity.ok(ApiResult.success(PageResponse.from(concerts)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<ConcertDto.Response>> getConcertById(@PathVariable Long id) {
        ConcertDto.Response response = concertService.findById(id)
                .map(ConcertDto.Response::from)
                .orElseGet(ConcertDto.Response::empty);
        return ResponseEntity.ok(ApiResult.success(response));
    }



}
