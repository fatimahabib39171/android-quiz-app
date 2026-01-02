package com.example.myquizapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var edtEmailOrPhone: EditText
    private lateinit var edtPassword: EditText
    private lateinit var chkShowPassword: CheckBox
    private lateinit var btnLogin: Button
    private lateinit var txtSignup: TextView

    private lateinit var dbHelper: DBHelper

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbHelper = DBHelper(this)

        edtEmailOrPhone = findViewById(R.id.edt_email_or_phone)
        edtPassword = findViewById(R.id.edt_password)
        chkShowPassword = findViewById(R.id.chk_show_password)
        btnLogin = findViewById(R.id.btn_login)
        txtSignup = findViewById(R.id.txt_signup)

        chkShowPassword.setOnCheckedChangeListener { _, isChecked ->
            edtPassword.inputType =
                if (isChecked)
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                else
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        btnLogin.setOnClickListener {
            val emailOrPhone = edtEmailOrPhone.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            when {
                emailOrPhone.isEmpty() ->
                    Toast.makeText(this, "Enter email or phone", Toast.LENGTH_SHORT).show()

                password.length < 8 ->
                    Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()

                else -> {
                    val user = dbHelper.getUser(emailOrPhone)

                    if (user == null) {
                        Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                    } else if (user.password == password) {
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this, DashboardActivity::class.java)
                        intent.putExtra("FULL_NAME", user.fullName)
                        intent.putExtra("ROLE", user.role)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Incorrect Password", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        txtSignup.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}
