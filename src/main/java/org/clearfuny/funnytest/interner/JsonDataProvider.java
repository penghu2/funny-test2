package org.clearfuny.funnytest.interner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.clearfuny.funnytest.interner.model.Constant;
import org.clearfuny.funnytest.util.FileUtils;
import org.clearfuny.funnytest.util.LogUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Iterator;

public class JsonDataProvider implements IDataProvider, Iterator<Object[]>{

    /* 解析索引 */
    private int readIndex=0;

    private JSONArray elements;

    private Object instance;

    private String methodName;

    public static JsonDataProvider getInstance(){
        return new JsonDataProvider();
    }

    @Override
    public Iterator<Object[]> getData(Method m, Class<? extends TestEngine> cls, Object instance) {
        try {

            String configPath = this.getConfigFilePath(m, cls);
            elements = JSON.parseArray(FileUtils.readFile(configPath));
            this.instance = instance;
            this.methodName = m.getName();
            return this;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected String getConfigFilePath(Method m, Class<? extends TestEngine> cls) throws UnsupportedEncodingException {
        String filename = cls.getSimpleName() + "." + m.getName() + ".json";
        LogUtil.info(String.format("start parse config file [%s]", filename));
        String path = cls.getResource("").getPath();
        path = java.net.URLDecoder.decode(path,"utf-8");
        return path+filename;
    }

    @Override
    public boolean hasNext() {
        if (elements != null && elements.size()>0){
            if (elements.size() - readIndex > 0){
                return true;
            }
        }

        return false;
    }

    @Override
    public synchronized Object[] next() {
        JSONObject obj = elements.getJSONObject(readIndex);
        readIndex++;
        Object[] res = new Object[2];
        res[0] = obj.getString("id");
        res[1] = obj;
        obj.put(Constant.THIS, this.instance);
        obj.put(Constant.THIS_METHOD, this.methodName);
        return res;
    }
}
