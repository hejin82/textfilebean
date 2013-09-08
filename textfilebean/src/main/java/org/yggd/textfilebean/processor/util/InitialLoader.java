package org.yggd.textfilebean.processor.util;

import org.yggd.textfilebean.processor.FieldColumn;

public interface InitialLoader {

    FieldColumn[] load(Class<?> beanClass);
}
