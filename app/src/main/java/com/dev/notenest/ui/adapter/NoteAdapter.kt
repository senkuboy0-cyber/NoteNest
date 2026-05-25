package com.dev.notenest.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dev.notenest.R
import com.dev.notenest.data.local.entity.NoteEntity
import com.dev.notenest.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NoteAdapter(
    private val onItemClick: (NoteEntity) -> Unit,
    private val onPinClick: (NoteEntity) -> Unit,
    private val onLongClick: (NoteEntity) -> Unit
) : ListAdapter<NoteEntity, NoteAdapter.NoteViewHolder>(NoteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note)
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val ivPin: ImageView = itemView.findViewById(R.id.ivPin)

        fun bind(note: NoteEntity) {
            tvTitle.text = note.title
            tvDescription.text = note.description
            tvDate.text = DateUtils.formatDate(note.timestamp)

            // Show pin icon if pinned
            ivPin.visibility = if (note.isPinned) View.VISIBLE else View.GONE

            // Set priority indicator color
            val priorityColor = when (note.priority) {
                2 -> Color.parseColor("#E53935") // High - Red
                1 -> Color.parseColor("#FFA726") // Medium - Orange
                else -> Color.parseColor("#66BB6A") // Low - Green
            }
            itemView.findViewById<View>(R.id.priorityIndicator)?.setBackgroundColor(priorityColor)

            // Click listeners
            itemView.setOnClickListener { onItemClick(note) }
            ivPin.setOnClickListener { onPinClick(note) }
            itemView.setOnLongClickListener {
                onLongClick(note)
                true
            }
        }
    }

    class NoteDiffCallback : DiffUtil.ItemCallback<NoteEntity>() {
        override fun areItemsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
            return oldItem == newItem
        }
    }
}