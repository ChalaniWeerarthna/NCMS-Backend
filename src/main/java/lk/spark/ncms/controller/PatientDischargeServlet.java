package lk.spark.ncms.controller;

import com.google.gson.JsonArray;
import lk.spark.ncms.bean.Moh;
import lk.spark.ncms.dao.MohDao;
import lk.spark.ncms.dao.PatientDao;
import lk.spark.ncms.database.DBConnectionPool;
import lk.spark.ncms.bean.Patient;

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
import java.util.Base64;
import java.util.Date;

@WebServlet(name = "PatientDischargeServlet")
public class PatientDischargeServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String patient_id = request.getParameter("patient_id");
        String discharged_by = request.getParameter("discharged_by");
        String discharge_date = request.getParameter("discharge_date");


        Patient patient = new Patient();
        patient.setPatient_id(patient_id);
        patient.setDischarged_by(discharged_by);
        patient.setDischarge_date(discharge_date);


        PatientDao patientDao = new PatientDao();
        String patientDischarged = patientDao.dischargePatient(patient);

        if (patientDischarged.equals("SUCCESS")) { //On success, you can display a message to user on Home page

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
}
