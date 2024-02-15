package doctor.eco5.obj;

import doctor.eco5.Eco5;
import doctor.eco5.InformationBlock;
import doctor.eco5.data.DataAdapter;
import doctor.eco5.obj.rp.RPUser;
import doctor.eco5.types.CompanyPermission;

import java.util.*;

public class Company {
    public String uuid;
    public String public_name;
    public String timestamp_from;
    public Set<CompanyMember> members;
    public Set<CompanyResource> resources;
    public Set<CompanyRang> companyRangs;
    public Integer balance;

    public HashMap<String, String> toHashMap() {
        HashMap<String, String> compData = new HashMap<>();

        compData.put("uuid", uuid);
        compData.put("public_name", public_name);
        compData.put("timestamp_from", timestamp_from);
        String m = "";
        for (CompanyMember cm : members) {
            m = m + cm.toHashMap() + ";";
        }
        compData.put("members", m);
        String r = "";
        for (CompanyResource cr : resources) {
            r = r + cr.toHashMap() + ";";
        }
        compData.put("resources", r);
        String c = "";
        for (CompanyRang cr : companyRangs) {
            c = c + cr.toHashMap() + ";";
        }
        compData.put("rangs", c);
        compData.put("balance", balance.toString());

        return compData;
    }

    public static Company fromHashMap(HashMap<String, String> sql_data) {
        Company company = new Company();

        company.uuid = sql_data.get("uuid");
        company.public_name = sql_data.get("public_name");
        company.timestamp_from = sql_data.get("timestamp_from");
        Set<CompanyRang> rangs = new HashSet<>();
        Set<HashMap<String, String>> ran = DataAdapter.getData(sql_data.get("rangs"));
        for (HashMap<String, String> ranData : ran) {
            rangs.add(CompanyRang.fromHashMap(ranData));
        }
        company.companyRangs = rangs;

        Set<CompanyMember> members = new HashSet<>();
        Set<HashMap<String, String>> mem = DataAdapter.getData(sql_data.get("members"));
        for (HashMap<String, String> memData : mem) {
            members.add(CompanyMember.fromHashMap(memData, rangs));
        }
        company.members = members;

        Set<CompanyResource> resources = new HashSet<>();
        Set<HashMap<String, String>> res = DataAdapter.getData(sql_data.get("resources"));
        for (HashMap<String, String> resData : res) {
            resources.add(CompanyResource.fromHashMap(resData));
        }
        company.resources = resources;
        company.balance = Integer.parseInt(sql_data.get("balance"));

        return company;
    }

    @Override
    public String toString() {
        String exit = "{";
        HashMap<String, String> hashMap = this.toHashMap();
        for (String key : hashMap.keySet()) {
            exit = exit + key + "=" + hashMap.get(key) + ",";
        }
        exit = exit.substring(0, exit.length()-1) + "}";
        return exit;
    }

    public CompanyRang createRang(String name, Integer salary, Integer priority) {
        CompanyRang companyRang = new CompanyRang();
        companyRang.name = name;
        companyRang.salary = salary;
        companyRang.priority = priority;
        companyRang.perms = new HashSet<>();
        this.companyRangs.add(companyRang);
        return companyRang;
    }

    public void removeRang(String name) {
        Set<CompanyRang> rangs = new HashSet<>();
        for (CompanyRang cr : this.companyRangs) {
            if (!cr.name.equals(name)) {
                rangs.add(cr);
            }
        }
        this.companyRangs = rangs;
    }

    public CompanyRang rangByPriority(Integer priority) {
        for (CompanyRang cr : this.companyRangs) {
            if (cr.priority.equals(priority)) {
                return cr;
            }
        }
        return null;
    }

