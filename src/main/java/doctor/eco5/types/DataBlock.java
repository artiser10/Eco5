package doctor.eco5.types;

import java.util.HashMap;

public enum DataBlock {
    rpUsers,
    ATMs,
    fines,
    doors,
    productionPlaces,
    companies;

    static final HashMap<DataBlock, String> codec = new HashMap<>();

    static {
        codec.put(rpUsers, "rpusers");
        codec.put(ATMs, "atms");
        codec.put(doors, "doors");
        codec.put(fines, "fines");
        codec.put(productionPlaces, "productionplaces");
        codec.put(companies, "companies");
    }

    @Override
    public String toString() {
        return codec.get(this);
    }

    public static DataBlock from_string(String s) {
        for (DataBlock db : codec.keySet()) {
            if (codec.get(db).equals(s)) {
                return db;
            }
        }
        return null;
    }
}