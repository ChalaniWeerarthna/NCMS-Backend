package lk.spark.ncms.dao;
import lk.spark.ncms.bean.Hospital;
import lk.spark.ncms.database.DBConnectionPool;
import lk.spark.ncms.bean.Patient;
import lk.spark.ncms.bean.Bed;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

public class PatientDao {
    public String registerPatient(Patient patient) {
        String INSERT_USERS_SQL = "INSERT INTO patient (patient_id, first_name, last_name, district,location_x, location_y,gender,contact,email,age ) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement statement2 = null;

        int result = 0;

        try {
            connection = DBConnectionPool.getInstance().getConnection();
            ResultSet resultSet2;
            // Step 2:Create a statement using connection object
            preparedStatement = connection.prepareStatement(INSERT_USERS_SQL);
            preparedStatement.setString(1, patient.getPatient_id());
            preparedStatement.setString(2, patient.getFirst_name());
            preparedStatement.setString(3, patient.getLast_name());
            preparedStatement.setString(4, patient.getDistrict());
            preparedStatement.setInt(5, patient.getLocation_x());
            preparedStatement.setInt(6, patient.getLocation_y());
            preparedStatement.setString(7, patient.getGender());
            preparedStatement.setString(8, patient.getContact());
            preparedStatement.setString(9, patient.getEmail());
            preparedStatement.setString(10, patient.getAge());


            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            result = preparedStatement.executeUpdate();
            Hospital hospital = new Hospital();
            String nearestHospital = hospital.assignHospital(patient.getLocation_x(), patient.getLocation_y());
            System.out.println("Nearest hospital: " + nearestHospital);

            Bed bed = new Bed();
            int bedId = bed.allocateBed(nearestHospital, patient.getPatient_id());
            System.out.println("Bed ID: " + bedId);
            int bedNo = 0;

            if(bedId == 0){
                statement2 = connection.prepareStatement("SELECT distinct hospital_id FROM hospital where hospital_id !='" + nearestHospital + "'");
                System.out.println(statement2);
                resultSet2 = statement2.executeQuery();
                String hosId ="";
                int queueLength;

                /* Allocate a bed */
                while(resultSet2.next()) {
                    if(bedId==0) {
                        hosId = resultSet2.getString("hospital_id");
                        System.out.println(hosId);
                        bedId = bed.allocateBed(hosId, patient.getPatient_id());
                    }
                }
                /* If there is no available beds, add to queue */
                bedNo = bedId;
                if(bedNo == 0){
                    QueueDao queue = new QueueDao();
                    queueLength = queue.addToQueue(patient.getPatient_id());
                }
            }

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
    public String admitPatient(Patient patient) {
        String INSERT_USERS_SQL = "UPDATE patient SET  hospital_id=?,bed_id=?,severity_level=?, admitted_by=?, admit_date=? WHERE patient_id=? ";
       // String INSERT_BEDS_SQL = "INSERT INTO beds (bed_id=?,hospital_id=?,patient_id=?) VALUES (?,?,?)";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        int result = 0;

        try {
            connection = DBConnectionPool.getInstance().getConnection();

            // Step 2:Create a statement using connection object
            preparedStatement = connection.prepareStatement(INSERT_USERS_SQL);
            preparedStatement.setString(1, patient.getHospital_id());
            preparedStatement.setString(2, patient.getBed_id());
            preparedStatement.setString(3, patient.getSeverity_level());
            preparedStatement.setString(4, patient.getAdmitted_by());
            preparedStatement.setDate(5, (Date) patient.getAdmit_date());
            preparedStatement.setString(6, patient.getPatient_id());


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

    public String dischargePatient(Patient patient) {
        String INSERT_USERS_SQL = "UPDATE patient SET  discharged_by=?,discharge_date=? WHERE patient_id=? ";


        Connection connection = null;
        PreparedStatement preparedStatement = null;

        int result = 0;

        try {
            connection = DBConnectionPool.getInstance().getConnection();

            // Step 2:Create a statement using connection object
            preparedStatement = connection.prepareStatement(INSERT_USERS_SQL);
            preparedStatement.setString(1, patient.getDischarged_by());
            preparedStatement.setDate(2, (Date) patient.getDischarge_date());
            preparedStatement.setString(3, patient.getPatient_id());


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
