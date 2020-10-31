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

@WebServlet(name = "PatientListServlet")
public class PatientListServlet extends HttpServlet {

    //////////////////////////////display patient list//////////////////////////
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Connection connection = null;
        PreparedStatement statement = null;
        int result = 0;

        try {
            connection = DBConnectionPool.getInstance().getConnection();
            ResultSet resultSet;
            JsonArray patients = new JsonArray();

//            statement = connection.prepareStatement("SELECT hospital.*, (SELECT COUNT(*) FROM beds WHERE beds.hospital_id = hospital.hospital_id AND beds.bed_id IS NOT NULL) AS patient_count FROM hospital INNER JOIN doctor ON doctor.hospital_id = hospital.hospital_id");
            statement = connection.prepareStatement("SELECT * FROM patient" );
            System.out.println(statement);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                JsonObject patient = new JsonObject();
                patient.addProperty("patient_id", resultSet.getString("patient_id"));
                patient.addProperty("first_name", resultSet.getString("first_name"));
                patient.addProperty("last_name", resultSet.getString("last_name"));
                patient.addProperty("district", resultSet.getString("district"));
                patient.addProperty("location_x", resultSet.getString("location_x"));
                patient.addProperty("location_y", resultSet.getString("location_y"));
                patient.addProperty("severity_level", resultSet.getString("severity_level"));
                patient.addProperty("hospital_id", resultSet.getString("hospital_id"));
                patient.addProperty("bed_id", resultSet.getString("bed_id"));
                patient.addProperty("gender", resultSet.getString("gender"));
                patient.addProperty("contact", resultSet.getString("contact"));
                patient.addProperty("email", resultSet.getString("email"));
                patient.addProperty("age", resultSet.getString("age"));
                patient.addProperty("admit_date", resultSet.getString("admit_date"));
                patient.addProperty("admitted_by", resultSet.getString("admitted_by"));
                patient.addProperty("discharge_date", resultSet.getString("discharge_date"));
                patient.addProperty("discharged_by", resultSet.getString("discharged_by"));

//                hospital.addProperty("patient_count", resultSet.getInt("patient_count"));
                patients.add(patient);
            }

            System.out.println(patients) ;

            PrintWriter printWriter = resp.getWriter();
            printWriter.println(patients.toString());

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
