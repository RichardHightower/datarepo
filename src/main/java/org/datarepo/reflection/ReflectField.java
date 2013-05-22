package org.datarepo.reflection;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.datarepo.reflection.Types.*;
import static org.datarepo.utils.Utils.*;

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
        readOnly = isFinal || isStatic ;
        type = f.getType();
        name = f.getName();
    }

    @Override
    public Object getValue(Object obj) {
        try {
            return field.get(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean getBoolean(Object obj) {
        try {
            return field.getBoolean(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public int getInt(Object obj) {
        try {
            return field.getInt(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public short getShort(Object obj) {
        try {
            return field.getShort(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public char getChar(Object obj) {
        try {
            return field.getChar(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public long getLong(Object obj) {
        try {
            return field.getLong(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public double getDouble(Object obj) {
        try {
            return field.getDouble(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public float getFloat(Object obj) {
        try {
            return field.getFloat(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte getByte(Object obj) {
        try {
            return field.getByte(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object getObject(Object obj) {
        return getValue(obj);
    }

    @Override
    public boolean getBoolean() {
        return getBoolean(null);
    }

    @Override
    public int getInt() {
        return getInt(null);

    }

    @Override
    public short getShort() {
        return getShort(null);
    }


    @Override
    public long getLong() {
        return getLong(null);
    }


    @Override
    public double getDouble() {
        return getDouble(null);
    }

    @Override
    public float getFloat() {
        return getFloat(null);
    }

    @Override
    public byte getByte() {
        return getByte(null);
    }

    @Override
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
            throw new RuntimeException(e);

        }

    }

    @Override
    public void setInt(Object obj, int value) {
        try {
            field.setInt(obj, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);

        }

    }

    @Override
    public void setShort(Object obj, short value) {
        try {
            field.setShort(obj, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);

        }

    }

    @Override
    public void setChar(Object obj, char value) {
        try {
            field.setChar(obj, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);

        }

    }

    @Override
    public void setLong(Object obj, long value) {
        try {
            field.setLong(obj, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);

        }

    }

    @Override
    public void setDouble(Object obj, double value) {
        try {
            field.setDouble(obj, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);

        }

    }

    @Override
    public void setFloat(Object obj, float value) {
        try {
            field.setFloat(obj, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);

        }

    }

    @Override
    public void setByte(Object obj, byte value) {
        try {
            field.setByte(obj, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);

        }

    }

    @Override
    public void setObject(Object obj, Object value) {
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);

        }

    }

}
