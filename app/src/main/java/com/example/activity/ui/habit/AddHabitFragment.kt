package com.example.activity.ui.habit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.activity.databinding.FragmentAddHabitBinding
import com.example.activity.model.HabitCategory
import com.example.activity.repository.HabitRepository
import kotlinx.coroutines.launch

class AddHabitFragment : Fragment() {

    private var _binding: FragmentAddHabitBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: HabitRepository
    private lateinit var categoryList: List<HabitCategory>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddHabitBinding.inflate(inflater, container, false)
        repository = HabitRepository(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadCategories()

        binding.btnCreateHabit.setOnClickListener { createHabit() }

        binding.btnCancelHabit.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun loadCategories() {
        lifecycleScope.launch {
            try {
                categoryList = repository.getCategories()

                val names = categoryList.map { it.name }

                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    names
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerCategory.adapter = adapter

            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Failed to load categories", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun createHabit() {

        val name = binding.etHabitName.text.toString()
        val desc = binding.etHabitDescription.text.toString()
        val goal = binding.etHabitGoal.text.toString()
        val selectedCategoryIndex = binding.spinnerCategory.selectedItemPosition

        if (name.isEmpty() || goal.isEmpty()) {
            Toast.makeText(requireContext(), "Name and Goal are required", Toast.LENGTH_SHORT).show()
            return
        }

        val categoryId = categoryList[selectedCategoryIndex].id

        lifecycleScope.launch {
            try {
                repository.createHabit(
                    name = name,
                    description = desc,
                    goal = goal,
                    categoryId = categoryId
                )

                Toast.makeText(requireContext(), "Habit created!", Toast.LENGTH_SHORT).show()

                findNavController().popBackStack()

            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
