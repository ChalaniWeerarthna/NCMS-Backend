package lk.spark.ncms.controller;

import com.google.gson.JsonObject;
import lk.spark.ncms.database.DBConnectionPool;
import lk.spark.ncms.dao.PatientDao;
import lk.spark.ncms.dao.QueueDao;
import lk.spark.ncms.bean.Bed;
import lk.spark.ncms.bean.Hospital;
import lk.spark.ncms.bean.Patient;

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

    //////////// Register Patient
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String patient_id = request.getParameter("patient_id");
        String first_name = request.getParameter("first_name");
        String last_name = request.getParameter("last_name");
        String contact = request.getParameter("contact");
        String district = request.getParameter("district");
        String email = request.getParameter("email");
        String age = request.getParameter("age");
        String location_x = request.getParameter("location_x");
        String location_y =request.getParameter("location_y");


        Patient patient = new Patient();
        patient.setPatient_id(patient_id);
        patient.setFirst_name(first_name);
        patient.setLast_name(last_name);
        patient.setContact(contact);
        patient.setDistrict(district);
        patient.setEmail(email);
        patient.setAge(age);
        patient.setLocation_x(location_x);
        patient.setLocation_y(location_y);


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
                String location_x = resultSet.getString("location_x");
                String location_y = resultSet.getString("location_y");

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

                Hospital hospital = new Hospital();
                String nearestHospital = hospital.assignHospital(location_x, location_y);
                System.out.println("Nearest hospital: " + nearestHospital);

                Bed bed = new Bed();
                int bed_id = bed.allocateBed(nearestHospital, patient_id);
                System.out.println("Bed ID: " + bed_id);
                int bedNo = 0;

                if(bed_id == 0){
                    statement2 = connection.prepareStatement("SELECT distinct hospital_id FROM hospital where hospital_id !='" + nearestHospital + "'");
                    System.out.println(statement2);
                    resultSet2 = statement2.executeQuery();
                    String hosId ="";
                    int queueLength;

                    /* Allocate a bed */
                    while(resultSet2.next()) {
                        if(bed_id==0) {
                            hosId = resultSet2.getString("hospital_id");
                            System.out.println(hosId);
                            bed_id = bed.allocateBed(hosId, patient_id);
                        }
                    }
                    /* If there is no available beds, add to queue */
                    bedNo = bed_id;
                    if(bedNo == 0){
                        QueueDao queue = new QueueDao();
                        queueLength = queue.addToQueue(patient_id);
                    }

                }
            }
            connection.close();

        } catch (Exception exception) {

        }
    }
}
