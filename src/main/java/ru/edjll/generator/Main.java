package ru.edjll.generator;

import org.postgresql.Driver;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

public class Main {

    public static void main(String[] args) throws SQLException {
        Driver driver = new Driver();
        Properties properties = new Properties();
        properties.put("user", "admin");
        properties.put("password", "admin");
        Connection connection = driver.connect("jdbc:postgresql://localhost:5432/social_network", properties);

        System.out.println("Connect");

        Statement statement = connection.createStatement();

        int moscow = 23541;
        int saratov = 25988;
        int volgograd = 41413;
        int saintPetersburg = 1773;

        String [] firstNames = new String[] { "Ivan", "Oleg", "Vladimir", "Aleksey", "Nikolay" };
        String [] lastNames = new String[] { "Kolishev", "Slonov", "Ivanov", "Topolev", "Leskov" };


        System.out.println("Start : " + LocalDateTime.now());

        for (int x = 0; x < 10000; x++) {
            StringBuilder sqlUserEntity = new StringBuilder();
            StringBuilder sqlUserInfo = new StringBuilder();
            StringBuilder sqlUserFriend = new StringBuilder();
            int city = moscow;
            String user_id = null;

            if (x % 10 == 0) city = saratov;
            else if (x % 8 == 0) city = volgograd;
            else if (x % 6 == 0) city = saintPetersburg;

            for (int y = 0; y < 100; y++) {
                UUID uuid = UUID.randomUUID();
                if (y == 0) user_id = uuid.toString();
                else {
                    sqlUserFriend.append("('").append(user_id).append("', '").append(uuid.toString()).append("', ").append(y % 2).append(", '").append(LocalDateTime.now().toString()).append("')");
                    if (y < 99) sqlUserFriend.append(", ");
                }
                sqlUserEntity.append("('").append(uuid.toString()).append("', '").append(uuid.toString()).append("', '").append(firstNames[(y + x) % 5]).append("', '").append(lastNames[(y + x) % 5]).append("', '").append(new Date().getTime()).append("', 'social_network', true)");
                sqlUserInfo.append("('").append(uuid.toString()).append("', ").append(city).append(")");
                if (y < 99) {
                    sqlUserEntity.append(", ");
                    sqlUserInfo.append(", ");
                }
            }

            statement.execute("insert into user_entity (id, username, first_name, last_name, created_timestamp, realm_id, enabled) values " + sqlUserEntity);
            statement.execute("insert into user_info (user_id, city_id) values " + sqlUserInfo);
            statement.execute("insert into user_friend (user_id, friend_id, status, date) values " + sqlUserFriend);

        }

        System.out.println("End : " + LocalDateTime.now());

        statement.close();

        System.out.println("Close statement");

        connection.close();

        System.out.println("Close connection");
    }
}
