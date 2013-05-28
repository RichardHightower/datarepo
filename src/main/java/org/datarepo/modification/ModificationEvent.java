package org.datarepo.modification;

import static org.datarepo.utils.Utils.*;

public abstract class ModificationEvent<KEY, ITEM> {

    private KEY key;
    private ITEM item;

    public ModificationEvent() {

    }

    public ModificationEvent(KEY k, ITEM i) {
        key = k;
        item = i;
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

    static class ModficationEventImpl <KEY, ITEM> extends ModificationEvent<KEY, ITEM> {

        public ModficationEventImpl() {

        }

        public ModficationEventImpl(KEY k, ITEM i) {
            super(k, i);
        }

        @Override
        public boolean booleanValue() {
            die("not supported");  return false;
        }

        @Override
        public int intValue() {
            die("not supported"); return 0;
        }

        @Override
        public short shortValue() {
            die("not supported"); return 0;
        }

        @Override
        public char charValue() {
            die("not supported"); return 'f';
        }

        @Override
        public byte byteValue() {
            die("not supported"); return 0;
        }

        @Override
        public long longValue() {
            die("not supported"); return 0;
        }

        @Override
        public float floatValue() {
            die("not supported"); return 0;
        }

        @Override
        public double doubleValue() {
            die("not supported"); return 0;
        }
    }


    public static <KEY, ITEM> ModificationEvent<KEY, ITEM> createModification(KEY key, ITEM item, boolean value) {
        return new ModficationEventImpl(key, item) {
              boolean v = value;
              public boolean booleanValue() {
                  return v;
              }
        };
    }

    public static <KEY, ITEM> ModificationEvent<KEY, ITEM> createModification(KEY key, ITEM item, byte value) {
        return new ModficationEventImpl(key, item) {
            byte v = value;
            public byte byteValue() {
                return v;
            }
        };
    }

    public static <KEY, ITEM> ModificationEvent<KEY, ITEM> createModification(KEY key, ITEM item, short value) {
        return new ModficationEventImpl(key, item) {
            short v = value;
            public short shortValue() {
                return v;
            }
        };
    }

    public static <KEY, ITEM> ModificationEvent<KEY, ITEM> createModification(KEY key, ITEM item, int value) {
        return new ModficationEventImpl(key, item) {
            int v = value;
            public int intValue() {
                return v;
            }
        };
    }

    public static <KEY, ITEM> ModificationEvent<KEY, ITEM> createModification(KEY key, ITEM item, long value) {
        return new ModficationEventImpl(key, item) {
            long v = value;
            public long longValue() {
                return v;
            }
        };
    }

    public static <KEY, ITEM> ModificationEvent<KEY, ITEM> createModification(KEY key, ITEM item, float value) {
        return new ModficationEventImpl(key, item) {
            float v = value;
            public float floatValue() {
                return v;
            }
        };
    }

    public static <KEY, ITEM> ModificationEvent<KEY, ITEM> createModification(KEY key, ITEM item, double value) {
        return new ModficationEventImpl(key, item) {
            double v = value;
            public double doubleValue() {
                return v;
            }
        };
    }

}
