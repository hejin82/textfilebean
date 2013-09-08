package org.yggd.textfilebean.processor;

import java.io.Reader;

import org.yggd.textfilebean.processor.util.AnnotationLoader;
import org.yggd.textfilebean.processor.util.InitialLoader;

public class AnnotationBeanReaderFactory<T> implements ReaderFactory<T> {

    private ReadProcessor<T> processor = new CharacterReadProcessor<T>();
    private Class<T> type = null;

    
    public AnnotationBeanReaderFactory(T... t) {
        @SuppressWarnings("unchecked")
        Class<T> type = (Class<T>) t.getClass().getComponentType();
        this.type = type;
    }

    private InitialLoader loader = new AnnotationLoader();

    public void changeLoader(InitialLoader loader) {
        this.loader = loader;
    }

    public void changeProcessor(ReadProcessor<T> processor) {
        this.processor = processor;
    }

    @Override
    public RecordReader<T> createReader(Reader reader) {
        BeanReader<T> beanReader = new BeanReader<T>(
                reader,
                type,
                loader,
                processor);
        beanReader.initLoader();
        return beanReader;
    }
}
