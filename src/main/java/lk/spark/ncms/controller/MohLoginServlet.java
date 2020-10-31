package lk.spark.ncms.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lk.spark.ncms.database.DBConnectionPool;
import lk.spark.ncms.dao.MohDao;
import lk.spark.ncms.bean.Moh;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "MohLoginServlet")
public class MohLoginServlet extends HttpServlet {

    ///////////////// Moh login control//////////////////

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //response.setContentType("text/html;charset=UTF-8");
        //response.sendRedirect("/moh.html");
        String email = request.getParameter("email");
        String moh_id = request.getParameter("moh_id");

        Moh moh = new Moh();
        moh.setEmail(email);
        moh.setMoh_id(moh_id);

        PrintWriter printWriter = response.getWriter();

        if(MohDao.validate(email, moh_id)) {
            response.getWriter().write("True");
            //RequestDispatcher requestDispatcher = request.getRequestDispatcher("/NCMS/moh.html");
            //requestDispatcher.forward(request,response);
        }else{
            printWriter.print("Sorry username or password error");
        }
        printWriter.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonArray queueArray = new JsonArray();

        Connection connection = null;
        PreparedStatement statement = null;
        PreparedStatement statement2 = null;
        int result =0;
        int queueLength = 4;
        int queueId = 0;
        int [] queue = new int[4];

        try {
            connection = DBConnectionPool.getInstance().getConnection();
            ResultSet resultSet;
            statement = connection.prepareStatement("SELECT patient_queue.id, patient_id, district FROM patient_queue INNER JOIN patient on patient_queue.patient_id = patient.patient_id");
            System.out.println(statement);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String patientId = resultSet.getString("patient_id");
                String district = resultSet.getString("district");

                JsonObject queueObj = new JsonObject();
                queueObj.addProperty("id", id);
                queueObj.addProperty("patientId", patientId);
                queueObj.addProperty("district", district);
                queueArray.add(queueObj);

            }
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(queueArray.toString());
            System.out.println(queueArray.toString());

            connection.close();

        } catch (Exception exception) {

        }

    }

}
