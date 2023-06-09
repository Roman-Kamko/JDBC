package com.kamko;

import com.kamko.util.ConnectionManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JdbcStarter {
    public static void main(String[] args) throws SQLException {
        Integer flightId = 1;
        List<Integer> ticketsByFlightId = getTicketsByFlightId(flightId);
        System.out.println(ticketsByFlightId);

        List<Integer> flightBetween = getFlightsBetween(
                LocalDateTime.of(2020, 10, 1, 0, 0),
                LocalDateTime.now()
                );
        System.out.println(flightBetween);

        try {
            getMetaInfo();
        } finally {
            ConnectionManager.closeConnection();
        }
    }

    private static void getMetaInfo() throws SQLException {
        try (Connection connection = ConnectionManager.getConnection()){
             DatabaseMetaData metaData = connection.getMetaData();
             ResultSet resultSet = metaData.getCatalogs();
             while (resultSet.next()) {
                 System.out.println(resultSet.getString("TABLE_CAT"));
             }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Integer> getFlightsBetween(LocalDateTime start, LocalDateTime end) throws SQLException {
        String sql = """
                SELECT id
                FROM flight
                WHERE departure_date BETWEEN ? AND ?;
                """;

        List<Integer> result = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setTimestamp(1, Timestamp.valueOf(start));
            preparedStatement.setTimestamp(2, Timestamp.valueOf(end));

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                result.add(resultSet.getObject("id", Integer.class));
            }
        }
        return result;
    }

    private static List<Integer> getTicketsByFlightId(Integer flightId) throws SQLException {

        String sql = """
                SELECT id
                FROM ticket
                WHERE flight_id = ?;
                """;

        List<Integer> result = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, flightId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
//                result.add(resultSet.getInt("id"));
                result.add(resultSet.getObject("id", Integer.class)); // NULL safe
            }
        }
        return result;
    }
}
