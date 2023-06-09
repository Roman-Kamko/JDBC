package com.kamko;

import com.kamko.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionRunner {
    public static void main(String[] args) throws SQLException {
        int flightId = 9;

        String deleteFlightSql = "DELETE FROM flight WHERE id = ?";
        String deleteTicketSql = "DELETE FROM ticket WHERE flight_id = ?";

        Connection connection = null;
        PreparedStatement deleteFlightStatement = null;
        PreparedStatement deleteTicketStatement = null;

        try {
            connection = ConnectionManager.getConnection();
            deleteFlightStatement = connection.prepareStatement(deleteFlightSql);
            deleteTicketStatement = connection.prepareStatement(deleteTicketSql);

            connection.setAutoCommit(false); // берем управление транзакциями на себя

            deleteFlightStatement.setInt(1, flightId);
            deleteTicketStatement.setInt(1, flightId);

            deleteTicketStatement.executeUpdate();

            if (true) {
                throw new RuntimeException("Ooops"); // имитация эксепшена, с автокомитом получили бы две транзакции
                                                     // причем транзакция с удалением билетов исполнилась бы, что херова,
                                                     // все или ничего
            }

            deleteFlightStatement.executeUpdate();

            connection.commit();  // закрываем транзакцию

        } catch (Exception e) {
            if (connection != null) { // канэкшен может быть не проинициализирован
                connection.rollback();
            }
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
            // обязаны проверить на null, чтобы не словить NPE
            if (deleteTicketStatement != null) {
                deleteTicketStatement.close();
            }
            if (deleteFlightStatement != null) {
                deleteFlightStatement.close();
            }
        }
    }
}
