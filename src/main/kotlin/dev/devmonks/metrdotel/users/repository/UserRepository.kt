package dev.devmonks.metrdotel.users.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import dev.devmonks.metrdotel.authentication.model.UserDetails
import dev.devmonks.metrdotel.users.model.ActivationToken
import dev.devmonks.metrdotel.users.model.PasswordResetToken
import dev.devmonks.metrdotel.users.model.User
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Component
import java.sql.ResultSet
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.sql.DataSource

const val usersTable = "users"
const val usersIdColumn = "id"
const val usersFirstNameColumn = "first_name"
const val usersLastNameColumn = "last_name"
const val usersEmailColumn = "email"
const val usersPasswordColumn = "password"
const val usersEnabledColumn = "enabled"
const val usersCreatedAtColumn = "created_at"
const val usersUpdatedAtColumn = "updated_at"
const val usersProfilePictureColumn = "profile_picture"

const val activationTokensTable = "activation_tokens"
const val activationTokensIdColumn = "id"
const val activationTokensEmailColumn = "email"
const val activationTokensTokenColumn = "token"
const val activationTokensExpiresAtColumn = "expires_at"

const val passwordResetTokensTable = "password_reset_tokens"
const val passwordResetTokensIdColumn = "id"
const val passwordResetTokensEmailColumn = "email"
const val passwordResetTokensTokenColumn = "token"
const val passwordResetTokensExpiresAtColumn = "expires_at"

@Component
class UserRepository @Autowired constructor(private val dataSource: DataSource): IUserRepository {

    private val logger = LoggerFactory.getLogger(UserRepository::class.java)
    private final val mapper: ObjectMapper

    init {
        val javaTimeModule = JavaTimeModule()
        javaTimeModule.addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
        this.mapper = jacksonObjectMapper().registerModule(javaTimeModule).findAndRegisterModules()
    }

    override fun loadUserDetailsByUsername(username: String): UserDetails? {
        val query: String =
            """SELECT 
            | $usersIdColumn,
            | $usersFirstNameColumn,
            | $usersLastNameColumn,
            | $usersEmailColumn,
            | $usersEnabledColumn,
            | $usersPasswordColumn
            | FROM $usersTable
            | WHERE ($usersEmailColumn = ?)
        """.trimMargin()

        val userList = JdbcTemplate(dataSource).query(query, arrayOf(username)){ rs: ResultSet, _: Int -> UserDetails(
                id = rs.getString(usersIdColumn),
                email = rs.getString(usersEmailColumn),
                firstName = rs.getString(usersFirstNameColumn),
                lastName = rs.getString(usersLastNameColumn),
                enabled = rs.getBoolean(usersEnabledColumn),
                password = rs.getString(usersPasswordColumn)
        )}

        return if (userList.isEmpty()) {
            null
        } else {
            userList.first()
        }
    }

    override fun createUser(id: String, firstName: String, lastName: String, email: String, password: String): Boolean {
        val insert = SimpleJdbcInsert(this.dataSource)
                .withTableName(usersTable)
                .usingColumns(
                        usersIdColumn,
                        usersFirstNameColumn,
                        usersLastNameColumn,
                        usersEmailColumn,
                        usersPasswordColumn,
                        usersCreatedAtColumn,
                        usersUpdatedAtColumn
                )

        val parameters = HashMap<String, Any>(1)
        parameters[usersIdColumn] = id
        parameters[usersFirstNameColumn] = firstName
        parameters[usersLastNameColumn] = lastName
        parameters[usersEmailColumn] = email
        parameters[usersPasswordColumn] = password
        parameters[usersCreatedAtColumn] = LocalDateTime.now()
        parameters[usersUpdatedAtColumn] = LocalDateTime.now()

        insert.execute(parameters)
        return true
    }

    override fun getAllUsers(): List<User> {
        val query: String =
                """SELECT 
            | $usersIdColumn,
            | $usersFirstNameColumn,
            | $usersLastNameColumn,
            | $usersEmailColumn,
            | $usersPasswordColumn
            | FROM $usersTable
        """.trimMargin()

        return JdbcTemplate(dataSource).query(query){
            rs: ResultSet, _: Int -> resultSetToUser(rs)
        }
    }

    override fun getUserByEmail(email: String): User? {
        val query: String =
                """SELECT 
            | $usersIdColumn, 
            | $usersFirstNameColumn, 
            | $usersLastNameColumn, 
            | $usersEmailColumn,
            | $usersEnabledColumn, 
            | $usersCreatedAtColumn, 
            | $usersUpdatedAtColumn, 
            | $usersProfilePictureColumn 
            | FROM $usersTable
            | WHERE ($usersEmailColumn = ?)
        """.trimMargin()

        val userList = JdbcTemplate(dataSource).query(query, arrayOf(email)){
            rs: ResultSet, _: Int -> this.resultSetToUser(rs)
        }

        return if (userList.isEmpty()) {
            null
        } else {
            userList.first()
        }
    }

