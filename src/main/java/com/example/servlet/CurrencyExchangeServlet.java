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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.google.gson.Gson;

@WebServlet("/currency/*")
public class CurrencyExchangeServlet extends HttpServlet {

    private static final String JDBC_URL = "jdbc:sqlite:identifier.sqlite";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("./")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Currency code is missing");
            return;
        }

        String code = pathInfo.substring(1).toUpperCase();
        Currency currency = null;

        try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM сurrencies WHERE Code = ?");
            stmt.setString(1, code);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                currency = new Currency(
                        rs.getInt("ID"),
                        rs.getString("Code"),
                        rs.getString("FullName"),
                        rs.getString("Sign")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
            return;
        }

        if (currency == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Currency not found");
            return;
        }

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(currency);

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

        public String getFullName() {
            return fullName;
        }

        public String getSign() {
            return sign;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        // Getters and setters...
    }
}
