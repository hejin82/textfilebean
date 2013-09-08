package org.yggd.textfilebean.processor;

import java.io.Reader;

import org.yggd.textfilebean.processor.util.InitialLoader;

public class BeanReader<T> implements RecordReader<T> {

    private Class<T> beanType = null;
    private FieldColumn[] fieldColumns = null;
    private InitialLoader loader = null;
    private ReadProcessor<T> processor = null;
    private Reader reader = null;

    protected BeanReader(Reader reader, Class<T> clazz, InitialLoader loader, ReadProcessor<T> processor) {
        this.reader = reader;
        this.beanType = clazz;
        this.loader = loader;
        this.processor = processor;
    }

    protected void initLoader() {
        assert loader != null;
        this.fieldColumns = loader.load(beanType);
    }

    @Override
    public T readLine() {
        return processor.process(reader ,beanType, fieldColumns);
    }
}
