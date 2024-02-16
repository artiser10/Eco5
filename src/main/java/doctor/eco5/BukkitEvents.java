package doctor.eco5;

import doctor.eco5.obj.ATM;
import doctor.eco5.obj.Door;
import doctor.eco5.obj.ProductionPlace;
import doctor.eco5.obj.eco.Economy;
import doctor.eco5.obj.rp.RPUser;
import doctor.eco5.types.ProductionType;
import doctor.eco5.utils.Gradient;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.spigotmc.event.entity.EntityDismountEvent;
import ru.komiss77.events.ChatPrepareEvent;

import java.util.ArrayList;
import java.util.List;

public class BukkitEvents implements Listener {
    @EventHandler
    public void onMessage(ChatPrepareEvent event) {
        InformationBlock.broadcast(Gradient.from_message(event.getMessage()));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        /*
        TextDisplay display = event.getPlayer().getLocation().getWorld().spawn(event.getPlayer().getLocation().clone().add(0, 3, 0), TextDisplay.class);
        display.setAlignment(TextDisplay.TextAlignment.CENTER);
        display.text(Component.text(event.getPlayer().getName()).color(TextColor.color(255, 89, 70)));
        event.getPlayer().addPassenger(display);
        */

        RPUser rpUser = RPUser.get(event.getPlayer());
        //InformationBlock.log_debug("RPUSERs: " + Eco5.rpUsers.size());
        if (rpUser == null) {
            rpUser = RPUser.registerNew(event.getPlayer());
            Eco5.rpUsers.add(rpUser);
        }
        InformationBlock.broadcast(Gradient.gradient("Приветствуем, " + rpUser.nickname + "!", 0xFF0000, 0xFFFFFF, 0x00FF00, false), Sound.BLOCK_AMETHYST_BLOCK_BREAK);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL) {
            return;
        }
        if (event.getHand() == EquipmentSlot.OFF_HAND) {
            return;
        }
        if (event.getAction().isLeftClick()) {
            return;
        }
        //InformationBlock.log_debug("call");
        Player player = event.getPlayer();
        RPUser rpUser = RPUser.get(player);
        if (rpUser == null) {
            return;
        }
        ItemStack is = event.getItem();
        Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }
        Door door = Door.get(block.getLocation());
        if (door != null) {
            event.setCancelled(true);
            //InformationBlock.log_debug("Ивент отменён");
            if (player.isSneaking()) {
                assert is != null;
                switch (is.getType()) { // шифт с предметом в руках по двери
                    case TRIPWIRE_HOOK -> {
                        //InformationBlock.log_debug("Пытаемся взаимодействовать с замком (откр/закр)");
                        door.change_lock_status(player, null, is);
                        //InformationBlock.log_debug("ключ вроде, отправли туда");
                    }
                    case REDSTONE -> {
                        //InformationBlock.log_debug("вошли");
                        ItemStack itemStack = new ItemStack(Material.TRIPWIRE_HOOK, 1);
                        ItemMeta met = itemStack.getItemMeta();
                        List<Component> lore = new ArrayList<>();
                        lore.add(Component.text(door.id).color(TextColor.color(0, 255, 255)));
                        met.lore(lore);
                        itemStack.setItemMeta(met);
                        player.getInventory().addItem(itemStack);
                    }
                }
            } else {
                //InformationBlock.log_debug("Пытаемся взаимодействовать с дверью (откр/закр)");
                door.open_door(player, null);
            }
        }
        //InformationBlock.log_debug("Блок1");
        ATM atm = ATM.get(block.getLocation());
        if (atm != null) {
            if (is != null) {
                if (is.getType() == Material.GLOWSTONE_DUST) {
                    //InformationBlock.log_debug("Удаляем ATM");
                    player.sendMessage(Eco5.prefix.append(Component.text("ATM №" + atm.id + " удалён.").color(TextColor.color(171, 255, 136))));
                    player.playSound(block.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1,1);
                    ATM.remove(block.getLocation());
                    return;
                }
            }
            //InformationBlock.log_debug("Открываем ATM");
            atm.open_for(player);
            return;
        }
        //InformationBlock.log_debug("Блок2");
        if (is != null) {
            switch (is.getType()) {

                case GLOWSTONE_DUST -> {
                    //InformationBlock.log_debug("Создаём ATM");
                    atm = ATM.create(block.getLocation(), "A" + ((int) System.currentTimeMillis() / 1000));
                    player.sendMessage(Eco5.prefix.append(Component.text("ATM №" + atm.id + " создан.").color(TextColor.color(171, 255, 136))));
                    player.playSound(block.getLocation(), Sound.BLOCK_ANVIL_USE, 1, 1);
                    return;
                }
                case GOLDEN_PICKAXE -> {
                    if (rpUser.isStaff()) {
                        for (ProductionPlace PP : Eco5.productionPlaces) {
                            if (PP.center.getCenterLoc(Eco5.world).distance(block.getLocation()) <= 1) {
                                // тут есть, удаляем
                                PP.delete();
                                rpUser.sendMessage("Зона удалена.");
                                return;
                            }
                        }
                        ProductionPlace.create(block.getLocation(), ProductionType.ORE, 15F);
                        rpUser.sendMessage("Зона создана.");
                    }
                }
            }
        }
        if (block.getBlockData() instanceof Stairs) {
            if (!Eco5.sit.containsKey(player)) {
                Location bl_loc = block.getLocation();
                if (bl_loc.clone().add(0, 1, 0).getBlock().getType() == Material.AIR) {
                    if (Math.abs(bl_loc.getY() - player.getLocation().getY()) <= 1) {
                        if (player.getLocation().distance(bl_loc) <= 4) {
                            Eco5.sit.put(player, player.getLocation());
                            BlockDisplay display = block.getWorld().spawn(block.getLocation().clone().add(0.5, 0.3, 0.5), BlockDisplay.class);
                            display.setGravity(false);
                            display.setBlock(Material.BARRIER.createBlockData());
                            display.setDisplayHeight(1.2f);
                            display.setDisplayWidth(1.2f);
                            display.addPassenger(player);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void on_leave_vehicle(EntityDismountEvent event) { // выход с транспорта
        Entity e = event.getEntity();
        //Bukkit.broadcast(Component.text("e:"+e));
        if (e instanceof Player) { // если это игрок
            Player player = (Player) event.getEntity();
            Entity entity = event.getDismounted();
            //Bukkit.broadcast(Component.text("ent:"+entity));
            if (entity instanceof BlockDisplay) {
                //Bukkit.broadcast(Component.text("Это бд"));
                entity.remove();
                Location loc = Eco5.sit.get(player);
                assert loc != null;
                //player.playSound(loc, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                player.teleport(loc);
                Eco5.sit.remove(player);
            }
        }
    }

    @EventHandler
    public static void onBlockBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();
        RPUser rpUser = RPUser.get(player);
        Block block = event.getBlock();

        assert rpUser != null;
        ProductionPlace PP = ProductionPlace.get(block.getLocation());
        if (PP != null) {
            PP.block_removed(block);
        } else {
            if (!rpUser.isStaff()) {
                event.setCancelled(true);
                rpUser.sendMessage("У вас нет полномочий ломать это здесь!");
            }
        }



        //Door check
        Door.remove(block.getLocation().clone().add(0, -1, 0));
        Door.remove(block.getLocation().clone().add(0,  0, 0));
        Door.remove(block.getLocation().clone().add(0, +1, 0));



    }

    @EventHandler
    public static void onBlockPlace(BlockPlaceEvent event) {

        Player player = event.getPlayer();
        RPUser rpUser = RPUser.get(player);
        Block block = event.getBlock();

        if (block.getBlockData() instanceof org.bukkit.block.data.type.Door) {
            Door.create(block.getLocation(), "D" + ((int) System.currentTimeMillis() * 1000));
        }

    }

    @EventHandler
    public static void onLeave(PlayerQuitEvent event) {
        RPUser rpUser = RPUser.get(event.getPlayer());
        assert rpUser != null;
        InformationBlock.broadcast(Gradient.gradient("Провожаем " + rpUser.nickname + "!", 0xFF0000, 0xFFFFFF, 0x00FF00, false), Sound.BLOCK_AMETHYST_BLOCK_BREAK);
    }
    /*
    @EventHandler
    public static void on_msg(ChatPrepareEvent event) {
        RPUser rpUser = RPUser.get(event.getPlayer());
        assert rpUser != null;
        event.setCancelled(true);
        Component component = Component.text("");
        if (rpUser.isStaff()) {
            component = component.append(Staff.badge);
        } else {
            InformationBlock.log_debug("Это не стафф, " + rpUser.nonRPGroup);
            component = component.append(Staff.badge);
        }
        component = component.append(Component.text(rpUser.nickname).color(TextColor.color(168, 255, 164))).append(Component.text(" " + event.getMessage()).color(TextColor.color(240, 171, 167)));
        Bukkit.broadcast(component);
    }

     */
}