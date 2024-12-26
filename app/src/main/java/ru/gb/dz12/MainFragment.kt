package ru.gb.dz12

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import ru.gb.dz12.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private var _binding: FragmentMainBinding? = null       //так будет для фрагмента
    private val binding get() = _binding!!

    //    private lateinit var binding: FragmentMainBinding     //так будет для активити
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.request.doOnTextChanged { text, start, before, count ->
            checkText()
        }
        return binding.root
    }

    private fun checkText() {
        if (binding.request.length() > 3) {
            binding.button.isEnabled = true
        } else binding.button.isEnabled = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
            val request = binding.request.text.toString()
            viewModel.onSignInClick(request)
        }
        viewLifecycleOwner.lifecycleScope
            .launchWhenStarted {
                viewModel.state
                    .collect { state ->
                        when (state) {
                            State.Loading -> {
                                status(true, null)
                                binding.button.isEnabled = false
                            }

                            State.Success -> {
                                status(false, null)
                                checkText()
                            }

                            is State.Error -> {
                                binding.progress.isVisible = false
                                binding.textView.text = state.requestError
                                checkText()
                            }
                        }
                    }
            }
    }
    private fun status(t: Boolean, s: String?) {
        binding.progress.isVisible = t
        binding.requestLayout.error = s
    }
}