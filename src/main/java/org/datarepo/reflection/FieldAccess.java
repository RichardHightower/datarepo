package org.datarepo.reflection;

import java.lang.reflect.Field;


public interface FieldAccess {
    public abstract String getName();

    public abstract Object getValue(Object obj);
    public abstract void setValue(Object obj, Object value);


    public abstract boolean getBoolean(Object obj);
    public abstract void setBoolean(Object obj, boolean value);


    public abstract int getInt(Object obj);
    public abstract void setInt(Object obj, int value);


    public abstract short getShort(Object obj);
    public abstract void setShort(Object obj, short value);

    public abstract char getChar(Object obj);
    public abstract void setChar(Object obj, char value);


    public abstract long getLong(Object obj);
    public abstract void setLong(Object obj, long value);


    public abstract double getDouble(Object obj);
    public abstract void setDouble(Object obj, double value);


    public abstract float getFloat(Object obj);
    public abstract void setFloat(Object obj, float value);


    public abstract byte getByte(Object obj);
    public abstract void setByte(Object obj, byte vaue);

    public abstract Object getObject(Object obj);
    public abstract void setObject(Object obj, Object value);


    public abstract boolean getBoolean();

    public abstract int getInt();

    public abstract short getShort();

    public abstract long getLong();

    public abstract double getDouble();

    public abstract float getFloat();

    public abstract byte getByte();

    public abstract Object getObject();

    public abstract Field getField();

    public abstract boolean isFinal();

    public abstract boolean isStatic();

    public abstract boolean isVolatile();

    public abstract boolean isQualified();

    public abstract boolean isReadOnly();

    public abstract Class<?> getType();

}