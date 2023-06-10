package com.kamko.dao;

import com.kamko.entity.Flight;
import com.kamko.exceptions.DaoException;
import com.kamko.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class FlightDao implements Dao<Integer, Flight> {
    private static final FlightDao INSTANCE = new FlightDao();
    private static final String GET_ALL_SQL = """
            SELECT id,
                   flight_no,
                   departure_date,
                   departure_airport_code,
                   arrival_date,
                   arrival_airport_code,
                   aircraft_id,
                   status
            FROM flight
            """;
    private static final String GET_ALL_BY_ID_SQL = GET_ALL_SQL + """
            WHERE id = ?
            """;

    private FlightDao() {
    }

    public static FlightDao getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Flight> getAll() {
        return null;
    }

    @Override
    public Optional<Flight> findById(Integer id) {
        try (Connection connection = ConnectionManager.getConnection()) {
            return findById(id, connection);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Optional<Flight> findById(Integer id, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            Flight flight = null;
            if (resultSet.next()) {
                flight = getFlight(resultSet);
            }
            return Optional.ofNullable(flight);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void update(Flight flight) {

    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public Flight save(Flight flight) {
        return null;
    }

    private Flight getFlight(ResultSet resultSet) throws SQLException {
        return new Flight(
                resultSet.getInt("id"),
                resultSet.getString("flight_no"),
                resultSet.getTimestamp("departure_date").toLocalDateTime(),
                resultSet.getString("departure_airport_code"),
                resultSet.getTimestamp("arrival_date").toLocalDateTime(),
                resultSet.getString("arrival_airport_code"),
                resultSet.getInt("aircraft_id"),
                resultSet.getString("status")
        );
    }
}
