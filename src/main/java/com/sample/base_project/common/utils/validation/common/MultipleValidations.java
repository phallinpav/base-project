package com.sample.base_project.common.utils.validation.common;


import com.sample.base_project.common.exception.ErrorMessageUtils;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.util.List;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MultipleValidations.MultipleValidationsValidator.class)
public @interface MultipleValidations {
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    MustBeOnCondition[] mustBeOnConditions() default {};
    MustHaveOnlyOne[] mustHaveOnlyOnes() default {};
    CanHaveOnlyOne[] canHaveOnlyOnes() default {};
    AllNullOrNotBlank[] allNullOrNotBlanks() default {};
    DefaultValue[] defaultValues() default {};
    CollectionSameSize[] collectionSameSizes() default {};


    class MultipleValidationsValidator implements ConstraintValidator<MultipleValidations, Object> {
        private List<Annotation[]> annotationsList;


        @Override
        public void initialize(MultipleValidations constraintAnnotation) {
            annotationsList = List.of(
                    constraintAnnotation.mustBeOnConditions(),
                    constraintAnnotation.mustHaveOnlyOnes(),
                    constraintAnnotation.canHaveOnlyOnes(),
                    constraintAnnotation.allNullOrNotBlanks(),
                    constraintAnnotation.defaultValues(),
                    constraintAnnotation.collectionSameSizes()
            );
        }

        @Override
        public boolean isValid(Object obj, ConstraintValidatorContext context) {
            return validate(obj, context, annotationsList);
        }

        private <T extends Annotation> boolean validate(Object obj, ConstraintValidatorContext context, List<T[]> annotationsList) {
            for (var val : annotationsList) {
                if (!validate(obj, context, val)) {
                    return false;
                }
            }
            return true;
        }

        private <T extends Annotation> boolean validate(Object obj, ConstraintValidatorContext context, T[] annotations) {
            for (var val : annotations) {
                try {
                    Class<?> validatorClass = getValidatorClass(val.annotationType());
                    if (validatorClass != null) {
                        ConstraintValidator<Annotation, Object> validator = instantiateValidator(validatorClass);
                        validator.initialize(val);
                        if (!validator.isValid(obj, context)) {
                            return false;
                        }
                    }
                } catch (Exception e) {
                    throw ErrorMessageUtils.error("multiple.validation.error", e);
                }
            }
            return true;
        }

        private Class<?> getValidatorClass(Class<? extends Annotation> annotationClass) {
            if (annotationClass.isAnnotationPresent(Constraint.class)) {
                Constraint constraintAnnotation = annotationClass.getAnnotation(Constraint.class);
                return constraintAnnotation.validatedBy()[0];
            }
            return null;
        }

        private ConstraintValidator<Annotation, Object> instantiateValidator(Class<?> validatorClass) throws Exception {
            Constructor<?> constructor = validatorClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            return (ConstraintValidator<Annotation, Object>) constructor.newInstance();
        }
    }

}
