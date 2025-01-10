package database;

import com.github.kyanbrix.Main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SQLBuilder {
    private Connection connection;
    private final List<Object> params = new ArrayList<>();
    private final String sqlString;


    public SQLBuilder(String sqlString) {
        this.sqlString = sqlString;

    }

    public SQLBuilder(Connection connection , String sqlString) {
        this.connection= connection;
        this.sqlString = sqlString;
    }

    public SQLBuilder addParameter(Object object) {
        this.params.add(object);
        return this;
    }

    public SQLBuilder addParameters(Object ... objects) {
        this.params.addAll(Arrays.asList(objects));
        return this;
    }

    public int executeUpdate() throws SQLException {

        Connection con = this.connection == null ? Main.getInstance().getConnectionFromPool().getConnection() : this.connection;

        try (var ps = con.prepareStatement(sqlString)) {

            if (!params.isEmpty()) {

                int i = 1;

                for (Object o : params) {
                    ps.setObject(i,o);
                    i++;
                }
            }

            return executeUpdate();

        }




    }



    public ResultSet executeQuery() throws SQLException {

        Connection con = this.connection == null ? Main.getInstance().getConnectionFromPool().getConnection() : this.connection;

        try (var ps = con.prepareStatement(sqlString)) {

            if (!params.isEmpty()) {

                int i = 1;

                for (Object o : params) {
                    ps.setObject(i,o);
                    i++;
                }
            }

            return executeQuery();
        }

    }

}
