package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.store.SessionStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionService {
    private final SessionStore store;

    public SessionService(SessionStore store) {
        this.store = store;
    }

    public Map<Integer, List<Integer>> calcFreeSeatsForSession(Session session) {
        List<Ticket> sessionTickets = store.selectAllTicketsForSession(session);
        Map<Integer, List<Integer>> seats = new ConcurrentHashMap<>();
        /* Initializing hall seats */
        for (int i = 1; i <= 10; i++) {
            /* Creating rows */
            seats.put(i, new ArrayList<>());
            /* Creating cells */
            for (int j = 1; j <= 10; j++) {
                seats.get(i).add(j);
            }
        }
        /* Deleting occupied seats */
        for (Ticket ticket : sessionTickets) {
            int row = ticket.getRow();
            if (seats.containsKey(row)) {
                seats.get(row).removeIf(cell -> cell == ticket.getCell());
            }
        }
        return seats;
    }

    public List<Session> findAllSessions() {
        return store.findAllSessions();
    }

    public Session findSessionById(int sessionId) {
        return store.findSessionById(sessionId);
    }

    public boolean buyTicket(int sessionId, int row, int cell, int userId) {
        boolean result = false;
        if (store.checkTicketForSession(sessionId, row, cell)) {
            store.buyTicket(sessionId, row, cell, userId);
            result = true;
        }
        return result;
    }
}
