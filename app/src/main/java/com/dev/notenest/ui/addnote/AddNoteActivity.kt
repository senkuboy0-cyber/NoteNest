package com.dev.notenest.ui.addnote

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dev.notenest.R
import com.dev.notenest.databinding.ActivityAddNoteBinding

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var viewModel: AddNoteViewModel
    private var noteId: Int = -1
    private var selectedPriority: Int = 0

    companion object {
        const val EXTRA_NOTE_ID = "extra_note_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[AddNoteViewModel::class.java]

        noteId = intent.getIntExtra(EXTRA_NOTE_ID, -1)

        setupToolbar()
        setupPriorityChips()
        setupSaveButton()
        setupDeleteButton()
        observeViewModel()

        if (noteId != -1) {
            viewModel.loadNote(noteId)
            binding.btnDelete.visibility = View.VISIBLE
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = if (noteId == -1) getString(R.string.add_note_title) else getString(R.string.edit_note_title)

        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupPriorityChips() {
        binding.chipLow.isChecked = true

        binding.chipLow.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedPriority = 0
                binding.chipMedium.isChecked = false
                binding.chipHigh.isChecked = false
            }
        }

        binding.chipMedium.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedPriority = 1
                binding.chipLow.isChecked = false
                binding.chipHigh.isChecked = false
            }
        }

        binding.chipHigh.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedPriority = 2
                binding.chipLow.isChecked = false
                binding.chipMedium.isChecked = false
            }
        }
    }

    private fun setupSaveButton() {
        binding.btnSave.setOnClickListener {
            val title = binding.editTitle.text.toString().trim()
            val description = binding.editDescription.text.toString().trim()

            if (title.isEmpty()) {
                binding.editTitle.error = "Title cannot be empty"
                return@setOnClickListener
            }

            viewModel.saveNote(
                title = title,
                description = description,
                priority = selectedPriority,
                noteId = noteId
            )
        }
    }

    private fun setupDeleteButton() {
        binding.btnDelete.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(R.string.delete_dialog_title)
                .setMessage(R.string.delete_dialog_message)
                .setPositiveButton(R.string.delete) { _, _ ->
                    viewModel.deleteNote(noteId)
                    finish()
                }
                .setNegativeButton(R.string.cancel, null)
                .show()
        }
    }

    private fun observeViewModel() {
        viewModel.note.observe(this) { note ->
            note?.let {
                binding.editTitle.setText(it.title)
                binding.editDescription.setText(it.description)
                selectedPriority = it.priority

                when (it.priority) {
                    0 -> binding.chipLow.isChecked = true
                    1 -> binding.chipMedium.isChecked = true
                    2 -> binding.chipHigh.isChecked = true
                }
            }
        }

        viewModel.saveResult.observe(this) { success ->
            if (success) {
                Toast.makeText(this, R.string.note_saved, Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}