package servlet;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;


@WebServlet(urlPatterns = {"/placeOrder"})
public class PlaceOrderServletAPI extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       //search order

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/web_pos", "root", "1234");
            PreparedStatement pstm = connection.prepareStatement("select * from orders");
            ResultSet rst = pstm.executeQuery();

            JsonArrayBuilder allOrders = Json.createArrayBuilder();
            while (rst.next()) {
                String order_id = rst.getString(1);
                String cus_id = rst.getString(2);
                String total = rst.getString(3);
                String date = rst.getString(3);

                JsonObjectBuilder orderObject = Json.createObjectBuilder();
                orderObject.add("order_id", order_id);
                orderObject.add("cus_id", cus_id);
                orderObject.add("total", total);
                orderObject.add("date", date);
                allOrders.add(orderObject.build());
            }

            resp.getWriter().print(allOrders.build());

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String order_id = req.getParameter("order_id");
        String cusID = req.getParameter("cusID");
        String total = req.getParameter("total");

        String code = req.getParameter("code");
        String qty = req.getParameter("qty");
        String price = req.getParameter("price");
        String date = req.getParameter("date");

        System.out.println(order_id + cusID + total + date);

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/web_pos", "root", "1234");
            connection.setAutoCommit(false);
            //Add an order
            PreparedStatement pstm = connection.prepareStatement("insert into orders values(?,?,?,?)");
            pstm.setObject(1, order_id);
            pstm.setObject(2, cusID);
            pstm.setObject(3, total);
            pstm.setObject(4, date);
            resp.addHeader("Content-Type", "application/json");

            if (pstm.executeUpdate() > 0) {

                PreparedStatement pstm1 = connection.prepareStatement("insert into order_detail values(?,?,?,?)");
                pstm1.setObject(1, order_id);
                pstm1.setObject(2, code);
                pstm1.setObject(3, qty);
                pstm1.setObject(4, price);

                if (pstm1.executeUpdate() > 0) {
                    PreparedStatement pstm2 = connection.prepareStatement("update item set qty=qty-? where item_code=?");
                    pstm2.setObject(1, qty);
                    pstm2.setObject(2, code);
                    if (pstm2.executeUpdate() > 0) {
                        JsonObjectBuilder response = Json.createObjectBuilder();
                        response.add("state", "Ok");
                        response.add("message", "Successfully Added.!");
                        response.add("data", "");
                        connection.setAutoCommit(true);
                        connection.commit();

                        resp.getWriter().print(response.build());
                    }else {
                        JsonObjectBuilder response = Json.createObjectBuilder();
                        response.add("state", "Error");
                        response.add("message", "Error.!");
                        response.add("data", "");
                        resp.getWriter().print(response.build());
                        connection.rollback();
                        connection.setAutoCommit(true);

                    }

                }else {
                    JsonObjectBuilder response = Json.createObjectBuilder();
                    response.add("state", "Error");
                    response.add("message", "Error.!");
                    response.add("data", "");
                    resp.getWriter().print(response.build());
                    connection.rollback();
                    connection.setAutoCommit(true);

                }
            }else {
                JsonObjectBuilder response = Json.createObjectBuilder();
                response.add("state", "Error");
                response.add("message", "Error.!");
                response.add("data", "");
                resp.getWriter().print(response.build());
                connection.rollback();
                connection.setAutoCommit(true);

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
        resp.addHeader("Content-Type", "application/json");

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();

        //Update an order
        String order_id = jsonObject.getString("order_id");
        String cusID = jsonObject.getString("cusID");
        String total = jsonObject.getString("total");
        String date = jsonObject.getString("date");
        System.out.println(order_id + cusID + total + date);

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/web_pos", "root", "1234");

            PreparedStatement pstm3 = connection.prepareStatement("update orders set cus_id=?,total=?,date=? where order_id=?");
            pstm3.setObject(1, cusID);
            pstm3.setObject(2, total);
            pstm3.setObject(3, date);
            pstm3.setObject(4, order_id);
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
        String order_id = req.getParameter("order_id");
        resp.addHeader("Content-Type", "application/json");
        System.out.println(order_id);

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/web_pos", "root", "1234");

            PreparedStatement pstm2 = connection.prepareStatement("delete from orders where order_id=?");
            pstm2.setObject(1, order_id);
            if (pstm2.executeUpdate() > 0) {
                JsonObjectBuilder response = Json.createObjectBuilder();
                response.add("state", "Ok");
                response.add("message", "Successfully Deleted.!");
                response.add("data", "");
                resp.getWriter().print(response.build());
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } catch (ClassNotFoundException e) {
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
        //add headers
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE");
        resp.addHeader("Access-Control-Allow-Headers", "Content-Type");
    }
}
