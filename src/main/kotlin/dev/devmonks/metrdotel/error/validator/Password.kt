package dev.devmonks.metrdotel.error.validator

import javax.validation.Constraint
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY_GETTER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [PasswordValidator::class])
@MustBeDocumented
annotation class Password(
        val message: String = "{Password.invalid}",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<*>> = []
)