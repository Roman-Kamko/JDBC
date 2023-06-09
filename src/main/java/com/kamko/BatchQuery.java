package com.kamko;

import com.kamko.util.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class BatchQuery {
    public static void main(String[] args) throws SQLException {
        int flightId = 8;

        String deleteFlightSql = "DELETE FROM flight WHERE id = " + flightId;
        String deleteTicketSql = "DELETE FROM ticket WHERE flight_id = " + flightId;

        Connection connection = null;
        Statement statement = null;

        try {
            connection = ConnectionManager.getConnection();
            statement = connection.createStatement();

            connection.setAutoCommit(false); // берем управление транзакциями на себя

            statement.addBatch(deleteTicketSql);
            statement.addBatch(deleteFlightSql);

            statement.executeBatch();

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
            if (statement != null) {
                statement.close();
            }
        }
    }
}

