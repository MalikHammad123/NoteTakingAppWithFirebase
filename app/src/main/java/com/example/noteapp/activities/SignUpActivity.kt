package com.example.noteapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.noteapp.R
import com.example.noteapp.databinding.ActivitySignUpBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore


class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firestore: FirebaseFirestore

    companion object {
        const val RC_SIGN_IN = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.apply {
            // Configure Google Sign-In
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            googleSignInClient = GoogleSignIn.getClient(this@SignUpActivity, gso)

            // Set up SignUp button
            signupButton.setOnClickListener {
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()
                val confirmPassword = confirmPasswordEditText.text.toString()

                // Get selected role
                val selectedRoleId = accountTypeRadioGroup.checkedRadioButtonId
                val selectedRole = when (selectedRoleId) {
                    R.id.noteMakerRadioButton -> "Maker"
                    R.id.noteWorkerRadioButton -> "Worker"
                    else -> null
                }

                if (validateInputs(email, password, confirmPassword, selectedRole)) {
                    if (selectedRole != null) {
                        createAccount(email, password, selectedRole)
                    }
                }
            }

            // Set up Google Sign-In button
            googleSignInButton.setOnClickListener {
                signInWithGoogle()
            }

            // Redirect to sign-in activity
            signinTextView.setOnClickListener {
                startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
            }
        }
    }

    private fun validateInputs(
        email: String,
        password: String,
        confirmPassword: String,
        role: String?
    ): Boolean {
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return false
        }
        if (role == null) {
            Toast.makeText(this, "Please select a role", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun createAccount(email: String, password: String, role: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                val userMap = mapOf(
                    "email" to email,
                    "role" to role
                )
                firestore.collection("users").document(userId).set(userMap)
                    .addOnCompleteListener { firestoreTask ->
                        if (firestoreTask.isSuccessful) {
                            Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT)
                                .show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT)
                                .show()
                        }
                }
            } else {
                Toast.makeText(
                    this,
                    "Failed to create account: ${task.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account?.idToken)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign-in failed: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
    fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val accountType = when (binding.accountTypeRadioGroup.checkedRadioButtonId) {
                        R.id.noteMakerRadioButton -> "Maker"
                        R.id.noteWorkerRadioButton -> "Worker"
                        else -> "Unknown"
                    }
                    uploadUserData(user?.uid, user?.email ?: "unknown", accountType)
                } else {
                    Toast.makeText(
                        this,
                        "Authentication failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun uploadUserData(userId: String?, email: String, accountType: String) {
        val user = mapOf(
            "userId" to userId,
            "email" to email,
            "username" to binding.userNameEditText.text.toString(),
            "accountType" to accountType
        )

        firestore.collection("users").document(userId ?: "unknown")
            .set(user)
            .addOnSuccessListener {
                Toast.makeText(this, "Account created and data uploaded!", Toast.LENGTH_SHORT)
                    .show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(
                    this,
                    "Failed to upload user data: ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}

