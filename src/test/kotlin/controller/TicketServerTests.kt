package controller

import main.controller.TicketServer
import main.model.Holdings
import main.model.SeatHold
import main.model.Theater
import main.ui.BookingView
import main.ui.TerminalBookingView
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

private const val SEAT_1 = "B1"
private const val SEAT_2 = "B2"
private const val BOOKING_ID_1 = 0
private const val BOOKING_ID_2 = 1
private const val EMAIL = "a@gmail.com"
private val LIST_1 = listOf(SEAT_1, SEAT_2)
private val SEAT_HOLD = SeatHold(BOOKING_ID_1, LIST_1, EMAIL)

@RunWith(MockitoJUnitRunner::class)
class TicketServerTests {

    private val view: BookingView = mock(TerminalBookingView::class.java)
    private val theater: Theater = mock(Theater::class.java)
    private val holdings: Holdings = mock(Holdings::class.java)
    private val ticketServer = TicketServer(theater, holdings, view)

    @Test
    fun testHoldSeatsSuccess() {
        `when`(theater.mutateSeats(LIST_1, true)).thenReturn(true)
        `when`(holdings.addHolding(LIST_1, EMAIL)).thenReturn(BOOKING_ID_1)
        val holdId = ticketServer.holdSeats(LIST_1, EMAIL)
        verify(theater).mutateSeats((LIST_1), true)
        verify(holdings).addHolding(LIST_1, EMAIL)
        assertEquals(BOOKING_ID_1, holdId)
    }

    @Test
    fun testHoldSeatsFailure() {
        `when`(theater.mutateSeats(LIST_1, true)).thenReturn(false)
        val holdId = ticketServer.holdSeats(LIST_1, EMAIL)
        verify(theater).mutateSeats((LIST_1), true)
        verifyNoInteractions(holdings)
        assertEquals(-1, holdId)
    }

    @Test
    fun testReserveSeatsSuccess() {
        `when`(holdings.getHolding(BOOKING_ID_1)).thenReturn(SEAT_HOLD)
        val success = ticketServer.reserveSeats(BOOKING_ID_1, EMAIL)
        verify(holdings).getHolding(BOOKING_ID_1)
        verify(holdings).removeHolding(BOOKING_ID_1)
        assertTrue(success)
    }

    @Test
    fun testReserveSeatsFailure() {
        `when`(holdings.getHolding(BOOKING_ID_1)).thenReturn(null)
        val success = ticketServer.reserveSeats(BOOKING_ID_1, EMAIL)
        verify(holdings).getHolding(BOOKING_ID_1)
        verify(holdings, never()).removeHolding(BOOKING_ID_1)
        assertFalse(success)
    }

    @Test
    fun testCancelHoldSuccess() {
        `when`(holdings.getHolding(BOOKING_ID_1)).thenReturn(SEAT_HOLD)
        val success = ticketServer.cancelHold(BOOKING_ID_1)
        verify(theater).mutateSeats(LIST_1, false)
        verify(holdings).getHolding(BOOKING_ID_1)
        verify(holdings).removeHolding(BOOKING_ID_1)
        assertTrue(success)
    }

    @Test
    fun testCancelHoldFailure() {
        `when`(holdings.getHolding(BOOKING_ID_1)).thenReturn(null)
        val success = ticketServer.cancelHold(BOOKING_ID_1)
        verify(holdings).getHolding(BOOKING_ID_1)
        verifyNoInteractions(theater)
        verifyNoMoreInteractions(holdings)
        assertFalse(success)
    }
}
