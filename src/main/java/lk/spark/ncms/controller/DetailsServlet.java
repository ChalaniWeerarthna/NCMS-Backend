package lk.spark.ncms.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lk.spark.ncms.database.DBConnectionPool;

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

@WebServlet(name = "DetailsServlet")
public class DetailsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("patient_id");

        JsonArray sendToPatientArray = new JsonArray();
        JsonArray sendToQueueArray = new JsonArray();
        Connection connection = null;
        PreparedStatement statement = null;
        PreparedStatement statement2 = null;
        int result = 0;

        try {
            connection = DBConnectionPool.getInstance().getConnection();
            ResultSet resultSet;
            ResultSet resultSet2;

            statement = connection.prepareStatement("SELECT beds.bed_id AS bed_id, hospital.hospital_id, hospital.district FROM patient INNER  JOIN beds ON patient.patient_id=beds.patient_id INNER JOIN hospital ON beds.hospital_id=hospital.hospital_id where patient.patient_id ='"+id+"'");
            System.out.println(statement);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                id = resultSet.getString("patient_id");
                String bed_id = resultSet.getString("bed_id");
                String hospital_id = resultSet.getString("hospital_id");
                String district = resultSet.getString("district");

                PrintWriter printWriter = response.getWriter();

                JsonObject sendToPatient = new JsonObject();
                //sendToPatient.addProperty("Id", id);
                sendToPatient.addProperty("patient_id", id);
                sendToPatient.addProperty("bed_id", bed_id);
                sendToPatient.addProperty("hospital_id", hospital_id);
                sendToPatient.addProperty("district", district);
                sendToPatientArray.add(sendToPatient);
            }

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            //response.getWriter().write(sendToPatientArray.toString());
            //System.out.println(sendToPatientArray.toString());

            if(sendToPatientArray.size()!=0) {
                response.getWriter().write(sendToPatientArray.toString());
                System.out.println(sendToPatientArray.toString());
            } else {
                statement2 = connection.prepareStatement("SELECT patient_queue.id as queueId FROM patient_queue INNER  JOIN patient ON patient.patient_id=patient_queue.patient_id where patient.patient_id ='"+id+"'");
                System.out.println(statement2);
                resultSet2 = statement2.executeQuery();
                while (resultSet2.next()) {
                    int queueId = resultSet2.getInt("queueId");
                    PrintWriter printWriter = response.getWriter();

                    JsonObject sendToPatientQueue = new JsonObject();
                    sendToPatientQueue.addProperty("Id", id);
                    sendToPatientQueue.addProperty("queueId", queueId);
                    sendToQueueArray.add(sendToPatientQueue);
                }
                response.getWriter().write(sendToQueueArray.toString());
                System.out.println(sendToQueueArray.toString());
            }
            connection.close();

        } catch (Exception exception) {

        }
    }
}
