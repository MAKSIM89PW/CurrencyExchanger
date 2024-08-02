package exchange.ServletService;

import com.fasterxml.jackson.databind.ObjectMapper;
import exchange.ConnectionDB;
import exchange.DTO.CurrenciesDTO;
import exchange.DTO.MessageDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ActionService {
    private Statement statement;
    private ObjectMapper objectMapper = new ObjectMapper();
    private PrintWriter out;

    //получить список валют
    public void getListOfCurrencies(HttpServletResponse resp) {
        ConnectionDB connectionDB = new ConnectionDB();
        String query = "SELECT * FROM currencies";

        try {
            statement = connectionDB.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            List<CurrenciesDTO> currenciesDTOList = new ArrayList<>();
            while (resultSet.next()) {
                currenciesDTOList.add(
                        new CurrenciesDTO(
                                resultSet.getInt(1),
                                resultSet.getString(2),
                                resultSet.getString(3),
                                resultSet.getString(4)));
            }
            resp.setContentType("application/json; charset=UTF-8");
            out = resp.getWriter();
            String out1 = objectMapper.writeValueAsString(currenciesDTOList);
            out.println(out1);
            statement.close();
            resultSet.close();
            connectionDB.closeDB();
        } catch (SQLException | IOException e) {
            resp.setStatus(500);
            showError(resp, "База данных не доступна");
        }
    }

    //добавить новую валюту
    public void addNewCurrency(HttpServletRequest req, HttpServletResponse resp) {
        ConnectionDB connectionDB = new ConnectionDB();
        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");
        String queryInsert = "INSERT INTO Currencies (Code, FullName, Sign) VALUES ('"
                + code + "', '" + name + "', '" + sign + "')";
        String querySelect = "select * from Currencies where Code='" + code + "'";
        if ("".equals(name) || "".equals(code) || "".equals(sign) || name == null || code == null || sign == null) {
            resp.setStatus(400);
            showError(resp, "Отсутствует нужное поле формы");
        } else if (findCurrencyIdByCode(code) != 0) {
            resp.setStatus(409);
            showError(resp, "Валюта с таким кодом уже существует");
        } else {
            try {
                statement = connectionDB.getConnection().createStatement();
                statement.executeUpdate(queryInsert);
                ResultSet resultSet = statement.executeQuery(querySelect);
                if (resultSet.next()) {
                    resp.setContentType("application/json; charset=UTF-8");
                    out = resp.getWriter();
                    String out1 = objectMapper.writeValueAsString(
                            new CurrenciesDTO(resultSet.getInt(1), resultSet.getString(2)
                                    , resultSet.getString(3), resultSet.getString(4)));
                    out.println(out1);
                }
                statement.close();
                resultSet.close();
                connectionDB.closeDB();
            } catch (SQLException | IOException e) {
                resp.setStatus(500);
                showError(resp, "База данных не доступна");
            }
        }
    }

    private int findCurrencyIdByCode(String code) {
        ConnectionDB connectionDB = new ConnectionDB();
        String query = "select * from Currencies where Code='" + code + "'";
        int out;
        try {
            Statement statement = connectionDB.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            out = resultSet.getInt(1);
            statement.close();
            resultSet.close();
            connectionDB.closeDB();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return out;
    }


    public static String getParameter(HttpServletRequest request) {
        BufferedReader br;
        String[] par;

        InputStreamReader reader;
        try {
            reader = new InputStreamReader(
                    request.getInputStream());
            br = new BufferedReader(reader);
            String data = br.readLine();
            par = data.split("=");
            if (par.length == 1) {
                return "";
            } else {
                return par[1];
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showError(HttpServletResponse resp, String message) {
        try {
            resp.setContentType("application/json; charset=UTF-8");
            out = resp.getWriter();
            out.println(objectMapper.writeValueAsString(new MessageDTO(message)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
