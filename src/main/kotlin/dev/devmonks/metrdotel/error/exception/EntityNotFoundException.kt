package dev.devmonks.metrdotel.error.exception

import org.springframework.util.StringUtils
import java.util.*
import java.util.stream.IntStream


class EntityNotFoundException(className: Class<*>, vararg searchParamsMap: Any) :
        RuntimeException(
        generateMessage(className.simpleName, toMap(String::class.java, String::class.java, *searchParamsMap))
) {
    companion object {
        private fun generateMessage(entity: String, searchParams: Map<String, String>): String {
            return StringUtils.capitalize(entity) +
                    " was not found for parameters " +
                    searchParams
        }

        private fun <K, V> toMap(
                keyType: Class<K>, valueType: Class<V>, vararg entries: Any): Map<K, V> {
            require(entries.size % 2 != 1) { "Invalid entries" }
            return IntStream.range(0, entries.size / 2).map { i: Int -> i * 2 }
                    .collect({ HashMap() },
                            { m: HashMap<K, V>, i: Int -> m[keyType.cast(entries[i])] = valueType.cast(entries[i + 1]) }) { obj: HashMap<K, V>, m: HashMap<K, V>? -> obj.putAll(m!!) }
        }
    }
}