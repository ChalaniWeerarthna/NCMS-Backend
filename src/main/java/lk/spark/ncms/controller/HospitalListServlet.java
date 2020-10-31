package lk.spark.ncms.controller;



import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import lk.spark.ncms.bean.Hospital;
import lk.spark.ncms.database.DBConnectionPool;
import lk.spark.ncms.dao.HospitalDao;
import lk.spark.ncms.bean.Bed;
import lk.spark.ncms.bean.Doctor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.annotation.MultipartConfig;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


@WebServlet(name = "HospitalListServlet")
public class HospitalListServlet extends HttpServlet {


     ////////////////////view hospital list///////////////

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Connection connection = null;
        PreparedStatement statement = null;
        int result = 0;

        try {
            connection = DBConnectionPool.getInstance().getConnection();
            ResultSet resultSet;
            JsonArray hospitals = new JsonArray();

//            statement = connection.prepareStatement("SELECT hospital.*, (SELECT COUNT(*) FROM beds WHERE beds.hospital_id = hospital.hospital_id AND beds.bed_id IS NOT NULL) AS patient_count FROM hospital INNER JOIN doctor ON doctor.hospital_id = hospital.hospital_id");
            statement = connection.prepareStatement("SELECT * FROM hospital" );
            System.out.println(statement);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                JsonObject hospital = new JsonObject();
                hospital.addProperty("hospital_id", resultSet.getString("hospital_id"));
                hospital.addProperty("name", resultSet.getString("name"));
                hospital.addProperty("district", resultSet.getString("district"));
                hospital.addProperty("location_x", resultSet.getString("location_x"));
                hospital.addProperty("location_y", resultSet.getString("location_y"));
                hospital.addProperty("build_date", resultSet.getString("build_date"));
//                hospital.addProperty("patient_count", resultSet.getInt("patient_count"));
                hospitals.add(hospital);
            }
            System.out.println(hospitals) ;

            PrintWriter printWriter = resp.getWriter();
            printWriter.println(hospitals.toString());

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
