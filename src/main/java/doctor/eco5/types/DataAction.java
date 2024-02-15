package doctor.eco5.types;

import java.util.HashMap;

public enum DataAction {
    initialization,
    archivization;

    static final HashMap<DataAction, String> codec = new HashMap<>();

    static {
        codec.put(initialization, "initialization");
        codec.put(archivization,   "archivization");
    }

    @Override
    public String toString() {
        return codec.get(this);
    }

    public static DataAction from_string(String s) {
        for (DataAction da : codec.keySet()) {
            if (codec.get(da).equals(s)) {
                return da;
            }
        }
        return null;
    }
}