/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SqlManagement;
import SensorClass.UdpData;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author zzl
 */
public class SqlManagement {
    Connection con;
    private final String driver = "com.mysql.jdbc.Driver";
    private Queue<UdpData> dataList;
    /*private String url  = "jdbc:mysql://localhost:3306/testdb";
    private String username = "root";
    private String password = "123456";*/
    public SqlManagement(String url,String username,String password){
        try{
            
            dataList = new LinkedBlockingQueue<>();
            con = DriverManager.getConnection(url, username, password);
            if(!con.isClosed()){
                System.out.println("Succeed in connecting database!");
            }
            else{
                System.out.print("Failed in connecting database!");
            }
            
            DbInertThread dit = new DbInertThread();
            dit.start();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void data_insert(UdpData ud){
        PreparedStatement psql;
        try{
            psql = con.prepareStatement("insert into t_data(equipment_id,data_content,data_time)" + "values(?,?,?)");
            psql.setString(1, ud.getEid()); 
            psql.setString(2, ud.getData());
            psql.setString(3, ud.getData_time());
            psql.executeUpdate(); 
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void sql_insert(String str){
        PreparedStatement psql;
        try{
            psql = con.prepareStatement(str);
            psql.executeUpdate(); 
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void inser_dataList(UdpData ud){
        dataList.add(ud);
    }
    
    
    class DbInertThread implements Runnable {
        private Thread t;
        
        @Override
        public void run() {
            while (true) {
                while(!dataList.isEmpty()){
                    data_insert(dataList.poll());
                }
            }
        }
        public void start() {
            System.out.println("DB data insert thread running...");
            t = new Thread(this);
            t.start();
        }
        
    }
    
    /*public Map<String,Integer> query_limit(String table){
        Map data_limit = new HashMap();
        return data_limit;
    }*/    
}
