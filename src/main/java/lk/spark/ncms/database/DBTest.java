package lk.spark.ncms.database;

import java.sql.*;
import java.util.UUID;

public class DBTest {

    private void simpleInsertQuery()
    {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement stmt = null;
        try
        {
            UUID uuid = UUID.randomUUID();

            con = DBConnectionPool.getInstance().getConnection();
            stmt = con.prepareStatement("INSERT INTO hospital (hospital_id, name, district, x_location, y_location) VALUES (?,?,?,?,?)");
            stmt.setString(1, uuid.toString());
            stmt.setString(2, "hos3");
            stmt.setString(3, "dis3");
            stmt.setInt(4, 380);
            stmt.setInt(5, 258);
            stmt.setDate(6, new Date(new java.util.Date().getTime()));
            int changedRows = stmt.executeUpdate();
            System.out.println( changedRows == 1 ? "Successfully inserted" : "Insertion failed");
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            DBConnectionPool.getInstance().close(rs);
            DBConnectionPool.getInstance().close(stmt);
            DBConnectionPool.getInstance().close(con);
        }
    }

    private void simpleSelectQuery()
    {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement stmt = null;
        try
        {
            con = DBConnectionPool.getInstance().getConnection();
            stmt = con.prepareStatement("SELECT * FROM hospital WHERE district = ?");
            stmt.setString(1, "dis3");
            rs = stmt.executeQuery();
            while(rs.next())
            {
                System.out.println(rs.getString("hospital_id"));
                System.out.println(rs.getString("name"));
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            DBConnectionPool.getInstance().close(rs);
            DBConnectionPool.getInstance().close(stmt);
            DBConnectionPool.getInstance().close(con);
        }
    }

    private void complexSelectQuery()
    {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement stmt = null;
        try
        {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT hospital.name, hospital.district, beds.availability ");
            sb.append("FROM ( ");
            sb.append("       SELECT hospital_id, COUNT(*) AS availability FROM beds ") ;
            sb.append("       WHERE patient_id IS NULL GROUP BY hospital_id ");
            sb.append(") beds, hospital ");
            sb.append("WHERE beds.hospital_id = hospital.hospital_id ");

            con = DBConnectionPool.getInstance().getConnection();
            stmt = con.prepareStatement(sb.toString());
            rs = stmt.executeQuery();
            while(rs.next())
            {
                System.out.println(rs.getString("name"));
                System.out.println(rs.getString("district"));
                System.out.println(rs.getString("availability"));
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            DBConnectionPool.getInstance().close(rs);
            DBConnectionPool.getInstance().close(stmt);
            DBConnectionPool.getInstance().close(con);
        }
    }


    public static void main(String[] args)
    {
        DBTest dbTest = new DBTest();
        dbTest.simpleInsertQuery();
        dbTest.simpleSelectQuery();
        dbTest.complexSelectQuery();
    }

}
