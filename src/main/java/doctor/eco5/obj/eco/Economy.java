package doctor.eco5.obj.eco;

import doctor.eco5.Eco5;
import doctor.eco5.InformationBlock;
import doctor.eco5.data.USQLAdapter;
import doctor.eco5.obj.Company;
import doctor.eco5.obj.CompanyMember;
import doctor.eco5.obj.CompanyRang;
import doctor.eco5.obj.rp.RPUser;
import doctor.eco5.types.CompanyPermission;
import doctor.eco5.types.NonRPGroup;
import doctor.eco5.types.TaxType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;


public class Economy {
    private static final float hoursUpd = 30 * 60; // <разница во времени для выплаты зп по личным счетам в секундах> 3600 * 0.005F ~18s
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
        //InformationBlock.log_debug("call");
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
                return;
            }
            for (CompanyMember p : pay.keySet()) {
                p.company_must = pay.get(p);
                //broadcast("Средства начислены на счёт компании. Новая сумма = " + p.company_must, p.rpUser);
                Component message_getter = Component.text("На Ваш накопительный счёт компании поступили средства в размере <"+pay.get(p)+">. Новое значение <"+p.company_must+">.").color(TextColor.color(190, 255, 162)).hoverEvent(HoverEvent.showText(Component.text("Компания: " + company.public_name + "").color(TextColor.color(219, 196, 255))));
                Player p1 = p.rpUser.getPlayer();
                assert p1 != null;
                p1.sendMessage(prefix.append(message_getter));
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
                co.balance = (int) (cobalance - (must * TaxType.Salary.amount()));
                Component message_getter =  Component.text("Вам начислена зарплата в размере <"+must+">.").color(TextColor.color(255, 230, 167)).hoverEvent(HoverEvent.showText(Component.text("Компания: " + co.public_name + "\nОбщий баланс: " + cm.rpUser.balance_b).color(TextColor.color(149, 209, 255))));
                Component message_owner = Component.text("Начислена зарплата сотруднику в размере <"+must+">("+must * TaxType.Salary.amount()+").").color(TextColor.color(255, 230, 167)).hoverEvent(HoverEvent.showText(Component.text("Компания: " + co.public_name + "\nСотрудник: " + cm.rpUser.nickname + "\nБаланс компании: " + co.balance).color(TextColor.color(149, 209, 255))));

                Player p1 = cm.rpUser.getPlayer();
                assert p1 != null;
                p1.sendMessage(prefix.append(message_getter));
                for (CompanyMember compmember : co.members) {
                    if (compmember.hasPermission(CompanyPermission.VIEW_FINANCE) || Objects.equals(compmember.rang.priority, co.maxRang().priority)) {
                        Player p2 = compmember.rpUser.getPlayer();
                        assert p2 != null;
                        p2.sendMessage(prefix.append(message_owner));
                    }
                }

                cm.company_must = 0;
                //InformationBlock.log_debug("Ну мы чё то там сделали, хз чё по ошибкам");
            }
        }
        broadcast("[ADM] Зарплаты выплачены.", NonRPGroup.ADMIN_SYSTEM);
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