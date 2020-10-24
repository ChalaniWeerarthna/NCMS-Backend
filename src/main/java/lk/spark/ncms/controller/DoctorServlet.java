package lk.spark.ncms.controller;


//import lk.spark.ncms.database.DBConnectionPool;
import lk.spark.ncms.dao.DoctorDao;
import lk.spark.ncms.bean.Doctor;
//import org.json.simple.JSONObject;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@WebServlet(name = "MohServlet")
public class DoctorServlet extends HttpServlet {

    // Register doctor
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String doctor_id = request.getParameter("doctor_id");
        String full_name = request.getParameter("full_name");
        String email = request.getParameter("email");
        String hospital_id = request.getParameter("hospital_id");
        Boolean is_director = Boolean.valueOf(request.getParameter("is_director"));

        Doctor doctor = new Doctor();
        doctor.setDoctor_id(doctor_id);
        doctor.setFull_Name(full_name);
        doctor.setEmail(email);
        doctor.setHospital_id(hospital_id);
        doctor.setIs_director(is_director);

        DoctorDao doctorDao = new DoctorDao();
        String doctorRegistered = doctorDao.registerDoctor(doctor);

        if(doctorRegistered.equals("SUCCESS"))   //On success, you can display a message to user on Home page
        {
            System.out.println("Success");
        }
        else   //On Failure, display a meaningful message to the User.
        {
            System.out.println("Failed");
        }

        try {
            doctorDao.registerDoctor(doctor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
