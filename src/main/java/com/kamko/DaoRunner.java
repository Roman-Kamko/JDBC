package com.kamko;

import com.kamko.dao.TicketDao;
import com.kamko.entity.Ticket;

import java.math.BigDecimal;

public class DaoRunner {
    public static void main(String[] args) {
//        saveTest();
        deleteTest();
    }

    private static void deleteTest() {
        TicketDao instance = TicketDao.getInstance();
        boolean deleteResult = instance.delete(22);
        System.out.println(deleteResult);
    }

    private static void saveTest() {
        TicketDao instance = TicketDao.getInstance();
        Ticket ticket = new Ticket();
        ticket.setPassengerNo("DAS556");
        ticket.setPassengerName("Test");
        ticket.setFlightId(4);
        ticket.setSeatNo("B3");
        ticket.setCoast(BigDecimal.valueOf(156));

        Ticket savedTicket = instance.save(ticket);
        System.out.println(savedTicket);
    }
}
