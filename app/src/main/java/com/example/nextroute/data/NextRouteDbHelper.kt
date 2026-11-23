package com.example.nextroute.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.security.MessageDigest

data class NextRouteUser(
    val id: Long,
    val name: String,
    val email: String,
    val phone: String,
    val cpf: String,
    val photoUri: String? = null
)

class NextRouteDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_EMAIL TEXT NOT NULL UNIQUE COLLATE NOCASE,
                $COLUMN_PHONE TEXT NOT NULL,
                $COLUMN_CPF TEXT NOT NULL UNIQUE,
                $COLUMN_PASSWORD TEXT NOT NULL,
                $COLUMN_PHOTO_URI TEXT
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $TABLE_FAVORITES (
                $COLUMN_FAVORITE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_FAVORITE_USER_ID INTEGER NOT NULL,
                $COLUMN_DESTINATION_NAME TEXT NOT NULL,
                UNIQUE($COLUMN_FAVORITE_USER_ID, $COLUMN_DESTINATION_NAME),
                FOREIGN KEY($COLUMN_FAVORITE_USER_ID)
                    REFERENCES $TABLE_USERS($COLUMN_USER_ID)
                    ON DELETE CASCADE
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $TABLE_AVISOS (
                $COLUMN_AVISO_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_AVISO_TITLE TEXT NOT NULL,
                $COLUMN_AVISO_DESCRIPTION TEXT NOT NULL,
                $COLUMN_AVISO_ACTIVE INTEGER NOT NULL DEFAULT 1
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Upgrade simples e seguro: mantém os dados existentes das versões anteriores.
        if (oldVersion < 2) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS $TABLE_FAVORITES (
                    $COLUMN_FAVORITE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $COLUMN_FAVORITE_USER_ID INTEGER NOT NULL,
                    $COLUMN_DESTINATION_NAME TEXT NOT NULL,
                    UNIQUE($COLUMN_FAVORITE_USER_ID, $COLUMN_DESTINATION_NAME),
                    FOREIGN KEY($COLUMN_FAVORITE_USER_ID)
                        REFERENCES $TABLE_USERS($COLUMN_USER_ID)
                        ON DELETE CASCADE
                )
                """.trimIndent()
            )
        }

        if (oldVersion < 3) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS $TABLE_AVISOS (
                    $COLUMN_AVISO_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $COLUMN_AVISO_TITLE TEXT NOT NULL,
                    $COLUMN_AVISO_DESCRIPTION TEXT NOT NULL,
                    $COLUMN_AVISO_ACTIVE INTEGER NOT NULL DEFAULT 1
                )
                """.trimIndent()
            )
        }
    }

    fun criarUsuario(
        nome: String,
        email: String,
        telefone: String,
        cpf: String,
        senha: String
    ): Long {
        val db = writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_NAME, nome)
            put(COLUMN_EMAIL, email.trim())
            put(COLUMN_PHONE, telefone)
            put(COLUMN_CPF, cpf)
            put(COLUMN_PASSWORD, hashPassword(senha))
            putNull(COLUMN_PHOTO_URI)
        }

        return try {
            db.insertOrThrow(TABLE_USERS, null, values)
        } catch (_: Exception) {
            -1L
        }
    }

    fun autenticarUsuario(email: String, senha: String): NextRouteUser? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USERS,
            null,
            "$COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?",
            arrayOf(email.trim(), hashPassword(senha)),
            null,
            null,
            null,
            "1"
        )

        cursor.use {
            if (!it.moveToFirst()) return null

            return NextRouteUser(
                id = it.getLong(it.getColumnIndexOrThrow(COLUMN_USER_ID)),
                name = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME)),
                email = it.getString(it.getColumnIndexOrThrow(COLUMN_EMAIL)),
                phone = it.getString(it.getColumnIndexOrThrow(COLUMN_PHONE)),
                cpf = it.getString(it.getColumnIndexOrThrow(COLUMN_CPF)),
                photoUri = it.getString(it.getColumnIndexOrThrow(COLUMN_PHOTO_URI))
            )
        }
    }

    fun obterUsuario(id: Long): NextRouteUser? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USERS,
            null,
            "$COLUMN_USER_ID = ?",
            arrayOf(id.toString()),
            null,
            null,
            null,
            "1"
        )

        cursor.use {
            if (!it.moveToFirst()) return null

            return NextRouteUser(
                id = it.getLong(it.getColumnIndexOrThrow(COLUMN_USER_ID)),
                name = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME)),
                email = it.getString(it.getColumnIndexOrThrow(COLUMN_EMAIL)),
                phone = it.getString(it.getColumnIndexOrThrow(COLUMN_PHONE)),
                cpf = it.getString(it.getColumnIndexOrThrow(COLUMN_CPF)),
                photoUri = it.getString(it.getColumnIndexOrThrow(COLUMN_PHOTO_URI))
            )
        }
    }

    fun atualizarUsuario(
        id: Long,
        nome: String,
        email: String,
        telefone: String,
        cpf: String,
        fotoUri: String?
    ): Boolean {
        val values = ContentValues().apply {
            put(COLUMN_NAME, nome)
            put(COLUMN_EMAIL, email.trim())
            put(COLUMN_PHONE, telefone)
            put(COLUMN_CPF, cpf)
            if (fotoUri == null) putNull(COLUMN_PHOTO_URI)
            else put(COLUMN_PHOTO_URI, fotoUri)
        }

        return try {
            writableDatabase.update(
                TABLE_USERS,
                values,
                "$COLUMN_USER_ID = ?",
                arrayOf(id.toString())
            ) > 0
        } catch (_: Exception) {
            false
        }
    }

    fun atualizarSenha(id: Long, novaSenha: String): Boolean {
        val values = ContentValues().apply {
            put(COLUMN_PASSWORD, hashPassword(novaSenha))
        }

        return writableDatabase.update(
            TABLE_USERS,
            values,
            "$COLUMN_USER_ID = ?",
            arrayOf(id.toString())
        ) > 0
    }

    fun listarFavoritos(userId: Long): List<String> {
        val favoritos = mutableListOf<String>()
        val cursor = readableDatabase.query(
            TABLE_FAVORITES,
            arrayOf(COLUMN_DESTINATION_NAME),
            "$COLUMN_FAVORITE_USER_ID = ?",
            arrayOf(userId.toString()),
            null,
            null,
            "$COLUMN_FAVORITE_ID DESC"
        )

        cursor.use {
            while (it.moveToNext()) {
                favoritos.add(
                    it.getString(
                        it.getColumnIndexOrThrow(COLUMN_DESTINATION_NAME)
                    )
                )
            }
        }
        return favoritos
    }

    fun adicionarFavorito(userId: Long, destinationName: String) {
        val values = ContentValues().apply {
            put(COLUMN_FAVORITE_USER_ID, userId)
            put(COLUMN_DESTINATION_NAME, destinationName)
        }

        writableDatabase.insertWithOnConflict(
            TABLE_FAVORITES,
            null,
            values,
            SQLiteDatabase.CONFLICT_IGNORE
        )
    }

    fun removerFavorito(userId: Long, destinationName: String) {
        writableDatabase.delete(
            TABLE_FAVORITES,
            "$COLUMN_FAVORITE_USER_ID = ? AND $COLUMN_DESTINATION_NAME = ?",
            arrayOf(userId.toString(), destinationName)
        )
    }

    fun listarAvisosAtivos(): List<Aviso> {
        val avisos = mutableListOf<Aviso>()
        val cursor = readableDatabase.query(
            TABLE_AVISOS,
            null,
            "$COLUMN_AVISO_ACTIVE = ?",
            arrayOf("1"),
            null,
            null,
            "$COLUMN_AVISO_ID DESC"
        )

        cursor.use {
            while (it.moveToNext()) {
                avisos.add(
                    Aviso(
                        id = it.getLong(
                            it.getColumnIndexOrThrow(COLUMN_AVISO_ID)
                        ).toString(),
                        title = it.getString(
                            it.getColumnIndexOrThrow(COLUMN_AVISO_TITLE)
                        ),
                        description = it.getString(
                            it.getColumnIndexOrThrow(COLUMN_AVISO_DESCRIPTION)
                        ),
                        active = it.getInt(
                            it.getColumnIndexOrThrow(COLUMN_AVISO_ACTIVE)
                        ) == 1
                    )
                )
            }
        }

        return avisos
    }

    fun inserirAviso(
        titulo: String,
        descricao: String,
        ativo: Boolean = true
    ): Long {
        val values = ContentValues().apply {
            put(COLUMN_AVISO_TITLE, titulo)
            put(COLUMN_AVISO_DESCRIPTION, descricao)
            put(COLUMN_AVISO_ACTIVE, if (ativo) 1 else 0)
        }

        return writableDatabase.insert(
            TABLE_AVISOS,
            null,
            values
        )
    }

    private fun hashPassword(value: String): String {
        val bytes = MessageDigest
            .getInstance("SHA-256")
            .digest(value.toByteArray())

        return bytes.joinToString("") { "%02x".format(it) }
    }

    companion object {
        private const val DATABASE_NAME = "nextroute.db"
        private const val DATABASE_VERSION = 3

        private const val TABLE_USERS = "users"
        private const val TABLE_FAVORITES = "favorites"
        private const val TABLE_AVISOS = "avisos"

        private const val COLUMN_USER_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PHONE = "phone"
        private const val COLUMN_CPF = "cpf"
        private const val COLUMN_PASSWORD = "password_hash"
        private const val COLUMN_PHOTO_URI = "photo_uri"

        private const val COLUMN_FAVORITE_ID = "id"
        private const val COLUMN_FAVORITE_USER_ID = "user_id"
        private const val COLUMN_DESTINATION_NAME = "destination_name"

        private const val COLUMN_AVISO_ID = "id"
        private const val COLUMN_AVISO_TITLE = "title"
        private const val COLUMN_AVISO_DESCRIPTION = "description"
        private const val COLUMN_AVISO_ACTIVE = "active"
    }
}
