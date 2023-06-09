package com.kamko;

import com.kamko.dao.TicketDao;
import com.kamko.entity.Ticket;

import java.math.BigDecimal;
import java.util.Optional;

public class DaoRunner {
    public static void main(String[] args) {
//        saveTest("DAS556", "Test", 4, "B3", BigDecimal.valueOf(156));
//        deleteTest(22);
//        findByIdTest(2);
//        updateTest(2, BigDecimal.valueOf(188.88));
        getAllTest();
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
                                 Integer flightId,
                                 String seatNo,
                                 BigDecimal coast) {
        TicketDao instance = TicketDao.getInstance();
        Ticket ticket = new Ticket();
        ticket.setPassengerNo(passengerNo);
        ticket.setPassengerName(passengerName);
        ticket.setFlightId(flightId);
        ticket.setSeatNo(seatNo);
        ticket.setCoast(coast);
        Ticket savedTicket = instance.save(ticket);
        System.out.println(savedTicket);
    }
}
