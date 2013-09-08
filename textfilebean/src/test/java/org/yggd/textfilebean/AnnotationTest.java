package org.yggd.textfilebean;

import static org.junit.Assert.*;

import org.junit.Test;
import org.yggd.textfilebean.processor.FieldColumn;
import org.yggd.textfilebean.processor.util.AnnotationLoader;
import org.yggd.textfilebean.processor.util.InitialLoader;

public class AnnotationTest {

    @Test
    public void test() {
        InitialLoader loader = new AnnotationLoader();
        FieldColumn[] infos =  loader.load(HogeBean.class);
        assertEquals("i", infos[0].getField().getName());
        assertEquals(3, infos[0].getSpec().getLength());
        assertEquals(TypeEnum.TYPE_INT, infos[0].getSpec().getTypeEnum());
    }

}
class HogeBean {

    @IntegerColumn(length=3) private int i;
    @CharColumn private char delim1;
    @StringColumn(length=10) private String str;
    @CharColumn private char delim2;
    @StringColumn(length=2) private String terminal;

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public char getDelim1() {
        return delim1;
    }

    public void setDelim1(char delim1) {
        this.delim1 = delim1;
    }

    public char getDelim2() {
        return delim2;
    }

    public void setDelim2(char delim2) {
        this.delim2 = delim2;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }
    
}
