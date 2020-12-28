package dev.devmonks.metrdotel.error.validator

import dev.devmonks.metrdotel.shared.Helper
import java.text.ParseException
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class DateTimeFormatValidator : ConstraintValidator<DateTimeFormat?, String?> {
    override fun isValid(dateString: String?, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        return if (dateString == null || dateString.isEmpty() || dateString.trim { it <= ' ' }.isEmpty()) {
            false
        } else try {
            Helper.getDateTimeFromString(dateString)
            true
        } catch (e: ParseException) {
            false
        }
    }
}