    public CompanyMember getStaff(RPUser rpUser) {
        for (CompanyMember cm : this.members) {
            if (cm.rpUser.uuid.equals(rpUser.uuid)) {
                return cm;
            }
        }
        return null;
    }
    public static Company create(String public_name) {
        Company company = new Company();
        company.uuid = UUID.randomUUID() + "";
        company.public_name = public_name;
        company.timestamp_from = Eco5.time() + "";
        company.members = new HashSet<>();
        company.resources = new HashSet<>();
        company.companyRangs = new HashSet<>();
        company.balance = 0;

        CompanyRang cr = new CompanyRang();
        cr.name = "default";
        cr.priority = 0;
        cr.salary = 0;
        cr.perms = CompanyPermission.encodePerms("100000");
        company.companyRangs.add(cr);

        InformationBlock.log_debug("старая длина " + Eco5.companies.size());
        Eco5.companies.add(company);
        InformationBlock.log_debug("успешно создали компанию, добавили. Новая длина " + Eco5.companies.size());
        return company;
    }

    public void remove() {
        Set<Company> companies = new HashSet<>();
        for (Company company : Eco5.companies) {
            if (!company.uuid.equals(this.uuid)) {
                companies.add(company);
            }
        }
        Eco5.companies = companies;
    }

    public static Company get(String uuid) {
        for (Company company : Eco5.companies) {
            if (company.uuid.equals(uuid)) {
                return company;
            }
        }
        return null;
    }

    public boolean hire(RPUser rpUser, CompanyRang cr) {
        try {
            if (cr == null) {
                return false; // не указан/не найден ранг
            }
            CompanyMember cm = new CompanyMember();
            cm.rpUser = rpUser;
            cm.company_uuid = this.uuid;
            cm.timestamp_from = (int) System.currentTimeMillis() / 1000 + "";
            cm.rang = cr;
            cm.company_must = 0;
            this.members.add(cm);
            return true;
        } catch (Exception e) {
            InformationBlock.report(e);
            return false;
        }
    }

    public boolean hire(RPUser rpUser) {
        return hire(rpUser, defaultRang());
    }

    public CompanyRang maxRang() {
        int max = -1;
        for (CompanyRang cr : companyRangs) {
            if (cr.priority >= max) {
                max = cr.priority;
            }
        }
        return rangByPriority(max);
    }

    public CompanyRang defaultRang() {
        return this.rangByPriority(0);
    }

    public boolean dismiss(RPUser rpUser) {
        try {
            Set<CompanyMember> companyMembers = new HashSet<>();
            boolean st = false;
            for (CompanyMember cm : this.members) {
                if (cm.rpUser.uuid.equals(rpUser.uuid)) {
                    st = true;
                    // попущен
                } else {
                    companyMembers.add(cm);
                }
            }
            this.members = companyMembers;
            return st;
        } catch (Exception e) {
            InformationBlock.report(e);
            return false;
        }
    }

    public boolean isStaff(RPUser rpUser) {
        try {
            for (CompanyMember cm : this.members) {
                if (cm.rpUser.uuid.equals(rpUser.uuid)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            InformationBlock.report(e);
            return false;
        }
    }

    public boolean addRang(CompanyRang cr) {
        try {
            this.companyRangs.add(cr);
            return true;
        } catch (Exception e) {
            InformationBlock.report(e);
            return false;
        }
    }

    public boolean removeRang(CompanyRang cr) {
        try {
            Set<CompanyRang> crs = new HashSet<>();
            for (CompanyRang crc : this.companyRangs) {
                if (Objects.equals(crc.priority, cr.priority)) {
                    // удаляем
                    if (cr.priority == 0) {
                        addRang(createRang("default", 0, 0));
                    }
                } else {
                    crs.add(crc);
                }
            }
            this.companyRangs = crs;
            for (CompanyMember cm : this.members) {
                if (Objects.equals(cm.rang.priority, cr.priority)) {
                    // у него удаляемая роль
                    cm.rang = this.rangByPriority(0);
                }
            }

            this.companyRangs.add(cr);
            return true;
        } catch (Exception e) {
            InformationBlock.report(e);
            return false;
        }
    }

}