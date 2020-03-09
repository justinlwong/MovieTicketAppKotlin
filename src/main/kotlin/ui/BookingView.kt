package main.ui

import main.model.Seat

interface BookingView {
    fun showSeats(seats: Map<String, Seat>, pitch: Int)
    fun displayMessage(message: String)
}