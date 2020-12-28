package dev.devmonks.metrdotel.error.validator

import javax.validation.Constraint
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [DateTimeFormatValidator::class])
@MustBeDocumented
annotation class DateTimeFormat(
        val message: String = "{DateTimeFormat.invalid}",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<*>> = []
)