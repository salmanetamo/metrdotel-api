package dev.devmonks.metrdotel.error.error

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver
import dev.devmonks.metrdotel.error.resolver.LowerCaseClassNameResolver
import lombok.Data
import org.hibernate.validator.internal.engine.path.PathImpl
import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import java.time.LocalDateTime
import java.util.*
import java.util.function.Consumer
import javax.validation.ConstraintViolation


@Data
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.CUSTOM, property = "error", visible = true)
@JsonTypeIdResolver(LowerCaseClassNameResolver::class)
class ApiError private constructor() {
    lateinit var status: HttpStatus

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    val timestamp: LocalDateTime = LocalDateTime.now()
    var message: String? = null
    var debugMessage: String? = null
    var subErrors: MutableList<ApiSubError>? = null

    constructor(status: HttpStatus) : this() {
        this.status = status
    }

    constructor(status: HttpStatus, ex: Throwable) : this() {
        this.status = status
        message = "Unexpected error"
        debugMessage = ex.localizedMessage
    }

    constructor(status: HttpStatus, message: String?, ex: Throwable) : this() {
        this.status = status
        this.message = message
        debugMessage = ex.localizedMessage
    }

    private fun addSubError(subError: ApiSubError) {
        if (subErrors == null) {
            subErrors = mutableListOf()
        }
        subErrors!!.add(subError)
    }

    private fun addValidationError(`object`: String, field: String, rejectedValue: Any?, message: String?) {
        addSubError(ApiValidationError(`object`, message?:"", field, rejectedValue))
    }

    private fun addValidationError(`object`: String, message: String) {
        addSubError(ApiValidationError(`object`, message, null, null))
    }

    private fun addValidationError(fieldError: FieldError?) {
        this.addValidationError(
                fieldError!!.objectName,
                fieldError.field,
                fieldError.rejectedValue,
                fieldError.defaultMessage
        )
    }

    fun addValidationErrors(fieldErrors: List<FieldError>) {
        fieldErrors.forEach(Consumer { fieldError: FieldError? -> this.addValidationError(fieldError) })
    }

    private fun addValidationError(objectError: ObjectError?) {
        if (objectError != null) {
            objectError.defaultMessage?.let {
                this.addValidationError(
                        objectError.objectName,
                        it)
            }
        }
    }

    fun addValidationError(globalErrors: List<ObjectError?>) {
        globalErrors.forEach(Consumer { objectError: ObjectError? -> this.addValidationError(objectError) })
    }

    /**
     * Utility method for adding error of ConstraintViolation. Usually when a @Validated validation fails.
     *
     * @param cv the ConstraintViolation
     */
    private fun addValidationError(cv: ConstraintViolation<*>) {
        this.addValidationError(cv.getRootBeanClass().getSimpleName(),
                (cv.getPropertyPath() as PathImpl).getLeafNode().asString(),
                cv.getInvalidValue(),
                cv.getMessage())
    }

    fun addValidationErrors(constraintViolations: Set<ConstraintViolation<*>?>) {
        constraintViolations.forEach{
            if (it != null) {
                this.addValidationError(it)
            }
        }
    }

    fun toJson(): String? {
        val mapper = ObjectMapper()
        return try {
            mapper.writeValueAsString(this)
        } catch (e: JsonProcessingException) {
            null
        }
    }

}
