package doctor.eco5.types;

import java.util.HashMap;

public enum DocumentType {
    License,
    Order;

    static final HashMap<DocumentType, String> codec = new HashMap<>();

    static {
        codec.put(License, "license");
        codec.put(Order,   "order");
    }

    @Override
    public String toString() {
        return codec.get(this);
    }

    public static DocumentType from_string(String s) {
        for (DocumentType da : codec.keySet()) {
            if (codec.get(da).equals(s)) {
                return da;
            }
        }
        return null;
    }
}