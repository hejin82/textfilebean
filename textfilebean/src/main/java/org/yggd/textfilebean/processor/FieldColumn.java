package org.yggd.textfilebean.processor;

import java.lang.reflect.Field;

public class FieldColumn {

    private Field field = null;
    private ColumnSpec spec = null;

    public FieldColumn(Field field, ColumnSpec spec) {
        this.field = field;
        this.spec = spec;
    }

    public Field getField() {
        return field;
    }
    public ColumnSpec getSpec() {
        return spec;
    }
}
