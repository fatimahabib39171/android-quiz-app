package com.example.myquizapp

import android.os.Bundle
import android.widget.Toast
import android.widget.Button
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myquizapp.adapters.QuestionAdapter
import com.example.myquizapp.models.Question

class ViewQuestionsActivity : AppCompatActivity() {

    private lateinit var rvAllQuestions: RecyclerView
    private lateinit var questionAdapter: QuestionAdapter
    private lateinit var questionList: MutableList<Question>
    private lateinit var dbHelper: DBHelper
    private lateinit var btnBack: Button
    private lateinit var btnDone: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_questions)

        btnBack = findViewById(R.id.btn_back)
        btnDone = findViewById(R.id.btn_done)
        rvAllQuestions = findViewById(R.id.rv_all_questions)
        dbHelper = DBHelper(this)

        // Load all questions from DB
        questionList = try {
            dbHelper.getAllQuestions()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error loading questions", Toast.LENGTH_SHORT).show()
            mutableListOf()
        }

        questionAdapter = QuestionAdapter(questionList, object : QuestionAdapter.QuestionActionListener {
            override fun onEditQuestion(position: Int) {
                val question = questionList[position]
                showEditQuestionDialog(position, question)
            }

            override fun onDeleteQuestion(position: Int) {
                val question = questionList[position]
                showDeleteConfirmation(position, question)
            }
        })

        rvAllQuestions.layoutManager = LinearLayoutManager(this)
        rvAllQuestions.adapter = questionAdapter

        if (questionList.isEmpty()) {
            Toast.makeText(this, "No questions stored yet.", Toast.LENGTH_SHORT).show()
        }

        btnBack.setOnClickListener {
            // Go back to CreateQuizActivity
            finish() // or use Intent if needed
        }

        btnDone.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }

    }

    // Show a confirmation dialog before deleting
    private fun showDeleteConfirmation(position: Int, question: Question) {
        AlertDialog.Builder(this)
            .setTitle("Delete Question")
            .setMessage("Are you sure you want to delete this question?")
            .setPositiveButton("Yes") { _, _ ->
                val success = dbHelper.deleteQuestion(question.id)
                if (success) {
                    questionList.removeAt(position)
                    questionAdapter.notifyItemRemoved(position)
                    Toast.makeText(this, "Question deleted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to delete question", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("No", null)
            .show()
    }

    // Simple edit dialog for updating question text and correct answer
    private fun showEditQuestionDialog(position: Int, question: Question) {
        val dialog = EditQuestionDialog(this, question) { updatedQuestion ->
            val success = dbHelper.updateQuestion(updatedQuestion)
            if (success) {
                questionList[position] = updatedQuestion
                questionAdapter.notifyItemChanged(position)
                Toast.makeText(this, "Question updated", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to update question", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }
}
