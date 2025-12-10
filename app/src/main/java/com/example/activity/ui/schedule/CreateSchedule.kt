package com.example.activity.ui.schedule

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.activity.databinding.FragmentCreateScheduleBinding
import com.example.activity.model.CreateScheduleRequest
import com.example.activity.model.HabitResponse
import com.example.activity.repository.ScheduleRepository
import kotlinx.coroutines.launch
import java.util.Calendar

class CreateScheduleFragment : Fragment() {

    private var _binding: FragmentCreateScheduleBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: ScheduleRepository
    private lateinit var habitList: List<HabitResponse>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateScheduleBinding.inflate(inflater, container, false)
        repository = ScheduleRepository(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadHabits()
        setupRepeatSpinner()
        setupDatePicker()
        setupTimePickers()

        binding.btnCreateSchedule.setOnClickListener {
            createSchedule()
        }

        binding.btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun loadHabits() {
        lifecycleScope.launch {
            try {
                habitList = repository.getAllHabits()

                val names = habitList.map { it.name }

                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    names
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                binding.spinnerHabits.adapter = adapter

            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Failed to load habits", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupRepeatSpinner() {
        val patterns = listOf("none", "daily", "weekly", "monthly")

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            patterns
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerRepeat.adapter = adapter
    }

    private fun setupDatePicker() {
        val calendar = Calendar.getInstance()

        binding.etDate.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    val m = (month + 1).toString().padStart(2, '0')
                    val d = dayOfMonth.toString().padStart(2, '0')
                    binding.etDate.setText("$year-$m-$d")
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun setupTimePickers() {

        binding.etStartTime.setOnClickListener {
            val cal = Calendar.getInstance()
            TimePickerDialog(
                requireContext(),
                { _, hour, minute ->
                    binding.etStartTime.setText(
                        "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
                    )
                },
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }

        binding.etEndTime.setOnClickListener {
            val cal = Calendar.getInstance()
            TimePickerDialog(
                requireContext(),
                { _, hour, minute ->
                    binding.etEndTime.setText(
                        "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
                    )
                },
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }
    }

    private fun createSchedule() {

        val habitIndex = binding.spinnerHabits.selectedItemPosition
        val habitId = habitList[habitIndex].id

        val date = binding.etDate.text.toString()
        val startTime = binding.etStartTime.text.toString()
        val endTime = binding.etEndTime.text.toString()
        val repeat = binding.spinnerRepeat.selectedItem.toString()
        val notes = binding.etNotes.text.toString()

        if (date.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
            Toast.makeText(requireContext(), "Date and times are required", Toast.LENGTH_SHORT).show()
            return
        }

        val request = CreateScheduleRequest(
            habitId = habitId,
            date = date,
            start_time = "${date}T${startTime}:00.000Z",
            end_time = "${date}T${endTime}:00.000Z",
            is_custom = true,
            notes = notes
        )

        lifecycleScope.launch {
            try {
                repository.createCustomSchedule(request)
                Toast.makeText(requireContext(), "Schedule created!", Toast.LENGTH_SHORT).show()
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
