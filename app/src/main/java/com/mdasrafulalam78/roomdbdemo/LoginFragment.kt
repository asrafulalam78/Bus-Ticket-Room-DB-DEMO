package com.mdasrafulalam78.roomdbdemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.mdasrafulalam78.roomdbdemo.databinding.FragmentLoginBinding
import com.mdasrafulalam78.roomdbdemo.databinding.FragmentNewScheduleBinding
import com.mdasrafulalam78.roomdbdemo.model.UserModel
import com.mdasrafulalam78.roomdbdemo.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var preference: LoginPreference
    private val loginViewModel: LoginViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.clear()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        preference = LoginPreference(requireContext())
        binding =  FragmentLoginBinding.inflate(layoutInflater, container, false)
        loginViewModel.errMsgLD.observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
        }
        binding.loginBtn.setOnClickListener {
            var email:String
            var password: String
            if (binding.emailInputET.text.isEmpty() || binding.passwordInputET.text.isEmpty()){
                loginViewModel.errMsgLD.value = "Please enter EMAIL and PASSWORD"
            }else{
                 email = binding.emailInputET.text.toString()
                 password = binding.passwordInputET.text.toString()
                loginViewModel.login(email, password) {
                    lifecycle.coroutineScope.launch {
                        preference.setLoginStatus(true, it, requireContext())
                        findNavController().popBackStack()
                    }
                }
            }

        }
        binding.regBtn.setOnClickListener {
            val email = binding.emailInputET.text.toString()
            val password = binding.passwordInputET.text.toString()
            val user = UserModel(
                email = email, password = password
            )
            loginViewModel.register(user){
                lifecycle.coroutineScope.launch {
                    preference.setLoginStatus(true, it, requireContext())
                    findNavController().popBackStack()
                }
            }
        }
        return binding.root
    }
}