package com.example.myquizapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.view.MenuItem
import com.example.myquizapp.User // Needed to reference the data class

class ProfileActivity : AppCompatActivity() {

    // 1. Declare Views and Database Helper
    private lateinit var editTextUsername: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonUpdate: Button
    private lateinit var textViewChangePicture: TextView

    // Initialize the DBHelper instance
    private lateinit var dbHelper: DBHelper

    // Hardcoded User ID for demonstration (Replace with actual logged-in user ID)
    private val currentUserId: Int = 1

    // Variable to hold the user's current data
    private var currentUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // 2. Action Bar/Toolbar Setup
        supportActionBar?.title = "Edit profile"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 3. Initialize Views and Database Helper
        editTextUsername = findViewById(R.id.editTextUsername)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPhone = findViewById(R.id.editTextPhone)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonUpdate = findViewById(R.id.buttonUpdate)
        textViewChangePicture = findViewById(R.id.textViewChangePicture)

        // Initialize the database helper
        dbHelper = DBHelper(this)

        // 4. Load Current User Data from DB
        loadCurrentUserData()

        // 5. Set Listeners
        textViewChangePicture.setOnClickListener {
            Toast.makeText(this, "Profile Picture change option selected.", Toast.LENGTH_SHORT).show()
        }

        // Call the new saveProfileData function when Update is clicked
        buttonUpdate.setOnClickListener {
            saveProfileData()
        }
    }

    /**
     * Loads the current user's details from the database and populates the EditText fields.
     */
    private fun loadCurrentUserData() {
        // Retrieve the User object using the ID
        currentUser = dbHelper.getUserById(currentUserId)

        if (currentUser != null) {
            // Populate the fields with data from the User object
            editTextUsername.setText(currentUser!!.fullName)
            editTextEmail.setText(currentUser!!.emailOrPhone)
            // Note: Never display actual password, just set dummy text or leave empty
            editTextPassword.setText("")
        } else {
            // If the user isn't found (e.g., first run), show a message
            Toast.makeText(this, "Error: User ID $currentUserId not found.", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Retrieves data from the EditText fields and saves it back to the database.
     */
    private fun saveProfileData() {
        val newUsername = editTextUsername.text.toString()
        val newEmail = editTextEmail.text.toString()
        val newPassword = editTextPassword.text.toString()

        if (currentUser == null) {
            Toast.makeText(this, "Cannot update profile: User data missing.", Toast.LENGTH_SHORT).show()
            return
        }

        var updateSuccess = true

        // 1. Update Username if changed
        if (newUsername != currentUser!!.fullName) {
            if (!dbHelper.updateUserName(currentUserId, newUsername)) {
                updateSuccess = false
            }
        }

        // 2. Update Email if changed
        if (newEmail != currentUser!!.emailOrPhone) {
            if (!dbHelper.updateUserEmail(currentUserId, newEmail)) {
                updateSuccess = false
            }
        }

        // 3. Update Password if changed (Check if user entered something other than "")
        // WARNING: In a real app, you must compare the entered password to the hash, or have a dedicated change password field.
        if (newPassword != "" && newPassword.isNotEmpty()) {
            if (!dbHelper.updateUserPassword(currentUserId, newPassword)) {
                updateSuccess = false
            }
        }

        // 4. Show result
        if (updateSuccess) {
            Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
            // Reload data to refresh the currentUser object after successful update
            loadCurrentUserData()
        } else {
            Toast.makeText(this, "Error updating profile. Check database operation.", Toast.LENGTH_LONG).show()
        }
    }

    // Back Arrow Functionality
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    // Optional: Include onOptionsItemSelected for robustness
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}