package com.example.myquizapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.myquizapp.models.Question

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, "QuizApp.db", null, 2) {

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL("""
            CREATE TABLE Users(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                fullName TEXT,
                emailOrPhone TEXT UNIQUE,
                password TEXT,
                role TEXT
            )
        """)

        db.execSQL("""
            CREATE TABLE Questions(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                questionText TEXT,
                option1 TEXT,
                option2 TEXT,
                option3 TEXT,
                option4 TEXT,
                correctAnswerIndex INTEGER
            )
        """)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Users")
        db.execSQL("DROP TABLE IF EXISTS Questions")
        onCreate(db)
    }


    // Add a user
    fun addUser(fullName: String, emailOrPhone: String, password: String, role: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("fullName", fullName)
            put("emailOrPhone", emailOrPhone)
            put("password", password)
            put("role", role)
        }
        val result = db.insert("Users", null, values)
        db.close()
        return result != -1L
    }

    // Get a user by email/phone
    fun getUser(emailOrPhone: String): User? {
        val db = readableDatabase
        val cursor =
            db.rawQuery("SELECT * FROM Users WHERE emailOrPhone = ?", arrayOf(emailOrPhone))
        val user: User? = if (cursor.moveToFirst()) {
            User(
                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                cursor.getString(cursor.getColumnIndexOrThrow("fullName")),
                cursor.getString(cursor.getColumnIndexOrThrow("emailOrPhone")),
                cursor.getString(cursor.getColumnIndexOrThrow("password")),
                cursor.getString(cursor.getColumnIndexOrThrow("role"))
            )
        } else null
        cursor.close()
        db.close()
        return user
    }

    fun getUserById(id: Int): User? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Users WHERE id=?", arrayOf(id.toString()))
        val user = if (cursor.moveToFirst()) {
            User(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                fullName = cursor.getString(cursor.getColumnIndexOrThrow("fullName")),
                emailOrPhone = cursor.getString(cursor.getColumnIndexOrThrow("emailOrPhone")),
                password = cursor.getString(cursor.getColumnIndexOrThrow("password")),
                role = cursor.getString(cursor.getColumnIndexOrThrow("role"))
            )
        } else null
        cursor.close()
        db.close()
        return user
    }

    fun updateUserName(id: Int, name: String): Boolean {
        val db = writableDatabase
        val values = ContentValues()
        values.put("fullName", name)
        val result = db.update("Users", values, "id=?", arrayOf(id.toString()))
        db.close()
        return result > 0
    }

    fun updateUserEmail(id: Int, email: String): Boolean {
        val db = writableDatabase
        val values = ContentValues()
        values.put("emailOrPhone", email)
        val result = db.update("Users", values, "id=?", arrayOf(id.toString()))
        db.close()
        return result > 0
    }

    fun updateUserPassword(id: Int, password: String): Boolean {
        val db = writableDatabase
        val values = ContentValues()
        values.put("password", password)
        val result = db.update("Users", values, "id=?", arrayOf(id.toString()))
        db.close()
        return result > 0
    }



    // Add a question
    fun addQuestion(question: Question): Boolean {
        val db = writableDatabase

        val values = ContentValues().apply {
            put("questionText", question.questionText)
            put("option1", question.options[0])
            put("option2", question.options[1])
            put("option3", question.options[2])
            put("option4", question.options[3])
            put("correctAnswerIndex", question.correctAnswerIndex)
        }

        val result = db.insert("Questions", null, values)
        db.close()

        return result != -1L
    }

    // Get all questions
    fun getAllQuestions(): MutableList<Question> {
        val list = mutableListOf<Question>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Questions", null)

        if (cursor.moveToFirst()) {
            do {
                list.add(
                    Question(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        questionText = cursor.getString(cursor.getColumnIndexOrThrow("questionText")),
                        options = listOf(
                            cursor.getString(cursor.getColumnIndexOrThrow("option1")),
                            cursor.getString(cursor.getColumnIndexOrThrow("option2")),
                            cursor.getString(cursor.getColumnIndexOrThrow("option3")),
                            cursor.getString(cursor.getColumnIndexOrThrow("option4"))
                        ),
                        correctAnswerIndex = cursor.getInt(
                            cursor.getColumnIndexOrThrow("correctAnswerIndex")
                        )
                    )
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return list
    }

    // Update an existing question
    fun updateQuestion(question: Question): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("questionText", question.questionText)
            put("option1", question.options[0])
            put("option2", question.options[1])
            put("option3", question.options[2])
            put("option4", question.options[3])
            put("correctAnswerIndex", question.correctAnswerIndex)
        }

        val result = db.update(
            "Questions",
            values,
            "id=?",
            arrayOf(question.id.toString())
        )
        db.close()
        return result > 0
    }

    // Delete a question by ID
    fun deleteQuestion(id: Int): Boolean {
        val db = writableDatabase
        val result = db.delete(
            "Questions",
            "id=?",
            arrayOf(id.toString())
        )
        db.close()
        return result > 0
    }

}


