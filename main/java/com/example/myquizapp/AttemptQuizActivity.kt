package com.example.myquizapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myquizapp.models.Question

class AttemptQuizActivity : AppCompatActivity() {

    private lateinit var tvQuestion: TextView
    private lateinit var rgOptions: RadioGroup
    private lateinit var rb1: RadioButton
    private lateinit var rb2: RadioButton
    private lateinit var rb3: RadioButton
    private lateinit var rb4: RadioButton
    private lateinit var btnPrev: Button
    private lateinit var btnNext: Button

    private lateinit var dbHelper: DBHelper
    private lateinit var questions: List<Question>

    private var currentIndex = 0
    private var score = 0

    // key = question index, value = selected option index (0–3)
    private val selectedAnswers = HashMap<Int, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attempt_quiz)

        initViews()

        dbHelper = DBHelper(this)
        questions = dbHelper.getAllQuestions()

        if (questions.isEmpty()) {
            Toast.makeText(this, "No questions found. Please create quiz first.", Toast.LENGTH_LONG)
                .show()
            finish()
            return
        }

        loadQuestion()

        btnNext.setOnClickListener {
            saveAnswer()

            if (currentIndex < questions.size - 1) {
                currentIndex++
                loadQuestion()
            } else {
                calculateScore()
                openResultActivity()
            }
        }

        btnPrev.setOnClickListener {
            if (currentIndex > 0) {
                saveAnswer()
                currentIndex--
                loadQuestion()
            }
        }
    }

    private fun initViews() {
        tvQuestion = findViewById(R.id.tvQuestion)
        rgOptions = findViewById(R.id.rgOptions)
        rb1 = findViewById(R.id.rbA)
        rb2 = findViewById(R.id.rbB)
        rb3 = findViewById(R.id.rbC)
        rb4 = findViewById(R.id.rbD)
        btnPrev = findViewById(R.id.btnPrev)
        btnNext = findViewById(R.id.btnNext)
    }

    private fun loadQuestion() {
        val question = questions[currentIndex]

        rgOptions.clearCheck()

        tvQuestion.text = "Q${currentIndex + 1}: ${question.questionText}"

        rb1.text = question.options[0]
        rb2.text = question.options[1]
        rb3.text = question.options[2]
        rb4.text = question.options[3]

        // Restore selected answer
        when (selectedAnswers[currentIndex]) {
            0 -> rb1.isChecked = true
            1 -> rb2.isChecked = true
            2 -> rb3.isChecked = true
            3 -> rb4.isChecked = true
        }

        btnPrev.isEnabled = currentIndex != 0
        btnNext.text = if (currentIndex == questions.size - 1) "Submit" else "Next"
    }

    private fun saveAnswer() {
        val selectedIndex = when (rgOptions.checkedRadioButtonId) {
            rb1.id -> 0
            rb2.id -> 1
            rb3.id -> 2
            rb4.id -> 3
            else -> -1
        }

        if (selectedIndex != -1) {
            selectedAnswers[currentIndex] = selectedIndex
        }
    }

    private fun calculateScore() {
        score = 0
        for (i in questions.indices) {
            if (selectedAnswers[i] == questions[i].correctAnswerIndex) {
                score++
            }
        }
    }

    private fun openResultActivity() {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("totalQuestions", questions.size)
        intent.putExtra("correctAnswers", score)
        startActivity(intent)
        finish()
    }
}
