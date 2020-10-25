package lk.spark.ncms.bean;
import com.google.gson.JsonObject;
import lk.spark.ncms.database.DBConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

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
    private String hospital_id;
    private String severity_level;
    private String admitted_by;
    private String admit_date;
    private String gender;
    private String discharged_by;
    private String discharge_date;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDischarged_by() {
        return discharged_by;
    }

    public void setDischarged_by(String discharged_by) {
        this.discharged_by = discharged_by;
    }

    public String getDischarge_date() {
        return discharge_date;
    }

    public void setDischarge_date(String discharge_date) {
        this.discharge_date = discharge_date;
    }


    public String getHospital_id() {
        return hospital_id;
    }

    public void setHospital_id(String hospital_id) {
        this.hospital_id = hospital_id;
    }

    public String getSeverity_level() {
        return severity_level;
    }

    public void setSeverity_level(String severity_level) {
        this.severity_level = severity_level;
    }

    public String getAdmitted_by() {
        return admitted_by;
    }

    public void setAdmitted_by(String admitted_by) {
        this.admitted_by = admitted_by;
    }

    public String getAdmit_date() {
        return admit_date;
    }

    public void setAdmit_date(String admit_date) {
        this.admit_date = admit_date;
    }



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
                this.severity_level = resultSet.getString("severity_level");
                this.hospital_id= resultSet.getString("hospital_id");
                this.gender = resultSet.getString("gender");
                this.contact = resultSet.getString("contact");
                this.email = resultSet.getString("email");
                this.age= resultSet.getString("age");
                this.admitted_by = resultSet.getString("admitted_by");
                this.admit_date= resultSet.getString("admit_date");
                this.discharged_by  = resultSet.getString("discharged_by ");
                this.discharge_date= resultSet.getString("discharge_date");

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
        data.addProperty("district", this.district);
        data.addProperty("location_x", this.location_x);
        data.addProperty("location_y", this.location_y);
        data.addProperty("district", this.severity_level);
        data.addProperty("district", this.hospital_id);
        data.addProperty("district", this.gender);
        data.addProperty("contact", this.contact);
        data.addProperty("email", this.email);
        data.addProperty("age", this.age);
        data.addProperty("district", this.admit_date);
        data.addProperty("district", this.admitted_by);
        data.addProperty("district", this.discharge_date);
        data.addProperty("district", this.discharged_by);




        return data;
    }

}
