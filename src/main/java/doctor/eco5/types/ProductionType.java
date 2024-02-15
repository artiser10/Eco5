package doctor.eco5.types;

import java.util.HashMap;

public enum ProductionType {
    IDN,  // Кто это...

    ORE,  // Руды (шахта)
    LOGS; // брёвна (лесопилка)


    public static final HashMap<ProductionType, String> codec = new HashMap<>();
    static {
        codec.put(IDN,    "D!");
        codec.put(ORE,   "ORE");
        codec.put(LOGS, "LOGS");
    }

    @Override
    public String toString() {
        return codec.getOrDefault(this, "D!");
    }

    public static ProductionType toProductionType(String s) {
        for (ProductionType productionType : codec.keySet()) {
            if (codec.get(productionType).equalsIgnoreCase(s)) {
                return productionType;
            }
        }
        return null;
    }
}