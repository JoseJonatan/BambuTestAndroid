package com.f8fit.bambutestandroid.presentation.loginModule.view

import android.content.Intent
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.f8fit.bambutestandroid.MainActivity
import com.f8fit.bambutestandroid.R
import com.f8fit.bambutestandroid.data.repository.AuthResultState
import com.f8fit.bambutestandroid.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import com.f8fit.bambutestandroid.presentation.loginModule.viewModel.AuthViewModel
import com.f8fit.bambutestandroid.presentation.registerModule.view.RegisterActivity
import com.google.firebase.auth.FirebaseAuth


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val vm: AuthViewModel by viewModels()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        // Si ya hay usuario logueado en Firebase
        auth.currentUser?.let {
            // Intentamos login biométrico
            showBiometricPrompt()
        }


        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vm.currentUserId()?.let {
            if (it.isNotEmpty()) {
                goToMain()
                return
            }
        }

        binding.tvGoRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val pass = binding.etPassword.text.toString().trim()


            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Rellena email y password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            vm.login(email, pass)
        }

        binding.btnGuest.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        lifecycleScope.launchWhenStarted {
            vm.state.collect { state ->
                when (state) {
                    is AuthResultState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is AuthResultState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        Toast.makeText(this@LoginActivity, "Login OK", Toast.LENGTH_SHORT).show()
                    }

                    is AuthResultState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this@LoginActivity, state.message ?: "Error", Toast.LENGTH_SHORT).show()
                    }

                    null -> {}
                }
            }
        }
    }

    private fun showBiometricPrompt() {
        val biometricManager = BiometricManager.from(this)

        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                val executor = ContextCompat.getMainExecutor(this)
                val biometricPrompt = BiometricPrompt(
                    this,
                    executor,
                    object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                            super.onAuthenticationError(errorCode, errString)
                            Toast.makeText(this@LoginActivity, "Error biométrico: $errString", Toast.LENGTH_SHORT).show()
                        }

                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                            super.onAuthenticationSucceeded(result)
                            // ✅ Autenticación correcta
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        }

                        override fun onAuthenticationFailed() {
                            super.onAuthenticationFailed()
                            Toast.makeText(this@LoginActivity, "Autenticación fallida", Toast.LENGTH_SHORT).show()
                        }
                    }
                )

                val promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Inicia sesión biométrica")
                    .setSubtitle("Usa tu huella o rostro para entrar")
                    .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                    .setNegativeButtonText("Usar correo y contraseña")
                    .build()

                biometricPrompt.authenticate(promptInfo)
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Toast.makeText(this, "El dispositivo no tiene hardware biométrico", Toast.LENGTH_SHORT).show()
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Toast.makeText(this, "Hardware biométrico no disponible", Toast.LENGTH_SHORT).show()
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Toast.makeText(this, "No hay huella o rostro registrados", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Biometría no soportada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goToMain() {
        Toast.makeText(this, "Ir a Main (implementación futura)", Toast.LENGTH_SHORT).show()
    }
}