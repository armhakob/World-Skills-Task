package com.example.jsonparsing

import androidx.lifecycle.ViewModel

data class Ticket(
//    val id: Int,
    val type: String,
    val name: String,
    val imageUri: String?,
    val currentDate: String,
    val seatNumber: String
)

class TicketViewModel : ViewModel() {
    var ticketList: MutableList<Ticket> = mutableListOf()
}
