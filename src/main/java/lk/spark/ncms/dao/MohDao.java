package lk.spark.ncms.dao;


import lk.spark.ncms.database.DBConnectionPool;
import lk.spark.ncms.bean.Doctor;
import lk.spark.ncms.bean.Moh;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MohDao {
    public String viewStatistics(Moh moh) {
        String INSERT_USERS_SQL = "INSERT INTO moh (moh_id,email,name) VALUES (?, ?, ?)";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int result = 0;

        try {
            connection = DBConnectionPool.getInstance().getConnection();


            preparedStatement = connection.prepareStatement(INSERT_USERS_SQL);
            preparedStatement.setString(1, moh.getMoh_id());
            preparedStatement.setString(2, moh.getEmail());
            preparedStatement.setString(3, moh.getName());


            System.out.println(preparedStatement);

            result = preparedStatement.executeUpdate();

            if (result != 0)
                return "SUCCESS";

        } catch (SQLException e) {

            printSQLException(e);
        }
        return "Something wrong!";
    }

    /////////////////////moh login//////////////////
    public String loginMoh(Moh moh) {
        String INSERT_USERS_SQL = "SELECT Count(*) AS count FROM moh WHERE moh_id ='" + moh.getMoh_id() + "' and email ='" + moh.getEmail() + "'";

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
