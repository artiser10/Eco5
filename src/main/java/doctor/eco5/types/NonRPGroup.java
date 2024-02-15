package doctor.eco5.types;

import java.util.HashMap;

public enum NonRPGroup {
    IDN,           // Кто это...
    PLAYER,        // Игрок

    ADMIN_TRAINEE, // Администратор - стажёр
    ADMIN_JUNIOR,  // Администратор - младший
    ADMIN_MIDDLE,  // Администратор - средний
    ADMIN_SENIOR,  // Администратор - старший
    ADMIN_MANAGER, // Администратор - руководитель
    ADMIN_SYSTEM;  // Администратор - системный

    public static final HashMap<NonRPGroup, String> codec = new HashMap<>();
    static {
        codec.put(PLAYER,         "N");
        codec.put(IDN,           "D!");
        codec.put(ADMIN_TRAINEE, "B1");
        codec.put(ADMIN_JUNIOR,  "B2");
        codec.put(ADMIN_MIDDLE,  "B3");
        codec.put(ADMIN_SENIOR,  "B4");
        codec.put(ADMIN_MANAGER, "B5");
        codec.put(ADMIN_SYSTEM,  "C1");
    }

    @Override
    public String toString() {
        return codec.getOrDefault(this, "D!");
    }

    public static NonRPGroup toNonRPGroup(String s) {
        for (NonRPGroup NonRPGroup : codec.keySet()) {
            if (codec.get(NonRPGroup).equalsIgnoreCase(s)) {
                return NonRPGroup;
            }
        }
        return IDN;
    }

}