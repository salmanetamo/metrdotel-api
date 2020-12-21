package dev.devmonks.metrdotel.configuration.filestorage

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "file")
class FileStorageProperties (
        val uploadDir: String,
        val profilePicturesUploadDir: String,
        val restaurantsCoverImagesUploadDir: String,
        val menuItemsPicturesUploadDir: String
        )