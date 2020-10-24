package lk.spark.ncms.dao;

import lk.spark.ncms.database.DBConnectionPool;
import lk.spark.ncms.bean.Hospital;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class QueueDao {
    public int addToQueue(String patient_id) {

        Connection connection = null;
        PreparedStatement statement = null;
        PreparedStatement statement2 = null;
        int result =0;
        int queueLength = 4;
        int queue_id = 0;
        int [] queue = new int[4];

        try {
            connection = DBConnectionPool.getInstance().getConnection();
            ResultSet resultSet;
            statement = connection.prepareStatement("SELECT * FROM patient_queue");
            System.out.println(statement);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String patient = resultSet.getString("patient_id");
                queue[id-1]=id;
            }
            for(int i=0; i<queueLength; i++){
                if(queue[i]==0){
                    queue_id = i+1;
                    System.out.println("Queue length: " + queue_id);
                    break;
                }
            }
            if(queue_id!=0) {
                statement2 = connection.prepareStatement("INSERT INTO patient_queue (id, patient_id) VALUES (" + queue_id + ",'" + patient_id + "')");
                System.out.println(statement2);
                result = statement2.executeUpdate();
            } else {
                Hospital hospital = new Hospital();
                //hospital.setId();
            }

            connection.close();

        } catch (Exception exception) {

        }

        return  queue_id;
    }

}
