package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.SessionService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@SessionAttributes({"currentSession", "seats", "row", "cell"})
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService service) {
        this.sessionService = service;
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String selectSession(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setUsername("Гость");
        }
        model.addAttribute("user", user);
        List<Session> sessionList = sessionService.findAllSessions();
        model.addAttribute("sessions", sessionList);
        return "index";
    }

    @PostMapping("/selectSession")
    public String selectSession(@RequestParam(value = "sessionId") int sessionId, ModelMap model) {
        Session currentSession = sessionService.findSessionById(sessionId);
        model.put("currentSession", currentSession);
        Map<Integer, List<Integer>> seatsForSession = sessionService.calcFreeSeatsForSession(currentSession);
        model.put("seats", seatsForSession);
        return "selectRow";
    }

    @PostMapping("/selectCell")
    public String selectCell(@RequestParam("row") int row, ModelMap model) {
        model.put("row", row);
        return "selectCell";
    }

    @PostMapping("/purchaseResult")
    public String purchaseResult(@RequestParam("cell") int cell, ModelMap model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        model.addAttribute("user", user);
        model.put("cell", cell);
        Session session = (Session) model.getAttribute("currentSession");
        boolean result = sessionService.buyTicket(session.getId(), (int) model.get("row"), (int) model.get("cell"), user.getId());
        model.addAttribute("ticketPurchaseResult", result);
        return "purchaseResult";
    }
}
