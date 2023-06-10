package com.kamko;

import com.kamko.dao.TicketDao;
import com.kamko.entity.Flight;
import com.kamko.dto.TicketFilter;
import com.kamko.entity.Ticket;

import java.math.BigDecimal;
import java.util.Optional;

public class DaoRunner {
    public static void main(String[] args) {
        TicketDao instance = TicketDao.getInstance();
        System.out.println(instance.findById(2));
    }

    private static void filterTest() {
        TicketFilter ticketFilter = new TicketFilter(3, 0, null, "A");
        TicketDao instance = TicketDao.getInstance();
        System.out.println(instance.getAll(ticketFilter));
    }

    private static void getAllTest() {
        TicketDao instance = TicketDao.getInstance();
        System.out.println(instance.getAll());
    }

    private static void updateTest(Integer id,
                                   BigDecimal coast) {   // для простоты изменим только стоимость билета
        TicketDao instance = TicketDao.getInstance();
        Optional<Ticket> ticket = instance.findById(id);
        ticket.ifPresent(t -> {
            t.setCoast(coast);
            instance.update(t);
        });
    }

    private static void findByIdTest(Integer id) {
        TicketDao instance = TicketDao.getInstance();
        Optional<Ticket> ticket = instance.findById(id);
        System.out.println(ticket);
    }

    private static void deleteTest(Integer id) {
        TicketDao instance = TicketDao.getInstance();
        boolean deleteResult = instance.delete(id);
        System.out.println(deleteResult);
    }

    private static void saveTest(String passengerNo,
                                 String passengerName,
                                 Flight flight,
                                 String seatNo,
                                 BigDecimal coast) {
        TicketDao instance = TicketDao.getInstance();
        Ticket ticket = new Ticket();
        ticket.setPassengerNo(passengerNo);
        ticket.setPassengerName(passengerName);
        ticket.setFlight(flight);
        ticket.setSeatNo(seatNo);
        ticket.setCoast(coast);
        Ticket savedTicket = instance.save(ticket);
        System.out.println(savedTicket);
    }
}
