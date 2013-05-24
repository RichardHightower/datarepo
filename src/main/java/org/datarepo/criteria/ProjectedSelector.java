package org.datarepo.criteria;

import org.datarepo.reflection.FieldAccess;

import java.util.List;
import java.util.Map;

import static org.datarepo.utils.Utils.*;

public abstract class ProjectedSelector extends Selector {

    public static List<ProjectedSelector> projections(ProjectedSelector... projections) {
        return list(projections);
    }


    public static Selector max(final String name) {
        return new Selector(name) {
            Comparable max;

            @Override
            public void handleRow(int index, Map<String, Object> row, Object item, Map<String, FieldAccess> fields) {
                Comparable value = (Comparable) fields.get(this.name).getValue(item);

                if (max == null)  {
                   max = value;
                }

                if (value.compareTo(max) > 0) {
                    max = value;
                }
            }

            @Override
            public void handleStart(List<? extends Object> results) {

            }

            @Override
            public void handleComplete(List<Map<String, Object>> rows) {
                if ( rows.size() > 0 ) {
                    rows.get(0).put(joinBy('.', "max", name), max);
                }
            }
        };
    }


    public static Selector min(final String name) {
        return new Selector(name) {
            Comparable min;

            @Override
            public void handleRow(int index, Map<String, Object> row, Object item, Map<String, FieldAccess> fields) {
                Comparable value = (Comparable) fields.get(this.name).getValue(item);

                if (min == null)  {
                    min = value;
                }

                if (value.compareTo(min) < 0) {
                    min = value;
                }
            }

            @Override
            public void handleStart(List<? extends Object> results) {

            }

            @Override
            public void handleComplete(List<Map<String, Object>> rows) {
                if ( rows.size() > 0 ) {
                    rows.get(0).put(joinBy('.', "min", name), min);
                }
            }
        };
    }


    public static Selector maxInt(final String name) {
        return new Selector(name) {
            int max = Integer.MIN_VALUE;

            @Override
            public void handleRow(int index, Map<String, Object> row, Object item, Map<String, FieldAccess> fields) {
                int value = fields.get(this.name).getInt(item);
                if (value > max) {
                    max = value;
                }
            }

            @Override
            public void handleStart(List<? extends Object> results) {
                max = Integer.MIN_VALUE;
            }

            @Override
            public void handleComplete(List<Map<String, Object>> rows) {
                if ( rows.size() > 0 ) {
                   rows.get(0).put(joinBy('.', "max", name), max);
                }
            }
        };
    }

    public static Selector minInt(final String name) {
        return new Selector(name) {
            int min = Integer.MAX_VALUE;

            @Override
            public void handleRow(int index, Map<String, Object> row, Object item, Map<String, FieldAccess> fields) {
                int value = fields.get(this.name).getInt(item);
                if (value < min) {
                    min = value;
                }
            }

            @Override
            public void handleStart(List<? extends Object> results) {
                min = Integer.MAX_VALUE;
            }

            @Override
            public void handleComplete(List<Map<String, Object>> rows) {
                if ( rows.size() > 0 ) {
                    rows.get(0).put(joinBy('.', "min", name), min);
                }
            }
        };
    }


    public static Selector maxFloat(final String name) {
        return new Selector(name) {
            float max = Float.MIN_VALUE;

            @Override
            public void handleRow(int index, Map<String, Object> row, Object item, Map<String, FieldAccess> fields) {
                float value = fields.get(this.name).getFloat(item);
                if (value > max) {
                    max = value;
                }
            }

            @Override
            public void handleStart(List<? extends Object> results) {
                max = Float.MIN_VALUE;
            }

            @Override
            public void handleComplete(List<Map<String, Object>> rows) {
                if ( rows.size() > 0 ) {
                    rows.get(0).put(joinBy('.', "max", name), max);
                }
            }
        };
    }

    public static Selector minFloat(final String name) {
        return new Selector(name) {
            float min = Float.MAX_VALUE;

            @Override
            public void handleRow(int index, Map<String, Object> row, Object item, Map<String, FieldAccess> fields) {
                float value = fields.get(this.name).getFloat(item);
                if (value < min) {
                    min = value;
                }
            }

            @Override
            public void handleStart(List<? extends Object> results) {
                min = Float.MAX_VALUE;
            }

            @Override
            public void handleComplete(List<Map<String, Object>> rows) {
                if ( rows.size() > 0 ) {
                    rows.get(0).put(joinBy('.', "min", name), min);
                }
            }
        };
    }

