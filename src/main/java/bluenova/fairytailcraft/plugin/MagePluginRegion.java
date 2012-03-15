/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bluenova.fairytailcraft.plugin;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class MagePluginRegion {
    /* API for creating Regions
     * like WorldEdit cuboids.
     * author = pyraetos
     */

    private Location loc1;
    private Location loc2;
    private World world;

    public MagePluginRegion(Location loc1, Location loc2, World world) {
        this.loc1 = loc1;
        this.loc2 = loc2;
        this.world = world;
    }

    public void set(Material material) {

        double a[] = {Math.floor(loc1.getX()), Math.floor(loc1.getY()), Math.floor(loc1.getZ())};
        double b[] = {Math.floor(loc2.getX()), Math.floor(loc2.getY()), Math.floor(loc2.getZ())};

        for (int xcounter = (int) Math.min(a[0], b[0]); xcounter <= Math.max(a[0], b[0]); xcounter++) {
            for (int ycounter = (int) Math.min(a[1], b[1]); ycounter <= Math.max(a[1], b[1]); ycounter++) {
                for (int zcounter = (int) Math.min(a[2], b[2]); zcounter <= Math.max(a[2], b[2]); zcounter++) {
                    Location loc = new Location(world, xcounter, ycounter, zcounter);
                    loc.getBlock().setType(material);
                }
            }
        }
    }
    
    public List<Block> getBlocks() {
        List<Block> blist =new ArrayList<Block>();
        double a[] = {Math.floor(loc1.getX()), Math.floor(loc1.getY()), Math.floor(loc1.getZ())};
        double b[] = {Math.floor(loc2.getX()), Math.floor(loc2.getY()), Math.floor(loc2.getZ())};

        for (int xcounter = (int) Math.min(a[0], b[0]); xcounter <= Math.max(a[0], b[0]); xcounter++) {
            for (int ycounter = (int) Math.min(a[1], b[1]); ycounter <= Math.max(a[1], b[1]); ycounter++) {
                for (int zcounter = (int) Math.min(a[2], b[2]); zcounter <= Math.max(a[2], b[2]); zcounter++) {
                    Location loc = new Location(world, xcounter, ycounter, zcounter);
                    blist.add(loc.getBlock());
                }
            }
        }
        return blist;
    }
    
    public List<Location> getLocations() {
        List<Location> blist =new ArrayList<Location>();
        double a[] = {Math.floor(loc1.getX()), Math.floor(loc1.getY()), Math.floor(loc1.getZ())};
        double b[] = {Math.floor(loc2.getX()), Math.floor(loc2.getY()), Math.floor(loc2.getZ())};

        for (int xcounter = (int) Math.min(a[0], b[0]); xcounter <= Math.max(a[0], b[0]); xcounter++) {
            for (int ycounter = (int) Math.min(a[1], b[1]); ycounter <= Math.max(a[1], b[1]); ycounter++) {
                for (int zcounter = (int) Math.min(a[2], b[2]); zcounter <= Math.max(a[2], b[2]); zcounter++) {
                    Location loc = new Location(world, xcounter, ycounter, zcounter);
                    blist.add(loc);
                }
            }
        }
        return blist;
    }
}