package doctor.eco5.obj;

import doctor.eco5.Eco5;
import doctor.eco5.types.ProductionType;
import org.bukkit.Location;
import ru.komiss77.modules.world.XYZ;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ProductionPlace {

    public ProductionType type;
    public XYZ center;
    public Float radius;

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
        }
        return null;
    }

    public static ProductionPlace create(Location location, ProductionType type, Float radius) {
        ProductionPlace productionPlace = new ProductionPlace();
        productionPlace.radius = radius;
        productionPlace.type = type;
        productionPlace.center = new XYZ(location);
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

}