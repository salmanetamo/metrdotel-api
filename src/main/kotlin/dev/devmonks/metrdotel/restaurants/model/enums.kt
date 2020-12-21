package dev.devmonks.metrdotel.restaurants.model

enum class Amenity(val value: String) {
    WIFI("WIFI"),
    TV("TV"),
    PARKING("PARKING"),
    AIR_CONDITIONING("AIR_CONDITIONING");

    companion object Factory {
        fun fromString(value: String): Amenity {
            for(amenity in Amenity.values()) {
                if (amenity.value.equals(value, true)) {
                    return amenity
                }
            }
            throw IllegalArgumentException("Unknown enum type, allowed values are: ${Amenity.values().toString()}")
        }
    }
}

enum class PlaceType(val value: String) {
    RESTAURANT("RESTAURANT"),
    CAFE("CAFE"),
    PIZZERIA("PIZZERIA"),
    BAR("BAR"),
    PUB("PUB");

    companion object Factory {
        fun fromString(value: String): PlaceType {
            for(place in PlaceType.values()) {
                if (place.value.equals(value, true)) {
                    return place
                }
            }
            throw IllegalArgumentException("Unknown enum type, allowed values are: ${PlaceType.values().toString()}")
        }
    }
}