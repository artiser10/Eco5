package doctor.eco5.types;

import java.util.HashMap;

public enum Sex {
    Rakom,
    Kakom,
    Male,
    Female;

    public static final HashMap<Sex, String> codec = new HashMap<>();
    static {
        codec.put(Rakom,  "R");
        codec.put(Kakom,  "K");
        codec.put(Male,   "M");
        codec.put(Female, "F");
    }

    @Override
    public String toString() {
        return codec.getOrDefault(this, "R");
    }

    public static Sex toSex(String s) {
        for (Sex sex : codec.keySet()) {
            if (codec.get(sex).equalsIgnoreCase(s)) {
                return sex;
            }
        }
        return Rakom;
    }
}
