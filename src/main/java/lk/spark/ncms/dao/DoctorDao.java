package lk.spark.ncms.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import lk.spark.ncms.database.DBConnectionPool;
import lk.spark.ncms.bean.Doctor;

public class DoctorDao {
    public String registerDoctor(Doctor doctor) {
        String INSERT_USERS_SQL = "INSERT INTO doctor (doctor_id, full_name, email, hospital_id, is_director) VALUES (?, ?, ?, ?, ?)";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int result = 0;

        try {
            connection = DBConnectionPool.getInstance().getConnection();

            preparedStatement = connection.prepareStatement(INSERT_USERS_SQL);
            preparedStatement.setString(1, doctor.getDoctor_id());
            preparedStatement.setString(2, doctor.getFull_name());
            preparedStatement.setString(3,doctor.getEmail());
            preparedStatement.setString(4, doctor.getHospital_id());
            preparedStatement.setString(5, doctor.getIs_director());

            System.out.println(preparedStatement);
            result = preparedStatement.executeUpdate();

            if (result != 0)
                return "SUCCESS";

        } catch (SQLException e) {
            printSQLException(e);
        }
        return "Oops.. Something went wrong there..!";
    }


    public String loginDoctor(Doctor doctor) {
        String INSERT_USERS_SQL = "SELECT Count(*) AS count FROM doctor WHERE doctor_id ='" + doctor.getDoctor_id() + "' and email ='" + doctor.getEmail() + "'";

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
