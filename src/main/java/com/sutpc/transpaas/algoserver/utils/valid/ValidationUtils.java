package com.sutpc.transpaas.algoserver.utils.valid;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import org.springframework.util.CollectionUtils;

/**
 * 参数校验工具类.
 */
public class ValidationUtils {

  public static final Validator validator = Validation.buildDefaultValidatorFactory()
      .getValidator();

  public ValidationUtils() {
  }

  public static <T> ValidationResult validateEntity(T obj) {
    return validateEntity(obj, Default.class);
  }

  /**
   * 集合验证.
   *
   * @param objs .
   * @param <T> .
   * @return
   */
  public static <T> ValidationResult validateList(List<T> objs) {
    ValidationResult result = new ValidationResult();
    result.setHasErrors(false);
    if (objs != null && objs.size() != 0) {
      Iterator iterator = objs.iterator();

      while (iterator.hasNext()) {
        Object obj = iterator.next();
        result = validateEntity(obj);
        if (result.isHasErrors()) {
          return result;
        }
      }

      return result;
    } else {
      result.setHasErrors(true);
      HashMap errorMsg = new HashMap();
      errorMsg.put("list", "入参为空");
      result.setErrorMsg(errorMsg);
      return result;
    }
  }

  /**
   * 属性验证.
   *
   * @param obj .
   * @param propertyName .
   * @param <T> .
   * @return
   */
  public static <T> ValidationResult validateProperty(T obj, String propertyName) {
    ValidationResult result = new ValidationResult();
    Set set = validator.validateProperty(obj, propertyName, new Class[]{Default.class});
    if (!CollectionUtils.isEmpty(set)) {
      result.setHasErrors(true);
      HashMap errorMsg = new HashMap();
      Iterator iterator = set.iterator();

      while (iterator.hasNext()) {
        ConstraintViolation cv = (ConstraintViolation) iterator.next();
        errorMsg.put(propertyName, cv.getMessage());
      }

      result.setErrorMsg(errorMsg);
    }

    return result;
  }

  /**
   * 多属性验证.
   *
   * @param obj .
   * @param propertyNames .
   * @param <T> .
   * @return
   */
  public static <T> ValidationResult validatePropertys(T obj, String... propertyNames) {
    ValidationResult result = new ValidationResult();
    result.setHasErrors(false);
    if (propertyNames != null) {
      String[] propertyArr = propertyNames;
      int len = propertyNames.length;

      for (int i = 0; i < len; ++i) {
        String name = propertyArr[i];
        Set set = validator.validateProperty(obj, name, new Class[]{Default.class});
        if (!CollectionUtils.isEmpty(set)) {
          HashMap errorMsg = new HashMap();
          Iterator iterator = set.iterator();

          while (iterator.hasNext()) {
            ConstraintViolation cv = (ConstraintViolation) iterator.next();
            errorMsg.put(name, cv.getMessage());
          }

          result.setErrorMsg(errorMsg);
          result.setHasErrors(true);
          break;
        }
      }
    }

    return result;
  }

  /**
   * .
   *
   * @param obj .
   * @param clazz .
   * @param <T> .
   * @return
   */
  public static <T> ValidationResult validateEntity(T obj, Class clazz) {
    ValidationResult result = new ValidationResult();
    Set set = validator.validate(obj, new Class[]{clazz});
    if (!CollectionUtils.isEmpty(set)) {
      result.setHasErrors(true);
      HashMap errorMsg = new HashMap();
      Iterator iterator = set.iterator();

      while (iterator.hasNext()) {
        ConstraintViolation cv = (ConstraintViolation) iterator.next();
        errorMsg.put(cv.getPropertyPath().toString(), cv.getMessage());
      }

      result.setErrorMsg(errorMsg);
    }

    return result;
  }

  /**
   * .
   *
   * @param obj .
   * @param clazz .
   * @param <T> .
   * @return
   */
  public static <T> ValidationResult validateEntity(T obj, Class... clazz) {
    ValidationResult result = new ValidationResult();
    Set set = validator.validate(obj, clazz);
    if (!CollectionUtils.isEmpty(set)) {
      result.setHasErrors(true);
      HashMap errorMsg = new HashMap();
      Iterator iterator = set.iterator();

      while (iterator.hasNext()) {
        ConstraintViolation cv = (ConstraintViolation) iterator.next();
        errorMsg.put(cv.getPropertyPath().toString(), cv.getMessage());
      }

      result.setErrorMsg(errorMsg);
    }

    return result;
  }
}
