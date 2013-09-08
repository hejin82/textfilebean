package org.yggd.textfilebean.processor;

import java.io.Reader;

public interface ReaderFactory<T> {

    RecordReader<T> createReader(Reader reader);

}
