package com.example.myquizapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import android.widget.Button

class DashboardActivity : AppCompatActivity() {

    private lateinit var tvGreeting: TextView
    private lateinit var cardProfile: CardView
    private lateinit var cardCreateQuiz: CardView
    private lateinit var cardStartQuiz: CardView
    private lateinit var cardViewResults: CardView
    private lateinit var cardLeaderboard: CardView
    private lateinit var btnLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Initialize views
        tvGreeting = findViewById(R.id.tvGreeting)
        cardProfile = findViewById(R.id.cardProfile)
        cardCreateQuiz = findViewById(R.id.cardCreateQuiz)
        cardStartQuiz = findViewById(R.id.cardStartQuiz)
        cardViewResults = findViewById(R.id.cardViewResults)
        cardLeaderboard = findViewById(R.id.cardLeaderboard)
        btnLogout = findViewById(R.id.btnLogout)

        // ------------------------------
        // GET USER DATA FROM LOGIN
        // ------------------------------
        val fullName = intent.getStringExtra("FULL_NAME") ?: "User"
        val role = intent.getStringExtra("ROLE") ?: "Student"

        // ------------------------------
        // SET GREETING
        // ------------------------------
        tvGreeting.text = "Welcome, $fullName"

        // ------------------------------
        // ROLE-BASED VISIBILITY
        // Teachers see "Create Quiz"
        // Students do NOT
        // ------------------------------
        if (role == "Teacher") {
            cardCreateQuiz.visibility = View.VISIBLE
        } else {
            cardCreateQuiz.visibility = View.GONE
        }

        // ------------------------------
        // CLICK LISTENERS
        // ------------------------------

        cardProfile.setOnClickListener {
            Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        cardCreateQuiz.setOnClickListener {
            Toast.makeText(this, "Create Quiz clicked", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, CreateQuizActivity::class.java))
        }

        cardStartQuiz.setOnClickListener {
            Toast.makeText(this, "Start Quiz clicked", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, AttemptQuizActivity::class.java))
        }

        cardViewResults.setOnClickListener {
            Toast.makeText(this, "View Results clicked", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, ResultActivity::class.java))
        }

        cardLeaderboard.setOnClickListener {
            Toast.makeText(this, "Leaderboard clicked", Toast.LENGTH_SHORT).show()
        }

        btnLogout.setOnClickListener {
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
