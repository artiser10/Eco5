package doctor.eco5.obj.eco;

import doctor.eco5.obj.Fine;
import doctor.eco5.obj.rp.RPUser;
import org.bukkit.Sound;

public class TransAction_Handler {
    public static void pay_fine(RPUser rpUser, Fine fine) {
        int rpUser_balance = rpUser.balance_b + 0;
        if (rpUser_balance < fine.amount) {
            rpUser.sendMessage("Ваш баланс меньше суммы штрафа.");
            return;
        }
        rpUser.balance_b = rpUser_balance - fine.amount;
        fine.remove();
        rpUser.playSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
        rpUser.sendMessage("Штраф ["+fine.amount+"] оплачен!");
    }

    public static void from_cash_to_card(RPUser rpUser, Integer amount) {
        int rpUser_cash = rpUser.balance_a + 0;
        if (rpUser_cash < amount) {
            rpUser.sendMessage("Не хватает наличности для пополнения на указанную сумму.");
            return;
        }
        rpUser.balance_a = rpUser_cash - amount;
        rpUser.balance_b = rpUser.balance_b + amount; // хз чё тут вообще происходит, обычно меняются св-ва странно

        rpUser.playSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
        rpUser.sendMessage("Успешная операция. Внесено " + amount + " на счёт.");
    }

    public static void from_card_to_cash(RPUser rpUser, Integer amount) {
        int rpUser_card = rpUser.balance_b + 0;
        if (rpUser_card < amount) {
            rpUser.sendMessage("Не хватает средств на счёте для снятия указанной суммы.");
            return;
        }
        rpUser.balance_b = rpUser_card - amount;
        rpUser.balance_a = rpUser.balance_a + amount; // хз чё тут вообще происходит, обычно меняются св-ва странно

        rpUser.playSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
        rpUser.sendMessage("Успешная операция. Получено " + amount + " наличности.");
    }
}