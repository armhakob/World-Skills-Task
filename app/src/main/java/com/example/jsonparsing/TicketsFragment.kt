package com.example.jsonparsing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class TicketsFragment:Fragment() {
    private lateinit var buttonCreate: Button
    private lateinit var ticketViewModel: TicketViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tickets, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ticketViewModel = ViewModelProvider(requireActivity()).get(TicketViewModel::class.java)
//        val ticketList = ticketViewModel.openTicketList + ticketViewModel.closeTicketList

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val openingAdapter = OpeningTicketAdapter(requireContext(), ticketViewModel.openTicketList){item->
            openDetailFragment(item)
        }
        val closingAdapter = ClosingTicketAdapter(requireContext(), ticketViewModel.closeTicketList){item->
            openDetailFragment(item)
        }
        val adapter = ConcatAdapter(openingAdapter, closingAdapter)
        recyclerView.adapter = adapter
        val itemTouchHelperCallback = object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                return makeMovementFlags(dragFlags, 0)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                openingAdapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
                closingAdapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            }

        }

        val forDelete = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }


            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                val ticketName = when(viewHolder) {
                    is OpeningTicketAdapter.TicketViewHolder -> {
                        val deletedOpeningTicket: Ticket =
                            ticketViewModel.openTicketList.get(position)
                        ticketViewModel.openTicketList.removeAt(position)
                        openingAdapter.notifyItemRemoved(position)

                        deletedOpeningTicket.name
                    }
                    is ClosingTicketAdapter.TicketViewHolder -> {

                        val deletedClosingTicket: Ticket =
                            ticketViewModel.closeTicketList.get((position))
                        ticketViewModel.closeTicketList.removeAt(position)
                        closingAdapter.notifyItemRemoved(position)

                        deletedClosingTicket.name
                    }
                    else -> ""
                }



                Snackbar.make(
                    recyclerView,
                    "Deleted " + ticketName,
                    Snackbar.LENGTH_LONG
                )
                Snackbar.make(
                    recyclerView,
                    "Deleted " + ticketName,
                    Snackbar.LENGTH_LONG
                )

            }
        }


        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        val itemTouchHelper2 = ItemTouchHelper(forDelete)
        itemTouchHelper2.attachToRecyclerView(recyclerView)

        buttonCreate = view.findViewById(R.id.createTicketButton)
        buttonCreate.setOnClickListener{
            val fragment = CreateTicketFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragment, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun openDetailFragment(item: Ticket) {
        val fragment = TicketDetailsFragment.newInstance(item)
        parentFragmentManager.beginTransaction()
            .replace(R.id.flFragment, fragment)
            .addToBackStack(null)
            .commit()
    }

//    override fun onPause() {
//        super.onPause()
//        val adapter = (view?.findViewById<RecyclerView>(R.id.recyclerView)?.adapter as? TicketAdapter) ?: return
//        val updatedList = adapter.getUpdatedList()
//        ticketViewModel.openTicketList = updatedList.filter { it.type == "Opening" }.toMutableList()
//        ticketViewModel.closeTicketList = updatedList.filter { it.type == "Closing" }.toMutableList()
//    }
}
