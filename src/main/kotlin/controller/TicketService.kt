package main.controller

interface TicketService {
    fun showSeats()
    fun holdSeats(requestedSeats: List<String>, email: String): Int
    fun cancelHold(holdId: Int): Boolean
    fun reserveSeats(holdId: Int, email: String): Boolean
}