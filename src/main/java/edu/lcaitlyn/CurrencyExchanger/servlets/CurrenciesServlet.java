package edu.lcaitlyn.CurrencyExchanger.servlets;

import com.example.currencyexchange.DatabaseManager;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Currency> currencies = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Currencies")) {

            while (rs.next()) {
                Currency currency = new Currency();
                currency.setId(rs.getInt("ID"));
                currency.setCode(rs.getString("Code"));
                currency.setFullName(rs.getString("FullName"));
                currency.setSign(rs.getString("Sign"));
                currencies.add(currency);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        resp.setContentType("application/json");
        resp.getWriter().write(mapper.writeValueAsString(currencies));
    }

    // Implement doPost, doPut for creating and updating currencies
}
