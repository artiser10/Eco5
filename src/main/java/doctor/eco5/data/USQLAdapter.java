package doctor.eco5.data;

import doctor.eco5.Eco5;


import java.sql.*;
import java.util.*;

public class USQLAdapter {
    private static final String url = keys.sql_get_url();
    private static final String user = keys.sql_get_user();
    private static final String password = keys.sql_get_password();

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public static ResultSet sql_request(String query, boolean confirm) {
        //InformationBlock.log_debug("SQL REQUEST: " + query);
        ResultSet rs;
        try {
            // JDBC variables for opening and managing connection
            Connection con = Eco5.sql_connection;
            Statement stmt;
            if (confirm) {
                stmt = con.createStatement();
                stmt.executeUpdate(query);
                return null;
            } else {
                stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                //stmt.execute("set character set utf8");
                //stmt.execute("set names utf8");
                rs = stmt.executeQuery(query);
            return rs;
            }
        } catch (Exception e) {
            //System.out.println(Arrays.toString(e.getStackTrace()));
        }
        return null;
    }

    public static Set<HashMap<String, String>> rs_to_data(ResultSet rs) throws SQLException {return rs_to_data(rs, false);}

    public static Set<HashMap<String, String>> rs_to_data(ResultSet rs, Boolean one) throws SQLException {
        Set<HashMap<String, String>> exit = new HashSet<>();
        HashMap<String, String> data;
        if (rs == null) {return exit;}
        rs.beforeFirst();
        String colname;
        String colval;
        while (rs.next()) {
            data = new HashMap<>();
            for (int i = 1; i < rs.getMetaData().getColumnCount() + 1; i++) {
                colname = rs.getMetaData().getColumnName(i);
                if (i == 1) {
                    colval = rs.getInt(i) + "";
                } else {
                    colval = rs.getString(i);
                }
                data.put(colname, colval);
            }
            exit.add(data);
            if (one) {
                return exit;
            }
        }
        return exit;
    }

    public static HashMap<String, String> get_hashMap(String param, String val) {
        HashMap<String, String> hashMap;
        hashMap = new HashMap<>();
        hashMap.put(param, val);
        return hashMap;
    }
    /**
     * @param name_table String name table for request.
     * @param action String type of Action with sql.
     * @param extra String data for insert or remove requests.
     * @return String data - ready sql request.
     */
    public static String request_generator(String name_table, String action, Set<HashMap<String, String>> extra) {
        switch (action) {
            case "get" -> {
                return "SELECT * FROM `" + name_table + "`;";
            }
            case "put" -> {
                String query = "INSERT INTO `" + name_table + "` (`id`,"; // ) VALUES
                for (HashMap<String, String> hashMap : extra) {
                    for (String col_name : hashMap.keySet()) {
                        query = query + "`" + col_name + "`,";
                    }
                    break;
                }
                query = query.substring(0, query.length() - 1) + ") VALUES ";
                for (HashMap<String, String> hashMap : extra) {
                    query = query + "('0',";
                    for (String col_name : hashMap.keySet()) {
                        query = query + "'" + hashMap.get(col_name) + "',";
                    }
                    query = query.substring(0, query.length() - 1);
                    query = query + "),";
                }
                query = query.substring(0, query.length() - 1) + ";";
                return query;
            }
            case "remove" -> {
                String param = "";
                String val = "";
                for (HashMap<String, String> hashMap : extra) {
                    for (String key : hashMap.keySet()) {
                        param = key;
                        val = hashMap.get(key);
                        break;
                    }
                    break;
                }
                return "DELETE * FROM `" + name_table + "` WHERE `" + param + "` = '" + val + "';";
            }

            case "clear" -> {
                return "TRUNCATE TABLE `" + name_table + "`;";
            }
        }
        return "";
    }

}