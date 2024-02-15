package doctor.eco5.data;

import doctor.eco5.Eco5;
import doctor.eco5.InformationBlock;
import doctor.eco5.obj.*;
import doctor.eco5.obj.rp.RPUser;
import doctor.eco5.types.DataAction;
import doctor.eco5.types.DataBlock;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class DataAdapter {

    public static Set<Integer> between(int min, int max) {
        Set<Integer> set = new HashSet<>();
        for (int i = min; i <= max; i++) {
            set.add(i);
        }
        return set;
    }

    public static Set<HashMap<String, String>> getData(String data_str) { // для дополнительной вложенности! Заменён символ
        Set<HashMap<String, String>> data = new HashSet<>();
        InformationBlock.log_debug("GD: " + data_str);

        if (data_str.equals("") | data_str.equals("{}")) {
            return data;
        }
        for (String i : data_str.split(";")) {
            HashMap<String, String> d = new HashMap<>();
            i = i.substring(1, i.length()-1);
            boolean first = true;
            for (String ii : i.split(",")) {
                if (!(ii.equals("{}") | ii.equals(""))) {
                    String[] pair = ii.split("=");
                    String key;
                    if (!first) {
                        key = pair[0].substring(1);
                    } else {
                        key = pair[0];
                    }
                    first = false;
                    if (pair.length != 2) {
                        InformationBlock.log_debug(Arrays.toString(pair));
                    }
                    String val = pair[1];
                    d.put(key, val);
                }
            }
            data.add(d);
        }
        InformationBlock.log_debug("GD2: " + data);
        return data;
    }
    // надо из этой хуйни сделать?
    // {company_uuid=1b0ba6df-a493-45bd-8edf-13465a9b4553, rpUser=019b1db8-5170-3976-83d4-a6beec0b63f6,
    // rang={name=Стажёр, pay=0, perms=100000, priority=0},
    // timestamp_from=2078303184};
    public static Set<HashMap<String, String>> getDataR(String data_str) { // для дополнительной вложенности! Заменён символ
        HashMap<String, String> hashMap;
        Set<HashMap<String, String>> set = new HashSet<>();
        InformationBlock.log_debug(data_str);
        if (data_str == null) {
            return set;
        }
        if (data_str.equals("null")) {
            return set;
        }
        for (String member_raw : data_str.split(";")) {
            hashMap = new HashMap<>();
            if (!member_raw.equals("")) {
                if (member_raw.startsWith("{")) {
                    member_raw = member_raw.substring(1, member_raw.length() - 1);
                }
                System.out.println(member_raw);
                int index_start = -1;
                int index_stop  = -1;
                int c = -1;
                Set<Integer> partial = new HashSet<>();
                for (String data_part : member_raw.split(",")) {
                    c = c + 1;
                    if (data_part.contains("{")) {
                        index_start = c;
                    }
                    if (data_part.contains("}")) {
                        index_stop  = c;
                    }
                }
                System.out.println("START:" + index_start + "; STOP:" + index_stop);
                if (index_start != -1 & index_stop != -1) {
                    partial = between(index_start, index_stop);
                }
                c = -1;
                String r = "";
                for (String data_part : member_raw.split(",")) {
                    c = c + 1;
                    if (partial.contains(c)) {
                        r = r + data_part;
                    } else {
                        if (c == index_stop) {
                            hashMap.put(member_raw.split(",")[index_start].split("=")[0], r);
                        } else {
                            String[] pair = data_part.split("=");
                            hashMap.put(pair[0], pair[1]);
                        }
                    }
                }
                String key = "";
                int cc = -1;
                for (String arg : r.split("=")) {
                    cc = cc + 1;
                    if (cc == 0) {
                        key = arg;
                    }
                }
                String ss = r.substring(key.length()+1);
                ss = ss.replaceAll(" ", ", ");
                hashMap.put(key, ss);
            }
            set.add(hashMap);
        }
        return set;
    }

    public static boolean dataAction(DataBlock dataBlock, DataAction dataAction) throws SQLException {
        try {
            String table;
            Set<HashMap<String, String>> set = new HashSet<>();
            switch (dataBlock) {
                case rpUsers -> {
                    for (RPUser rpUser : Eco5.rpUsers) {
                        HashMap<String, String> uh = rpUser.toHashMap();
                        set.add(uh);
                    }
                }
                case ATMs -> {
                    for (ATM atm : Eco5.atms) {
                        HashMap<String, String> uh = atm.toHashMap();
                        set.add(uh);
                    }
                }
                case fines -> {
                    for (Fine fine : Eco5.fines) {
                        HashMap<String, String> uh = fine.toHashMap();
                        set.add(uh);
                    }
                }
                case doors -> {
                    for (Door door : Eco5.doors) {
                        HashMap<String, String> uh = door.toHashMap();
                        set.add(uh);
                    }
                }
                case companies -> {
                    for (Company company : Eco5.companies) {
                        HashMap<String, String> uh = company.toHashMap();
                        set.add(uh);
                    }
                }
                case productionPlaces -> {
                    for (ProductionPlace place : Eco5.productionPlaces) {
                        HashMap<String, String> uh = place.toHashMap();
                        set.add(uh);
                    }
                }
                case default -> table = "nd";
            }
            table = dataBlock.toString().toLowerCase();
            if (table.equalsIgnoreCase("nd")) {
                InformationBlock.report("DA1", dataBlock.toString());
                return false;
            }
            switch (dataAction) {
                case archivization -> {
                    String que = USQLAdapter.request_generator(table, "get", null);
                    ResultSet rs = USQLAdapter.sql_request(que, false);
                    Set<HashMap<String, String>> data_first = USQLAdapter.rs_to_data(rs);
                    USQLAdapter.sql_request("TRUNCATE TABLE `" + table + "`;", true);
                    InformationBlock.log_debug("Длина сета '" + table + "' = " + set.size());
                    USQLAdapter.sql_request(USQLAdapter.request_generator(table, "put", set), true);
                    rs = USQLAdapter.sql_request(que, false);
                    Set<HashMap<String, String>> data_second = USQLAdapter.rs_to_data(rs);
                    //que = USQLAdapter.request_generator()
                    InformationBlock.log_debug("ARCH [" + table + "] изменения " + !data_first.equals(data_second) + ".");
                    return true;
                }
                case initialization -> {
                    String que = USQLAdapter.request_generator(table.toLowerCase(), "get", null);
                    ResultSet rs = USQLAdapter.sql_request(que, false);
                    Set<HashMap<String, String>> data = USQLAdapter.rs_to_data(rs);
                    switch (dataBlock) {
                        case rpUsers -> {
                            Set<RPUser> users = new HashSet<>();
                            for (HashMap<String, String> hashMap : data) {
                                users.add(RPUser.fromHashMap(hashMap));
                            }
                            Eco5.rpUsers = users;
                            return true;
                        }
                        case ATMs -> {
                            Set<ATM> atms = new HashSet<>();
                            for (HashMap<String, String> hashMap : data) {
                                atms.add(ATM.fromHashMap(hashMap));
                            }
                            Eco5.atms = atms;
                            return true;
                        }
                        case fines -> {
                            Set<Fine> fines = new HashSet<>();
                            for (HashMap<String, String> hashMap : data) {
                                fines.add(Fine.fromHashMap(hashMap));
                            }
                            Eco5.fines = fines;
                            return true;
                        }
                        case doors -> {
                            Set<Door> doors = new HashSet<>();
                            for (HashMap<String, String> hashMap : data) {
                                doors.add(Door.fromHashMap(hashMap));
                            }
                            Eco5.doors = doors;
                            return true;
                        }
                        case companies -> {
                            Set<Company> companies = new HashSet<>();
                            for (HashMap<String, String> hashMap : data) {
                                companies.add(Company.fromHashMap(hashMap));
                            }
                            Eco5.companies = companies;
                            return true;
                        }
                        case productionPlaces -> {
                            Set<ProductionPlace> places = new HashSet<>();
                            for (HashMap<String, String> hashMap : data) {
                                places.add(ProductionPlace.fromHashMap(hashMap));
                            }
                            Eco5.productionPlaces = places;
                            return true;
                        }
                    }
                }
            }
            return false;
        } catch (Exception e) {
            InformationBlock.report("DataAdapter: " + Arrays.toString(e.getStackTrace()));
            return false;
        }
    }
}