    override fun getUserById(id: String): User? {
        val query: String =
                """SELECT 
            | $usersIdColumn, 
            | $usersFirstNameColumn, 
            | $usersLastNameColumn, 
            | $usersEmailColumn,
            | $usersEnabledColumn, 
            | $usersCreatedAtColumn, 
            | $usersUpdatedAtColumn, 
            | $usersProfilePictureColumn 
            | FROM $usersTable
            | WHERE ($usersIdColumn = ?)
        """.trimMargin()

        val userList = JdbcTemplate(dataSource).query(query, arrayOf(id)){
            rs: ResultSet, _: Int -> this.resultSetToUser(rs)
        }

        return if (userList.isEmpty()) {
            null
        } else {
            userList.first()
        }
    }

    override fun addProfilePicture(id: String, fileName: String): Boolean {
        val query: String =
                """UPDATE $usersTable
                    | SET $usersProfilePictureColumn = ?
                    | WHERE $usersIdColumn = ?
                """.trimMargin()

        JdbcTemplate(this.dataSource)
                .update(
                        query,
                        ArgumentPreparedStatementSetter(
                                arrayOf(
                                        fileName,
                                        id
                                )
                        )
                )

        return true
    }

    override fun createActivationToken(activationToken: ActivationToken): Boolean {
        deleteActivationToken(activationToken.email?: "")
        val insert = SimpleJdbcInsert(this.dataSource)
                .withTableName(activationTokensTable)
                .usingColumns(
                        activationTokensIdColumn,
                        activationTokensEmailColumn,
                        activationTokensTokenColumn,
                        activationTokensExpiresAtColumn
                )
        val parameters = HashMap<String, Any>(1)
        parameters[activationTokensIdColumn] = activationToken.id
        parameters[activationTokensEmailColumn] = activationToken.email
        parameters[activationTokensTokenColumn] = activationToken.token
        parameters[activationTokensExpiresAtColumn] = activationToken.expiresAt

        insert.execute(parameters)
        return true
    }

    override fun getActivationToken(token: String): ActivationToken? {
        val query: String =
                """SELECT
                    | $activationTokensIdColumn,
                    | $activationTokensEmailColumn,
                    | $activationTokensTokenColumn,
                    | $activationTokensExpiresAtColumn
                    | FROM $activationTokensTable
                    | WHERE $activationTokensTokenColumn = ?
                """.trimMargin()

        val activationTokensList = JdbcTemplate(dataSource).query(query, arrayOf(token)){
            rs: ResultSet, _: Int -> ActivationToken(
                id = rs.getString(activationTokensIdColumn),
                token = rs.getString(activationTokensTokenColumn),
                email = rs.getString(activationTokensEmailColumn),
                expiresAt = rs.getTimestamp(activationTokensExpiresAtColumn).toLocalDateTime()
            )
        }
        return if (activationTokensList.isEmpty()) {
            null
        } else {
            activationTokensList.first()
        }
    }

    override fun getActivationTokenByEmail(email: String): ActivationToken? {
        val query: String =
                """SELECT
                    | $activationTokensIdColumn,
                    | $activationTokensEmailColumn,
                    | $activationTokensTokenColumn,
                    | $activationTokensExpiresAtColumn
                    | FROM $activationTokensTable
                    | WHERE $activationTokensEmailColumn = ?
                """.trimMargin()

        val activationTokensList = JdbcTemplate(dataSource).query(query, arrayOf(email)){
            rs: ResultSet, _: Int -> ActivationToken(
                id = rs.getString(activationTokensIdColumn),
                token = rs.getString(activationTokensTokenColumn),
                email = rs.getString(activationTokensEmailColumn),
                expiresAt = rs.getTimestamp(activationTokensExpiresAtColumn).toLocalDateTime()
            )
        }
        return if (activationTokensList.isEmpty()) {
            null
        } else {
            activationTokensList.first()
        }
    }

    override fun deleteActivationToken(email: String): Boolean {
        val query: String =
                """DELETE FROM $activationTokensTable
                    | WHERE $activationTokensEmailColumn = ?
                """.trimMargin()

        JdbcTemplate(this.dataSource).update(query, ArgumentPreparedStatementSetter(arrayOf(email)))
        return true
    }

    override fun enableUser(email: String): Boolean {
        val query: String =
                """UPDATE $usersTable
                    | SET $usersEnabledColumn = ?
                    | WHERE $usersEmailColumn = ?
                """.trimMargin()

        JdbcTemplate(this.dataSource)
                .update(
                        query,
                        ArgumentPreparedStatementSetter(
                                arrayOf(
                                        true,
                                        email
                                )
                        )
                )

        return true
    }

