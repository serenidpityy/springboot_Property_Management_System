package com.wfit.springbootbookstores;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest
class DBConnectionTest {

    @Autowired
    private DataSource dataSource;

    @Test
    void testConnection() {
        try (Connection connection = dataSource.getConnection()) {
            assert connection != null;
            assert connection.isValid(1);
            System.out.println("数据库连接成功！");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("数据库连接失败", e);
        }
    }
}