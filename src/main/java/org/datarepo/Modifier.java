package org.datarepo;

/**
 * This encapsulates modifying a property.
 * The property could be deeply nested or just simple access.
 * If you don't specify property modifiers, then one gets generated that uses
 * reflection, byte code manipulation or UnSafe.
 */
public interface Modifier {
        void setValue(Object object);
        void setInt(int intValue);
        void setLong(long longValue);
        void setString(String stringValue);

}
