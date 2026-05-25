package com.dev.notenest.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dev.notenest.R
import com.dev.notenest.databinding.ActivityMainBinding
import com.dev.notenest.ui.adapter.NoteAdapter
import com.dev.notenest.ui.addnote.AddNoteActivity
import com.dev.notenest.ui.settings.SettingsActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setupToolbar()
        setupRecyclerView()
        setupFab()
        observeViewModel()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
    }

    private fun setupRecyclerView() {
        noteAdapter = NoteAdapter(
            onItemClick = { note ->
                val intent = Intent(this, AddNoteActivity::class.java)
                intent.putExtra(AddNoteActivity.EXTRA_NOTE_ID, note.id)
                startActivity(intent)
            },
            onPinClick = { note ->
                viewModel.togglePin(note)
            },
            onLongClick = { note ->
                viewModel.toggleComplete(note)
            }
        )

        binding.recyclerNotes.apply {
            adapter = noteAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        // Swipe to delete
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val note = noteAdapter.currentList[position]

                viewModel.deleteNote(note)

                Snackbar.make(binding.root, R.string.note_deleted, Snackbar.LENGTH_LONG)
                    .setAction(R.string.undo) {
                        viewModel.setSearchQuery("") // Refresh list
                    }
                    .show()
            }
        }

        ItemTouchHelper(swipeHandler).attachToRecyclerView(binding.recyclerNotes)
    }

    private fun setupFab() {
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AddNoteActivity::class.java))
        }
    }

    private fun observeViewModel() {
        viewModel.notes.observe(this) { notes ->
            noteAdapter.submitList(notes)

            if (notes.isEmpty()) {
                binding.emptyView.visibility = View.VISIBLE
                binding.recyclerNotes.visibility = View.GONE
            } else {
                binding.emptyView.visibility = View.GONE
                binding.recyclerNotes.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.queryHint = getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setSearchQuery(newText ?: "")
                return true
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sort_date -> {
                viewModel.setSortType(MainViewModel.SortType.DATE)
                true
            }
            R.id.sort_priority -> {
                viewModel.setSortType(MainViewModel.SortType.PRIORITY)
                true
            }
            R.id.sort_alphabetical -> {
                viewModel.setSortType(MainViewModel.SortType.ALPHABETICAL)
                true
            }
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
