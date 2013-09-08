package org.yggd.textfilebean.processor;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.yggd.textfilebean.TypeEnum;
import org.yggd.textfilebean.processor.exception.CreateBeanException;
import org.yggd.textfilebean.processor.exception.RecordProcessException;

public class CharacterReadProcessor<T> implements ReadProcessor<T> {

    @Override
    public T process(Reader reader, Class<T> beanType, FieldColumn[] fieldColumns) {
        assert reader != null;
        assert beanType != null;

        T t = createInstance(beanType);
        try {
            for (FieldColumn fieldColumn : fieldColumns) {
                char[] readChars = null;
                if (TypeEnum.TYPE_DELIM.equals(fieldColumn.getSpec().getTypeEnum())) {
                    readChars = readDynamicLength(reader, fieldColumn);
                } else {
                    readChars = readStaticLength(reader, fieldColumn);
                }
                if (readChars == null) {
                    return null;
                }
                bindColumn(t, fieldColumn, readChars);
            }
        } catch (IOException e) {
            throw new RecordProcessException(e);
        }
        return t;
    }

    protected char[] readStaticLength(Reader reader, FieldColumn column) throws IOException {
        int length = column.getSpec().getLength();
        char[] readChars = new char[length];
        int readLength = reader.read(readChars);
        if (readLength == -1) {
            // EOF”»’è
            return null;
        }
        if (readLength < length) {
            // Žw’èŒ…ˆÈ‰º‚Ì“Ç‚ÝŽæ‚è‚¾‚Á‚½ê‡Anull¬“ü‚ð‚³‚¯‚é‚½‚ß‚É”z—ñ’·‚ð•â³
            char[] columnRead = new char[readLength];
            for (int i = 0; i < readLength; i++) {
                columnRead[i] = readChars[i];
            }
            readChars = columnRead;
        }
        if (column.getSpec().isStrict() && readLength != length) {
            throw new RecordProcessException("invalid column[" + String.valueOf(readChars) + "] read length:" + readLength + " expected:" + length);
        }
        return readChars;
    }

    protected char[] readDynamicLength(Reader reader, FieldColumn column) throws IOException {
        StringBuilder sb = new StringBuilder();
        int ch = -1;
        char delim = column.getSpec().getDelim();
        while (true) {
            ch = reader.read();
            if (ch == -1) {
                break;
            }
            char readChar = (char) ch;
            if (delim == readChar) {
                break;
            }
            sb.append(readChar);
        }
        if (ch == -1) {
            // EOF”»’è
            return null;
        }
        return sb.toString().toCharArray();
    }

    protected void bindColumn(T t, FieldColumn fieldColumn, char[] columnChars) {
        Field field = fieldColumn.getField();
        try {
            PropertyDescriptor prop = new PropertyDescriptor(field.getName(), t.getClass());
            Method setter = prop.getWriteMethod();
            Object obj = typeResolve(fieldColumn, columnChars);
            setter.invoke(t, new Object[]{obj});
        } catch (IntrospectionException e) {
            throw new RecordProcessException(e);
        } catch (IllegalAccessException e) {
            throw new RecordProcessException(e);
        } catch (IllegalArgumentException e) {
            throw new RecordProcessException(e);
        } catch (InvocationTargetException e) {
            throw new RecordProcessException(e);
        }
    }

    protected Object typeResolve(FieldColumn fieldColumn, char[] columnChars) {
        assert fieldColumn != null;
        assert columnChars != null;
        
        TypeEnum typeEnum = fieldColumn.getSpec().getTypeEnum();
        if (TypeEnum.TYPE_STRING.equals(typeEnum) || TypeEnum.TYPE_DELIM.equals(typeEnum)) {
            // TODO padding.
            return new String(columnChars);
        }
        if (TypeEnum.TYPE_INT.equals(typeEnum)) {
            // TODO padding.
            return Integer.valueOf(new String(columnChars));
        }
        return new Character(columnChars[0]);
    }

    protected T createInstance(Class<T> beanType) {
        try {
            return beanType.newInstance();
        } catch (InstantiationException e) {
            throw new CreateBeanException(e);
        } catch (IllegalAccessException e) {
            throw new CreateBeanException(e);
        }
    }
}
