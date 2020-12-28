package dev.devmonks.metrdotel.error.error

import lombok.AllArgsConstructor
import lombok.Data

import lombok.EqualsAndHashCode


@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
class ApiValidationError(val `object`: String, val message: String, var field: String?, var rejectedValue: Any?): ApiSubError() {
}