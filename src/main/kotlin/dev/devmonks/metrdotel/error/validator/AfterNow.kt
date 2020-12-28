package dev.devmonks.metrdotel.error.validator

import javax.validation.Constraint
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [AfterNowValidator::class])
@MustBeDocumented
annotation class AfterNow(
        val message: String = "{DateTime.invalid}",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<*>> = []
)
