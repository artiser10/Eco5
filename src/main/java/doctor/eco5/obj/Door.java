package doctor.eco5.obj;

import doctor.eco5.Eco5;
import doctor.eco5.InformationBlock;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.komiss77.ApiOstrov;
import ru.komiss77.utils.TCUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Door {

    public String id;
    public Location loc;
    public Boolean lock_opened;

    public HashMap<String, String> toHashMap() {
        HashMap<String, String> doorData = new HashMap<>();
        doorData.put("uuid", id);
        doorData.put("location", ((int) loc.getX()) + ";" + ((int) loc.getY()) + ";" + ((int) loc.getZ()));
        if (lock_opened) {
            doorData.put("lock_opened", "yes");
        } else {
            doorData.put("lock_opened", "no");
        }

        return doorData;
    }

    public static Door fromHashMap(HashMap<String, String> sql_data) {
        Door door = new Door();

        String[] sql_loc_data = sql_data.get("location").split(";");
        String state = sql_data.get("lock_opened");

        door.id = sql_data.get("uuid");
        door.loc = new Location(Eco5.world, Integer.parseInt(sql_loc_data[0]), Integer.parseInt(sql_loc_data[1]), Integer.parseInt(sql_loc_data[2]));
        door.lock_opened = state.equalsIgnoreCase("yes");
        return door;
    }

    @Override
    public String toString() {
        return this.toHashMap().toString();
    }

    public static Door get(Location location) {
        for (Door door : Eco5.doors) {
            if (door.loc.x() == location.x() & door.loc.y() == location.y() & door.loc.z() == location.z()) {
                return door;
            }
            if (door.loc.x() == location.x() & door.loc.y() == location.y() + 1 & door.loc.z() == location.z()) {
                return door;
            }
            if (door.loc.x() == location.x() & door.loc.y() == location.y() - 1 & door.loc.z() == location.z()) {
                return door;
            }
        }
        Block block = location.getBlock();
        if (block.getBlockData() instanceof org.bukkit.block.data.type.Door) {
            Door door = Door.create(location, "D" + ((int) System.currentTimeMillis() * 1000));
            Eco5.doors.add(door);
            return door;
        }
        return null;
    }

    public static boolean remove(Location location) {
        Set<Door> doors = new HashSet<>();
        boolean state = false;
        for (Door door : Eco5.doors) {
            if (door.loc.x() == location.x() & door.loc.y() == location.y() & door.loc.z() == location.z()) {
                state = true;
            } else {
                doors.add(door);
            }
        }
        Eco5.doors = doors;
        return state;
    }

    public static Door create(Location location, String id) {
        Door door = new Door();
        door.loc = location;
        door.id = id;
        door.lock_opened = false;
        Eco5.doors.add(door);
        return door;
    }

    public boolean open_door(Player user, Boolean open) {
        //InformationBlock.log_debug("OD");
        if (this.lock_opened) {
            //InformationBlock.log_debug("Открыто, чё-то делаем");
            Location location = this.loc;
            Block blockn = location.clone().add(0,-1,0).getBlock();
            Block block0 = location.clone().getBlock();
            Block blockv = location.clone().add(0,+1,0).getBlock();
            int c = 0;
            if (block0.getBlockData() instanceof org.bukkit.block.data.type.Door door) {
                if (open == null) {
                    open = !door.isOpen();
                }
                door.setPowered(open);
                door.setOpen(open);
                block0.setBlockData(door, false);
                c = c + 1;
            }
            if (blockn.getBlockData() instanceof org.bukkit.block.data.type.Door door) {
                door.setPowered(open);
                door.setOpen(open);
                blockn.setBlockData(door, false);
                c = c + 1;
            }
            if (blockv.getBlockData() instanceof org.bukkit.block.data.type.Door door) {
                door.setPowered(open);
                door.setOpen(open);
                blockv.setBlockData(door, false);
                c = c + 1;
            }

            user.stopAllSounds();
            if (open) {
                user.playSound(user.getLocation(), Sound.BLOCK_WOODEN_DOOR_OPEN, 1, 1);
                user.sendActionBar(Component.text("Дверь открылась").color(TextColor.color(124, 255, 61)));
            } else {
                user.playSound(user.getLocation(), Sound.BLOCK_WOODEN_DOOR_CLOSE, 1, 1);
                user.sendActionBar(Component.text("Дверь закрылась").color(TextColor.color(255, 38, 34)));
            }
            return c != 0;
        } else {
            user.stopAllSounds();
            user.playSound(user.getLocation(), Sound.ENTITY_ARMOR_STAND_HIT, 1, 1);
            user.sendActionBar(Component.text("Дверь заперта").color(TextColor.color(60, 60, 60)));
            user.playSound(user.getLocation(), Sound.ENTITY_SHULKER_CLOSE, 1, 1);
        }
        return false;
    }

    public void change_lock_status(Player user, Boolean open, ItemStack key) {
        ItemMeta im_key = key.getItemMeta();
        if (im_key == null) {
            //InformationBlock.log_debug("IM_KEY = null");
            return;
        }

        //InformationBlock.log_debug("IM_KEY.Lore() = " + im_key.lore().get(0).toString());
        //InformationBlock.log_debug("L1:" + im_key.lore().get(0) + "; L2:" + Component.text(this.id).color(TextColor.color(0, 255, 255)));
        if (TCUtils.toString(im_key.lore().get(0)).equalsIgnoreCase(TCUtils.toString(Component.text(this.id).color(TextColor.color(0, 255, 255))))) {
            // ключ сошёлся
            if (open == null) {
                lock_opened = !lock_opened;
            } else {
                lock_opened = open;
            }
            if (lock_opened) {
                user.sendActionBar(Component.text("Дверной замок открылся").color(TextColor.color(195, 132, 255)));
                user.playSound(user.getLocation(), Sound.BLOCK_IRON_TRAPDOOR_OPEN, 1, 1);
            } else {
                user.sendActionBar(Component.text("Дверной замок закрылся").color(TextColor.color(255, 97, 185)));
                user.playSound(user.getLocation(), Sound.BLOCK_IRON_TRAPDOOR_CLOSE, 1, 1);
            }
        }
    }
    public static boolean isDoorMat(Block block) {
        return block.getBlockData() instanceof org.bukkit.block.data.type.Door;
    }
}