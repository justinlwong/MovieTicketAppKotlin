package main.controller

import main.model.Holdings
import main.model.SeatHold
import main.model.Theater
import main.ui.BookingView
import java.util.*

private const val EXPIRATION_TIME: Long = 5000

class TicketServer(private val theater: Theater,
                   private val holdings: Holdings,
                   private val view: BookingView): TicketService {

    override fun showSeats() {
        view.showSeats(theater.seatMap, theater.pitch)
    }

    /**
     * Holds seats and returns holding id
     */
    @Synchronized
    override fun holdSeats(requestedSeats: List<String>, email: String): Int {
        val success = theater.mutateSeats(requestedSeats.toList(), true)
        if (!success) {
            println("Failed to book seats! Seats are either invalid or unavailable!")
            return -1
        }
        // Create holding
        val newHoldId = holdings.addHolding(requestedSeats, email)
        view.displayMessage("Held seats! Your holding id is $newHoldId")

        Timer("Expired", false).schedule(object: TimerTask() {
            override fun run() {
                val cancelled = cancelHold(newHoldId)
                if (cancelled) {
                    println("Seat hold $newHoldId has expired!")
                }
            }
        }, EXPIRATION_TIME)
        return newHoldId
    }

    @Synchronized
    override fun cancelHold(holdId: Int): Boolean =
        holdings.getHolding(holdId)?.let { seatHold ->
            theater.mutateSeats(seatHold.seatsHeld, false)
            holdings.removeHolding(holdId)
            true
        } ?: false

    @Synchronized
    override fun reserveSeats(holdId: Int, email: String): Boolean {
        holdings.getHolding(holdId)?.let {
            holdings.removeHolding(holdId)
            view.displayMessage("Seats are booked!")
            return true
        }
        view.displayMessage("Invalid id, your reservation hold might have expired")
        return false
    }
}
