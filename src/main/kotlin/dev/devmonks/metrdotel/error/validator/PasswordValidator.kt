package dev.devmonks.metrdotel.error.validator

import java.util.regex.Pattern
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class PasswordValidator : ConstraintValidator<Password?, String?> {
    private val passwordPattern = Pattern.compile(PASSWORD_REGEX)

    override fun isValid(password: String?, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        if (password == null || password.trim { it <= ' ' }.isEmpty() || password.isEmpty()) {
            return false
        }
        val matcher = passwordPattern.matcher(password)
        val length = password.length
        return matcher.matches() && length >= PASSWORD_MIN_SIZE && length <= PASSWORD_MAX_SIZE
    }
}