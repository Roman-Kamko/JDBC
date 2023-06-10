package com.kamko.dao;

import com.kamko.dto.TicketFilter;
import com.kamko.entity.Flight;
import com.kamko.entity.Ticket;
import com.kamko.exceptions.DaoException;
import com.kamko.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.*;

//CRUD operation with JDBC
public class TicketDao implements Dao<Integer, Ticket> {
    // singleton
    private static final TicketDao INSTANCE = new TicketDao();
    private final FlightDao flightDao = FlightDao.getInstance();
    private static final String GET_ALL = """
            SELECT ticket.id,
                   passenger_no,
                   passenger_name,
                   flight_id,
                   seat_no,
                   coast,
                   f.status,
                   f.flight_no,
                   f.aircraft_id,
                   f.arrival_airport_code,
                   f.arrival_date,
                   f.departure_airport_code,
                   f.departure_date
            FROM ticket
            JOIN flight f
                ON ticket.flight_id = f.id
            """;
    private static final String FIND_BY_ID_SQL = GET_ALL + """
            WHERE ticket.id = ?;
            """;
    private static final String DELETE_SQL = """
            DELETE 
            FROM ticket
            WHERE id = ?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO ticket (passenger_no, 
                                passenger_name, 
                                flight_id, 
                                seat_no, 
                                coast)
            VALUES (?, ?, ?, ?, ?)
            """;
    private static final String UPDATE_SQL = """
            UPDATE ticket
            SET passenger_no = ?,
                passenger_name = ?,
                flight_id = ?,
                seat_no = ?,
                coast = ?
            WHERE id = ?;
            """;

    private TicketDao() {
    }

    public List<Ticket> getAll(TicketFilter ticketFilter) {
        List<Object> parameters = new ArrayList<>();
        List<String> whereSql = new ArrayList<>();
        if (ticketFilter.seatNo() != null) {
            whereSql.add("seat_no LIKE ?");
            parameters.add("%" + ticketFilter.seatNo() + "%");
        }
        if (ticketFilter.passengerName() != null) {
            whereSql.add("passenger_name = ?");
            parameters.add(ticketFilter.passengerName());
        }
        parameters.add(ticketFilter.limit());
        parameters.add(ticketFilter.offset());
        String where = whereSql.stream()
                .collect(joining(" AND ", " WHERE ", " LIMIT ? OFFSET ? "));
        String sql = GET_ALL + where;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Ticket> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(getTicket(resultSet));
            }
            return result;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public List<Ticket> getAll() {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Ticket> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(getTicket(resultSet));
            }
            return result;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Optional<Ticket> findById(Integer id) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            Ticket ticket = null;

            if (resultSet.next()) {
                ticket = getTicket(resultSet);
            }

            return Optional.ofNullable(ticket);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public void update(Ticket ticket) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {

            preparedStatement.setString(1, ticket.getPassengerNo());
            preparedStatement.setString(2, ticket.getPassengerName());
            preparedStatement.setInt(3, ticket.getFlight().id());
            preparedStatement.setString(4, ticket.getSeatNo());
            preparedStatement.setBigDecimal(5, ticket.getCoast());
            preparedStatement.setInt(6, ticket.getId());

            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }


    public boolean delete(Integer id) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Ticket save(Ticket ticket) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, ticket.getPassengerNo());
            preparedStatement.setString(2, ticket.getPassengerName());
            preparedStatement.setInt(3, ticket.getFlight().id());
            preparedStatement.setString(4, ticket.getSeatNo());
            preparedStatement.setBigDecimal(5, ticket.getCoast());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                ticket.setId(generatedKeys.getInt(1));
            }

            return ticket;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private Ticket getTicket(ResultSet resultSet) throws SQLException {
        return new Ticket(
                resultSet.getInt("id"),
                resultSet.getString("passenger_no"),
                resultSet.getString("passenger_name"),
                flightDao.findById(resultSet.getInt("flight_id"),
                        resultSet.getStatement().getConnection()).orElse(null),
                resultSet.getString("seat_no"),
                resultSet.getBigDecimal("coast")
        );
    }

    public static TicketDao getInstance() {
        return INSTANCE;
    }
}