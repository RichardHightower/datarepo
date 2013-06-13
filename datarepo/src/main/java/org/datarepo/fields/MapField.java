package org.datarepo.fields;

import org.datarepo.utils.Types;

import java.lang.reflect.Field;
import java.util.Map;

import static org.datarepo.utils.Utils.complain;

public class MapField implements FieldAccess {

    private String name;

    public MapField() {

    }

    public MapField(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getValue(Object obj) {
        if (obj instanceof Map) {
            Map map = (Map) obj;
            return map.get(name);
        }
        complain("Object must be a map");
        return null;
    }

    @Override
    public void setValue(Object obj, Object value) {
        if (obj instanceof Map) {
            Map map = (Map) obj;
            map.put(name, value);
        }
        complain("Object must be a map");
    }

    @Override
    public boolean getBoolean(Object obj) {
        if (obj instanceof Map) {
            Map map = (Map) obj;
            return Types.toBoolean(map.get(name));
        }
        complain("Object must be a map");
        return false;
    }

    @Override
    public void setBoolean(Object obj, boolean value) {
        if (obj instanceof Map) {
            Map map = (Map) obj;
            map.put(name, value);
        }
        complain("Object must be a map");
    }

    @Override
    public int getInt(Object obj) {
        if (obj instanceof Map) {
            Map map = (Map) obj;
            return Types.toInt(map.get(name));
        }
        complain("Object must be a map");
        return -1;
    }

    @Override
    public void setInt(Object obj, int value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public short getShort(Object obj) {
        if (obj instanceof Map) {
            Map map = (Map) obj;
            return Types.toShort(map.get(name));
        }
        complain("Object must be a map");
        return -1;
    }

    @Override
    public void setShort(Object obj, short value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public char getChar(Object obj) {
        if (obj instanceof Map) {
            Map map = (Map) obj;
            return Types.toChar(map.get(name));
        }
        complain("Object must be a map");
        return 0;
    }

    @Override
    public void setChar(Object obj, char value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public long getLong(Object obj) {
        if (obj instanceof Map) {
            Map map = (Map) obj;
            return Types.toLong(map.get(name));
        }
        complain("Object must be a map");
        return -1;
    }

    @Override
    public void setLong(Object obj, long value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public double getDouble(Object obj) {
        if (obj instanceof Map) {
            Map map = (Map) obj;
            return Types.toDouble(map.get(name));
        }
        complain("Object must be a map");
        return Double.NaN;
    }

    @Override
    public void setDouble(Object obj, double value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public float getFloat(Object obj) {
        if (obj instanceof Map) {
            Map map = (Map) obj;
            return Types.toFloat(map.get(name));
        }
        complain("Object must be a map");
        return Float.NaN;
    }

    @Override
    public void setFloat(Object obj, float value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public byte getByte(Object obj) {
        if (obj instanceof Map) {
            Map map = (Map) obj;
            return Types.toByte(map.get(name));
        }
        complain("Object must be a map");
        return Byte.MAX_VALUE;
    }

    @Override
    public void setByte(Object obj, byte vaue) {

    }

    @Override
    public Object getObject(Object obj) {
        if (obj instanceof Map) {
            Map map = (Map) obj;
            return map.get(name);
        }
        complain("Object must be a map");
        return -1;
    }

    @Override
    public void setObject(Object obj, Object value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public Field getField() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isFinal() {
        return false;
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    @Override
    public boolean isVolatile() {
        return false;
    }

    @Override
    public boolean isQualified() {
        return false;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public Class<?> getType() {
        return null;
    }
}
