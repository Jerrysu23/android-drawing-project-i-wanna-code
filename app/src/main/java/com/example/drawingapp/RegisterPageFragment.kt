package com.example.drawingapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class RegisterPageFragment : Fragment() {
    private val viewModel : DrawingViewModel by activityViewModels(){
        DrawingViewModelFactory((activity?.application as DrawingApplication).drawingRepository)
    }
    private lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = Firebase.auth

        return ComposeView(requireContext()).apply {
            setContent {
                LoginPageContent()
            }
        }
    }

    @Composable
    fun LoginPageContent() {
        Column {
            // Username Field
            var emailText by remember { mutableStateOf("") }
            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    modifier = Modifier.weight(1f),
                    value = emailText,
                    onValueChange = {
                        emailText = it
                    },
                    label = { Text("Email") }
                )
            }

            // Password Field
            var passwordText by remember { mutableStateOf("") }
            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    modifier = Modifier.weight(1f),
                    value = passwordText,
                    onValueChange = {
                        passwordText = it
                    },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation()
                )
            }

            // Confirm Password Field
            var confirmPasswordText by remember { mutableStateOf("") }
            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    modifier = Modifier.weight(1f),
                    value = confirmPasswordText,
                    onValueChange = {
                        confirmPasswordText = it
                    },
                    label = { Text("Confirm Password") },
                    visualTransformation = PasswordVisualTransformation()
                )
            }

            // Register Button
            Button(
                onClick = {
                    if (emailText == "" || passwordText == "" || confirmPasswordText == "") {
                        Toast.makeText(
                            this@RegisterPageFragment.context,
                            "Please enter a username and password",
                            Toast.LENGTH_LONG
                        ).show()
                        return@Button
                    }

                    if (passwordText != confirmPasswordText) {
                        Toast.makeText(
                            this@RegisterPageFragment.context,
                            "Passwords do not match.",
                            Toast.LENGTH_LONG
                        ).show()
                        return@Button
                    }

                    auth.createUserWithEmailAndPassword(emailText, passwordText)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("RegisterPage", "Successfully created account for $emailText")
                                viewModel.loggedIn.postValue(true)
                                viewModel.loggedInEmail.postValue(auth.currentUser?.email)
                                findNavController().navigate(R.id.finishRegister)
                            } else {
                                Log.w("RegisterPage", "Could not create account", task.exception)
                                Toast.makeText(
                                    this@RegisterPageFragment.context,
                                    "Authentication failed.",
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }
                        }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create Account")
            }
        }
    }
}