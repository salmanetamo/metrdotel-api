package dev.devmonks.metrdotel.error.handler

import com.auth0.jwt.exceptions.JWTVerificationException
import dev.devmonks.metrdotel.error.error.ApiError
import dev.devmonks.metrdotel.error.exception.EntityNotFoundException
import lombok.extern.slf4j.Slf4j
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.http.converter.HttpMessageNotWritableException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest

import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.function.Consumer
import javax.validation.ConstraintViolationException

import org.springframework.http.HttpStatus.*

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
class RestExceptionHandler : ResponseEntityExceptionHandler() {
    /**
     * Handle MissingServletRequestParameterException. Triggered when a 'required' request parameter is missing.
     *
     * @param ex      MissingServletRequestParameterException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    override fun handleMissingServletRequestParameter(ex: MissingServletRequestParameterException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val error = ex.parameterName + " parameter is missing"
        return buildResponseEntity(ApiError(BAD_REQUEST, error, ex))
    }

    /**
     * Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is invalid as well.
     *
     * @param ex      HttpMediaTypeNotSupportedException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    override fun handleHttpMediaTypeNotSupported(ex: HttpMediaTypeNotSupportedException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val builder = StringBuilder()
        builder.append(ex.contentType)
        builder.append(" media type is not supported. Supported media types are ")
        ex.supportedMediaTypes.forEach(Consumer { t: MediaType? -> builder.append(t).append(", ") })
        return buildResponseEntity(ApiError(UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length - 2), ex))
    }

    /**
     * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid validation.
     *
     * @param ex      the MethodArgumentNotValidException that is thrown when @Valid validation fails
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    override fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val apiError = ApiError(BAD_REQUEST)
        apiError.message = "Validation error"
        apiError.addValidationErrors(ex.bindingResult.fieldErrors)
        apiError.addValidationError(ex.bindingResult.globalErrors)
        return buildResponseEntity(apiError)
    }

    /**
     * Handles javax.validation.ConstraintViolationException. Thrown when @Validated fails.
     *
     * @param ex the ConstraintViolationException
     * @return the ApiError object
     */
    @ExceptionHandler(ConstraintViolationException::class)
    protected fun handleConstraintViolation(
            ex: ConstraintViolationException): ResponseEntity<Any> {
        val apiError = ApiError(BAD_REQUEST)
        apiError.message = "Validation error"
        apiError.addValidationErrors(ex.constraintViolations)
        return buildResponseEntity(apiError)
    }

    /**
     * Handles EntityNotFoundException. Created to encapsulate errors with more detail than javax.persistence.EntityNotFoundException.
     *
     * @param ex the EntityNotFoundException
     * @return the ApiError object
     */
    @ExceptionHandler(EntityNotFoundException::class)
    protected fun handleEntityNotFound(
            ex: EntityNotFoundException): ResponseEntity<Any> {
        val apiError = ApiError(NOT_FOUND)
        apiError.message = ex.message
        return buildResponseEntity(apiError)
    }

    /**
     * Handles JwtException.
     *
     * @param ex the JWTVerificationException
     * @return the ApiError object
     */
    @ExceptionHandler(JWTVerificationException::class)
    protected fun handleInvalidOrExpiredToken(ex: JWTVerificationException): ResponseEntity<Any> {
        val apiError = ApiError(BAD_REQUEST)
        apiError.message = ex.message
        return buildResponseEntity(apiError)
    }

    @ExceptionHandler(BadCredentialsException::class)
    protected fun handleBadCredentials(ex: BadCredentialsException): ResponseEntity<Any> {
        val apiError = ApiError(BAD_REQUEST)
        apiError.message = ex.message
        return buildResponseEntity(apiError)
    }

    /**
     * Handle HttpMessageNotReadableException. Happens when request JSON is malformed.
     *
     * @param ex      HttpMessageNotReadableException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    override fun handleHttpMessageNotReadable(ex: HttpMessageNotReadableException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val servletWebRequest = request as ServletWebRequest
        logger.info("${servletWebRequest.httpMethod} to ${servletWebRequest.request.servletPath}")
        val error = "Malformed JSON request"
        return buildResponseEntity(ApiError(BAD_REQUEST, error, ex))
    }

    /**
     * Handle HttpMessageNotWritableException.
     *
     * @param ex      HttpMessageNotWritableException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    override fun handleHttpMessageNotWritable(ex: HttpMessageNotWritableException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val error = "Error writing JSON output"
        return buildResponseEntity(ApiError(INTERNAL_SERVER_ERROR, error, ex))
    }

    /**
     * Handle NoHandlerFoundException.
     *
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    override fun handleNoHandlerFoundException(ex: NoHandlerFoundException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val apiError = ApiError(BAD_REQUEST)
        apiError.message = String.format("Could not find the %s method for URL %s", ex.httpMethod, ex.requestURL)
        apiError.debugMessage = ex.message
        return buildResponseEntity(apiError)
    }
    /**
     * Handle DataIntegrityViolationException, inspects the cause for different DB causes.
     *
     * @param ex the DataIntegrityViolationException
     * @return the ApiError object
     */
    @ExceptionHandler(DataIntegrityViolationException::class)
    protected fun handleDataIntegrityViolation(ex: DataIntegrityViolationException,
                                               request: WebRequest?): ResponseEntity<Any> {
        return if (ex.cause is ConstraintViolationException) {
            buildResponseEntity(ApiError(CONFLICT, "Database error", ex.cause as ConstraintViolationException))
        } else buildResponseEntity(ApiError(INTERNAL_SERVER_ERROR, ex))
    }

    /**
     * Handle Exception, handle generic Exception.class
     *
     * @param ex the Exception
     * @return the ApiError object
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    protected fun handleMethodArgumentTypeMismatch(ex: MethodArgumentTypeMismatchException,
                                                   request: WebRequest?): ResponseEntity<Any> {
        val apiError = ApiError(BAD_REQUEST)
        apiError.message = String.format("The parameter '%s' of value '%s' could not be converted to type '%s'", ex.name, ex.value, ex.requiredType!!.simpleName)
        apiError.debugMessage = ex.message
        return buildResponseEntity(apiError)
    }

    private fun buildResponseEntity(apiError: ApiError): ResponseEntity<Any> {
        return ResponseEntity(apiError, apiError.status)
    }
}
