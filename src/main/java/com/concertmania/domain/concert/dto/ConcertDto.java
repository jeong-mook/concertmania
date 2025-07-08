package com.concertmania.domain.concert.dto;

import com.concertmania.domain.concert.model.Concert;
import com.concertmania.domain.concert.model.Seat;
import com.concertmania.domain.location.model.Location;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class ConcertDto {

    public record CreateRequest(
    @NotBlank(message = "제목은 필수 입력값입니다.")
    @Size(max = 100, message = "제목은 최대 100자까지 입력 가능합니다.")
    String name,

    @Size(max = 500, message = "설명은 최대 500자까지 입력 가능합니다.")
    String description,

    @NotBlank(message = "장소는 필수 입력값입니다.")
    Location location,

    @NotBlank(message = "콘서트 날짜는 필수 입력값 입니다.")
    LocalDateTime concertDate,

    @NotNull(message = "예약 오픈 시간은 필수 입력값입니다.")
    LocalDateTime openAt
    ) {}

    public record Request(
            String name,
            String location,
            LocalDateTime concertStartDate,
            LocalDateTime concertEndDate,
            LocalDateTime openStartAt,
            LocalDateTime openEndAt
    ){}

    public record Response(
            Long concertId,
            String concertName,
            String locationName,
            String locationAddress,
            LocalDateTime concertDate,
            LocalDateTime openAt
    ) {
        public static Response from (Concert concert) {
            return new Response(
                    concert.getId(),
                    concert.getName(),
                    concert.getLocation().getName(),
                    concert.getLocation().getLocation(),
                    concert.getConcertDate(),
                    concert.getOpenAt()
            );
        }

        public static Response empty () {
            return new Response(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
        }
    }

    private Long id;

    @NotBlank(message = "제목은 필수 입니다.")
    @Size(max = 100, message = "제목은 최대 100자까지 입력 가능합니다.")
    private String name;

    @Size(max = 500, message = "설명은 최대 500자까지 입력 가능합니다.")
    private String description;

    @NotNull(message = "장소는 필수 입력값입니다.")
    private Location location;

    @NotNull(message = "시작 시간은 필수 입력값입니다.")
    private LocalDateTime concertDate;

    @NotNull(message = "예약 오픈 시간은 필수 입력값입니다.")
    private LocalDateTime openAt;

    private LocalDateTime createdAt;


    private List<Seat> seats = new ArrayList<>();

    public static ConcertDto from(Concert concert) {
        return ConcertDto.builder()
                .id(concert.getId())
                .name(concert.getName())
                .description(concert.getDescription())
                .location(concert.getLocation())
                .concertDate(concert.getConcertDate())
                .openAt(concert.getOpenAt())
                .createdAt(concert.getCreatedAt())
                .seats(concert.getSeats())
                .build();
    }
}