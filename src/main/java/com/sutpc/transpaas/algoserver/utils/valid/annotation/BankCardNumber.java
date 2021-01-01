package com.sutpc.transpaas.algoserver.utils.valid.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

@NotBlank(
    message = "银行卡号不能为空"
)
@Size(
    min = 16,
    max = 21,
    message = "不符合银行卡号格式，长度异常"
)
@Digits(
    integer = 21,
    fraction = 0,
    message = "不符合银行卡号格式,必须为数字"
)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
    validatedBy = {}
)
@SupportedValidationTarget({ValidationTarget.ANNOTATED_ELEMENT})
public @interface BankCardNumber {

  String message() default "银行卡校验失败";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
