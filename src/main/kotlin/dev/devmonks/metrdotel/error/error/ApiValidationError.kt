package dev.devmonks.metrdotel.error.error

import lombok.AllArgsConstructor
import lombok.Data

import lombok.EqualsAndHashCode


@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
class ApiValidationError internal constructor(private val `object`: String, private val message: String) : ApiSubError() {
    private var field: String? = null
    private var rejectedValue: Any? = null

    constructor(`object`: String, message: String, field: String?, rejectedValue: Any?) : this(`object`, message) {
        this.field = field
        this.rejectedValue = rejectedValue
    }
}