package doctor.eco5.data;

public class keys {

    private static final boolean local_mode = false;

    //private static final String sql_url = "jdbc:mysql://localhost/fatta?useUnicode=true&characterEncoding=utf8";//jdbc:mysql://31.31.198.36:3306/u1759379_users-fatta
    private static final String sql_url = "jdbc:mysql://localhost/fatta?useUnicode=true&characterEncoding=utf8";//jdbc:mysql://31.31.198.36:3306/u1759379_users-fatta
    private static final String sql_user = "mysql";//u1759379_server
    private static final String sql_password = "mysql";//u1759379_server@123

/*
    private static final String sql_url = "jdbc:mysql://37.9.134.94:10004/eco4";//jdbc:mysql://31.31.198.36:3306/u1759379_users-fatta
    private static final String sql_user = "komiss";//u1759379_server
    private static final String sql_password = "44444444";//u1759379_server@123
*/
    public static String sql_get_url() {return sql_url;}
    public static String sql_get_user() {return sql_user;}
    public static String sql_get_password() {return sql_password;}
}