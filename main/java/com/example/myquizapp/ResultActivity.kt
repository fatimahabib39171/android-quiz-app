package com.example.myquizapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {

    private lateinit var tvScore: TextView
    private lateinit var tvCorrect: TextView
    private lateinit var tvIncorrect: TextView
    private lateinit var llQuestions: LinearLayout
    private lateinit var btnRestart: Button
    private lateinit var btnExit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // Initialize views
        tvScore = findViewById(R.id.text_view_score)
        tvCorrect = findViewById(R.id.text_view_correct_answers)
        tvIncorrect = findViewById(R.id.text_view_incorrect_answers)
        llQuestions = findViewById(R.id.linear_layout_questions)
        btnRestart = findViewById(R.id.button_restart_quiz)
        btnExit = findViewById(R.id.button_exit)

        // Receive data from AttemptQuizActivity
        val totalQuestions = intent.getIntExtra("totalQuestions", 0)
        val correctAnswersCount = intent.getIntExtra("correctAnswers", 0)
        val questions = intent.getStringArrayListExtra("questions") ?: arrayListOf()
        val correctAnswerList = intent.getStringArrayListExtra("correctAnswerList") ?: arrayListOf()

        val incorrectAnswersCount = totalQuestions - correctAnswersCount

        // Show score
        tvScore.text = "$correctAnswersCount / $totalQuestions"
        tvCorrect.text = "Correct Answers: $correctAnswersCount"
        tvIncorrect.text = "Incorrect Answers: $incorrectAnswersCount"

        // Show questions with correct answers
        llQuestions.removeAllViews()

        for (i in questions.indices) {
            val tv = TextView(this)
            tv.text = "Q${i + 1}: ${questions[i]}\nCorrect Answer: ${correctAnswerList.getOrNull(i) ?: "N/A"}"
            tv.textSize = 16f
            tv.setPadding(0, 12, 0, 12)
            llQuestions.addView(tv)
        }

        // Re-take Quiz
        btnRestart.setOnClickListener {
            startActivity(Intent(this, AttemptQuizActivity::class.java))
            finish()
        }

        // Exit to Dashboard
        btnExit.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }
    }
}
