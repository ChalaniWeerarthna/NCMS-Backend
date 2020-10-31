package lk.spark.ncms.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lk.spark.ncms.database.DBConnectionPool;
import lk.spark.ncms.dao.PatientDao;
import lk.spark.ncms.bean.Patient;
import lk.spark.ncms.dao.QueueDao;
import lk.spark.ncms.bean.Bed;
import lk.spark.ncms.bean.Hospital;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


@WebServlet(name = "PatientServlet")
public class PatientServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    //////////// Register Patient////////////////////
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String patient_id = request.getParameter("patient_id");
        String first_name = request.getParameter("first_name");
        String last_name = request.getParameter("last_name");
        String district = request.getParameter("district");
        int location_x = Integer.parseInt(request.getParameter("location_x"));
        int location_y = Integer.parseInt(request.getParameter("location_y"));
        String gender =request.getParameter("gender");
        String contact = request.getParameter("contact");
        String email = request.getParameter("email");
        String age = request.getParameter("age");




        Patient patient = new Patient();
        patient.setPatient_id(patient_id);
        patient.setFirst_name(first_name);
        patient.setLast_name(last_name);
        patient.setDistrict(district);
        patient.setLocation_x(location_x);
        patient.setLocation_y(location_y);
        patient.setGender(gender);
        patient.setContact(contact);
        patient.setEmail(email);
        patient.setAge(age);



        PatientDao patientDao = new PatientDao();
        String patientRegistered = patientDao.registerPatient(patient);

        if(patientRegistered.equals("SUCCESS")) { //On success, you can display a message to user on Home page

            System.out.println("Success");
        } else {  //On Failure, display a meaningful message to the User.
            System.out.println("Failed");
        }

        try {
            patientDao.registerPatient(patient);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    ///////// View patient details

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String patient_id = request.getParameter("patient_id");

        Connection connection = null;
        PreparedStatement statement = null;
        PreparedStatement statement2 = null;
        int result = 0;

        try {
            connection = DBConnectionPool.getInstance().getConnection();
            ResultSet resultSet;
            ResultSet resultSet2;

            statement = connection.prepareStatement("SELECT * FROM patient WHERE patient_id=? ");
            statement.setString(1, patient_id);
            System.out.println(statement);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                patient_id = resultSet.getString("patient_id");
                String first_name = resultSet.getString("first_name");
                String last_name = resultSet.getString("last_name");
                String contact = resultSet.getString("contact");
                String district = resultSet.getString("district");
                String email = resultSet.getString("email");
                String age = resultSet.getString("age");
                int location_x = Integer.parseInt(resultSet.getString("location_x"));
                int location_y = Integer.parseInt(resultSet.getString("location_y"));
             //   String serial_no = resultSet.getString("serial_no");

                PrintWriter printWriter = response.getWriter();

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("patient_id", patient_id);
                jsonObject.addProperty("first_name", first_name);
                jsonObject.addProperty("last_name", last_name);
                jsonObject.addProperty("contact", contact);
                jsonObject.addProperty("district", district);
                jsonObject.addProperty("email", email);
                jsonObject.addProperty("age", age);
                jsonObject.addProperty("location_x", location_x);
                jsonObject.addProperty("location_y", location_y);
                printWriter.print(jsonObject.toString());

                System.out.println("doGet patient success");


            }
            connection.close();

        } catch (Exception exception) {

        }
    }
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");

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

            statement = connection.prepareStatement("SELECT patient.serial_no, beds.bed_id AS bed_id, hospital.name, hospital.district FROM patient INNER  JOIN beds ON patient.patient_id=beds.patient_id INNER JOIN hospital ON beds.hospital_id=hospital.hospital_id where patient.patient_id ='"+id+"'");
            System.out.println(statement);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String serialNo = resultSet.getString("serial_no");
                String bedId = resultSet.getString("bed_id");
                String name = resultSet.getString("name");
                String district = resultSet.getString("district");

                PrintWriter printWriter = response.getWriter();

                JsonObject sendToPatient = new JsonObject();
                sendToPatient.addProperty("Id", id);
                sendToPatient.addProperty("serialNo", serialNo);
                sendToPatient.addProperty("bedId", bedId);
                sendToPatient.addProperty("District", district);
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
