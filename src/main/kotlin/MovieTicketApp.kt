package main

import main.controller.TicketServer
import main.model.Holdings
import main.model.Theater
import main.ui.MovieTicketSwing

fun main(args: Array<String>) {
    // Models
    val theater = Theater(10, 10)
    val holdings = Holdings()
    val view = MovieTicketSwing("Movie Ticket App")
    view.isVisible = true

    // Controller
    val controller = TicketServer(theater, holdings, view)

    controller.showSeats()
}
