package data;

import database.SQLBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.OffsetDateTime;

public class GetMessages {

    private static final Logger log = LoggerFactory.getLogger(GetMessages.class);

    public static void insertUserMessages(Connection connection, String content, long msgID, long userID, OffsetDateTime msgCreatedTime) {

        var ps = "INSERT INTO messages (userid, msg_id, msg_cnt, createdtime) VALUES (?,?,?,?)";

        var sql = new SQLBuilder(ps)
                .
                .addParameters(userID,msgID,content,msgCreatedTime);

        try {
            sql.executeUpdate();
        }catch (SQLException e) {
            log.error("Cannot add message data!",e);
        }


    }

}
