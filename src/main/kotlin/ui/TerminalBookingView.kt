package main.ui

import main.model.Seat

class TerminalBookingView: BookingView {
    override fun showSeats(seats: Map<String, Seat>, pitch: Int) {
        seats.values.forEachIndexed{ i, seat ->
            val seatStr = when {
                seat.taken -> "XX"
                else -> seat.seatId
            }
            print("[$seatStr] ")
            if (i % pitch == pitch - 1) {
                println("\n")
            }
        }
    }

    override fun displayMessage(message: String) {
        println(message)
    }
}