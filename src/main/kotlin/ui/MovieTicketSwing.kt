package main.ui

import main.model.Seat
import java.awt.Dimension
import java.lang.StringBuilder
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.SwingConstants.LEADING

class MovieTicketSwing(title: String) : JFrame(), BookingView {

    private val seatMap: JLabel = JLabel("", null, LEADING).apply {
        minimumSize = Dimension(400, 400)
        isOpaque = true
    }

    init {
        createUI(title)
    }

    private fun createUI(title: String) {
        createSeatMap()

        setTitle(title)
        setSize(400, 400)
        defaultCloseOperation = EXIT_ON_CLOSE
        setLocationRelativeTo(null)
    }

    private fun createSeatMap() {
        add(seatMap)
    }

    override fun showSeats(seats: Map<String, Seat>, pitch: Int) {
        val map = StringBuilder()
        seats.values.forEachIndexed{ i, seat ->
            val seatStr = when {
                seat.taken -> "XX"
                else -> seat.seatId
            }
            map.append("[$seatStr] ")
            if (i % pitch == pitch - 1) {
                map.appendln("\n")
            }
        }

        seatMap.text = map.toString()
    }

    override fun displayMessage(message: String) {
        //TODO("Not yet implemented")
    }
}
