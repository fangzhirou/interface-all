package com.confirm.controller;

import com.confirm.Util.dbconnector;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Listening {
    //检测version_record表内有多少条记录
    public Integer count() {
        String countSQL = "select count(*) as result from version_record";
        dbconnector sql = dbconnector.createInstance();
        sql.connectDB();
        int count = 0;
        try {
            ResultSet rs = sql.executeQuery(countSQL);
            while (rs.next()) {
                count = rs.getInt(1); // count = rs.getInt("result");
            }
            sql.closeDB();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    //获取version_record表内最新一条数据
 public String newmessage(){
        String version="",url="",message="";
        String sql="SELECT version,url FROM version_record where time=( SELECT MAX(time) FROM version_record)";
     dbconnector db = dbconnector.createInstance();
     db.connectDB();
     try {
         ResultSet rs = db.executeQuery(sql);
         while (rs.next()) {
             version = rs.getString(1);
             url=rs.getString(2);

         }
         db.closeDB();
         message="version:"+version+" url:"+url;
         return message;

     } catch (SQLException e) {
         e.printStackTrace();
     }

     return message;
 }

    public String newversion(){
        String version="";
        String sql="SELECT version FROM version_record where time=( SELECT MAX(time) FROM version_record)";
        dbconnector db = dbconnector.createInstance();
        db.connectDB();
        try {
            ResultSet rs = db.executeQuery(sql);
            while (rs.next()) {
                version = rs.getString(1);
            }
            db.closeDB();
            return version;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return version;
    }

    public String newurl(){
        String url="";
        String sql="SELECT url FROM version_record where time=( SELECT MAX(time) FROM version_record)";
        dbconnector db = dbconnector.createInstance();
        db.connectDB();
        try {
            ResultSet rs = db.executeQuery(sql);
            while (rs.next()) {
                url = rs.getString(1);
            }
            db.closeDB();
            return url;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return url;
    }

}
