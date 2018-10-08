package org.clearfuny.funnytest.interner;

import org.clearfuny.funnytest.util.DBUtil;
import org.dbunit.DatabaseUnitException;
import org.clearfuny.funnytest.App;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.TestInstance;

import javax.sql.DataSource;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * 集成测试基类
 */

public abstract class TestEngine extends AbstractTestNGSpringContextTests implements IDataProvider
{
    protected DBUtil dbUtil;


    @BeforeClass
    public void beforeAllClass() throws SQLException, DatabaseUnitException, IOException {

        dbUtil = new DBUtil(this.getDataSource().getConnection(), DBUtil.DBType.MYSQL);

    }

    @DataProvider(name = "JsonDataProvider")
    public Iterator<Object[]> getData(Method m, @TestInstance Object instance){
        try {
            return (new JsonDataProvider()).getData(m, this.getClass(), instance);
        } catch (Exception e) {
            // TODO 说明解析失败了
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterator<?> getData(Method m, Class<? extends TestEngine> cls, Object instance) {
        return null;
    }

    public DBUtil getDBUtil() {
        return dbUtil;
    }

    protected abstract DataSource getDataSource();
}
