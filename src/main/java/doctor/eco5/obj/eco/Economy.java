package doctor.eco5.obj.eco;

import doctor.eco5.Eco5;
import doctor.eco5.InformationBlock;
import doctor.eco5.data.USQLAdapter;
import doctor.eco5.obj.Company;
import doctor.eco5.obj.CompanyMember;
import doctor.eco5.obj.CompanyRang;
import doctor.eco5.obj.rp.RPUser;
import doctor.eco5.types.NonRPGroup;
import doctor.eco5.types.TaxType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Set;


public class Economy {
    private static final float hoursUpd = 30 * 60; // <разница во времени для выплаты зп по личным счетам в секундах>  3600 * 0.005F ~18s
    public static final long secondsTimer = 20 * 10; // <раз во сколько секунд будет вызываться Timer>
    private static final int everyI = 6 * 10; // <каждые сколько ^ периодов начислять средства>

    // каждые (everyI * secondsTimer) на счёт компании. Каждые hoursUpd на личный
    static int c = 0;
    static Component prefix = Component.text("[").color(TextColor.color(127, 127, 127)).append(Component.text("ECO5").color(TextColor.color(119, 255, 122))).append(Component.text("] ").color(TextColor.color(127, 127, 127)));

    public static void broadcast(String msg, NonRPGroup group) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            RPUser rpUser = RPUser.get(p);
            assert rpUser != null;
            if (rpUser.nonRPGroup == group) {
                if (!p.isOnline()) {
                    return;
                }
                p.sendMessage(prefix.append(Component.text(msg).color(TextColor.color(247, 255, 209))));
                p.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            }
        }
    }

    public static void broadcast(String msg, RPUser rpUser) {
        Player p = rpUser.getPlayer();
        if (p == null || !p.isOnline()) {
            return;
        }
        p.sendMessage(prefix.append(Component.text(msg).color(TextColor.color(247, 255, 209))));
        p.playSound(rpUser.getPlayer(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
    }

    public static void addPayPerTime() {
        //InformationBlock.log_debug("call APPT");
        for (Company company : Eco5.companies) {
            HashMap<CompanyMember, Integer> pay = new HashMap<>();
            int sum = 0;
            for (CompanyMember cm : company.members) {
                if (cm.isOnline()) {
                    CompanyRang rang = cm.rang;
                    int old = cm.company_must;
                    int add = rang.salary;
                    sum = sum + add;
                    pay.put(cm, old + add); // вносим сколько установить
                }
            }
            int cobalance = company.balance;
            //InformationBlock.log_debug("BAL: " + cobalance + "; " + (cobalance < 0));
            if (cobalance - sum < 0) {
                //InformationBlock.log_debug("Не хватает деняк на выплату, уйдём в минус");
                //InformationBlock.broadcast(Component.text(""), Sound.ENTITY_ENDER_DRAGON_DEATH);
                return;
            }
            for (CompanyMember p : pay.keySet()) {
                p.company_must = pay.get(p);
                broadcast("Средства начислены на счёт компании. Новая сумма = " + p.company_must, p.rpUser);
            }
        }
        broadcast("На внутренние счета сотрудников добавлены средства.", NonRPGroup.ADMIN_SYSTEM);
    }

    public static void executePay() {
        //InformationBlock.log_debug("call EP");
        for (Company co : Eco5.companies) {
            for (CompanyMember cm : co.members) {
                int cobalance = co.balance;
                int must  = cm.company_must;
                int rpbal = cm.rpUser.balance_b;
                cm.rpUser.balance_b = rpbal + must;
                int withTax = (int) (cobalance - (must * TaxType.Salary.amount()));
                co.balance = withTax;
                broadcast("Начислена зарплата. Общая сумма на счёте = " + cm.rpUser.balance_b + "; (со счёта компании " + must * TaxType.Salary.amount() + ", новый " + withTax + ").", cm.rpUser);
                cm.company_must = 0;
                //InformationBlock.log_debug("Ну мы чё то там сделали, хз чё по ошибкам");
            }
        }
        broadcast("Зарплаты выплачены.", NonRPGroup.ADMIN_SYSTEM);
    }

    public static void Timer() { //

        //InformationBlock.log_debug("EXT: c=" + c);
        c = c + 1;
        try {
            tryUpdateTime();
        } catch (Exception e) {
            InformationBlock.report(e);
        }
        if (c == everyI) {
            c = 0;
            addPayPerTime();
        }

    }

    public static void tryUpdateTime() throws SQLException {
        //InformationBlock.log_debug("call TUT");
        long a = getNowTime();
        long b = getLastPayTime();
        //InformationBlock.log_debug("a="+a+"; b="+b+";|a-b|="+Math.abs(a - b) + "; hoursUpd="+hoursUpd+";l="+(Math.abs(a - b) >= hoursUpd));
        if (Math.abs(a - b) >= hoursUpd) {
            executePay();
            setLastPayTime(a);
            //InformationBlock.log_debug("Ну мы установили");
        }
    }

    public static Long getLastPayTime() throws SQLException {
        Set<HashMap<String, String>> data = USQLAdapter.rs_to_data(USQLAdapter.sql_request("SELECT * FROM `eco_pays`;", false));
        if (data.size() != 0) {
            //InformationBlock.log_debug(data.toString());
            HashMap<String, String> hashMap = data.iterator().next(); // max
            String d = hashMap.get("value");
            if (d == null) {
                d = hashMap.get("MAX(value)");
            }
            //InformationBlock.log_debug(d);
            return Long.parseLong(d);
        } else {
            return 0L;
        }
    }

    public static void setLastPayTime(Long l) {
        USQLAdapter.sql_request("UPDATE `eco_pays` SET `value` = "+l.toString()+";", true);
    }

    public static Long getNowTime() {
        return (System.currentTimeMillis() / 1000L);
    }
}