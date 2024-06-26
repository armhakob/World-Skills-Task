package com.example.jsonparsing

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TicketAdapter(private val context: Context, private val items: MutableList<Ticket>, val onClick: (item: Ticket) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    companion object {
        const val VIEW_TYPE_OPENING_HEADER = 0
        const val VIEW_TYPE_CLOSING_HEADER = 1
        const val VIEW_TYPE_TICKET = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is Ticket -> {
                if (items[position].type == "Opening") VIEW_TYPE_OPENING_HEADER
                else if (items[position].type == "Opening") VIEW_TYPE_CLOSING_HEADER
                else VIEW_TYPE_TICKET
            }
            else -> throw IllegalArgumentException("Invalid item type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_OPENING_HEADER -> HeaderViewHolder(
                LayoutInflater.from(context).inflate(R.layout.opening_header, parent, false)
            )
            VIEW_TYPE_CLOSING_HEADER -> HeaderViewHolder(
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
}
