package model

import main.model.Theater
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TheaterTests {

    private val theater = Theater(NUM_ROWS, NUM_COLS)

    companion object {
        const val NUM_ROWS = 3
        const val NUM_COLS = 3
        const val SEAT_1 = "B1"
        const val SEAT_2 = "B2"
        const val ILLEGAL_SEAT = "D4"
    }

    @Test
    fun testMutateSeatsHold() {
        assertTrue(theater.mutateSeats(listOf(SEAT_1, SEAT_2), true))
        assertFalse(theater.mutateSeats(listOf(SEAT_1, SEAT_2), true))
        assertFalse(theater.mutateSeats(listOf(ILLEGAL_SEAT), true))
    }

    @Test
    fun testMutateSeatsClear() {
        assertTrue(theater.mutateSeats(listOf(SEAT_1, SEAT_2), true))
        assertTrue(theater.mutateSeats(listOf(SEAT_1, SEAT_2), false))
        assertTrue(theater.mutateSeats(listOf(SEAT_1, SEAT_2), true))
    }
}
