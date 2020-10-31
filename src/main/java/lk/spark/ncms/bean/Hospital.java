package lk.spark.ncms.bean;


import com.google.gson.JsonObject;
import lk.spark.ncms.database.DBConnectionPool;


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.util.Comparator.comparingDouble;

public class Hospital {

    private String hospital_id;
    private String name;
    private String district;
    private int location_x;
    private int location_y;

    public String getBuild_date() {
        return build_date;
    }

    public void setBuild_date(String build_date) {
        this.build_date = build_date;
    }

    private String build_date;

    public Hospital() {

    }

    public Hospital(String hospital_id){
        this.hospital_id = hospital_id;
    }

    public String getHospital_id() {
        return hospital_id;
    }

    public void setHospital_id(String hospital_id) {
        this.hospital_id = hospital_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getLocation_x() { return location_x; }

    public void setLocation_x(int location_x) { this.location_x = location_x; }

    public int getLocation_y() {
        return location_y;
    }

    public void setLocation_y(int location_y) { this.location_y = location_y; }

    public JsonObject serialize() {
        JsonObject data = new JsonObject();

        data.addProperty("hospital_id", this.hospital_id);
        data.addProperty("name", this.name);
        data.addProperty("district", this.district);
        data.addProperty("location_x", this.location_x);
        data.addProperty("location_y", this.location_y);
        data.addProperty("build_date", this.build_date);

        return data;
    }

    public String assignHospital(int patientLocationX, int patientLocationY) {
        Connection connection = null;
        PreparedStatement statement = null;
        Map<String, Double> distance = new HashMap<String, Double>();

        double dist;
        String nearestHospital = "";

        try {
            connection = DBConnectionPool.getInstance().getConnection();
            ResultSet resultSet;

            statement = connection.prepareStatement("SELECT * FROM hospital");
            System.out.println(statement);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String id = resultSet.getString("hospital_id");
                int locationX = resultSet.getInt("location_x");
                int locationY = resultSet.getInt("location_y");
                int distanceX = Math.abs(locationX - patientLocationX);
                int distanceY = Math.abs(locationY - patientLocationY);

                dist = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
                distance.put(id,dist);
            }

            System.out.println(distance);
            System.out.println(Collections.min(distance.values()));
            nearestHospital = Collections.min(distance.entrySet(), comparingDouble(Map.Entry::getValue)).getKey();
            System.out.println(nearestHospital);
            connection.close();

        } catch (Exception exception) {

        }
        return nearestHospital;
    }

}
