package lk.spark.ncms.dao;
import lk.spark.ncms.bean.Doctor;
import lk.spark.ncms.database.DBConnectionPool;
import lk.spark.ncms.bean.Patient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PatientDao {
    public String registerPatient(Patient patient) {
        String INSERT_USERS_SQL = "INSERT INTO patient (patient_id, first_name, last_name,contact, district,email,age, location_x, location_y) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        int result = 0;

        try {
            connection = DBConnectionPool.getInstance().getConnection();

            // Step 2:Create a statement using connection object
            preparedStatement = connection.prepareStatement(INSERT_USERS_SQL);
            preparedStatement.setString(1, patient.getPatient_id());
            preparedStatement.setString(2, patient.getFirst_name());
            preparedStatement.setString(3, patient.getLast_name());
            preparedStatement.setString(4, patient.getContact());
            preparedStatement.setString(5, patient.getDistrict());
            preparedStatement.setString(6, patient.getEmail());
            preparedStatement.setString(7, patient.getAge());
            preparedStatement.setString(8, patient.getLocation_x());
            preparedStatement.setString(9, patient.getLocation_y());

            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            result = preparedStatement.executeUpdate();

            if (result!=0)  //Just to ensure data has been inserted into the database
                return "SUCCESS";

        } catch (SQLException e) {
            // process sql exception
            printSQLException(e);
        }
        return "Oops.. Something went wrong there..!"; // On failure, send a message from here.
    }
    public String loginPatient(Patient patient) {
        String INSERT_USERS_SQL = "SELECT Count(*) AS count FROM patient WHERE patient_id ='" +patient.getPatient_id() + "' and email ='" + patient.getEmail() + "'";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int result = 0;

        try {
            connection = DBConnectionPool.getInstance().getConnection();
            ResultSet resultSet;

            preparedStatement = connection.prepareStatement(INSERT_USERS_SQL);
            System.out.println(preparedStatement);
            resultSet = preparedStatement.executeQuery();

            int x=0;
            while (resultSet.next()) {
                int id_count=resultSet.getInt("count");
                System.out.println(id_count);

            }x=x+1;



            if (x == 1)
                return "SUCCESS";

        } catch (SQLException e) {
            printSQLException(e);
        }
        return "something wrong!";
    }


    private void printSQLException(SQLException ex) {
        for (Throwable e: ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }


}