    public static Selector maxDouble(final String name) {
        return new Selector(name) {
            double max = Double.MIN_VALUE;

            @Override
            public void handleRow(int index, Map<String, Object> row, Object item, Map<String, FieldAccess> fields) {
                double value = fields.get(this.name).getDouble(item);
                if (value > max) {
                    max = value;
                }
            }

            @Override
            public void handleStart(List<? extends Object> results) {
                max = Double.MIN_VALUE;
            }

            @Override
            public void handleComplete(List<Map<String, Object>> rows) {
                if ( rows.size() > 0 ) {
                    rows.get(0).put(joinBy('.', "max", name), max);
                }
            }
        };
    }

    public static Selector minDouble(final String name) {
        return new Selector(name) {
            double min = Double.MAX_VALUE;

            @Override
            public void handleRow(int index, Map<String, Object> row, Object item, Map<String, FieldAccess> fields) {
                double value = fields.get(this.name).getDouble(item);
                if (value < min) {
                    min = value;
                }
            }

            @Override
            public void handleStart(List<? extends Object> results) {
                min = Double.MIN_VALUE;
            }

            @Override
            public void handleComplete(List<Map<String, Object>> rows) {
                if ( rows.size() > 0 ) {
                    rows.get(0).put(joinBy('.', "max", name), min);
                }
            }
        };
    }

    public static Selector minShort(final String name) {
        return new Selector(name) {
            short min = Short.MAX_VALUE;

            @Override
            public void handleRow(int index, Map<String, Object> row, Object item, Map<String, FieldAccess> fields) {
                short value = fields.get(this.name).getShort(item);
                if (value < min) {
                    min = value;
                }
            }

            @Override
            public void handleStart(List<? extends Object> results) {
                min = Short.MAX_VALUE;
            }

            @Override
            public void handleComplete(List<Map<String, Object>> rows) {
                if ( rows.size() > 0 ) {
                    rows.get(0).put(joinBy('.', "min", name), min);
                }
            }
        };
    }

    public static Selector maxShort(final String name) {
        return new Selector(name) {
            short max = Short.MIN_VALUE;

            @Override
            public void handleRow(int index, Map<String, Object> row, Object item, Map<String, FieldAccess> fields) {
                short value = fields.get(this.name).getShort(item);
                if (value > max) {
                    max = value;
                }
            }

            @Override
            public void handleStart(List<? extends Object> results) {
                max = Short.MIN_VALUE;
            }

            @Override
            public void handleComplete(List<Map<String, Object>> rows) {
                if ( rows.size() > 0 ) {
                    rows.get(0).put(joinBy('.', "max", name), max);
                }
            }
        };
    }

    public static Selector maxByte(final String name) {
        return new Selector(name) {
            byte max = Byte.MIN_VALUE;

            @Override
            public void handleRow(int index, Map<String, Object> row, Object item, Map<String, FieldAccess> fields) {
                byte value = fields.get(this.name).getByte(item);
                if (value > max) {
                    max = value;
                }
            }

            @Override
            public void handleStart(List<? extends Object> results) {
                max = Byte.MIN_VALUE;
            }

            @Override
            public void handleComplete(List<Map<String, Object>> rows) {
                if ( rows.size() > 0 ) {
                    rows.get(0).put(joinBy('.', "max", name), max);
                }
            }
        };
    }


    public static Selector minByte(final String name) {
        return new Selector(name) {
            byte min = Byte.MAX_VALUE;

            @Override
            public void handleRow(int index, Map<String, Object> row, Object item, Map<String, FieldAccess> fields) {
                byte value = fields.get(this.name).getByte(item);
                if (value < min) {
                    min = value;
                }
            }

            @Override
            public void handleStart(List<? extends Object> results) {
                min = Byte.MIN_VALUE;
            }

            @Override
            public void handleComplete(List<Map<String, Object>> rows) {
                if ( rows.size() > 0 ) {
                    rows.get(0).put(joinBy('.', "min", name), min);
                }
            }
        };
    }

}
