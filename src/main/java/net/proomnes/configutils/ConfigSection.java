package net.proomnes.configutils;

import java.util.*;

/*
 * Code by https://github.com/CloudburstMC/Nukkit
 */

public class ConfigSection extends LinkedHashMap<String, Object> {

    public ConfigSection() {
    }

    public ConfigSection(String key, Object value) {
        this();
        this.set(key, value);
    }

    public ConfigSection(LinkedHashMap<String, Object> map) {
        this();
        if (map != null && !map.isEmpty()) {
            Iterator var2 = map.entrySet().iterator();

            while (var2.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry) var2.next();
                if (entry.getValue() instanceof LinkedHashMap) {
                    super.put(entry.getKey(), new ConfigSection((LinkedHashMap) entry.getValue()));
                } else if (entry.getValue() instanceof List) {
                    super.put(entry.getKey(), this.parseList((List) entry.getValue()));
                } else {
                    super.put(entry.getKey(), entry.getValue());
                }
            }

        }
    }

    private List parseList(List list) {
        List<Object> newList = new ArrayList();
        Iterator var3 = list.iterator();

        while (var3.hasNext()) {
            Object o = var3.next();
            if (o instanceof LinkedHashMap) {
                newList.add(new ConfigSection((LinkedHashMap) o));
            } else {
                newList.add(o);
            }
        }

        return newList;
    }

    public Map<String, Object> getAllMap() {
        return new LinkedHashMap(this);
    }

    public ConfigSection getAll() {
        return new ConfigSection(this);
    }

    public Object get(String key) {
        return this.get(key, null);
    }

    public <T> T get(String key, T defaultValue) {
        if (key != null && !key.isEmpty()) {
            if (super.containsKey(key)) {
                return (T) super.get(key);
            } else {
                String[] keys = key.split("\\.", 2);
                if (!super.containsKey(keys[0])) {
                    return defaultValue;
                } else {
                    Object value = super.get(keys[0]);
                    if (value instanceof ConfigSection) {
                        ConfigSection section = (ConfigSection) value;
                        return section.get(keys[1], defaultValue);
                    } else {
                        return defaultValue;
                    }
                }
            }
        } else {
            return defaultValue;
        }
    }

    public void set(String key, Object value) {
        String[] subKeys = key.split("\\.", 2);
        if (subKeys.length > 1) {
            ConfigSection childSection = new ConfigSection();
            if (this.containsKey(subKeys[0]) && super.get(subKeys[0]) instanceof ConfigSection) {
                childSection = (ConfigSection) super.get(subKeys[0]);
            }

            childSection.set(subKeys[1], value);
            super.put(subKeys[0], childSection);
        } else {
            super.put(subKeys[0], value);
        }

    }

    public boolean isSection(String key) {
        Object value = this.get(key);
        return value instanceof ConfigSection;
    }

    public ConfigSection getSection(String key) {
        return this.get(key, new ConfigSection());
    }

    public ConfigSection getSections() {
        return this.getSections(null);
    }

    public ConfigSection getSections(String key) {
        ConfigSection sections = new ConfigSection();
        ConfigSection parent = key != null && !key.isEmpty() ? this.getSection(key) : this.getAll();
        if (parent == null) {
            return sections;
        } else {
            parent.forEach((key1, value) -> {
                if (value instanceof ConfigSection) {
                    sections.put(key1, value);
                }

            });
            return sections;
        }
    }

    public int getInt(String key) {
        return this.getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        return ((Number) this.get(key, defaultValue)).intValue();
    }

    public boolean isInt(String key) {
        Object val = this.get(key);
        return val instanceof Integer;
    }

    public long getLong(String key) {
        return this.getLong(key, 0L);
    }

    public long getLong(String key, long defaultValue) {
        return ((Number) this.get(key, defaultValue)).longValue();
    }

    public boolean isLong(String key) {
        Object val = this.get(key);
        return val instanceof Long;
    }

    public double getDouble(String key) {
        return this.getDouble(key, 0.0D);
    }

    public double getDouble(String key, double defaultValue) {
        return ((Number) this.get(key, defaultValue)).doubleValue();
    }

    public boolean isDouble(String key) {
        Object val = this.get(key);
        return val instanceof Double;
    }

    public String getString(String key) {
        return this.getString(key, "");
    }

    public String getString(String key, String defaultValue) {
        Object result = this.get(key, defaultValue);
        return String.valueOf(result);
    }

    public boolean isString(String key) {
        Object val = this.get(key);
        return val instanceof String;
    }

    public boolean getBoolean(String key) {
        return this.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return this.get(key, defaultValue);
    }

    public boolean isBoolean(String key) {
        Object val = this.get(key);
        return val instanceof Boolean;
    }

    public List getList(String key) {
        return this.getList(key, null);
    }

    public List getList(String key, List defaultList) {
        return this.get(key, defaultList);
    }

    public boolean isList(String key) {
        Object val = this.get(key);
        return val instanceof List;
    }

    public List<String> getStringList(String key) {
        List value = this.getList(key);
        if (value == null) {
            return new ArrayList(0);
        } else {
            List<String> result = new ArrayList();
            Iterator var4 = value.iterator();

            while (true) {
                Object o;
                do {
                    if (!var4.hasNext()) {
                        return result;
                    }

                    o = var4.next();
                } while (!(o instanceof String) && !(o instanceof Number) && !(o instanceof Boolean) && !(o instanceof Character));

                result.add(String.valueOf(o));
            }
        }
    }

    public List<Integer> getIntegerList(String key) {
        List<?> list = this.getList(key);
        if (list == null) {
            return new ArrayList(0);
        } else {
            List<Integer> result = new ArrayList();
            Iterator var4 = list.iterator();

            while (var4.hasNext()) {
                Object object = var4.next();
                if (object instanceof Integer) {
                    result.add((Integer) object);
                } else if (object instanceof String) {
                    try {
                        result.add(Integer.valueOf((String) object));
                    } catch (Exception var7) {
                    }
                } else if (object instanceof Character) {
                    result.add(Integer.valueOf((Character) object));
                } else if (object instanceof Number) {
                    result.add(((Number) object).intValue());
                }
            }

            return result;
        }
    }

    public List<Boolean> getBooleanList(String key) {
        List<?> list = this.getList(key);
        if (list == null) {
            return new ArrayList(0);
        } else {
            List<Boolean> result = new ArrayList();
            Iterator var4 = list.iterator();

            while (var4.hasNext()) {
                Object object = var4.next();
                if (object instanceof Boolean) {
                    result.add((Boolean) object);
                } else if (object instanceof String) {
                    if (Boolean.TRUE.toString().equals(object)) {
                        result.add(true);
                    } else if (Boolean.FALSE.toString().equals(object)) {
                        result.add(false);
                    }
                }
            }

            return result;
        }
    }

    public List<Double> getDoubleList(String key) {
        List<?> list = this.getList(key);
        if (list == null) {
            return new ArrayList(0);
        } else {
            List<Double> result = new ArrayList();
            Iterator var4 = list.iterator();

            while (var4.hasNext()) {
                Object object = var4.next();
                if (object instanceof Double) {
                    result.add((Double) object);
                } else if (object instanceof String) {
                    try {
                        result.add(Double.valueOf((String) object));
                    } catch (Exception var7) {
                    }
                } else if (object instanceof Character) {
                    result.add((double) (Character) object);
                } else if (object instanceof Number) {
                    result.add(((Number) object).doubleValue());
                }
            }

            return result;
        }
    }

    public List<Float> getFloatList(String key) {
        List<?> list = this.getList(key);
        if (list == null) {
            return new ArrayList(0);
        } else {
            List<Float> result = new ArrayList();
            Iterator var4 = list.iterator();

            while (var4.hasNext()) {
                Object object = var4.next();
                if (object instanceof Float) {
                    result.add((Float) object);
                } else if (object instanceof String) {
                    try {
                        result.add(Float.valueOf((String) object));
                    } catch (Exception var7) {
                    }
                } else if (object instanceof Character) {
                    result.add((float) (Character) object);
                } else if (object instanceof Number) {
                    result.add(((Number) object).floatValue());
                }
            }

            return result;
        }
    }

    public List<Long> getLongList(String key) {
        List<?> list = this.getList(key);
        if (list == null) {
            return new ArrayList(0);
        } else {
            List<Long> result = new ArrayList();
            Iterator var4 = list.iterator();

            while (var4.hasNext()) {
                Object object = var4.next();
                if (object instanceof Long) {
                    result.add((Long) object);
                } else if (object instanceof String) {
                    try {
                        result.add(Long.valueOf((String) object));
                    } catch (Exception var7) {
                    }
                } else if (object instanceof Character) {
                    result.add((long) (Character) object);
                } else if (object instanceof Number) {
                    result.add(((Number) object).longValue());
                }
            }

            return result;
        }
    }

    public List<Byte> getByteList(String key) {
        List<?> list = this.getList(key);
        if (list == null) {
            return new ArrayList(0);
        } else {
            List<Byte> result = new ArrayList();
            Iterator var4 = list.iterator();

            while (var4.hasNext()) {
                Object object = var4.next();
                if (object instanceof Byte) {
                    result.add((Byte) object);
                } else if (object instanceof String) {
                    try {
                        result.add(Byte.valueOf((String) object));
                    } catch (Exception var7) {
                    }
                } else if (object instanceof Character) {
                    result.add((byte) object);
                } else if (object instanceof Number) {
                    result.add(((Number) object).byteValue());
                }
            }

            return result;
        }
    }

    public List<Character> getCharacterList(String key) {
        List<?> list = this.getList(key);
        if (list == null) {
            return new ArrayList(0);
        } else {
            List<Character> result = new ArrayList();
            Iterator var4 = list.iterator();

            while (var4.hasNext()) {
                Object object = var4.next();
                if (object instanceof Character) {
                    result.add((Character) object);
                } else if (object instanceof String) {
                    String str = (String) object;
                    if (str.length() == 1) {
                        result.add(str.charAt(0));
                    }
                } else if (object instanceof Number) {
                    result.add((char) ((Number) object).intValue());
                }
            }

            return result;
        }
    }

    public List<Short> getShortList(String key) {
        List<?> list = this.getList(key);
        if (list == null) {
            return new ArrayList(0);
        } else {
            List<Short> result = new ArrayList();
            Iterator var4 = list.iterator();

            while (var4.hasNext()) {
                Object object = var4.next();
                if (object instanceof Short) {
                    result.add((Short) object);
                } else if (object instanceof String) {
                    try {
                        result.add(Short.valueOf((String) object));
                    } catch (Exception var7) {
                    }
                } else if (object instanceof Character) {
                    result.add((short) object);
                } else if (object instanceof Number) {
                    result.add(((Number) object).shortValue());
                }
            }

            return result;
        }
    }

    public List<Map> getMapList(String key) {
        List<Map> list = this.getList(key);
        List<Map> result = new ArrayList();
        if (list == null) {
            return result;
        } else {
            Iterator var4 = list.iterator();

            while (var4.hasNext()) {
                Object object = var4.next();
                if (object instanceof Map) {
                    result.add((Map) object);
                }
            }

            return result;
        }
    }

    public boolean exists(String key, boolean ignoreCase) {
        if (ignoreCase) {
            key = key.toLowerCase();
        }

        Iterator var3 = this.getKeys(true).iterator();

        String existKey;
        do {
            if (!var3.hasNext()) {
                return false;
            }

            existKey = (String) var3.next();
            if (ignoreCase) {
                existKey = existKey.toLowerCase();
            }
        } while (!existKey.equals(key));

        return true;
    }

    public boolean exists(String key) {
        return this.exists(key, false);
    }

    public void remove(String key) {
        if (key != null && !key.isEmpty()) {
            if (super.containsKey(key)) {
                super.remove(key);
            } else if (this.containsKey(".")) {
                String[] keys = key.split("\\.", 2);
                if (super.get(keys[0]) instanceof ConfigSection) {
                    ConfigSection section = (ConfigSection) super.get(keys[0]);
                    section.remove(keys[1]);
                }
            }

        }
    }

    public Set<String> getKeys(boolean child) {
        Set<String> keys = new LinkedHashSet();
        this.forEach((key, value) -> {
            keys.add(key);
            if (value instanceof ConfigSection && child) {
                ((ConfigSection) value).getKeys(true).forEach((childKey) -> {
                    keys.add(key + "." + childKey);
                });
            }

        });
        return keys;
    }

    public Set<String> getKeys() {
        return this.getKeys(true);
    }
}
