package main.model

class Theater(numRows: Int, numCols: Int) {
    val seatMap: MutableMap<String, Seat> = mutableMapOf()
    val pitch = numCols

    init {
        (0 until numRows).forEach { row ->
            (1..numCols).forEach {col ->
                val rowName: Char = 'A' + row
                val seatId = "$rowName$col"
                // Create Seat
                val seat = Seat(seatId, false)
                seatMap[seatId] = seat
            }
        }
    }

    fun mutateSeats(seatIds: List<String>, hold: Boolean): Boolean {
        seatIds.forEach { seatId ->
            // Return false if seat is invalid or taken
            val seat = seatMap[seatId] ?: return@mutateSeats false
            if (hold && seat.taken) {
                return@mutateSeats false
            }
            // Mutate seat if not taken or not trying to hold
            seatMap[seatId] = seat.copy(taken = hold)
        }
        return true
    }
}
