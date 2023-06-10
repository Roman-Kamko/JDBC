package com.kamko.entity;

import java.math.BigDecimal;

public class Ticket {
    private int id;
    private String passengerNo;
    private String passengerName;
    private Flight flight;
    private String seatNo;
    private BigDecimal coast;

    public Ticket(int id, String passengerNo, String passengerName, Flight flight, String seatNo, BigDecimal coast) {
        this.id = id;
        this.passengerNo = passengerNo;
        this.passengerName = passengerName;
        this.flight = flight;
        this.seatNo = seatNo;
        this.coast = coast;
    }

    public Ticket() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassengerNo() {
        return passengerNo;
    }

    public void setPassengerNo(String passengerNo) {
        this.passengerNo = passengerNo;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public BigDecimal getCoast() {
        return coast;
    }

    public void setCoast(BigDecimal coast) {
        this.coast = coast;
    }

    @Override
    public String toString() {
        return "Ticket{" +
               "id=" + id +
               ", passengerNo='" + passengerNo + '\'' +
               ", passengerName='" + passengerName + '\'' +
               ", flight=" + flight +
               ", seatNo='" + seatNo + '\'' +
               ", coast=" + coast +
               '}';
    }
}
