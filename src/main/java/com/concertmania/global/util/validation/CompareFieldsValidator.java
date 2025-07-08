package com.concertmania.global.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Method;

public class CompareFieldsValidator implements ConstraintValidator<CompareFields, Object> {

    private String baseField;
    private String matchField;
    private boolean lessThan;

    @Override
    public void initialize(CompareFields constraintAnnotation) {
        this.baseField = constraintAnnotation.baseField();
        this.matchField = constraintAnnotation.matchField();
        this.lessThan = constraintAnnotation.lessThan();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) return true;

        try {
            // record는 getXxx()가 아니라 필드명() 메서드 호출
            Method baseGetter = value.getClass().getMethod(baseField);
            Method matchGetter = value.getClass().getMethod(matchField);

            Object baseValue = baseGetter.invoke(value);
            Object matchValue = matchGetter.invoke(value);

            if (baseValue == null || matchValue == null) {
                return true; // null은 @NotNull이 처리
            }

            if (!(baseValue instanceof Comparable) || !(matchValue instanceof Comparable)) {
                return false;
            }

            @SuppressWarnings("unchecked")
            Comparable<Object> baseComparable = (Comparable<Object>) baseValue;
            Comparable<Object> matchComparable = (Comparable<Object>) matchValue;

            int compareResult = baseComparable.compareTo(matchValue);

            return lessThan ? (compareResult < 0) : (compareResult <= 0);

        } catch (Exception e) {
            return false;
        }
    }
}

