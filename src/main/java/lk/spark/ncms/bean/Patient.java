package lk.spark.ncms.bean;
import com.google.gson.JsonObject;
import lk.spark.ncms.database.DBConnectionPool;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Patient {
    private String patient_id;
    private String first_name;
    private String last_name;
    private String contact;
    private String district;
    private String email;
    private String age;
    private String location_x;
    private String location_y;

    public Patient(String patient_id) {
        this.patient_id = patient_id;
    }

    public Patient() {

    }


    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getLocation_x() {
        return location_x;
    }

    public void setLocation_x(String x_location) {
        this.location_x = location_x;
    }

    public String getLocation_y() {
        return location_y;
    }

    public void setLocation_y(String location_y) {
        this.location_y = location_y;
    }

    public void getModel() {
        try {
            Connection connection = DBConnectionPool.getInstance().getConnection();
            PreparedStatement statement;
            ResultSet resultSet;

            statement = connection.prepareStatement("SELECT * FROM patient WHERE patient_id=?");
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                this.patient_id = resultSet.getString("doctor_id");
                this.first_name = resultSet.getString("first_name");
                this.last_name = resultSet.getString("last_name");
                this.district= resultSet.getString("district");
                this.location_x= resultSet.getString("location_x");
                this.location_y= resultSet.getString("location_y");
                this.email = resultSet.getString("email");
                this.contact = resultSet.getString("contact");
                this.age= resultSet.getString("age");

            }

            connection.close();

        } catch (Exception exception) {

        }
    }

    public JsonObject serialize() {
        JsonObject data = new JsonObject();

        data.addProperty("patient_id", this.patient_id);
        data.addProperty("first_name", this.first_name);
        data.addProperty("last_name", this.last_name);
        data.addProperty("contact", this.contact);
        data.addProperty("district", this.district);
        data.addProperty("email", this.email);
        data.addProperty("age", this.age);
        data.addProperty("location_x", this.location_x);
        data.addProperty("location_y", this.location_y);
        data.addProperty("district", this.district);


        return data;
    }

}
