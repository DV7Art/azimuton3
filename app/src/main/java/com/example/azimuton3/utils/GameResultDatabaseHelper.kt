package com.example.azimuton3.utils

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.azimuton3.model.GameResult

class GameResultDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "GameResultDatabase.db"
        internal const val TABLE_NAME = "game_results"

        // Колонки таблицы
        const val COLUMN_ID = "id"
        const val COLUMN_SCORE = "score"
        const val COLUMN_DURATION = "duration"
        const val COLUMN_BACKGROUND_COLOR = "background_color"
    }

    // Создание таблицы
    override fun onCreate(db: SQLiteDatabase) {
        val createTableSQL = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_SCORE INTEGER,
                $COLUMN_DURATION INTEGER,
                $COLUMN_BACKGROUND_COLOR INTEGER
            )
        """.trimIndent()
        db.execSQL(createTableSQL)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun insertGameResult(score: Int, duration: Long, backgroundColor: Int) {
        writableDatabase.use { db ->
            val values = ContentValues().apply {
                put(COLUMN_SCORE, score)
                put(COLUMN_DURATION, duration)
                put(COLUMN_BACKGROUND_COLOR, backgroundColor)
            }
            db.insert(TABLE_NAME, null, values)
        }
    }

    fun getAllGameResults(): List<GameResult> {
        val results = mutableListOf<GameResult>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )

        while (cursor.moveToNext()) {
            val idIndex = cursor.getColumnIndex(COLUMN_ID)
            val scoreIndex = cursor.getColumnIndex(COLUMN_SCORE)
            val durationIndex = cursor.getColumnIndex(COLUMN_DURATION)
            val backgroundColorIndex = cursor.getColumnIndex(COLUMN_BACKGROUND_COLOR)

            // Проверка наличия столбцов перед извлечением их значений
            if (idIndex != -1 && scoreIndex != -1 && durationIndex != -1 && backgroundColorIndex != -1) {
                val id = cursor.getLong(idIndex)
                val score = cursor.getInt(scoreIndex)
                val duration = cursor.getLong(durationIndex)
                val backgroundColor = cursor.getInt(backgroundColorIndex)

                val gameResult = GameResult(id, score, duration, backgroundColor)
                results.add(gameResult)
            }
        }

        cursor.close()
        db.close()

        return results
    }

    fun clearGameResults() {
        val db = writableDatabase
        db.delete(TABLE_NAME, null, null)
        db.close()
    }
}
