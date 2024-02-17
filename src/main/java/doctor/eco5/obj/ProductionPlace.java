package doctor.eco5.obj;

import doctor.eco5.Eco5;
import doctor.eco5.InformationBlock;
import doctor.eco5.types.ProductionType;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;
import ru.komiss77.modules.world.XYZ;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ProductionPlace {

    public ProductionType type;
    public XYZ center;
    public Float radius;

    public Set<PBlock> breaked_blocks = new HashSet<>();

    public void delete() {
        Set<ProductionPlace> places = new HashSet<>();
        for (ProductionPlace PP : Eco5.productionPlaces) {
            if (PP.center == this.center) {
                // оно, не добавляем
            } else {
                places.add(PP);
            }
        }
        Eco5.productionPlaces = places;
    }

    public HashMap<String, String> toHashMap() {
        HashMap<String, String> placeData = new HashMap<>();
        placeData.put("type",     type.toString());
        placeData.put("center", center.toString());
        placeData.put("radius", radius.toString());
        return placeData;
    }

    public static ProductionPlace fromHashMap(HashMap<String, String> sql_data) {
        ProductionPlace productionPlace = new ProductionPlace();

        productionPlace.type   = ProductionType.toProductionType(sql_data.get("type"));
        productionPlace.center = XYZ.fromString(sql_data.get("center"));
        productionPlace.radius = Float.parseFloat(sql_data.get("radius"));
        productionPlace.breaked_blocks = new HashSet<>();

        return productionPlace;
    }

    @Override
    public String toString() {
        return this.toHashMap().toString();
    }

    public static ProductionPlace get(Location location) {
        for (ProductionPlace productionPlace : Eco5.productionPlaces) {
            XYZ xyz = new XYZ(location);
            if (Objects.equals(productionPlace.center, xyz)) {
                return productionPlace;
            }
            if (productionPlace.center.getCenterLoc(Eco5.world).distance(location) <= productionPlace.radius) {
                return productionPlace;
            }
        }
        return null;
    }

    public static boolean inZone(Location location) {
        for (ProductionPlace PP : Eco5.productionPlaces) {
            Location center = PP.center.getCenterLoc(Eco5.world);
            if (center.distance(location) <= PP.radius) {
                return true;
            }
        }
        return false;
    }

    public static ProductionPlace create(Location location, ProductionType type, Float radius) {
        ProductionPlace productionPlace = new ProductionPlace();
        productionPlace.radius = radius;
        productionPlace.type = type;
        productionPlace.center = new XYZ(location);

        Eco5.productionPlaces.add(productionPlace);
        return productionPlace;
    }

    public boolean remove(Location location) {
        Set<ProductionPlace> places = new HashSet<>();
        boolean st = false;
        for (ProductionPlace productionPlace : Eco5.productionPlaces) {
            if (Objects.equals(productionPlace.center, new XYZ(location))) {
                st = true; // удаляем
            } else {
                places.add(productionPlace);
            }
        }
        Eco5.productionPlaces = places;
        return st;
    }

    public void block_removed(Block block) {
        int time = (int) (System.currentTimeMillis() / 1000);
        if (block.getBlockData().getMaterial().toString().contains("_")) {
            String[] spl = block.getBlockData().getMaterial().toString().split("_");
            for (String s : spl) {
                if (s.equalsIgnoreCase("ORE")) {
                    this.breaked_blocks.add(PBlock.PBlock(block.getType(), block.getLocation().add(-0.5, -0.5, -0.5), time));
                    block.setType(Material.AIR, true);
                    Eco5.world.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, block.getLocation(), 20);
                }
            }
        }

    }

    public static void checker() {
        //InformationBlock.log_debug("Вызван чекер, длина " + Eco5.productionPlaces.size());
        for (ProductionPlace PP : Eco5.productionPlaces) {
            //InformationBlock.log_debug("PP типа на " + PP.center + "; кол-во блоков " + PP.breaked_blocks.size());
            for (PBlock block : PP.breaked_blocks) {
                int time = block.time;
                int now = (int) (System.currentTimeMillis() / 1000);
                if (Math.abs(now - time) == 30) {
                    //InformationBlock.log_debug("B1:" + block.location.getBlock().getType());
                    //InformationBlock.log_debug("B1.5:" + block.material);
                    Bukkit.getScheduler().runTask(Eco5.plugin, () -> {
                        Eco5.world.setBlockData(block.location, block.material.createBlockData());
                        Eco5.world.spawnParticle(Particle.NAUTILUS, block.location, 20);
                        Eco5.world.playSound(block.location, Sound.ENTITY_CAMEL_DASH_READY, 1, 1);
                    });
                    //InformationBlock.log_debug("B2:" + block.location.getBlock().getType());
                    PP.breaked_blocks.remove(block);
                }
            }
        }

    }



}