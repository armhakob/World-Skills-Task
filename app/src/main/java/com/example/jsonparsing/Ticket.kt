package com.example.jsonparsing

import androidx.lifecycle.ViewModel

data class Ticket(
//    val id: Int,
    val type: String,
    val name: String,
    val imageUri: String?,
    val currentDate: String?,
    val seatNumber: String
)

class TicketViewModel : ViewModel() {
    var ticketList: MutableList<Ticket> = mutableListOf()
    var openTicketList: MutableList<Ticket> = mutableListOf()
    var closeTicketList: MutableList<Ticket> = mutableListOf()
    init {
        openTicketList.add(0, Ticket(type = "Opening", name = "", currentDate = "", imageUri = "", seatNumber = ""))
        closeTicketList.add(0, Ticket(type = "Closing", name = "", currentDate = "", imageUri = "", seatNumber = ""))
    }

    fun addOpenTicket(ticket: Ticket) {
        openTicketList.add(1, ticket)
    }

    fun addCloseTicket(ticket: Ticket) {
        closeTicketList.add(1, ticket)
    }


}
