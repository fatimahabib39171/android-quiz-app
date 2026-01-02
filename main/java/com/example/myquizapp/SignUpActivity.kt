package com.example.myquizapp

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class SignUpActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        dbHelper = DBHelper(this)

        val edtFullName = findViewById<EditText>(R.id.edt_full_name)
        val edtEmailOrPhone = findViewById<EditText>(R.id.edt_email_or_phone)
        val edtPassword = findViewById<EditText>(R.id.edt_password)
        val edtConfirmPassword = findViewById<EditText>(R.id.edt_confirm_password)
        val chkShowPassword = findViewById<CheckBox>(R.id.chk_show_password)
        val rgUserRole = findViewById<RadioGroup>(R.id.rg_user_role)
        val btnSignup = findViewById<Button>(R.id.btn_signup)
        val txtLogin = findViewById<TextView>(R.id.txt_login)

        chkShowPassword.setOnCheckedChangeListener { _, isChecked ->
            val type = if (isChecked)
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            else
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

            edtPassword.inputType = type
            edtConfirmPassword.inputType = type
        }

        btnSignup.setOnClickListener {
            val fullName = edtFullName.text.toString().trim()
            val emailOrPhone = edtEmailOrPhone.text.toString().trim()
            val password = edtPassword.text.toString()
            val confirmPassword = edtConfirmPassword.text.toString()
            val selectedRoleId = rgUserRole.checkedRadioButtonId

            when {
                fullName.isEmpty() ->
                    Toast.makeText(this, "Enter full name", Toast.LENGTH_SHORT).show()

                emailOrPhone.isEmpty() ->
                    Toast.makeText(this, "Enter email or phone", Toast.LENGTH_SHORT).show()

                password.length < 8 ->
                    Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()

                password != confirmPassword ->
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()

                selectedRoleId == -1 ->
                    Toast.makeText(this, "Select a role", Toast.LENGTH_SHORT).show()

                else -> {
                    val role = findViewById<RadioButton>(selectedRoleId).text.toString()

                    val inserted = dbHelper.addUser(fullName, emailOrPhone, password, role)

                    if (inserted) {
                        Toast.makeText(this, "Signup Successful!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "User already exists!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        txtLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
