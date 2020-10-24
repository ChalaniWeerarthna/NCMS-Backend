package lk.spark.ncms.bean;

import com.google.gson.JsonObject;
import lk.spark.ncms.database.DBConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class Moh {

    private String moh_id;
    private String email;
    private String name;



    public String getMoh_id() { return moh_id; }

    public void setMoh_id(String moh_id) { this.moh_id = moh_id; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Moh(String moh_id) {
        this.moh_id = moh_id;
    }

    public Moh(){

    }

    public JsonObject serialize() {
        JsonObject data = new JsonObject();


        data.addProperty("moh_id", this.moh_id);
        data.addProperty("email", this.email);
        data.addProperty("name", this.name);

        return data;
    }

    public void getModel() {
        try {
            Connection connection = DBConnectionPool.getInstance().getConnection();
            PreparedStatement statement;
            ResultSet resultSet;

            statement = connection.prepareStatement("SELECT * FROM moh WHERE email=?");
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                this.email = resultSet.getString("email");
                this.moh_id = resultSet.getString("moh_id");
                this.name = resultSet.getString("name");
            }

            connection.close();

        } catch (Exception exception) {

        }
    }
}
