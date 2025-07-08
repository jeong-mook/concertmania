package com.concertmania.domain.concert.repository;

import com.concertmania.domain.concert.model.Concert;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ConcertSpecifications {

    public Specification<Concert> nameContains(String name) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase());
    }

    public Specification<Concert> locationContains(String location) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("location")), "%" + location.toLowerCase());
    }

    public Specification<Concert> concertStartAt(LocalDateTime concertStartAt) {
        return (root, query, cb) ->
                cb.greaterThan(root.get("concertDate"), concertStartAt);
    }

    public Specification<Concert> concertEndAt(LocalDateTime concertEndAt) {
        return (root, query, cb) ->
                cb.lessThan(root.get("concertDate"), concertEndAt);
    }

    public Specification<Concert> concertBeteween(LocalDateTime start, LocalDateTime end) {
        return (root, query, cb) ->
                cb.between(root.get("concertDate"), start, end);
    }

    public Specification<Concert> openStartAt(LocalDateTime openStartAt) {
        return (root, query, cb) ->
                cb.greaterThan(root.get("openAt"), openStartAt);
    }

    public Specification<Concert> openEndAt(LocalDateTime openEndAt) {
        return (root, query, cb) ->
                cb.lessThan(root.get("openAt"), openEndAt);
    }

    public Specification<Concert> openBetweenAt(LocalDateTime start, LocalDateTime end) {
        return (root, query, cb) ->
                cb.between(root.get("openAt"), start, end);
    }
}
