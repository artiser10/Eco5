package doctor.eco5.obj;

import org.bukkit.Location;
import org.bukkit.Material;

public class PBlock {
    public Material material;
    public Location location;
    public int time;

    public static PBlock PBlock(Material material, Location location, Integer time) {
        PBlock pBlock = new PBlock();
        pBlock.material = material;
        pBlock.location = location;
        pBlock.time = time;

        return pBlock;
    }
}