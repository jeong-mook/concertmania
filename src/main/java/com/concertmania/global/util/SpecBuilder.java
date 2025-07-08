package com.concertmania.global.util;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;


public class SpecBuilder<T> {
    private final List<Specification<T>> andSpecifications;
    private final List<Specification<T>> orSpecifications;

    private SpecBuilder() {
        this.andSpecifications = new ArrayList<>();
        this.orSpecifications = new ArrayList<>();
    }

    public static <T> SpecBuilder<T> create() {
        return new SpecBuilder<>();
    }

    public SpecBuilder<T> and(boolean condition, Specification<T> newSpec) {
        if (condition && newSpec != null) {
            andSpecifications.add(newSpec);
        }
        return this;
    }

    public SpecBuilder<T> and(Supplier<Boolean> conditionSupplier, Specification<T> newSpec) {
        return and(conditionSupplier.get(), newSpec);
    }

    public SpecBuilder<T> or(boolean condition, Specification<T> newSpec) {
        if (condition && newSpec != null) {
            orSpecifications.add(newSpec);
        }
        return this;
    }

    public SpecBuilder<T> or(Supplier<Boolean> conditionSupplier, Specification<T> newSpec) {
        return or(conditionSupplier.get(), newSpec);
    }

    public Specification<T> build() {
        Specification<T> andSpec = andSpecifications.isEmpty()
                ? (root, query, criteriaBuilder) -> criteriaBuilder.conjunction()
                : Specification.allOf(andSpecifications);

        Specification<T> orSpec = orSpecifications.isEmpty()
                ? (root, query, criteriaBuilder) -> criteriaBuilder.conjunction()
                : Specification.anyOf(orSpecifications);

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        andSpec.toPredicate(root, query, criteriaBuilder),
                        orSpec.toPredicate(root, query, criteriaBuilder)
                );
    }

}
