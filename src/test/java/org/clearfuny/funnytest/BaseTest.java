package org.clearfuny.funnytest;

import org.clearfuny.funnytest.interner.TestEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@SpringBootTest(classes=App.class)
public class BaseTest extends TestEngine {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    protected DataSource getDataSource() {
        return jdbcTemplate.getDataSource();
    }
}
