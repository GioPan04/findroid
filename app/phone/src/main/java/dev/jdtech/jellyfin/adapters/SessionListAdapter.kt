package dev.jdtech.jellyfin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.jdtech.jellyfin.databinding.SessionItemBinding
import dev.jdtech.jellyfin.models.FindroidSession

class SessionListAdapter(
    private val onClickListener: (session: FindroidSession) -> Unit
) : ListAdapter<FindroidSession, SessionListAdapter.SessionViewHolder>(DiffCallback) {

    class SessionViewHolder(private var binding: SessionItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(session: FindroidSession) {
            binding.sessionDeviceName.text = "${session.deviceName} - ${session.client}"
            binding.sessionUserName.text = session.userName
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<FindroidSession>() {
        override fun areContentsTheSame(
            oldItem: FindroidSession,
            newItem: FindroidSession
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areItemsTheSame(oldItem: FindroidSession, newItem: FindroidSession): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        return SessionViewHolder(SessionItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        val session = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener(session)
        }

        holder.bind(session)
    }
}