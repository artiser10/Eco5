package doctor.eco5.types;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public enum CompanyPermission {
    VIEW_MEMBERS,
    MANAGE_MEMBERS,
    VIEW_RESOURCES,
    MANAGE_RESOURCES,
    VIEW_FINANCE,
    MANAGE_FINANCE;

    static HashMap<CompanyPermission, String> codec = new HashMap<>();
    static {
        codec.put(VIEW_MEMBERS,     "VIEW_MEMBERS");
        codec.put(MANAGE_MEMBERS,   "MANAGE_MEMBERS");
        codec.put(VIEW_RESOURCES,   "VIEW_RESOURCES");
        codec.put(MANAGE_RESOURCES, "MANAGE_RESOURCES");
        codec.put(VIEW_FINANCE,     "VIEW_FINANCE");
        codec.put(MANAGE_FINANCE,   "MANAGE_FINANCE");
    }

    @Override
    public String toString() {
        return codec.get(this);
    }

    public static CompanyPermission fromString(String s) {
        for (CompanyPermission companyPermission : codec.keySet()) {
            if (s.equals(codec.get(companyPermission))) {
                return companyPermission;
            }
        }
        return null;
    }

    public static String codePerms(Set<CompanyPermission> data) {
        StringBuilder exit = new StringBuilder();
        exit.append(data.contains(VIEW_MEMBERS) ? 1 : 0);
        exit.append(data.contains(MANAGE_MEMBERS) ? 1 : 0);
        exit.append(data.contains(VIEW_RESOURCES) ? 1 : 0);
        exit.append(data.contains(MANAGE_RESOURCES) ? 1 : 0);
        exit.append(data.contains(VIEW_FINANCE) ? 1 : 0);
        exit.append(data.contains(MANAGE_FINANCE) ? 1 : 0);

        return exit.toString();
    }

    public static Set<CompanyPermission> encodePerms(String data) {
        Set<CompanyPermission> exit = new HashSet<>();
        if (data.charAt(0) == '1') {exit.add(VIEW_MEMBERS)    ;}
        if (data.charAt(1) == '1') {exit.add(MANAGE_MEMBERS)  ;}
        if (data.charAt(2) == '1') {exit.add(VIEW_RESOURCES)  ;}
        if (data.charAt(3) == '1') {exit.add(MANAGE_RESOURCES);}
        if (data.charAt(4) == '1') {exit.add(VIEW_FINANCE)    ;}
        if (data.charAt(5) == '1') {exit.add(MANAGE_FINANCE)  ;}

        return exit;
    }
}