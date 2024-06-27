package com.example.jsonparsing

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class ClosingTicketAdapter(private val context: Context, private val items: MutableList<Ticket>, val onClick: (item: Ticket) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), ItemTouchHelperAdapter {
    companion object {
        const val VIEW_TYPE_HEADER = 0
        const val VIEW_TYPE_TICKET = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is Ticket -> {
                if (items[position].type == "Closing") VIEW_TYPE_HEADER
                else VIEW_TYPE_TICKET
            }
            else -> throw IllegalArgumentException("Invalid item type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> HeaderViewHolder(
                LayoutInflater.from(context).inflate(R.layout.closing_header, parent, false)
            )
            VIEW_TYPE_TICKET -> TicketViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false)
            )
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {

            }
            is TicketViewHolder -> {
                val item = items[position] as Ticket
                holder.bind(item)
                holder.itemView.setOnClickListener {
                    onClick(item)
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view)

    class TicketViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvName: TextView = view.findViewById(R.id.tv_name)
        private val tvSeat: TextView = view.findViewById(R.id.tv_seat)

        fun bind(ticket: Ticket) {
            tvName.text = ticket.name
            tvSeat.text = ticket.seatNumber
        }
    }
    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition == 0 || toPosition == 0)
        {
            Toast.makeText(
                context,
                "You can't move header",
                Toast.LENGTH_SHORT
            ).show()
        }
        else
        {
            if (fromPosition < toPosition) {
                for (i in fromPosition until toPosition) {
                    items.add(i + 1, items.removeAt(i))
                }
            } else {
                for (i in fromPosition downTo toPosition + 1) {
                    items.add(i - 1, items.removeAt(i))
                }
            }
            notifyItemMoved(fromPosition, toPosition)
        }
        return true
    }

    override fun onItemDismiss(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }
}