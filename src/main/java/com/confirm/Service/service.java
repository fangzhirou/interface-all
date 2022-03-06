package com.confirm.Service;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import java.sql.*;
import java.util.Base64;
import com.confirm.Util.dbconnector;
import org.springframework.web.bind.annotation.RequestBody;


public class service {
    /**
     * 登录
     */
    public boolean login(String username, String password) {
        byte[] decoded = Base64.getDecoder().decode(password);
        String encode = new String(decoded);
        if (username.equals("admin") && encode.equals("password")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 注册
     *
     * @param username
     * @param password
     * @return
     */
    public boolean register(String username, String password, String ID, String place) {

        if ((!("").equals(username)) && (!("").equals(password))) {
            // 获取Sql插入语句
            String regSql = "insert into data(username, password,ID,place) values('" + username + "','" + password + "','" + ID + "','" + place + "') ";
            // 获取DB对象
            dbconnector sql = dbconnector.createInstance();
            sql.connectDB();
            //插入数据
            int ret = sql.executeUpdate(regSql);
            if (ret != 0) {
                sql.closeDB();
                return true;
            }
            sql.closeDB();
            return false;
        } else {
            return false;
        }

    }

    /**
     * 日志表
     */

    public boolean log(String ip, String deviceType, String osVersion, String username, String deviceManufacturer, String Time) {

        if ((!("").equals(ip))) {
            // 获取Sql插入语句
            String regSql = "insert into log(ip,deviceType,osVersion,username,deviceManufacturer,Time) values('" + ip + "','" + deviceType + "','" + osVersion + "','" + username + "','" + deviceManufacturer + "','" + Time + "') ";
            // 获取DB对象
            dbconnector sql = dbconnector.createInstance();
            sql.connectDB();
            int ret = sql.executeUpdate(regSql);
            if (ret != 0) {
                sql.closeDB();
                return true;
            } else {
                sql.closeDB();
                return false;
            }
        } else {
            return false;
        }
    }
    /**
     * 预约界面
     */

    public boolean reservation(String unit, String time,String person,String place,String note,String imagePath ) {

        if ((!("").equals(unit))) {
            // 获取Sql插入语句
            String regSql = "insert into reservation(unit, time,person,place,note,imagePath ) values('" + unit + "','" + time+ "','" +person + "','" + place + "','" + note+ "','"+imagePath+"') ";
            // 获取DB对象
            dbconnector sql = dbconnector.createInstance();
            sql.connectDB();
            int ret = sql.executeUpdate(regSql);
            if (ret != 0) {
                sql.closeDB();
                return true;
            } else {
                sql.closeDB();
                return false;
            }
        } else {
            return false;
        }
    }


    /**
     * 查询用户
     *
     * @param ID
     * @return
     */
    public boolean execute(String ID) {

        if ((!("").equals(ID))) {

            String exeSql = "select * from data where ID ='" + ID + "'";
            boolean flag = true;
            dbconnector sql = dbconnector.createInstance();
            sql.connectDB();

            try {
                ResultSet exe = sql.executeQuery(exeSql);
                //username存在，返回true
                if (exe.next()) {
                    sql.closeDB();
                    flag = true;
                } else {
                    flag = false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return flag;
        } else {
            return false;
        }
    }


    /**
     * 查询ftp服务器数据
     *
     * @param Key
     * @return
     */
    /*public void find(Integer Key) {

        if ((!("").equals(Key))) {

            String sql = "select * from ftpinformation where Key ='" + Key+ "'";
            dbconnector Sql = dbconnector.createInstance();
            Sql.connectDB();

            try {
                ResultSet exe = Sql.executeQuery(sql);
                //username存在，返回true
                while (exe.next()) {
                    String ftpHost=exe.getString(1);
                    Integer ftpPort=exe.getInt(2);
                    String ftpUsername=exe.getString(3);
                    String ftpPassword=exe.getString(4);
                    System.out.println(ftpHost+ftpPort+ftpUsername+ftpPassword);


                }
                    Sql.closeDB();




            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }*/





    /**
     * 封装json
     *
     * @param type
     * @return
     */
    public String toJson(int type) {
        JSONObject jsonObject = new JSONObject();
        String jsonString ="";
        try {
            jsonObject.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonString = jsonObject.toString();
        return jsonString;
    }
    /**
     * 封装json
     *
     * @param FtpUrl
     * @return
     */
    public String toJson(String FtpUrl) {
        JSONObject jsonObject = new JSONObject();
        String jsonString ="";
        try {
            jsonObject.put("FtpUrl", FtpUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonString = jsonObject.toString();
        return jsonString;
    }

    public String toJson(int type,String version,String DownloadUrl) {
        JSONObject jsonObject = new JSONObject();
        String jsonString ="";
        try {
            jsonObject.put("type", type);
            jsonObject.put("version", version);
            jsonObject.put("DownloadUrl", DownloadUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonString = jsonObject.toString();
        return jsonString;
    }
}
