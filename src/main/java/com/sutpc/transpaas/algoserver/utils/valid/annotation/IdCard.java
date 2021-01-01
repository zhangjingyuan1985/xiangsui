package com.sutpc.transpaas.algoserver.utils.valid.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

@NotBlank(
    message = "身份证号码不能为空"
)
@Pattern(
    regexp = "^(\\d{15,15}|\\d{18,18}|\\d{17}(\\d|X|x))",
    message = "不符合身份证格式"
)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
    validatedBy = {}
)
@SupportedValidationTarget({ValidationTarget.ANNOTATED_ELEMENT})
public @interface IdCard {

  String message() default "身份证校验失败";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
