package lk.spark.ncms.dao;


import lk.spark.ncms.database.DBConnectionPool;
import lk.spark.ncms.bean.Doctor;
import lk.spark.ncms.bean.Moh;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MohDao {
    public static boolean validate(String email, String moh_id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Boolean status = false;

        try {
            connection = DBConnectionPool.getInstance().getConnection();
            ResultSet resultSet;

            // Step 2:Create a statement using connection object
            preparedStatement = connection.prepareStatement("SELECT * from moh WHERE email=? AND moh_id=?");
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,moh_id);
            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            resultSet = preparedStatement.executeQuery();
            status = resultSet.next();
            System.out.println(status);

        } catch (SQLException e) {
            System.out.println(e);
        }
        return status;
    }

}
