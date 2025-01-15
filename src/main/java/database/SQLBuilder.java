package database;

import com.github.kyanbrix.Main;
import utilities.Utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SQLBuilder {
    private final List<Object> params = new ArrayList<>();
    private Connection connection;
    private final String sqlString;
    private Boolean closeConnection = true;



    public SQLBuilder(String sqlString) {
        this.sqlString = sqlString;
    }

    public SQLBuilder addConnection(Connection connection) {
        this.connection = connection;
        closeConnection = false;
        return this;
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

        try (Connection c = connection == null ? Main.getInstance().getConnectionFromPool().getConnection() : connection) {
            var ps = c.prepareStatement(sqlString);

            if (!params.isEmpty()) {
                int i = 1;

                for (Object o : params) {
                    ps.setObject(i,o);
                    i++;
                }
            }

            return ps.executeUpdate();

        } finally {

            if (closeConnection) {
                Utils.closeables(connection);
            }
        }

    }



    public ResultSet executeQuery() throws SQLException {

        connection = Main.getInstance().getConnectionFromPool().getConnection();

        var ps = connection.prepareStatement(sqlString);

            if (!params.isEmpty()) {
                int i = 1;

                for (Object o : params) {
                    ps.setObject(i,o);
                    i++;
                }
            }

        return ps.executeQuery();

    }


    public void close() {

        try {

            connection.close();

        }catch (SQLException e) {

            throw new RuntimeException(e);
        }


    }

}
