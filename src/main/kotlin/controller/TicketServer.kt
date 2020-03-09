package main.controller

import main.model.Holdings
import main.model.SeatHold
import main.model.Theater
import main.ui.BookingView
import java.util.*

class TicketServer(private val theater: Theater,
                   private val holdings: Holdings,
                   private val view: BookingView): TicketService {

    companion object {
        const val EXPIRATION_TIME: Long = 5000
    }

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
    override fun cancelHold(holdId: Int): Boolean {
        val seatHold = holdings.getHolding(holdId) ?: return false
        theater.mutateSeats(seatHold.seatsHeld, false)
        holdings.removeHolding(holdId)
        return true
    }

    @Synchronized
    override fun reserveSeats(holdId: Int, email: String): Boolean {
        val seatHold: SeatHold? = holdings.getHolding(holdId)
        if (seatHold == null) {
            view.displayMessage("Invalid id, your reservation hold might have expired")
            return false
        }
        holdings.removeHolding(holdId)
        view.displayMessage("Seats are booked!")
        return true
    }
}
