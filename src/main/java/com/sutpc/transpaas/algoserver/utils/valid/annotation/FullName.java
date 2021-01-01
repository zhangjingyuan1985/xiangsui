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
import javax.validation.constraints.Size;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

@NotBlank
@Size(
    min = 2,
    max = 30,
    message = "姓名应在2-30字符长度内"
)
@Pattern(
    regexp = "^[\\u4e00-\\u9fa5|·]+$",
    message = "姓名要求为中文"
)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
    validatedBy = {}
)
@SupportedValidationTarget({ValidationTarget.ANNOTATED_ELEMENT})
public @interface FullName {

  String message() default "姓名校验失败";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
