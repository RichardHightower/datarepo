package org.datarepo.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;


import sun.misc.Unsafe;

import static org.datarepo.reflection.Types.*;
import static org.datarepo.Utils.*;


public class UnsafeField implements FieldAccess {



    private static Unsafe getUnsafe() {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            return (Unsafe)f.get(null);
        } catch (Exception e) { return null; }
    }

    static final Unsafe unsafe = getUnsafe();
    protected final Field field;
    protected  long offset;
    protected final boolean isFinal;
    protected final Object base;
    protected final boolean isStatic;
    protected final boolean isVolatile;
    protected final boolean qualified;
    protected final boolean readOnly;
    private final Class<?> type;
    private final String name;

    public UnsafeField(Field f) {
        name = f.getName();
        field = f;

        isFinal = Modifier.isFinal(field.getModifiers());
        isStatic = Modifier.isStatic(field.getModifiers());

        if (isStatic) {
            base = unsafe.staticFieldBase(field);
            offset = unsafe.staticFieldOffset(field);
        } else {
            offset = unsafe.objectFieldOffset(field);
            base = null;
        }
        isVolatile = Modifier.isVolatile(field.getModifiers());
        qualified = isFinal || isVolatile;
        readOnly = isFinal || isStatic;
        type = f.getType();
    }


    @Override
    public Object getValue(Object obj) {
        if (type == pint) {
            int i = this.getInt(obj);
            return Integer.valueOf(i);
        } else if (type == plong) {
            long l = this.getLong(obj);
            return Long.valueOf(l);
        } else if (type == pbyte) {
            byte b = this.getByte(obj);
            return Byte.valueOf(b);
        } else if (type == pshort) {
            short s = this.getShort(obj);
            return Short.valueOf(s);
        } else if (type == pchar) {
            char c = this.getChar(obj);
            return Character.valueOf(c);
        } else if (type == pdouble) {
            double d = this.getDouble(obj);
            return Double.valueOf(d);
        } else if (type == pfloat) {
            float f = this.getFloat(obj);
            return Float.valueOf(f);
        } else {
            return this.getObject(obj);
        }
    }


    @Override
    public boolean getBoolean(Object obj) {
        if (isVolatile) {
            return unsafe.getBooleanVolatile(obj, offset);
        } else {
            return unsafe.getBoolean(obj, offset);
        }
    }

    @Override
    public int getInt(Object obj) {
        if (isVolatile) {
            return unsafe.getIntVolatile(obj, offset);
        } else {
            return unsafe.getInt(obj, offset);
        }

    }

    /* (non-Javadoc)
     * @see org.facile.reflect.FieldAccess#getShort(java.lang.Object)
     */
    @Override
    public short getShort(Object obj) {
        if (isVolatile) {
            return unsafe.getShortVolatile(obj, offset);
        } else {
            return unsafe.getShort(obj, offset);
        }

    }


    @Override
    public char getChar(Object obj) {
        if (isVolatile) {
            return unsafe.getCharVolatile(obj, offset);
        } else {
            return unsafe.getChar(obj, offset);
        }

    }


    @Override
    public long getLong(Object obj) {
        if (isVolatile) {
            return unsafe.getLongVolatile(obj, offset);
        } else {
            return unsafe.getLong(obj, offset);
        }

    }


    @Override
    public double getDouble(Object obj) {
        if (isVolatile) {
            return unsafe.getDoubleVolatile(obj, offset);
        } else {
            return unsafe.getDouble(obj, offset);
        }

    }


    @Override
    public float getFloat(Object obj) {
        if (isVolatile) {
            return unsafe.getFloatVolatile(obj, offset);
        } else {
            return unsafe.getFloat(obj, offset);
        }

    }


    @Override
    public byte getByte(Object obj) {
        if (isVolatile) {
            return unsafe.getByteVolatile(obj, offset);
        } else {
            return unsafe.getByte(obj, offset);
        }

    }


    @Override
    public Object getObject(Object obj) {
        if (isVolatile) {
            return unsafe.getObjectVolatile(obj, offset);
        } else {
            return unsafe.getObject(obj, offset);
        }

    }




    @Override
    public boolean getBoolean() {
        return getBoolean(base);
    }


    @Override
    public int getInt() {
        return getInt(base);

    }


    @Override
    public short getShort() {
        return getShort(base);


    }


    @Override
    public long getLong() {
        return getLong(base);
    }

    @Override
    public double getDouble() {
        return getDouble(base);
    }


    @Override
    public float getFloat() {
        return getFloat(base);
    }


    @Override
    public byte getByte() {
        return getByte(base);
    }


    @Override
    public Object getObject() {
        return getObject(base);
    }


    @Override
    public Field getField() {
        return field;
    }


    @Override
    public boolean isFinal() {
        return isFinal;
    }

    public Object getBase() {
        return base;
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

        if (isVolatile) {
            unsafe.putBooleanVolatile(obj, offset, value);
        } else {
            unsafe.putBoolean(obj, offset, value);
        }

    }


    @Override
    public void setInt(Object obj, int value) {
        if (isVolatile) {
            unsafe.putIntVolatile(obj, offset, value);
        } else {
            unsafe.putInt(obj, offset, value);
        }

    }


    @Override
    public void setShort(Object obj, short value) {
        if (isVolatile) {
            unsafe.putShortVolatile(obj, offset, value);
        } else {
            unsafe.putShort(obj, offset, value);
        }


    }


    @Override
    public void setChar(Object obj, char value) {
        if (isVolatile) {
            unsafe.putCharVolatile(obj, offset, value);
        } else {
            unsafe.putChar(obj, offset, value);
        }

    }


    @Override
    public void setLong(Object obj, long value) {
        if (isVolatile) {
            unsafe.putLongVolatile(obj, offset, value);
        } else {
            unsafe.putLong(obj, offset, value);
        }

    }


    @Override
    public void setDouble(Object obj, double value) {
        if (isVolatile) {
            unsafe.putDoubleVolatile(obj, offset, value);
        } else {
            unsafe.putDouble(obj, offset, value);
        }

    }


    @Override
    public void setFloat(Object obj, float value) {
        if (isVolatile) {
            unsafe.putFloatVolatile(obj, offset, value);
        } else {
            unsafe.putFloat(obj, offset, value);
        }
    }


    @Override
    public void setByte(Object obj, byte value) {
        if (isVolatile) {
            unsafe.putByteVolatile(obj, offset, value);
        } else {
            unsafe.putByte(obj, offset, value);
        }
    }


    @Override
    public void setObject(Object obj, Object value) {
        if (isVolatile) {
            unsafe.putObjectVolatile(obj, offset, value);
        } else {
            unsafe.putObject(obj, offset, value);
        }

    }


    @Override
    public String toString() {
        return "UnsafeField [field=" + field + ", offset=" + offset
                + ", isFinal=" + isFinal + ", base=" + base + ", isStatic="
                + isStatic + ", isVolatile=" + isVolatile + ", qualified="
                + qualified + ", readOnly=" + readOnly + ", type=" + type
                + ", name=" + name + "]";
    }



}
