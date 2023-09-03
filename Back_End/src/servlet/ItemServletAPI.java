package servlet;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet(urlPatterns = "/item")
public class ItemServletAPI extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin","*");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/web_pos", "root", "1234");

            //check if the request is for a specific item
            String code = req.getParameter("code");
            if (code != null) {
                PreparedStatement pstm = connection.prepareStatement("select * from Item where code=?");
                pstm.setObject(1, code);
                ResultSet rst = pstm.executeQuery();

                if (rst.next()) {
                    String itemCode = rst.getString(1);
                    String description = rst.getString(2);
                    int qty = rst.getInt(3);
                    double unitPrice = rst.getDouble(4);

                    JsonObjectBuilder itemObject = Json.createObjectBuilder();
                    itemObject.add("itemCode", itemCode);
                    itemObject.add("description", description);
                    itemObject.add("qty", qty);
                    itemObject.add("unitPrice", unitPrice);

                    resp.getWriter().print(itemObject.build());

                } else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
                return;
            }

            PreparedStatement pstm = connection.prepareStatement("select * from Item");
            ResultSet rst = pstm.executeQuery();

            JsonArrayBuilder allItems = Json.createArrayBuilder();

            while (rst.next()) {
                String item_code = rst.getString(1);
                String name = rst.getString(2);
                int qtyOnHand = rst.getInt(3);
                double unitPrice = rst.getDouble(4);

                JsonObjectBuilder itemObject = Json.createObjectBuilder();
                itemObject.add("itemCode", item_code);
                itemObject.add("itemName", name);
                itemObject.add("qtyOnHand", qtyOnHand);
                itemObject.add("unitPrice", unitPrice);
                allItems.add(itemObject.build());
            }
            resp.getWriter().print(allItems.build());

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            JsonObjectBuilder response = Json.createObjectBuilder();
            response.add("state", "Error");
            response.add("message", e.getMessage());
            response.add("data", "");
            resp.setStatus(400);
            resp.getWriter().print(response.build());
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin","*");

        int code = Integer.parseInt(req.getParameter("code"));
        String itemName = req.getParameter("description");
        String qty = req.getParameter("qty");
        String unitPrice = req.getParameter("unitPrice");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/web_pos", "root", "1234");

                    PreparedStatement pstm = connection.prepareStatement("insert into Item values(?,?,?,?)");
                    pstm.setObject(1, code);
                    pstm.setObject(2, itemName);
                    pstm.setObject(3, qty);
                    pstm.setObject(4, unitPrice);
                    resp.addHeader("Content-Type", "application/json");

                    if (pstm.executeUpdate() > 0) {
                        JsonObjectBuilder response = Json.createObjectBuilder();
                        response.add("state", "Ok");
                        response.add("message", "Successfully Added.!");
                        response.add("data", "");
                        resp.getWriter().print(response.build());
                    }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            JsonObjectBuilder response = Json.createObjectBuilder();
            response.add("state", "Error");
            response.add("message", e.getMessage());
            response.add("data", "");
            resp.setStatus(400);
            resp.getWriter().print(response.build());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin","*");

        String code = req.getParameter("code");
        String itemName = req.getParameter("description");
        String qty = req.getParameter("qty");
        String unitPrice = req.getParameter("unitPrice");
        String option = req.getParameter("option");


        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/web_pos", "root", "1234");

            PreparedStatement pstm3 = connection.prepareStatement("update Item set description=?,qtyOnHand=?,unitPrice=? where code=?");
            pstm3.setObject(1, itemName);
            pstm3.setObject(2, qty);
            pstm3.setObject(3, unitPrice);
            pstm3.setObject(4, code);

            if (pstm3.executeUpdate() > 0) {
                JsonObjectBuilder response = Json.createObjectBuilder();
                response.add("state", "Ok");
                response.add("message", "Successfully Updated.!");
                response.add("data", "");
                resp.getWriter().print(response.build());
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);

        } catch (SQLException e) {
            JsonObjectBuilder response = Json.createObjectBuilder();
            response.add("state", "Error");
            response.add("message", e.getMessage());
            response.add("data", "");
            resp.setStatus(400);
            resp.getWriter().print(response.build());
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin","*");

        String code = req.getParameter("code");
        resp.addHeader("Content-Type", "application/json");
        System.out.println(code);

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/web_pos", "root", "1234");

            PreparedStatement pstm2 = connection.prepareStatement("delete from Item where code=?");
            pstm2.setObject(1, code);
            if (pstm2.executeUpdate() > 0) {
                JsonObjectBuilder response = Json.createObjectBuilder();
                response.add("state", "Ok");
                response.add("message", "Successfully Deleted.!");
                response.add("data", "");
                resp.getWriter().print(response.build());

            }


        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);

        } catch (SQLException e) {
            JsonObjectBuilder response = Json.createObjectBuilder();
            response.add("state", "Error");
            response.add("message", e.getMessage());
            response.add("data", "");
            resp.setStatus(400);
            resp.getWriter().print(response.build());
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE");
        resp.addHeader("Access-Control-Allow-Headers", "Content-Type");
    }
}
