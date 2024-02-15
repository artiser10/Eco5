package doctor.eco5.obj;

import doctor.eco5.Eco5;
import doctor.eco5.obj.guis.ATM_card_cash;
import doctor.eco5.obj.guis.ATM_cash_card;
import doctor.eco5.obj.guis.ATM_fines;
import doctor.eco5.obj.guis.ATM_main;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import ru.komiss77.utils.inventory.SmartInventory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ATM {

    public String id;
    public Location loc;

    public HashMap<String, String> toHashMap() {
        HashMap<String, String> atmData = new HashMap<>();
        atmData.put("uuid", id);
        atmData.put("location", ((int) loc.getX()) + ";" + ((int) loc.getY()) + ";" + ((int) loc.getZ()));
        return atmData;
    }

    public static ATM fromHashMap(HashMap<String, String> sql_data) {
        ATM atm = new ATM();

        String[] sql_loc_data = sql_data.get("location").split(";");

        atm.id = sql_data.get("uuid");
        atm.loc = new Location(Eco5.world, Integer.parseInt(sql_loc_data[0]), Integer.parseInt(sql_loc_data[1]), Integer.parseInt(sql_loc_data[2]));

        return atm;
    }

    @Override
    public String toString() {
        return this.toHashMap().toString();
    }

    public static ATM get(Location location) {
        for (ATM atm : Eco5.atms) {
            if (atm.loc.x() == location.x() & atm.loc.y() == location.y() & atm.loc.z() == location.z()) {
                return atm;
            }
        }
        return null;
    }

    public static boolean remove(Location location) {
        Set<ATM> atms = new HashSet<>();
        boolean state = false;
        for (ATM atm : Eco5.atms) {
            if (atm.loc.x() == location.x() & atm.loc.y() == location.y() & atm.loc.z() == location.z()) {
                state = true;
            } else {
                atms.add(atm);
            }
        }
        Eco5.atms = atms;
        return state;
    }

    public static ATM create(Location location, String id) {
        for (ATM a : Eco5.atms) {
            if (a.loc == location) {
                return null;
            }
        }
        ATM atm = new ATM();
        atm.loc = location;
        atm.id = id;
        Eco5.atms.add(atm);
        return atm;
    }

    public void open_for(Player player) {
        SmartInventory.builder().size(6).title("§4Банкомат №" + this.id).provider(new ATM_main()).build().open(player);
    }
    public static void open_for_fines(Player player) {
        SmartInventory.builder().size(6).title("§4Банкомат - Оплата штрафов").provider(new ATM_fines()).build().open(player);
    }
    public static void open_cash_card(Player player) {
        SmartInventory.builder().size(6).title("§4Банкомат - Пополнение счёта").provider(new ATM_cash_card()).build().open(player);
    }
    public static void open_card_cash(Player player) {
        SmartInventory.builder().size(6).title("§4Банкомат - Снятие наличных").provider(new ATM_card_cash()).build().open(player);
    }
}