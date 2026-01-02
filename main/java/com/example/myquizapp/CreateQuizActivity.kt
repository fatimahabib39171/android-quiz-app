package com.example.myquizapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myquizapp.adapters.QuestionAdapter
import com.example.myquizapp.models.Question
import android.content.Intent

class CreateQuizActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var questionList: MutableList<Question>
    private lateinit var questionAdapter: QuestionAdapter

    private lateinit var etQuestionText: EditText
    private lateinit var etOption1: EditText
    private lateinit var etOption2: EditText
    private lateinit var etOption3: EditText
    private lateinit var etOption4: EditText
    private lateinit var rgCorrectAnswer: RadioGroup
    private lateinit var rvQuestionsList: RecyclerView
    private lateinit var btnAddQuestion: Button
    private lateinit var btnSaveQuiz: Button
    private lateinit var btnViewQuestions: Button     // FIXED: was missing


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_quiz)

        dbHelper = DBHelper(this)

        // Load existing questions safely
        questionList = mutableListOf()

        initializeViews()

        questionAdapter = QuestionAdapter(questionList)
        rvQuestionsList.layoutManager = LinearLayoutManager(this)
        rvQuestionsList.adapter = questionAdapter

        btnAddQuestion.setOnClickListener { addQuestion() }
        btnSaveQuiz.setOnClickListener { saveQuestionsToDB() }
        btnViewQuestions.setOnClickListener {
            val intent = Intent(this, ViewQuestionsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initializeViews() {
        etQuestionText = findViewById(R.id.et_question_text)
        etOption1 = findViewById(R.id.et_option_1)
        etOption2 = findViewById(R.id.et_option_2)
        etOption3 = findViewById(R.id.et_option_3)
        etOption4 = findViewById(R.id.et_option_4)
        rgCorrectAnswer = findViewById(R.id.rg_correct_answer)
        rvQuestionsList = findViewById(R.id.rv_questions_list)
        btnAddQuestion = findViewById(R.id.btn_add_question)
        btnSaveQuiz = findViewById(R.id.btn_save_quiz)
        btnViewQuestions = findViewById(R.id.btn_view_quiz)
    }

    private fun addQuestion() {
        val questionText = etQuestionText.text.toString().trim()
        val optionA = etOption1.text.toString().trim()
        val optionB = etOption2.text.toString().trim()
        val optionC = etOption3.text.toString().trim()
        val optionD = etOption4.text.toString().trim()

        if (questionText.isEmpty() || optionA.isEmpty() || optionB.isEmpty() ||
            optionC.isEmpty() || optionD.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedId = rgCorrectAnswer.checkedRadioButtonId
        if (selectedId == -1) {
            Toast.makeText(this, "Select correct answer.", Toast.LENGTH_SHORT).show()
            return
        }

        val correctIndex = when (selectedId) {
            R.id.rb_correct_1 -> 0
            R.id.rb_correct_2 -> 1
            R.id.rb_correct_3 -> 2
            R.id.rb_correct_4 -> 3
            else -> 0
        }

        val newQuestion = Question(
            id = 0,
            questionText = questionText,
            options = listOf(optionA, optionB, optionC, optionD),
            correctAnswerIndex = correctIndex
        )

        questionList.add(newQuestion)
        questionAdapter.notifyItemInserted(questionList.size - 1)

        clearFields()
        Toast.makeText(this, "Question added!", Toast.LENGTH_SHORT).show()
    }

    private fun clearFields() {
        etQuestionText.text.clear()
        etOption1.text.clear()
        etOption2.text.clear()
        etOption3.text.clear()
        etOption4.text.clear()
        rgCorrectAnswer.clearCheck()
    }

    private fun saveQuestionsToDB() {
        if (questionList.isEmpty()) {
            Toast.makeText(this, "Add questions first.", Toast.LENGTH_SHORT).show()
            return
        }

        var savedCount = 0
        for (q in questionList)
            if (dbHelper.addQuestion(q)) savedCount++

        Toast.makeText(this, "$savedCount questions saved to database!", Toast.LENGTH_LONG).show()

        questionList.clear()
        questionAdapter.notifyDataSetChanged()
    }
}
