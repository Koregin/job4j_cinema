package ru.job4j.cinema.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class SessionStore {
    private final BasicDataSource pool;

    public SessionStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Session> findAllSessions() {
        List<Session> sessions = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM sessions")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    sessions.add(new Session(it.getInt("id"), it.getString("name")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sessions;
    }

    public List<Ticket> selectAllTicketsForSession(Session session) {
        List<Ticket> tickets = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM ticket WHERE session_id = ?")
        ) {
            ps.setInt(1, session.getId());
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    tickets.add(new Ticket(it.getInt("id"), it.getInt("line"), it.getInt("cell")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    public Optional<Session> findSessionById(int sessionId) {
        Session session = new Session();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM sessions WHERE id = ?")
        ) {
            ps.setInt(1, sessionId);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    session.setId(it.getInt("id"));
                    session.setName(it.getString("name"));
                }
            }
        } catch (SQLException e) {
            return Optional.empty();
        }
        return Optional.of(session);
    }

    public Optional<Ticket> buyTicket(int sessionId, int row, int cell, int userId) {
        Ticket ticket = new Ticket(0, row, cell);
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO ticket(session_id, line, cell, user_id) VALUES (?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, sessionId);
            ps.setInt(2, row);
            ps.setInt(3, cell);
            ps.setInt(4, userId);
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    ticket.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            Optional.empty();
        }
        return Optional.of(ticket);
    }

    public boolean checkTicketForSession(int sessionId, int row, int cell) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM ticket WHERE session_id = ? AND line = ? AND cell = ?")
        ) {
            ps.setInt(1, sessionId);
            ps.setInt(2, row);
            ps.setInt(3, cell);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
