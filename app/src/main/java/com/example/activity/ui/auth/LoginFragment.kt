package com.example.activity.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.activity.R
import com.example.activity.utils.SessionManager
import androidx.navigation.fragment.findNavController
import com.example.activity.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {
    // Binding reference (cleared when view is destroyed)
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()

    var lastLoginEmail: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate binding
        _binding = FragmentLoginBinding.inflate(
            inflater, container,
            false
        )
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            Log.d("LoginFragment", "Email: $email, Password: $password")
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Email and Password are required",
                    Toast.LENGTH_SHORT).show()
            } else {
                viewModel.login(email, password)
            }
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                binding.buttonLogin.isEnabled = false
                binding.buttonLogin.text = "Logging in..."
            } else {
                binding.buttonLogin.isEnabled = true
                binding.buttonLogin.text = "Login"
            }
        }

        viewModel.authResult.observe(viewLifecycleOwner) { result ->
            Log.d("LoginFragment", "authResult: $result")
            result.onSuccess { authResponse ->
                // Handle successful login (e.g., navigate to home screen)
                Toast.makeText(requireContext(), "Welcome ${authResponse.user.email}", Toast.LENGTH_LONG).show()
                Log.d("LoginFragment", "Access token: ${authResponse.tokens.accessToken}")
                val session = SessionManager(requireContext().applicationContext)
                session.saveAuthToken(authResponse.tokens.accessToken)

                // Navigate to HomeFragment

                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }.onFailure { error ->
                // Handle login failure
                Toast.makeText(requireContext(), "Login failed: ${error.message}",
                    Toast.LENGTH_LONG).show()
            }
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        // Avoid memory leaks
        _binding = null
    }
}