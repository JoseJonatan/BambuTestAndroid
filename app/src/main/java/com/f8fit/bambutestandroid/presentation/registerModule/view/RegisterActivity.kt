package com.f8fit.bambutestandroid.presentation.registerModule.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.f8fit.bambutestandroid.data.repository.AuthResultState
import com.f8fit.bambutestandroid.databinding.ActivityRegisterBinding
import com.f8fit.bambutestandroid.presentation.loginModule.view.LoginActivity
import com.f8fit.bambutestandroid.presentation.loginModule.viewModel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {


    private lateinit var binding: ActivityRegisterBinding
    private val vm: AuthViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val pass = binding.etPassword.text.toString().trim()
            val confirm = binding.etConfirmPassword.text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(this, "El nombre es obligatorio", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (email.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            if (pass != confirm) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            vm.register(name = name, email= email, password = pass)
        }

        lifecycleScope.launchWhenStarted {
            vm.state.collect { state ->
                when (state) {
                    is AuthResultState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is AuthResultState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this@RegisterActivity, "Registro exitoso. Ahora inicia sesión", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                        finish() // vuelve a LoginActivity
                    }
                    is AuthResultState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this@RegisterActivity, state.message ?: "Error en registro", Toast.LENGTH_SHORT).show()
                    }
                    null -> {}
                }
            }
        }
    }
}