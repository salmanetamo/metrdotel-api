package dev.devmonks.metrdotel.error.validator

import dev.devmonks.metrdotel.shared.Helper
import java.text.ParseException
import java.time.LocalDateTime
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class AfterNowValidator : ConstraintValidator<AfterNow?, String?> {
    override fun isValid(dateString: String?, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        return if (dateString == null || dateString.isEmpty() || dateString.trim { it <= ' ' }.isEmpty()) {
            false
        } else try {
            val dateInput: LocalDateTime = Helper.getDateTimeFromString(dateString)
            dateInput.isAfter(LocalDateTime.now())
        } catch (e: ParseException) {
            false
        }
    }
}