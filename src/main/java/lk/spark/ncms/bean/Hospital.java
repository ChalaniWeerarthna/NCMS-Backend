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
    private String location_x;
    private String location_y;

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

    public String getLocation_x() { return location_x; }

    public void setLocation_x(String location_x) { this.location_x = location_x; }

    public String getLocation_y() {
        return location_y;
    }

    public void setLocation_y(String location_y) { this.location_y = location_y; }

    public JsonObject serialize() {
        JsonObject data = new JsonObject();

        data.addProperty("hospital_id", this.hospital_id);
        data.addProperty("name", this.name);
        data.addProperty("district", this.district);
        data.addProperty("location_x", this.location_x);
        data.addProperty("location_y", this.location_y);

        return data;
    }

    public String assignHospital(String patientLocationX, String patientLocationY) {
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
                String hospital_id = resultSet.getString("hospital_id");
                String hos_x = resultSet.getString("location_x");;
                double location_x = Double.parseDouble(hos_x);
                //System.out.println(pi);
                //double x_location = resultSet.getInt("location_x");
                String hos_y= resultSet.getString("location_y");
                double location_y = Double.parseDouble(hos_y);
                double pat_x=Double.parseDouble(String.valueOf(patientLocationX));
                double pat_y=Double.parseDouble(String.valueOf(patientLocationY));
                double distanceX = Math.abs(location_x - pat_x);
                double distanceY = Math.abs(location_y - pat_y);

                dist = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
                distance.put(hospital_id,dist);
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
