package doctor.eco5.obj;

import java.util.HashMap;

public class CompanyResource {
    public String name;
    public Integer amount;

    public HashMap<String, String> toHashMap() {
        HashMap<String, String> crData = new HashMap<>();

        crData.put("name", name);
        crData.put("amount", amount.toString());

        return crData;
    }

    public static CompanyResource fromHashMap(HashMap<String, String> sql_data) {
        CompanyResource cr = new CompanyResource();

        cr.name = sql_data.get("name");
        cr.amount = Integer.parseInt(sql_data.get("amount"));

        return cr;
    }

    @Override
    public String toString() {
        return this.toHashMap().toString();
    }
}