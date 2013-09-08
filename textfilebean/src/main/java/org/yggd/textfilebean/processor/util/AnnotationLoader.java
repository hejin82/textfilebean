package org.yggd.textfilebean.processor.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.yggd.textfilebean.CharColumn;
import org.yggd.textfilebean.DelimCharColumn;
import org.yggd.textfilebean.IntegerColumn;
import org.yggd.textfilebean.StringColumn;
import org.yggd.textfilebean.TypeEnum;
import org.yggd.textfilebean.processor.ColumnSpec;
import org.yggd.textfilebean.processor.FieldColumn;

public class AnnotationLoader implements InitialLoader {

    @SuppressWarnings("unchecked")
    protected static Class<? extends Annotation>[] processorTypes() {
        return (Class<? extends Annotation>[]) new Class<?>[]{
                StringColumn.class,
                IntegerColumn.class,
                CharColumn.class,
                DelimCharColumn.class,
        };
    }

    protected Annotation getFieldDeclareAnnotation(Field f, Class<? extends Annotation> type) {
        assert type != null;
        if (f == null) {
            return null;
        }
        Annotation ann = f.getAnnotation(type);
        return ann;
    }

    @Override
    public FieldColumn[] load(Class<?> beanClass) {
        List<FieldColumn> recordInfos = new ArrayList<FieldColumn>();
        Field[] fields = beanClass.getDeclaredFields();
        for (Field f : fields) {
            for (Class<? extends Annotation> type : processorTypes()) {
                Annotation ann = getFieldDeclareAnnotation(f, type);
                if (ann != null) {
                    recordInfos.add(new FieldColumn(f, getSpec(ann)));
                    break;
                }
            }
        }
        return recordInfos.toArray(new FieldColumn[0]);
    }

    protected ColumnSpec getSpec(Annotation ann) {
        assert ann != null;
        Class<? extends Annotation> type = ann.annotationType();
        assert CharColumn.class.equals(type)
            || IntegerColumn.class.equals(type)
            || StringColumn.class.equals(type)
            || DelimCharColumn.class.equals(type);

        if (ann instanceof StringColumn) {
            StringColumn stringColumn = (StringColumn)ann;
            ColumnSpec spec = new ColumnSpec();
            spec.setLength(stringColumn.length());
            spec.setTypeEnum(TypeEnum.TYPE_STRING);
            spec.setStrict(stringColumn.strict());
            return spec;
        } else if (ann instanceof IntegerColumn) {
            IntegerColumn integerColumn = (IntegerColumn)ann;
            ColumnSpec spec = new ColumnSpec();
            spec.setLength(integerColumn.length());
            spec.setTypeEnum(TypeEnum.TYPE_INT);
            spec.setStrict(integerColumn.strict());
            return spec;
        } else if (ann instanceof DelimCharColumn) {
            int length = -1;
            ColumnSpec spec = new ColumnSpec();
            spec.setLength(length);
            spec.setTypeEnum(TypeEnum.TYPE_DELIM);
            spec.setDelim(((DelimCharColumn) ann).delim());
            return spec;
        }
        ColumnSpec spec = new ColumnSpec();
        spec.setLength(1);
        spec.setTypeEnum(TypeEnum.TYPE_CHAR);
        return spec;
    }
}
