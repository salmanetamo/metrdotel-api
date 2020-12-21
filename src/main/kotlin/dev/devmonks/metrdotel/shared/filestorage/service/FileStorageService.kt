package dev.devmonks.metrdotel.shared.filestorage.service

import dev.devmonks.metrdotel.configuration.filestorage.FileStorageProperties
import dev.devmonks.metrdotel.error.exception.FileNotFoundException
import dev.devmonks.metrdotel.error.exception.FileStorageException
import dev.devmonks.metrdotel.shared.filestorage.FileStorageConstants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.*

@Service
@EnableConfigurationProperties(FileStorageProperties::class)
class FileStorageService @Autowired constructor(private val fileStorageProperties: FileStorageProperties) {
    private var fileStorageLocation: Path = Paths.get(this.fileStorageProperties.uploadDir)
            .toAbsolutePath()
            .normalize()
    private var profilePicturesStorageLocation: Path = Paths.get(this.fileStorageProperties.profilePicturesUploadDir)
            .toAbsolutePath()
            .normalize()
    private var restaurantsCoverImagesStorageLocation: Path = Paths.get(this.fileStorageProperties.restaurantsCoverImagesUploadDir)
            .toAbsolutePath()
            .normalize()
    private var menuItemsPicturesStorageLocation: Path = Paths.get(this.fileStorageProperties.menuItemsPicturesUploadDir)
            .toAbsolutePath()
            .normalize()

    init {
        try {
            Files.createDirectories(this.fileStorageLocation)
            Files.createDirectories(this.profilePicturesStorageLocation)
            Files.createDirectories(this.restaurantsCoverImagesStorageLocation)
            Files.createDirectories(this.menuItemsPicturesStorageLocation)
        } catch (ex: Exception) {
            throw FileStorageException("Could not create the directories where the uploaded files will be stored.", ex)
        }
    }

    fun storeFile(file: MultipartFile, fileType: FileStorageConstants?): String {
        val fileName = StringUtils.cleanPath(System.currentTimeMillis().toString() + "." + StringUtils.getFilenameExtension(file.originalFilename))
        return try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw FileStorageException("Sorry! Filename contains invalid path sequence $fileName")
            }
            val targetLocation: Path
            when (fileType) {
                FileStorageConstants.PROFILE_PIC -> {
                    targetLocation = profilePicturesStorageLocation.resolve(fileName)
                    Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)
                }
                FileStorageConstants.RESTAURANT_COVER_IMAGE -> {
                    targetLocation = this.restaurantsCoverImagesStorageLocation.resolve(fileName)
                    Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)
                }
                FileStorageConstants.MENU_ITEM_PIC -> {
                    targetLocation = this.menuItemsPicturesStorageLocation.resolve(fileName)
                    Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)
                }
                else -> throw FileStorageException("Sorry! File could not be stored.")
            }
            fileName
        } catch (ex: IOException) {
            throw FileStorageException("Could not store file $fileName. Please try again!", ex)
        }
    }

    @Throws(FileNotFoundException::class)
    fun loadFileAsResource(fileName: String, fileType: FileStorageConstants?): Resource? {
        return try {
            val filePath: Path = when (fileType) {
                FileStorageConstants.PROFILE_PIC -> profilePicturesStorageLocation
                        .resolve(fileName)
                        .normalize()
                FileStorageConstants.RESTAURANT_COVER_IMAGE -> this.restaurantsCoverImagesStorageLocation
                        .resolve(fileName)
                        .normalize()
                FileStorageConstants.MENU_ITEM_PIC -> this.menuItemsPicturesStorageLocation
                        .resolve(fileName)
                        .normalize()
                else -> throw FileNotFoundException("File not found $fileName")
            }
            val resource: Resource = UrlResource(filePath.toUri())
            if (resource.exists()) {
                resource
            } else {
                throw FileNotFoundException("File not found $fileName")
            }
        } catch (ex: MalformedURLException) {
            throw FileNotFoundException("File not found $fileName", ex)
        }
    }
}