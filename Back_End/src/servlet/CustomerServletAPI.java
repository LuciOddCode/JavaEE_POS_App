package servlet;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;


@WebServlet(urlPatterns = {"/customer"})
public class CustomerServletAPI extends HttpServlet {

    public CustomerServletAPI(){
        
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin","*");
        resp.addHeader("Content-Type","application/json");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/web_pos", "root", "1234");

            //check if the request is for a specific customer

            String option = req.getParameter("option");

            switch (option) {
                case "getID":
                    String cusID = req.getParameter("cusID");

                    PreparedStatement pstm = connection.prepareStatement("select * from customer where cus_id=?");
                    pstm.setObject(1, cusID);
                    ResultSet rst = pstm.executeQuery();

                    if (rst.next()) {
                        String cus_id = rst.getString(1);
                        String cus_name = rst.getString(2);
                        String cus_address = rst.getString(3);

                        JsonObjectBuilder customerObject = Json.createObjectBuilder();
                        customerObject.add("cus_id", cus_id);
                        customerObject.add("cus_name", cus_name);
                        customerObject.add("cus_address", cus_address);

                        resp.getWriter().print(customerObject.build());


                    }
                    break;
                case "getAll":
                    PreparedStatement pstm1 = connection.prepareStatement("select * from customer");
                    ResultSet rst1 = pstm1.executeQuery();

                    JsonArrayBuilder allCustomers = Json.createArrayBuilder();
                    while (rst1.next()) {
                        String id = rst1.getString(1);
                        String name = rst1.getString(2);
                        String address = rst1.getString(3);

                        JsonObjectBuilder customerObject = Json.createObjectBuilder();
                        customerObject.add("id", id);
                        customerObject.add("name", name);
                        customerObject.add("address", address);
                        allCustomers.add(customerObject.build());
                    }
                    resp.getWriter().print(allCustomers.build().toString());
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin","*");

        String cusID = req.getParameter("cus_id");
        String cusName = req.getParameter("cus_name");
        String cusAddress = req.getParameter("cus_address");
        System.out.println(cusID + cusName + cusAddress);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/web_pos", "root", "1234");


            PreparedStatement pstm = connection.prepareStatement("insert into customer values(?,?,?)");
            pstm.setObject(1, cusID);
            pstm.setObject(2, cusName);
            pstm.setObject(3, cusAddress);
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

/*        JsonReader reader = Json.createReader(req.getReader());
        JsonArray jsonValues = reader.readArray();

        for (JsonValue jsonValue : jsonValues) {
            JsonObject jsonObject = jsonValue.asJsonObject();
            String cusID = jsonObject.getString("cusID");
            String cusName = jsonObject.getString("cusName");
            String cusAddress = jsonObject.getString("cusAddress");
            System.out.println(cusID + cusName + cusAddress);
    }*/
    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Content-Type", "application/json");
        resp.addHeader("Access-Control-Allow-Origin","*");


        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();

        String cusID = jsonObject.getString("cusID");
        String cusName = jsonObject.getString("cusName");
        String cusAddress = jsonObject.getString("cusAddress");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/web_pos", "root", "1234");

            PreparedStatement pstm3 = connection.prepareStatement("update customer set cus_name=?,cus_address=? where cus_id=?");
            pstm3.setObject(3, cusID);
            pstm3.setObject(1, cusName);
            pstm3.setObject(2, cusAddress);
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
/*

        JsonReader reader = Json.createReader(req.getReader());
        JsonArray jsonValues = reader.readArray();
        for (JsonValue jsonValue : jsonValues) {
            JsonObject jsonObject = jsonValue.asJsonObject();
            String cusID = jsonObject.getString("oid");
            String cusName = jsonObject.getString("date");
            JsonArray item = jsonObject.getJsonArray("orderDetails");
            for (JsonValue value:item) {
                JsonObject object = value.asJsonObject();
                String itemCode = object.getString("code");

                System.out.println(itemCode);

            }

        }
*/

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin","*");

        String cusID = req.getParameter("cusID");
        resp.addHeader("Content-Type", "application/json");
        System.out.println(cusID);

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/web_pos", "root", "1234");

            PreparedStatement pstm2 = connection.prepareStatement("delete from customer where cus_id=?");
            pstm2.setObject(1, cusID);

            if (pstm2.executeUpdate() > 0) {
                JsonObjectBuilder response = Json.createObjectBuilder();
                response.add("message", "Customer Deleted");
                resp.getWriter().print(response.build());
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } catch (ClassNotFoundException e) {
            JsonObjectBuilder response = Json.createObjectBuilder();
            response.add("state", "Error");
            response.add("message", "Class not found: " + e.getMessage());
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
