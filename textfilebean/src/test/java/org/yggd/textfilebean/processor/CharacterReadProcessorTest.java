package org.yggd.textfilebean.processor;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;

import org.junit.Test;
import org.yggd.textfilebean.CharColumn;
import org.yggd.textfilebean.DelimCharColumn;
import org.yggd.textfilebean.IntegerColumn;
import org.yggd.textfilebean.StringColumn;
import org.yggd.textfilebean.TypeEnum;
import org.yggd.textfilebean.processor.CharacterReadProcessor;
import org.yggd.textfilebean.processor.exception.RecordProcessException;
import org.yggd.textfilebean.processor.util.AnnotationLoader;
import org.yggd.textfilebean.processor.util.InitialLoader;

public class CharacterReadProcessorTest {

    @Test
    public void testBindColumn() throws Exception {
        CharacterReadProcessor<FooBean> processor = new CharacterReadProcessor<FooBean>();
        FooBean bean = new FooBean();

        // bind-i
        ColumnSpec spec = new ColumnSpec();
        spec.setLength(3);
        spec.setTypeEnum(TypeEnum.TYPE_INT);
        Field field = FooBean.class.getDeclaredField("i");
        FieldColumn fieldColumn = new FieldColumn(field, spec);
        char[] columnChars = new char[]{'0', '1', '2'};
        processor.bindColumn(bean, fieldColumn, columnChars);
        assertEquals(12, bean.getI());
        
        // bind-user
        spec.setLength(10);
        spec.setTypeEnum(TypeEnum.TYPE_STRING);
        field = FooBean.class.getDeclaredField("user");
        fieldColumn = new FieldColumn(field, spec);
        columnChars = " ïêé“è¨òHé¿ìƒÅ@Å@Å@".toCharArray();
        processor.bindColumn(bean, fieldColumn, columnChars);
        assertEquals(" ïêé“è¨òHé¿ìƒÅ@Å@Å@", bean.getUser());
    }

    @Test
    public void testProcess01() throws Exception {
        CharacterReadProcessor<FooBean> processor = new CharacterReadProcessor<FooBean>();
        InitialLoader loader = new AnnotationLoader();
        FieldColumn[] fieldColumns = loader.load(FooBean.class);
        Reader reader = null;
        try  {
            reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream("sample/sample.txt"), "Windows-31J"));
            // 1st line.
            FooBean bean = processor.process(reader, FooBean.class, fieldColumns);
            assertEquals(123, bean.getI());
            assertEquals(':', bean.getDelim1());
            assertEquals("1234567890", bean.getUser());
            assertEquals(':', bean.getDelim2());
            assertEquals("1234567890", bean.getName());
            assertEquals("\n", bean.getTerminal());

            // 2nd.
            bean = processor.process(reader, FooBean.class, fieldColumns);
            assertEquals("Michel    ", bean.getUser());

            // 3rd.
            bean = processor.process(reader, FooBean.class, fieldColumns);
            assertEquals("Jacson    ", bean.getName());

            // 4th.
            bean = processor.process(reader, FooBean.class, fieldColumns);
            assertEquals("Williams  ", bean.getName());

            // 5th.
            bean = processor.process(reader, FooBean.class, fieldColumns);
            assertEquals("å†ï∫âq       ", bean.getName());

            // last
            try {
                processor.process(reader, FooBean.class, fieldColumns);
                fail("must throws RecordProcessException.");
            } catch (RecordProcessException e) {
                assertEquals("invalid column[a\n] read length:2 expected:10", e.getMessage());
            }

            // EOF
            assertNull(processor.process(reader, FooBean.class, fieldColumns));
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    @Test
    public void testProcess02() throws Exception {
        AnnotationBeanReaderFactory<DynamicBean> factory = new AnnotationBeanReaderFactory<DynamicBean>();
        Reader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream("sample/dynamic.txt"), "Windows-31J"));
            RecordReader<DynamicBean> beanReader = factory.createReader(reader);
            // 1st line.
            DynamicBean bean = beanReader.readLine();
            assertEquals(1, bean.getIndex());
            assertEquals(':', bean.getDelimColon());
            assertEquals("This", bean.getCol1());
            assertEquals("is", bean.getCol2());
            assertEquals("dynamic length text file.", bean.getCol3());

            // 2nd.
            bean = beanReader.readLine();
            assertEquals(2, bean.getIndex());
            assertEquals(':', bean.getDelimColon());
            assertEquals("Space", bean.getCol1());
            assertEquals("is", bean.getCol2());
            assertEquals("delimiter character and end line with LF.", bean.getCol3());

            // EOF
            assertNull(beanReader.readLine());
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
}

class DynamicBean {
    @IntegerColumn(length=3) private int index;
    @CharColumn private char delimColon;
    @DelimCharColumn(delim=' ') private String col1;
    @DelimCharColumn(delim=' ') private String col2;
    @DelimCharColumn(delim='\n') private String col3;

    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
    public char getDelimColon() {
        return delimColon;
    }
    public void setDelimColon(char delimColon) {
        this.delimColon = delimColon;
    }
    public String getCol1() {
        return col1;
    }
    public void setCol1(String col1) {
        this.col1 = col1;
    }
    public String getCol2() {
        return col2;
    }
    public void setCol2(String col2) {
        this.col2 = col2;
    }
    public String getCol3() {
        return col3;
    }
    public void setCol3(String col3) {
        this.col3 = col3;
    }
}

class FooBean {
    @IntegerColumn(length=3) private int i;
    @CharColumn private char delim1;
    @StringColumn(length=10, strict=true) private String user;
    @CharColumn private char delim2;
    @StringColumn(length=10) private String name;
    @StringColumn(length=1) private String terminal;

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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public char getDelim2() {
        return delim2;
    }
    public void setDelim2(char delim2) {
        this.delim2 = delim2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }


}
