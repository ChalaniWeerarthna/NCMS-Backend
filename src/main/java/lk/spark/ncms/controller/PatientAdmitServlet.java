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
//import java.util.Date;
import java.sql.Date;

@WebServlet(name = "PatientAdmitServlet")
public class PatientAdmitServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String patient_id = request.getParameter("patient_id");
        String hospital_id = request.getParameter("hospital_id");
        String bed_id = request.getParameter("bed_id");
        String severity_level = request.getParameter("severity_level");
        String admitted_by = request.getParameter("admitted_by");
        Date admit_date = Date.valueOf(request.getParameter("admit_date"));


        Patient patient = new Patient();
        patient.setPatient_id(patient_id);
        patient.setHospital_id(hospital_id);
        patient.setBed_id(bed_id);
        patient.setSeverity_level(severity_level);
        patient.setAdmitted_by(admitted_by);
        patient.setAdmit_date(admit_date);


        PatientDao patientDao = new PatientDao();
        String patientAdmitted = patientDao.admitPatient(patient);

        if (patientAdmitted.equals("SUCCESS")) { //On success, you can display a message to user on Home page

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
