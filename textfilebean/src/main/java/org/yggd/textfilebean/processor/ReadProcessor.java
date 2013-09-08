package org.yggd.textfilebean.processor;

import java.io.Reader;

public interface ReadProcessor<T> {
    T process(Reader reader, Class<T> beanType, FieldColumn[] fieldColumns);
}