    override fun createPasswordResetToken(passwordResetToken: PasswordResetToken): Boolean {
        deletePasswordResetToken(passwordResetToken.email?: "")
        val insert = SimpleJdbcInsert(this.dataSource)
                .withTableName(passwordResetTokensTable)
                .usingColumns(
                        passwordResetTokensIdColumn,
                        passwordResetTokensEmailColumn,
                        passwordResetTokensTokenColumn,
                        passwordResetTokensExpiresAtColumn
                )
        val parameters = HashMap<String, Any>(1)
        parameters[passwordResetTokensIdColumn] = passwordResetToken.id
        parameters[passwordResetTokensEmailColumn] = passwordResetToken.email
        parameters[passwordResetTokensTokenColumn] = passwordResetToken.token
        parameters[passwordResetTokensExpiresAtColumn] = passwordResetToken.expiresAt

        insert.execute(parameters)
        return true
    }

    override fun getPasswordResetToken(token: String): PasswordResetToken? {
        val query: String =
                """SELECT
                    | $passwordResetTokensIdColumn,
                    | $passwordResetTokensEmailColumn,
                    | $passwordResetTokensTokenColumn,
                    | $passwordResetTokensExpiresAtColumn
                    | FROM $passwordResetTokensTable
                    | WHERE $passwordResetTokensTokenColumn = ?
                """.trimMargin()

        val passwordResetTokensList = JdbcTemplate(dataSource).query(query, arrayOf(token)){
            rs: ResultSet, _: Int -> PasswordResetToken(
                id = rs.getString(passwordResetTokensIdColumn),
                token = rs.getString(passwordResetTokensTokenColumn),
                email = rs.getString(passwordResetTokensEmailColumn),
                expiresAt = rs.getTimestamp(passwordResetTokensExpiresAtColumn).toLocalDateTime()
        )
        }
        return if (passwordResetTokensList.isEmpty()) {
            null
        } else {
            passwordResetTokensList.first()
        }
    }

    override fun getPasswordResetTokenByEmail(email: String): PasswordResetToken? {
        val query: String =
                """SELECT
                    | $passwordResetTokensIdColumn,
                    | $passwordResetTokensEmailColumn,
                    | $passwordResetTokensTokenColumn,
                    | $passwordResetTokensExpiresAtColumn
                    | FROM $passwordResetTokensTable
                    | WHERE $passwordResetTokensEmailColumn = ?
                """.trimMargin()

        val passwordResetTokensList = JdbcTemplate(dataSource).query(query, arrayOf(email)){
            rs: ResultSet, _: Int -> PasswordResetToken(
                id = rs.getString(passwordResetTokensIdColumn),
                token = rs.getString(passwordResetTokensTokenColumn),
                email = rs.getString(passwordResetTokensEmailColumn),
                expiresAt = rs.getTimestamp(passwordResetTokensExpiresAtColumn).toLocalDateTime()
        )
        }
        return if (passwordResetTokensList.isEmpty()) {
            null
        } else {
            passwordResetTokensList.first()
        }
    }

    override fun deletePasswordResetToken(email: String): Boolean {
        val query: String =
                """DELETE FROM $passwordResetTokensTable
                    | WHERE $passwordResetTokensEmailColumn = ?
                """.trimMargin()

        JdbcTemplate(this.dataSource).update(query, ArgumentPreparedStatementSetter(arrayOf(email)))
        return true
    }

    override fun resetPassword(email: String, password: String): Boolean {
        val query: String =
                """UPDATE $usersTable
                    | SET $usersPasswordColumn = ?
                    | WHERE $usersEmailColumn = ?
                """.trimMargin()

        JdbcTemplate(this.dataSource)
                .update(
                        query,
                        ArgumentPreparedStatementSetter(
                                arrayOf(
                                        password,
                                        email
                                )
                        )
                )
        return true
    }

    private fun resultSetToUser(resultSet: ResultSet): User {
        return User(
                id = resultSet.getString(usersIdColumn),
                firstName = resultSet.getString(usersFirstNameColumn),
                lastName = resultSet.getString(usersLastNameColumn),
                email = resultSet.getString(usersEmailColumn),
                enabled = resultSet.getBoolean(usersEnabledColumn),
                createdAt = resultSet.getTimestamp(usersCreatedAtColumn).toLocalDateTime(),
                updatedAt = resultSet.getTimestamp(usersUpdatedAtColumn).toLocalDateTime(),
                profilePicture = resultSet.getString(usersProfilePictureColumn)?: "",
                passwordResetToken = this.getPasswordResetTokenByEmail(resultSet.getString(usersEmailColumn)),
                activationToken = this.getActivationTokenByEmail(resultSet.getString(usersEmailColumn))
        )
    }
}