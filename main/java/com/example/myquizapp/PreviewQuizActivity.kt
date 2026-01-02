package com.example.myquizapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PreviewQuizActivity : AppCompatActivity() {

    // Define a constant for the key used to pass the difficulty level
    companion object {
        const val EXTRA_DIFFICULTY = "extraDifficulty"
    }

    // Define the views globally for easier access in methods
    private lateinit var radioGroupDifficulty: RadioGroup
    private lateinit var buttonStartQuiz: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview_quiz) // Ensure you have this layout file

        // 1. Initialize Views
        radioGroupDifficulty = findViewById(R.id.radio_group_difficulty)
        buttonStartQuiz = findViewById(R.id.button_start_quiz)

        // 2. Set up the Start Button Listener
        buttonStartQuiz.setOnClickListener {
            startQuiz()
        }
    }

    /**
     * Handles the logic for starting the main QuizActivity.
     */
    private fun startQuiz() {
        // 1. Determine selected difficulty
        val selectedId = radioGroupDifficulty.checkedRadioButtonId

        // Check if a radio button is selected
        if (selectedId == -1) {
            Toast.makeText(this, "Please select a difficulty level.", Toast.LENGTH_SHORT).show()
            return
        }

        // Get the selected RadioButton object
        val selectedRadioButton: RadioButton = findViewById(selectedId)
        // The difficulty string is taken from the text property of the selected RadioButton
        val difficulty = selectedRadioButton.text.toString()

        // 2. Create the Intent to launch the QuizActivity
        val intent = Intent(this, PreviewQuizActivity::class.java)

        // 3. Pass the selected difficulty to the QuizActivity
        intent.putExtra(EXTRA_DIFFICULTY, difficulty)

        // 4. Start the quiz
        startActivity(intent)
        // Optionally, you can call finish() if you don't want the user to navigate back here
        // from the QuizActivity by pressing the back button.
        // finish()
    }
}