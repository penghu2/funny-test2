package org.clearfuny.funnytest.interner;

import java.lang.reflect.Method;
import java.util.Iterator;

public interface IDataProvider {

    public Iterator<?> getData(Method m, Class<? extends TestEngine> cls, Object instance);
}
