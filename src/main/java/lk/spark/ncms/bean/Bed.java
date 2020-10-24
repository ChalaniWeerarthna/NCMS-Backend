package lk.spark.ncms.bean;

import lk.spark.ncms.database.DBConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;


public class Bed {
    private int bed_id;
    private String hospital_id;
    private String patient_id;

    public int getBed_id() {
        return bed_id;
    }

    public void setBed_id(int bed_id) {
        this.bed_id = bed_id;
    }

    public String getHospital_id() {
        return hospital_id;
    }

    public void setHospital_id(String hospital_id) {
        this.hospital_id = hospital_id;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public int allocateBed(String hospital_id, String patient_id) {
        setHospital_id(hospital_id);
        setPatient_id(patient_id);
        int bed_id = 0;
        int noOfBeds = 10;
        int bed_count = 0;

        Connection connection = null;
        PreparedStatement statement = null;
        PreparedStatement statement2 = null;
        PreparedStatement statement3 = null;
        int result =0;
        int [] bed = new int[10];

        try {
            connection = DBConnectionPool.getInstance().getConnection();
            ResultSet resultSet;
            statement = connection.prepareStatement("SELECT * FROM beds where hospital_id= '" + getHospital_id() + "'");
            System.out.println(statement);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("bed_id");
                bed[id-1]=id;
            }
            for(int i=0; i< noOfBeds; i++){
                if(bed[i]==0){
                    bed_id = i+1;
                    bed_count = bed_id;
                    break;
                }
            }
            if(bed_id!=0) {
                statement2 = connection.prepareStatement("INSERT INTO beds (bed_id, hospital_id, patient_id) VALUES (" + bed_id + ",'" + hospital_id + "','" + patient_id + "')");
                System.out.println(statement2);
                result = statement2.executeUpdate();
            }
            connection.close();

        } catch (Exception exception) {

        }

        return  bed_id;
    }

    public void makeAvailable(String patient_id, String hospital_id) {
        Connection connection = null;
        PreparedStatement statement = null;
        PreparedStatement statement2 = null;
        PreparedStatement statement3 = null;
        PreparedStatement statement4 = null;
        PreparedStatement statement5 = null;

        try {
            connection = DBConnectionPool.getInstance().getConnection();
            int result=0;
            int result2=0;
            ResultSet resultSet;
            ResultSet resultSet2;
            Map<Integer,String> queueDetails = new HashMap<Integer,String>();

            statement = connection.prepareStatement("DELETE FROM beds WHERE patient_id='"+patient_id+"'");
            System.out.println(statement);
            result = statement.executeUpdate();

            statement2 = connection.prepareStatement("SELECT * FROM patient_queue ORDER BY id ASC");
            System.out.println(statement2);
            resultSet = statement2.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String patient = resultSet.getString("patient_id");
                //queueDetails.put(id,patient);
                allocateBed(hospital_id, patient);

                statement3 = connection.prepareStatement("DELETE FROM patient_queue where patient_id = '"+patient+"'");
                System.out.println(statement3);
                result2 = statement3.executeUpdate();

                statement4 = connection.prepareStatement("SELECT * FROM patient_queue ORDER BY id ASC");
                System.out.println(statement4);
                resultSet2 = statement4.executeQuery();

                while(resultSet2.next()) {
                    int currentId = resultSet2.getInt("id");
                    int nextId = currentId-1;
                    System.out.println(nextId);
                    statement5 = connection.prepareStatement("UPDATE patient_queue SET id=" + nextId + " Where id="+ currentId);
                    System.out.println(statement5);
                    statement5.executeUpdate();
                }
            }
            connection.close();

        } catch (Exception exception) {

        }
    }
}
