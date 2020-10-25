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

@WebServlet(name = "StatisticsServlet")
public class StatisticsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    //to load statistics
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Connection connection = null;
        PreparedStatement statement = null;
        PreparedStatement statement2 = null;
        PreparedStatement statement3 = null;
        PreparedStatement statement4 = null;
        PreparedStatement statement5 = null;
        PreparedStatement statement6 = null;
        PreparedStatement statement7 = null;

        try {
            connection = DBConnectionPool.getInstance().getConnection();
            ResultSet resultSet1;
            ResultSet resultSet2;
            ResultSet resultSet3;
            ResultSet resultSet4;
            ResultSet resultSet5;
            ResultSet resultSet6;
            ResultSet resultSet7;

            JsonObject data = new JsonObject();
            JsonArray jsonArray;

            ///////////////////////// Hospital level////////////////////////

            statement = connection.prepareStatement("SELECT name FROM hospital");
            resultSet1 = statement.executeQuery();
            jsonArray = new JsonArray();

            while (resultSet1.next()) {
                String  hospital_name = resultSet1.getString("name");
                statement6 = connection.prepareStatement("SELECT COUNT(patient_id) AS hospitalLevel FROM patient  where discharge_date is null ORDER BY hospital_id");
                resultSet6 = statement6.executeQuery();

                while (resultSet6.next()) {
                    int patientCount = resultSet6.getInt("hospitalLevel");
                    System.out.println(patientCount);

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("name", hospital_name);
                    jsonObject.addProperty("statistics", patientCount);

                    jsonArray.add(jsonObject);

                }

                data.add("hospitalPatients", jsonArray);
            }


             /////////////////////// District level /////////////////////////////////

            statement7 = connection.prepareStatement("SELECT district FROM hospital");
            resultSet7 = statement7.executeQuery();
            jsonArray = new JsonArray();
            while (resultSet7.next()) {
                String  district = resultSet7.getString("district");
                statement2 = connection.prepareStatement("SELECT COUNT(patient_id) AS districtLevel FROM patient  where discharge_date is null ORDER BY district");

                System.out.println(statement2);
                resultSet2 = statement2.executeQuery();



                while (resultSet2.next()) {
                    int disPatientCount = resultSet2.getInt("districtLevel");
                    int districtPatientCount = disPatientCount;
                        System.out.println(districtPatientCount);
//                        PrintWriter printWriter = response.getWriter();


                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("district", district);
                        jsonObject.addProperty("statistics", districtPatientCount);

                        jsonArray.add(jsonObject);

                    data.add("districtPatients", jsonArray);
                }
            }


             ////////////////////////////// Country level//////////////////////////////////

            statement3 = connection.prepareStatement("SELECT COUNT(patient_id) AS countryLevel FROM patient where discharge_date is null");


            resultSet3 = statement3.executeQuery();

            jsonArray = new JsonArray();
            while (resultSet3.next()) {
                int hospitalPatientCount = resultSet3.getInt("countryLevel");
                int countryPatientCount = hospitalPatientCount ;
                System.out.println(countryPatientCount);

//                    PrintWriter printWriter = response.getWriter();

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("statistics", countryPatientCount);

                    jsonArray.add(jsonObject);
//
                data.add("countryPatients", jsonArray);
            }

            PrintWriter printWriter = response.getWriter();
            printWriter.print(data.toString());
            connection.close();

        } catch (Exception exception) {

        }
    }
}
