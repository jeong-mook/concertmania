package com.concertmania.domain.concert.controller;

import com.concertmania.domain.concert.dto.ConcertCreateRequest;
import com.concertmania.domain.concert.dto.ConcertDto;
import com.concertmania.domain.concert.dto.UpdateConcertRequestDto;
import com.concertmania.domain.concert.service.ConcertService;
import com.concertmania.domain.location.service.LocationService;
import com.concertmania.global.dto.ApiResult;
import com.concertmania.global.dto.PageResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/admin/concerts")
public class AdminConcertController {

    private final ConcertService concertService;
    private final LocationService locationService;


    public AdminConcertController(ConcertService concertService, LocationService locationService) {
        this.concertService = concertService;
        this.locationService = locationService;
    }

    // 관리자만 콘서트 등록 가능
    @PostMapping
    public ResponseEntity<ApiResult<ConcertDto.Response>> createConcert(@RequestBody @Valid ConcertDto.createRequest request) {
        // 콘서트 생성 로직
        ConcertDto.Response response = concertService.createConcert(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResult.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<ConcertDto.Response>> updateConcert(@PathVariable Long id,
                                                                        @Valid @RequestBody UpdateConcertRequestDto updateRequest) {
        ConcertDto.Response response = concertService.updateConcert(id, updateRequest)
                .map(ConcertDto.Response::from)
                .orElseGet(ConcertDto.Response::empty);

        return ResponseEntity.ok(ApiResult.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConcert(@PathVariable Long id) {
        concertService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<ApiResult<PageResponse<ConcertDto.Response>>> listConcerts(Pageable pageable) {
        Page<ConcertDto.Response> concerts = concertService.findAll(pageable);
        return ResponseEntity.ok(ApiResult.success(PageResponse.from(concerts)));
    }

    //TODO API Response 포맷 협의후 변경 필요.
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<ConcertDto.Response>> getConcert(@PathVariable Long id) {
        ConcertDto.Response response = concertService.findById(id)
                .map(ConcertDto.Response::from)
                .orElseGet(ConcertDto.Response::empty);
        return ResponseEntity.ok(ApiResult.success(response));
    }

}
