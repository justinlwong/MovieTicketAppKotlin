package main.model

class Holdings() {
    private val unconfirmedBookings: MutableMap<Int, SeatHold> = mutableMapOf()
    private var bookingIndex = 0

    fun addHolding(requestedSeats: List<String>, email: String): Int {
        val newHolding = SeatHold(bookingIndex, requestedSeats, email)
        unconfirmedBookings[bookingIndex] = newHolding
        bookingIndex += 1
        return newHolding.holdId
    }

    fun removeHolding(holdId: Int) {
        unconfirmedBookings.remove(holdId)
    }

    fun getHolding(holdId: Int): SeatHold? = unconfirmedBookings[holdId]
}