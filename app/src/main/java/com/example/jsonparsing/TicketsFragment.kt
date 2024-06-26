package com.example.jsonparsing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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
        val ticketList = ticketViewModel.ticketList

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = TicketAdapter(requireContext(), ticketList) { item ->
            openDetailFragment(item)
        }
        recyclerView.adapter = adapter

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
}
