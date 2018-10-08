package org.clearfuny.funnytest.util;

import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.*;
import org.dbunit.dataset.datatype.IDataTypeFactory;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.dataset.xml.XmlDataSetWriter;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.ext.oracle.Oracle10DataTypeFactory;
import org.dbunit.ext.oracle.OracleDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.dbunit.util.fileloader.CsvDataFileLoader;
import org.junit.Assert;

import java.io.*;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DBUtil {

    private IDatabaseConnection conn;

    public String ROOT_URL = "/tmp/";

    /**
     * DBUtil 默认构造函数
     * @param connection
     * @param type
     * @throws DatabaseUnitException
     */
    public DBUtil(Connection connection, DBType type) throws DatabaseUnitException {
        /* 初始化根目录 */
        ROOT_URL = this.getClass().getClassLoader().getResource("").getPath();
        this.conn = new DatabaseConnection(connection);

        DatabaseConfig dbConfig = conn.getConfig();

        //默认是mysql
        IDataTypeFactory dataTypeFactory = new MySqlDataTypeFactory();

        //设置不同的数据工厂，主要是数据上的差异
        if (type !=null ){
            switch (type) {
                case MYSQL:
                    dataTypeFactory = new MySqlDataTypeFactory();
                    break;
                case ORACLE:
                    dataTypeFactory = new OracleDataTypeFactory();
                    break;
                case ORACLE10:
                    dataTypeFactory = new Oracle10DataTypeFactory();
                    break;
            }
        }
        dbConfig.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY,  dataTypeFactory);
    }

    public enum DBType{
        MYSQL, ORACLE, ORACLE10
    }


    /**
     * 从xml文件中获取DataSet
     * @param path 文件名 & 相对路径
     * @return
     * @throws DataSetException
     * @throws IOException
     */
    public IDataSet getXmlDataSet(String path) throws DataSetException, IOException {
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        builder.setColumnSensing(true);
        return builder.build(new File(path));
    }

    /**
     * 清空table
     * @param tableName 表名
     * @throws Exception
     */
    public void clearTable(String tableName) throws Exception {
        DefaultDataSet dataset = new DefaultDataSet();
        dataset.addTable(new DefaultTable(tableName));
        DatabaseOperation.DELETE_ALL.execute(conn, dataset);
    }

    /**
     * 验证表是否为空
     * @param tableName
     * @throws DataSetException
     * @throws SQLException
     */
    public void verifyTableEmpty(String tableName) throws DataSetException, SQLException {
        Assert.assertEquals(0, conn.createDataSet().getTable(tableName).getRowCount());
    }

    /**
     * 验证表不为空
     * @param tableName
     * @throws DataSetException
     * @throws SQLException
     */
    protected void verifyTableNotEmpty(String tableName) throws DataSetException, SQLException {
        Assert.assertNotEquals(0, conn.createDataSet().getTable(tableName).getRowCount());
    }

    /**
     * 导出数据
     * @param table 表名
     * @param sql sql语句
     * @param resultFile 结果文件
     * @throws SQLException
     * @throws DatabaseUnitException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void exportData(String table, String sql, String resultFile) throws SQLException, DatabaseUnitException, FileNotFoundException, IOException {
        QueryDataSet dataSet = null;

        try {
            dataSet = new QueryDataSet(conn);
            dataSet.addTable(table, sql);
        } finally {
            if (dataSet != null) {
                FlatXmlDataSet.write(dataSet, new FileOutputStream(resultFile));
            }
        }
    }

    /**
     * 执行sql
     * @param sql
     * @throws Exception
     */
    public boolean execSql(String sql) throws SQLException {
        Connection con = this.conn.getConnection();
        Statement stmt = con.createStatement();
        try {
            return stmt.execute(sql);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    /**
     * 校验data
     * @param tableName
     * @param sql
     * @param expectedDataSet
     * @throws Exception
     */
    public void assertDataSet(String tableName, String sql, IDataSet expectedDataSet) throws Exception {

        QueryDataSet loadedDataSet = new QueryDataSet(conn);
        loadedDataSet.addTable(tableName, sql);
        ITable table1 = loadedDataSet.getTable(tableName);
        ITable table2 = expectedDataSet.getTable(tableName);
        Assert.assertEquals(table2.getRowCount(), table1.getRowCount());

        /* 过滤不需要比较的字段，对标expectData */
        ITable filteredTable = DefaultColumnFilter.includedColumnsTable(table1, table2.getTableMetaData().getColumns());
        Assertion.assertEquals(table2, filteredTable);
    }


    /**
     * 导出data
     * @param tableNameList
     * @param resultFile
     * @throws DataSetException
     * @throws IOException
     */
    public void exportData(List<String> tableNameList, String resultFile) throws DataSetException, IOException {
        QueryDataSet dataSet = null;

        if (tableNameList == null || tableNameList.size() == 0) {
            return;
        }
        try {
            dataSet = new QueryDataSet(conn);
            for (String tableName : tableNameList) {
                dataSet.addTable(tableName);
            }
        } finally {
            if (dataSet != null) {
                FlatXmlDataSet.write(dataSet, new FileOutputStream(resultFile));
            }
        }
    }

    /**
     * 插入csv
     * @param csvFilePath
     * @throws DatabaseUnitException
     * @throws SQLException
     */
    public void insertByCsv(String csvFilePath) throws DatabaseUnitException, SQLException {

        IDataSet dataSet = new CsvDataFileLoader().load(csvFilePath);
        DatabaseOperation.INSERT.execute(conn, dataSet);
    }

    public void insertByXml(String xmlPath) throws MalformedURLException, DatabaseUnitException, SQLException {
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(new File(xmlPath));
        insertDataSet(dataSet);
    }

    public void deleteByXml(String xmlPath) throws DatabaseUnitException, SQLException, MalformedURLException {
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(new File(xmlPath));
        DatabaseOperation.DELETE.execute(conn, dataSet);
    }

    public void insertDataSet(IDataSet dataSet) throws DatabaseUnitException, SQLException {
        DatabaseOperation.INSERT.execute(conn, dataSet);
    }


    /**
     * 备份
     * @param tempFile 备份文件
     * @param tableName 表名称
     * @throws Exception
     */
    public void backupCustom(File tempFile, String... tableName) throws Exception {
        // back up specific files
        QueryDataSet qds = new QueryDataSet(conn);
        for (String str : tableName) {

            qds.addTable(str);
        }
        tempFile = File.createTempFile("temp", ".xml");
        FlatXmlDataSet.write(qds, new FileWriter(tempFile), "UTF8");
//        Writer writer = new FileWriter(tempFile);
//        XmlDataSetWriter w = new XmlDataSetWriter(writer, "UTF-8");
//        w.write(qds);
//        writer.flush();
//        writer.close();
    }


    /**
     * 回滚
     * @param backFile 备份文件
     * @throws FileNotFoundException
     * @throws DatabaseUnitException
     * @throws SQLException
     */
    public void rollback(File backFile) throws FileNotFoundException, DatabaseUnitException, SQLException {
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        builder.setColumnSensing(true);
        IDataSet ds =builder.build(new FileInputStream(backFile));
        DatabaseOperation.CLEAN_INSERT.execute(conn, ds);
    }

    /**
     * 替换dataset中的 [null] 占位标记为 null 对象
     * @param dataSet
     * @return
     */
    protected ReplacementDataSet createReplacementDataSet(IDataSet dataSet) {
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(dataSet);

        // Configure the replacement dataset to replace '[NULL]' strings with null.
        replacementDataSet.addReplacementObject("[null]", null);

        return replacementDataSet;
    }
}
