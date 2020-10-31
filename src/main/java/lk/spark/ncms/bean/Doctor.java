package lk.spark.ncms.bean;


import lk.spark.ncms.database.DBConnectionPool;

import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Doctor {
    private String doctor_id;
    private String full_name;
    private String email;
    private String hospital_id;
    private String is_director;

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getHospital_id() {
        return hospital_id;
    }

    public void setHospital_id(String hospital_id) {
        this.hospital_id = hospital_id;
    }

    public String getIs_director() {
        return is_director;
    }

    public void setIs_director(String is_director) {
        this.is_director = is_director;
    }

    public Doctor(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public Doctor() {

    }


    public void getModel() {
        try {
            Connection connection = DBConnectionPool.getInstance().getConnection();
            PreparedStatement statement;
            ResultSet resultSet;

            statement = connection.prepareStatement("SELECT * FROM doctor WHERE doctor_id=?");
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                this.doctor_id = resultSet.getString("doctor_id");
                this.full_name = resultSet.getString("full_name");
                this.email = resultSet.getString("email");
                this.hospital_id = resultSet.getString("hospital_id");
                this.is_director= resultSet.getString("is_director");
            }

            connection.close();

        } catch (Exception exception) {

        }
    }

    public void dischargePatients(String patient_id, String hospital_id) {
        Connection connection = null;
        PreparedStatement statement = null;
        PreparedStatement statement2 = null;
        int result = 0;

        try {
            connection = DBConnectionPool.getInstance().getConnection();
            ResultSet resultSet;

            statement2 = connection.prepareStatement("SELECT * FROM doctor WHERE hospital_id='" +hospital_id + "' AND is_director=1");
            resultSet = statement2.executeQuery();
            System.out.println(statement2);
            while (resultSet.next()) {
                String director = resultSet.getString("doctor_id");
                System.out.println(director);
//                statement = connection.prepareStatement("UPDATE patient SET discharge_date=? , discharged_by= '" + director + "' WHERE patient_id = '" + patient_id + "'");
//                statement.setDate(1, new java.sql.Date(new java.util.Date().getTime()));
                System.out.println(statement);
                result = statement.executeUpdate();

                if(result!=0){
                    System.out.println("success");
                }else
                    System.out.println("Failed");
            }

            System.out.println("Id: " + doctor_id);
            System.out.println("Name: " + full_name);
            System.out.println("Email: "+email);
            System.out.println("Hospital id: " + hospital_id);
            System.out.println("Is Director: " + is_director);
            System.out.println("doGet doctor success");

            connection.close();
        } catch (Exception exception) {

        }


    }

}
