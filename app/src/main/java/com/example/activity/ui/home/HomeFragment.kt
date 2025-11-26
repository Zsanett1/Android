package com.example.activity.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.activity.databinding.FragmentHomeBinding
import com.example.activity.repository.ScheduleRepository
import java.time.LocalDate

class HomeViewModelFactory(
    private val context: Context) : ViewModelProvider.Factory {
        override fun <T :ViewModel> create(modelClass: Class<T>) : T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                val repository = ScheduleRepository(context)
                return  HomeViewModel(repository) as T
            }
            throw  IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

class HomeFragment: Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: HomeScheduleAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
// Initialize the ViewModel using the custom factory
        val factory = HomeViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupObservers()
    }

    private fun setupUi() {
        adapter = HomeScheduleAdapter()
        binding.rvSchedules.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSchedules.adapter = adapter
        binding.rvSchedules.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        val today = try {
            LocalDate.now().toString()
        } catch (_: Exception) {
            "2025-10-26"
        }
        viewModel.getScheduleByDay(today)
    }

    private fun setupObservers() {
        viewModel.schedules.observe(viewLifecycleOwner) { schedules ->
            if (!schedules.isNullOrEmpty()) {
                adapter.submitList(schedules)
                binding.tvEmpty.visibility = View.GONE
            } else {
                adapter.submitList(emptyList())
                binding.tvEmpty.visibility = View.VISIBLE
            }
        }

        //viewModel.isLoading.observe

        //viewModel.errorMessage.observe
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}