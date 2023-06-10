package com.kamko.dao;

import com.kamko.dto.TicketFilter;
import com.kamko.entity.Ticket;

import java.util.List;
import java.util.Optional;

public interface Dao<K, E> {
    List<E> getAll();
    Optional<E> findById(K id);
    void update(E e);
    boolean delete(K id);
    E save(E e);
}
