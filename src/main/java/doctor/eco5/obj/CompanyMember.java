package doctor.eco5.obj;

import doctor.eco5.obj.rp.RPUser;
import doctor.eco5.types.CompanyPermission;

import java.util.HashMap;
import java.util.Set;

public class CompanyMember {
    public String company_uuid;
    public RPUser rpUser;
    public CompanyRang rang;
    public String timestamp_from;
    public Integer company_must;

    public HashMap<String, String> toHashMap() {
        HashMap<String, String> cmData = new HashMap<>();

        cmData.put("company_uuid", company_uuid);
        cmData.put("rpUser", rpUser.uuid);
        cmData.put("rang", String.valueOf(rang.priority));
        cmData.put("timestamp_from", timestamp_from);
        cmData.put("company_must", company_must.toString());

        return cmData;
    }

    public static CompanyMember fromHashMap(HashMap<String, String> sql_data, Set<CompanyRang> crs) {
        CompanyMember cm = new CompanyMember();

        cm.company_uuid = sql_data.get("company_uuid");
        cm.rpUser = RPUser.get(sql_data.get("rpUser"));
        for (CompanyRang cr : crs) {
            if (cr.priority == Integer.parseInt(sql_data.get("rang"))) {
                cm.rang = cr;
            }
        }
        cm.timestamp_from = sql_data.get("timestamp_from");
        cm.company_must = Integer.parseInt(sql_data.get("company_must"));

        return cm;
    }

    public boolean isOnline() {
        return this.rpUser.isOnline();
    }

    @Override
    public String toString() {
        return this.toHashMap().toString();
    }

    public boolean hasPermission(CompanyPermission permission) {
        CompanyRang companyRang = this.rang;
        return companyRang.perms.contains(permission);
    }

    public void dismiss() {
        Company company = Company.get(this.company_uuid);
        assert company != null;
        company.dismiss(this.rpUser);
    }
}