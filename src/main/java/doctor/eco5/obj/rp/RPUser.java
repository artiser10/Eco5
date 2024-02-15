package doctor.eco5.obj.rp;

import doctor.eco5.Eco5;
import doctor.eco5.InformationBlock;
import doctor.eco5.obj.Company;
import doctor.eco5.obj.CompanyMember;
import doctor.eco5.types.NonRPGroup;
import doctor.eco5.types.Sex;
import doctor.eco5.obj.guis.Company_list;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import ru.komiss77.utils.inventory.SmartInventory;

import java.util.HashMap;
import java.util.UUID;

public class RPUser {

    public String uuid;
    public String nickname;
    public NonRPGroup nonRPGroup;
    public String did;
    public Sex sex;

    public Integer balance_a;
    public Integer balance_b;

    public Player getPlayer() {
        return Bukkit.getPlayer(UUID.fromString(this.uuid));
    }

    public static RPUser registerNew(Player player) {
        RPUser rpUser = new RPUser();

        rpUser.uuid = player.getUniqueId() + "";
        rpUser.nickname = player.getName();
        rpUser.nonRPGroup = NonRPGroup.PLAYER;
        rpUser.did = "";
        rpUser.sex = Sex.Rakom;
        rpUser.balance_a = 0;
        rpUser.balance_b = 0;

        return rpUser;
    }

    public static RPUser get(Player player) {
        for (RPUser rpUser : Eco5.rpUsers) {
            if (rpUser.uuid.equals(player.getUniqueId().toString())) {
                return rpUser;
            }
        }
        return null;
    }

    public static RPUser get(String uuid) {
        for (RPUser rpUser : Eco5.rpUsers) {
            if (rpUser.uuid.equals(uuid)) {
                return rpUser;
            }
        }
        return null;
    }

    public HashMap<String, String> toHashMap() {
        HashMap<String, String> userData = new HashMap<>();

        userData.put("uuid", uuid);
        userData.put("nickname", nickname);
        userData.put("nonRPGroup", nonRPGroup.toString());
        userData.put("did", did);
        userData.put("sex", sex.toString());
        userData.put("balance_a", balance_a.toString());
        userData.put("balance_b", balance_b.toString());

        return userData;
    }

    public static RPUser fromHashMap(HashMap<String, String> sql_data) {
        RPUser rpUser = new RPUser();

        rpUser.uuid = sql_data.get("uuid");
        rpUser.nickname = sql_data.get("nickname");
        rpUser.nonRPGroup = NonRPGroup.toNonRPGroup(sql_data.get("nonRPGroup"));
        rpUser.did = sql_data.get("did");
        rpUser.sex = Sex.toSex(sql_data.get("sex"));
        rpUser.balance_a = Integer.parseInt(sql_data.get("balance_a"));
        rpUser.balance_b = Integer.parseInt(sql_data.get("balance_b"));

        return rpUser;
    }

    public boolean isStaff() {
        //InformationBlock.log_debug("STAFF: " + this.nonRPGroup);
        return !(this.nonRPGroup.equals(NonRPGroup.PLAYER) | this.nonRPGroup.equals(NonRPGroup.IDN));
    }

    public boolean isStaff(Company company) {
        for (CompanyMember cm : company.members) {
            if (cm.rpUser.uuid.equals(this.uuid)) {
                return true;
            }
        }
        return false;
    }

    public boolean isOnline() {
        if (this.getPlayer() == null) {
            return false;
        }
        return this.getPlayer().isOnline();
    }
    public void sendMessage(String msg) {
        Player player = Bukkit.getPlayer(UUID.fromString(uuid));
        assert player != null;
        player.sendMessage(Eco5.prefix.append(Component.text(msg).color(TextColor.color(202, 255, 199))));
    }

    public void playSound(Sound sound) {
        Player player = Bukkit.getPlayer(UUID.fromString(uuid));
        assert player != null;
        player.playSound(player.getLocation(), sound, 1, 1);
    }

    @Override
    public String toString() {
        return this.toHashMap().toString();
    }

    public Company getCompany() {
        for (Company company : Eco5.companies) {
            for (CompanyMember cm : company.members) {
                if (cm.rpUser.uuid.equals(this.uuid)) {
                    return company;
                }
            }
        }
        return null;
    }

    public void open_companies() {
        SmartInventory.builder().size(6).title("§4Компании").provider(new Company_list(true, this)).build().open(this.getPlayer());
    }

}