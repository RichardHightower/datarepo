package org.boon.fields;


import org.boon.utils.Types;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.boon.utils.Types.*;
import static org.boon.utils.Utils.*;

public class ReflectField implements FieldAccess {
    protected final Field field;
    protected final boolean isFinal;
    protected final boolean isStatic;
    protected final boolean isVolatile;
    protected final boolean qualified;
    protected final boolean readOnly;
    private final Class<?> type;
    private final String name;

    public ReflectField(Field f) {
        field = f;
        isFinal = Modifier.isFinal(field.getModifiers());
        isStatic = Modifier.isStatic(field.getModifiers());
        isVolatile = Modifier.isVolatile(field.getModifiers());
        qualified = isFinal || isVolatile;
        readOnly = isFinal || isStatic;
        type = f.getType();
        name = f.getName();
    }

    @Override
    public Object getValue(Object obj) {
        try {
            return field.get(obj);
        } catch (Exception e) {
            analyzeError(e, obj);
            return null;
        }
    }

    private void analyzeError(Exception e, Object obj) {
        die(lines(
                e.getClass().getName(),
                sprintf("cause %s", e.getCause()),
                sprintf("Field info name %s, type %s, class that declared field %s", this.getName(), this.getType(), this.getField().getDeclaringClass()),
                sprintf("Type of object passed %s", obj.getClass().getName())
        ), e);

    }

    public boolean getBoolean(Object obj) {
        try {
            return field.getBoolean(obj);
        } catch (Exception e) {
            analyzeError(e, obj);
            return false;
        }

    }

    @Override
    public int getInt(Object obj) {
        try {
            return field.getInt(obj);
        } catch (Exception e) {
            analyzeError(e, obj);
            return 0;
        }
    }

    @Override
    public short getShort(Object obj) {
        try {
            return field.getShort(obj);
        } catch (Exception e) {
            analyzeError(e, obj);
            return 0;
        }
    }

    @Override
    public char getChar(Object obj) {
        try {
            return field.getChar(obj);
        } catch (Exception e) {
            analyzeError(e, obj);
            return 0;
        }
    }

    @Override
    public long getLong(Object obj) {
        try {
            return field.getLong(obj);
        } catch (Exception e) {
            analyzeError(e, obj);
            return 0;
        }
    }

    @Override
    public double getDouble(Object obj) {
        try {
            return field.getDouble(obj);
        } catch (Exception e) {
            analyzeError(e, obj);
            return 0;
        }

    }

    @Override
    public float getFloat(Object obj) {
        try {
            return field.getFloat(obj);
        } catch (Exception e) {
            analyzeError(e, obj);
            return 0;
        }
    }

    @Override
    public byte getByte(Object obj) {
        try {
            return field.getByte(obj);
        } catch (Exception e) {
            analyzeError(e, obj);
            return 0;
        }
    }

    @Override
    public Object getObject(Object obj) {
        return getValue(obj);
    }

    public boolean getStaticBoolean() {
        return getBoolean(null);
    }

    public int getStaticInt() {
        return getInt(null);

    }

    public short getStaticShort() {
        return getShort(null);
    }


    public long getStaticLong() {
        return getLong(null);
    }


    public double getStaticDouble() {
        return getDouble(null);
    }

    public float getStaticFloat() {
        return getFloat(null);
    }

    public byte getStaticByte() {
        return getByte(null);
    }

    public Object getObject() {
        return getObject(null);
    }

    @Override
    public Field getField() {
        return field;
    }


    @Override
    public boolean isFinal() {
        return isFinal;
    }


    @Override
    public boolean isStatic() {
        return isStatic;
    }

    @Override
    public boolean isVolatile() {
        return isVolatile;
    }


    @Override
    public boolean isQualified() {
        return qualified;
    }

    @Override
    public boolean isReadOnly() {
        return readOnly;
    }


    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setValue(Object obj, Object value) {
        if (obj.getClass() == this.type) {
            this.setObject(obj, value);
            return;
        }

        if (type == pint) {
            setInt(obj, toInt(value));
        } else if (type == plong) {
            setLong(obj, toLong(value));
        } else if (type == pbyte) {
            setByte(obj, toByte(value));

        } else if (type == pshort) {
            setShort(obj, toShort(value));

        } else if (type == pchar) {
            setChar(obj, toChar(value));

        } else if (type == pdouble) {
            setDouble(obj, toDouble(value));

        } else if (type == pfloat) {
            setFloat(obj, toFloat(value));

        } else {
            setObject(obj, Types.coerce(type, value));
        }
    }

    @Override
    public void setBoolean(Object obj, boolean value) {
        try {
            field.setBoolean(obj, value);
        } catch (IllegalAccessException e) {
            analyzeError(e, obj);
        }

    }

    @Override
    public void setInt(Object obj, int value) {
        try {
            field.setInt(obj, value);
        } catch (IllegalAccessException e) {
            analyzeError(e, obj);
        }

    }

    @Override
    public void setShort(Object obj, short value) {
        try {
            field.setShort(obj, value);
        } catch (IllegalAccessException e) {
            analyzeError(e, obj);
        }

    }

    @Override
    public void setChar(Object obj, char value) {
        try {
            field.setChar(obj, value);
        } catch (IllegalAccessException e) {
            analyzeError(e, obj);
        }

    }

    @Override
    public void setLong(Object obj, long value) {
        try {
            field.setLong(obj, value);
        } catch (IllegalAccessException e) {
            analyzeError(e, obj);
        }

    }

    @Override
    public void setDouble(Object obj, double value) {
        try {
            field.setDouble(obj, value);
        } catch (IllegalAccessException e) {
            analyzeError(e, obj);
        }

    }

    @Override
    public void setFloat(Object obj, float value) {
        try {
            field.setFloat(obj, value);
        } catch (IllegalAccessException e) {
            analyzeError(e, obj);
        }

    }

    @Override
    public void setByte(Object obj, byte value) {
        try {
            field.setByte(obj, value);
        } catch (IllegalAccessException e) {
            analyzeError(e, obj);
        }

    }

    @Override
    public void setObject(Object obj, Object value) {
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            analyzeError(e, obj);
        }

    }

}
