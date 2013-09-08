package org.yggd.textfilebean.processor;

import org.yggd.textfilebean.TypeEnum;

public class ColumnSpec {

    private int length = -1;
    private TypeEnum typeEnum = null;
    private char delim;
    private boolean strict = false;

    public int getLength() {
        return length;
    }
    public void setLength(int length) {
        this.length = length;
    }
    public TypeEnum getTypeEnum() {
        return typeEnum;
    }
    public void setTypeEnum(TypeEnum typeEnum) {
        this.typeEnum = typeEnum;
    }
    public char getDelim() {
        return delim;
    }
    public void setDelim(char delim) {
        this.delim = delim;
    }
    public boolean isStrict() {
        return strict;
    }
    public void setStrict(boolean strict) {
        this.strict = strict;
    }
}
