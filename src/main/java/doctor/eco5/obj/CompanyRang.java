package doctor.eco5.obj;

import doctor.eco5.types.CompanyPermission;

import java.util.HashMap;
import java.util.Set;

public class CompanyRang {
    public String name;
    public Integer salary;
    public Integer priority;
    public Set<CompanyPermission> perms;

    public HashMap<String, String> toHashMap() {
        HashMap<String, String> crData = new HashMap<>();

        crData.put("name", name);
        crData.put("pay", salary.toString());
        crData.put("priority", priority.toString());
        crData.put("perms", CompanyPermission.codePerms(perms));

        return crData;
    }

    public static CompanyRang fromHashMap(HashMap<String, String> sql_data) {
        CompanyRang cr = new CompanyRang();

        cr.name = sql_data.get("name");
        cr.salary = Integer.parseInt(sql_data.get("pay"));
        cr.priority = Integer.parseInt(sql_data.get("priority"));
        cr.perms = CompanyPermission.encodePerms(sql_data.get("perms"));

        return cr;
    }

    @Override
    public String toString() {
        return this.toHashMap().toString();
    }
}