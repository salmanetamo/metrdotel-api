package dev.devmonks.metrdotel.error.validator

const val PASSWORD_MIN_SIZE = 8
const val PASSWORD_MAX_SIZE = 128
const val PASSWORD_REGEX = "^.*(?=.{8,})(?=.*\\d)(?=.*[a-zA-Z])|(?=.{8,})(?=.*\\d)(?=.*[!@#$%^&])|(?=.{8,})(?=.*[a-zA-Z])(?=.*[!@#$%^&]).*$"