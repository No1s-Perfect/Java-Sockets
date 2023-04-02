package org.una.server.data.us;


import java.sql.Connection;
import java.sql.DriverManager;
public class Servicio {
    
   private Connection conexion;
   private static Servicio instance = null;

   private Servicio() {
       var user = "lol";
       var password = "root";
       var url = "jdbc:oracle:thin:@192.168.100.122:1521:XE";
       try {
           Class.forName("oracle.jdbc.driver.OracleDriver");
           this.conexion = DriverManager.getConnection(url,user,password);
       } catch (Exception ex) {
           System.err.println("Couldn't connect to database");
           System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
           
       }
   }

   public static Servicio getInstance() {
       if (instance == null) instance = new Servicio();
       return instance;
   }

   public Connection getConnection() {
       return conexion;
   }
    
}
