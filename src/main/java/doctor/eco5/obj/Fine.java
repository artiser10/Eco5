package doctor.eco5.obj;

import doctor.eco5.Eco5;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Fine {
    public String uuid; // кому
    public String company; // выдал
    public Integer amount; // сумма
    public String desc; // описание

    public HashMap<String, String> toHashMap() {
        HashMap<String, String> fineData = new HashMap<>();
        fineData.put("uuid", uuid);
        fineData.put("company", company);
        fineData.put("amount", String.valueOf(amount));
        fineData.put("desc", desc);
        return fineData;
    }

    public static Fine fromHashMap(HashMap<String, String> sql_data) {
        Fine fine = new Fine();
        fine.uuid = sql_data.get("uuid");
        fine.company = sql_data.get("company");
        fine.amount = Integer.valueOf(sql_data.get("amount"));
        fine.desc = sql_data.get("desc");
        return fine;
    }

    @Override
    public String toString() {
        return this.toHashMap().toString();
    }

    public static Set<Fine> get(String uuid) {
        Set<Fine> res = new HashSet<>();
        for (Fine fine : Eco5.fines) {
            if (fine.uuid.equals(uuid)) {
                res.add(fine);
            }
        }
        return res;
    }

    public void remove() {
        Eco5.fines.remove(this);
    }
}