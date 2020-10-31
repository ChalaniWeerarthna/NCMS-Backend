package lk.spark.ncms.controller;


import com.google.gson.JsonArray;
import lk.spark.ncms.dao.DoctorDao;
import lk.spark.ncms.database.DBConnectionPool;
import lk.spark.ncms.bean.Doctor;

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


@WebServlet(name = "LoginServlet")
public class DoctorLoginServlet extends HttpServlet {

    //////////////////doctor login control//////////////////////////

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String password = req.getParameter("doctor_id");
        String email = req.getParameter("email");

        Doctor doctor = new Doctor();
        doctor.setDoctor_id(password);
        doctor.setEmail(email);

        DoctorDao doctorDao = new DoctorDao();
        String doctorLogin = doctorDao.loginDoctor(doctor);


        if (doctorLogin.equals("SUCCESS")) {
            System.out.println("Success");

            Doctor doctor2 = new Doctor(password);
            doctor2.getModel();

            PrintWriter printWriter = resp.getWriter();
            printWriter.println(doctor2.toString());

        } else {
            System.out.println("Failed");
        }
    }

}
