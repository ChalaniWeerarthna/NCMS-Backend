package lk.spark.ncms.controller;

import com.google.gson.JsonArray;
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


@WebServlet(name = "PatientLoginServlet")
public class PatientLoginServlet extends HttpServlet {

    //////////////////Patient login control//////////////////////////

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String password = req.getParameter("patient_id");
        String email = req.getParameter("email");

        Patient patient = new Patient();
        patient.setPatient_id(password);
        patient.setEmail(email);

        PatientDao patientDao = new PatientDao();
        String patientLogin = patientDao.loginPatient(patient);


        if (patientLogin.equals("SUCCESS")) {
            System.out.println("Success");

            Patient patient2 = new Patient(password);
            patient2.getModel();

            PrintWriter printWriter = resp.getWriter();
            printWriter.println(patient2.toString());

        } else {
            System.out.println("Failed");
        }
    }
}
