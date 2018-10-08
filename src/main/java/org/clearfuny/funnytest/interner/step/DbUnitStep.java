package org.clearfuny.funnytest.interner.step;

import com.alibaba.fastjson.JSONObject;
import org.clearfuny.funnytest.exception.RunFailException;
import org.clearfuny.funnytest.interner.TestEngine;
import org.clearfuny.funnytest.interner.model.Constant;
import org.clearfuny.funnytest.util.DBUtil;
import org.dbunit.DatabaseUnitException;
import org.clearfuny.funnytest.interner.runner.TestStepRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.sql.SQLException;

public class DbUnitStep extends TestStepRunner {

    @Override
    public Object execute(JSONObject mainContext) throws RunFailException {
        //获取instance实例
        Object instance = mainContext.get(Constant.THIS);


        //通过反射从实例中获取DBUtil
        if (!(instance instanceof TestEngine))
        {
            throw RunFailException.build(this.getId(), "测试类不是BaseTest类或者其子类", null);
        }

        try {
            Method m = instance.getClass().getMethod("getDBUtil");
            m.setAccessible(true);
            DBUtil dbUtil = (DBUtil) m.invoke(instance);

            String method = params.getString("method");

            //通过方法判断执行哪个函数
            switch (method) {
                case "execSql":
                    execSql(dbUtil);
                    break;
                case "insertByXml":
                    insertByXml(dbUtil, instance);
                    break;
                case "deleteByXml":
                    deleteByXml(dbUtil, instance);
                    break;
            }

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw RunFailException.build(this.getId(), "反射获取|执行 getDBUtil()方法失败", e);
        } catch (SQLException e) {
            throw RunFailException.build(this.getId(), "sql执行异常", e);
        } catch (DatabaseUnitException | MalformedURLException e) {
            throw RunFailException.build(this.getId(), "执行xml插入异常", e);
        }


        return "OK";
    }

    /**
     * 变量替换
     * @param mainContext 主上下文
     */
    @Override
    public void replaceParams(JSONObject mainContext) {
        return;
    }

    private void execSql(DBUtil dbUtil) throws SQLException {
        String sql = this.getParams().getString("sql");
        dbUtil.execSql(sql);
    }

    private void insertByXml(DBUtil dbUtil, Object instance) throws DatabaseUnitException, SQLException, MalformedURLException {
        String xml = this.getParams().getString("xml");
        String path = instance.getClass().getResource(xml).getPath();
        dbUtil.insertByXml(path);
    }

    private void deleteByXml(DBUtil dbUtil, Object instance) throws DatabaseUnitException, SQLException, MalformedURLException {
        String xml = this.getParams().getString("xml");
        String path = instance.getClass().getResource(xml).getPath();
        dbUtil.deleteByXml(path);
    }
}
