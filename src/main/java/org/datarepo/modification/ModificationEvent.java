package org.datarepo.modification;

import java.util.ArrayList;
import java.util.List;

import static org.datarepo.utils.Utils.*;

public abstract class ModificationEvent<KEY, ITEM> {

    public static final String ROOT_PROPERTY = "ROOT";

    private KEY key;
    private ITEM item;
    private String property = ROOT_PROPERTY;

    private ModificationType type;

    public ModificationEvent() {

    }

    public ModificationEvent(KEY k, ITEM i, ModificationType t, String p) {
        key = k;
        item = i;
        type = t;
        if (p != null) {
            this.property = p;
        }
    }

    public ITEM getItem() {
        return item;
    }

    public KEY getKey() {
        return key;
    }


    public abstract boolean booleanValue();

    public abstract int intValue();

    public abstract short shortValue();

    public abstract char charValue();

    public abstract byte byteValue();

    public abstract long longValue();

    public abstract float floatValue();

    public abstract double doubleValue();

    public abstract Object objectValue();

    public abstract String value();

    @Override
    public String toString() {
        return "ModificationEvent{" +
                "key=" + key +
                ", item=" + item +
                ", property='" + property + '\'' +
                ", type=" + type +
                '}';
    }

    static class ModficationEventImpl<KEY, ITEM> extends ModificationEvent<KEY, ITEM> {


        public ModficationEventImpl() {

        }

        public ModficationEventImpl(KEY k, ITEM i, ModificationType t, String p) {
            super(k, i, t, p);
        }

        @Override
        public boolean booleanValue() {
            die("not supported");
            return false;
        }

        @Override
        public int intValue() {
            die("not supported");
            return 0;
        }

        @Override
        public short shortValue() {
            die("not supported");
            return 0;
        }

        @Override
        public char charValue() {
            die("not supported");
            return 'f';
        }

        @Override
        public byte byteValue() {
            die("not supported");
            return 0;
        }

        @Override
        public long longValue() {
            die("not supported");
            return 0;
        }


        @Override
        public float floatValue() {
            die("not supported");
            return 0;
        }

        @Override
        public double doubleValue() {
            die("not supported");
            return 0;
        }

        @Override
        public String value() {
            die("not supported");
            return null;
        }

        @Override
        public Object objectValue() {
            die("not supported");
            return null;
        }

    }


    public static <KEY, ITEM> ModificationEvent<KEY, ITEM> createModification(ModificationType type, KEY key, ITEM item, String property, boolean value) {
        return new ModficationEventImpl(key, item, type, property) {
            boolean v = value;

            public boolean booleanValue() {
                return v;
            }
        };
    }

    public static <KEY, ITEM> ModificationEvent<KEY, ITEM> createModification(ModificationType type, KEY key, ITEM item, String property, Object value) {
        return new ModficationEventImpl(key, item, type, property) {
            Object v = value;

            public Object objectValue() {
                return v;
            }
        };
    }

    public static <KEY, ITEM> ModificationEvent<KEY, ITEM> createModification(ModificationType type, KEY key, ITEM item, String property, String value) {
        return new ModficationEventImpl(key, item, type, property) {
            String v = value;

            public String value() {
                return v;
            }
        };
    }

    public static <KEY, ITEM> ModificationEvent<KEY, ITEM> createModification(ModificationType type, KEY key, ITEM item, String property, byte value) {
        return new ModficationEventImpl(key, item, type, property) {
            byte v = value;

            public byte byteValue() {
                return v;
            }
        };
    }

    public static <KEY, ITEM> ModificationEvent<KEY, ITEM> createModification(ModificationType type, KEY key, ITEM item, String property, short value) {
        return new ModficationEventImpl(key, item, type, property) {
            short v = value;

            public short shortValue() {
                return v;
            }
        };
    }

    public static <KEY, ITEM> ModificationEvent<KEY, ITEM> createModification(ModificationType type, KEY key, ITEM item, String property, int value) {
        return new ModficationEventImpl(key, item, type, property) {
            int v = value;

            public int intValue() {
                return v;
            }
        };
    }

    public static <KEY, ITEM> ModificationEvent<KEY, ITEM> createModification(ModificationType type, KEY key, ITEM item, String property, long value) {
        return new ModficationEventImpl(key, item, type, property) {
            long v = value;

            public long longValue() {
                return v;
            }
        };
    }

    public static <KEY, ITEM> ModificationEvent<KEY, ITEM> createModification(ModificationType type, KEY key, ITEM item, String property, float value) {
        return new ModficationEventImpl(key, item, type, property) {
            float v = value;

            public float floatValue() {
                return v;
            }
        };
    }

    public static <KEY, ITEM> ModificationEvent<KEY, ITEM> createModification(ModificationType type, KEY key, ITEM item, String property, double value) {
        return new ModficationEventImpl(key, item, type, property) {
            double v = value;

            public double doubleValue() {
                return v;
            }
        };
    }

}
