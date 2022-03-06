package com.confirm.Config;
import java.sql.*;
import java.util.Base64;
import com.confirm.Util.dbconnector;

public class FtpInformation {
    public FtpInformation(){};
    String ftpHost="",ftpUsername="",ftpPassword="";
    Integer ftpPort=null;
    //获取数据库中ftpHost
    public String getHost(Integer code) {
        if ((!("").equals(code))) {
           String sql = "select * from ftpinformation where code ='" + code+ "'";
            dbconnector Sql = dbconnector.createInstance();
            Sql.connectDB();
            try {
                ResultSet exe = Sql.executeQuery(sql);
                if(exe.next()) {
                    ftpHost = exe.getString(1);
                  //  System.out.println(ftpHost);
                }
                Sql.closeDB();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return ftpHost;
    }
    //获取ftpPort
    public int getPort(Integer code) {
        if ((!("").equals(code))) {
            String sql = "select * from ftpinformation Where code='"+code+"'";
            dbconnector Sql = dbconnector.createInstance();
            Sql.connectDB();
            try {
                ResultSet exe = Sql.executeQuery(sql);
                if (exe.next()) {
                    ftpPort=exe.getInt(2);
                }
                Sql.closeDB();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return ftpPort;

        }
        //获取ftpUsername
    public String getFtpUsername(Integer code) {
        if ((!("").equals(code))) {
            String sql = "select * from ftpinformation where code ='" + code+ "'";
            dbconnector Sql = dbconnector.createInstance();
            Sql.connectDB();
            try {
                ResultSet exe = Sql.executeQuery(sql);
               while (exe.next()) {
                    ftpUsername = exe.getString(3);
               }
                Sql.closeDB();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ftpUsername;
    }
    //获取ftpPassword
    public String getFtpPassword(Integer code) {
        if ((!("").equals(code))) {
           String sql = "select * from ftpinformation where code ='" + code+ "'";
            //String sql = "select * from ftpinformation ";
            dbconnector Sql = dbconnector.createInstance();
            Sql.connectDB();
            try {
                ResultSet exe = Sql.executeQuery(sql);
               while (exe.next()) {
                    ftpPassword = exe.getString(4);
                    //System.out.println(ftpPassword);
             }
                Sql.closeDB();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ftpPassword;
    }


}
