package com.example.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;

@WebServlet("/currencies")
public class CurrencyServlet extends HttpServlet {

    private static final String JDBC_URL = "jdbc:sqlite:identifier.sqlite";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Currency> currencies = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM сurrencies");
            while (rs.next()) {
                Currency currency = new Currency(
                        rs.getInt("ID"),
                        rs.getString("Code"),
                        rs.getString("FullName"),
                        rs.getString("Sign")
                );
                currencies.add(currency);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
            return;
        }

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(currencies);

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print(jsonResponse);
        out.flush();
    }

    class Currency {
        private int id;
        private String code;
        private String fullName;
        private String sign;

        public Currency(int id, String code, String fullName, String sign) {
            this.id = id;
            this.code = code;
            this.fullName = fullName;
            this.sign = sign;
        }

        public int getId() {
            return id;
        }

        public String getCode() {
            return code;
        }

        public String getSign() {
            return sign;
        }

        public String getFullName() {
            return fullName;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        // Getters and setters...
    }
}
