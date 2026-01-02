package com.example.myquizapp

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.*
import com.example.myquizapp.models.Question

class EditQuestionDialog(
    context: Context,
    private val question: Question,
    private val onQuestionUpdated: (Question) -> Unit
) : Dialog(context) {

    private lateinit var etQuestionText: EditText
    private lateinit var etOption1: EditText
    private lateinit var etOption2: EditText
    private lateinit var etOption3: EditText
    private lateinit var etOption4: EditText
    private lateinit var rgCorrectAnswer: RadioGroup
    private lateinit var rb1: RadioButton
    private lateinit var rb2: RadioButton
    private lateinit var rb3: RadioButton
    private lateinit var rb4: RadioButton
    private lateinit var btnUpdate: Button
    private lateinit var btnCancel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_question_dialog)

        etQuestionText = findViewById(R.id.et_question_text)
        etOption1 = findViewById(R.id.et_option_1)
        etOption2 = findViewById(R.id.et_option_2)
        etOption3 = findViewById(R.id.et_option_3)
        etOption4 = findViewById(R.id.et_option_4)
        rgCorrectAnswer = findViewById(R.id.rg_correct_answer)
        rb1 = findViewById(R.id.rb_correct_1)
        rb2 = findViewById(R.id.rb_correct_2)
        rb3 = findViewById(R.id.rb_correct_3)
        rb4 = findViewById(R.id.rb_correct_4)
        btnUpdate = findViewById(R.id.btn_update_question)
        btnCancel = findViewById(R.id.btn_cancel_update)

        // Pre-fill existing question
        etQuestionText.setText(question.questionText)
        etOption1.setText(question.options[0])
        etOption2.setText(question.options[1])
        etOption3.setText(question.options[2])
        etOption4.setText(question.options[3])
        when (question.correctAnswerIndex) {
            0 -> rb1.isChecked = true
            1 -> rb2.isChecked = true
            2 -> rb3.isChecked = true
            3 -> rb4.isChecked = true
        }

        btnUpdate.setOnClickListener {
            val updatedQuestionText = etQuestionText.text.toString().trim()
            val updatedOptions = listOf(
                etOption1.text.toString().trim(),
                etOption2.text.toString().trim(),
                etOption3.text.toString().trim(),
                etOption4.text.toString().trim()
            )

            val selectedId = rgCorrectAnswer.checkedRadioButtonId
            if (updatedQuestionText.isEmpty() || updatedOptions.any { it.isEmpty() } || selectedId == -1) {
                Toast.makeText(context, "Please fill all fields and select correct answer", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val correctIndex = when (selectedId) {
                R.id.rb_correct_1 -> 0
                R.id.rb_correct_2 -> 1
                R.id.rb_correct_3 -> 2
                R.id.rb_correct_4 -> 3
                else -> 0
            }

            val updatedQuestion = question.copy(
                questionText = updatedQuestionText,
                options = updatedOptions,
                correctAnswerIndex = correctIndex
            )

            onQuestionUpdated(updatedQuestion)
            dismiss()
        }

        btnCancel.setOnClickListener { dismiss() }
    }
}
