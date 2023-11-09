package hemok98.coreprotectadvance;

import net.coreprotect.CoreProtect;
import net.coreprotect.consumer.Queue;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.raid.RaidTriggerEvent;
import org.bukkit.inventory.BeaconInventory;

import java.util.Collections;
import java.util.EnumSet;

public class FixListener extends Queue implements Listener {

    private final EnumSet<Material> BEACON_ITEMS = EnumSet.of(Material.IRON_INGOT, Material.GOLD_INGOT, Material.DIAMOND, Material.NETHERITE_INGOT, Material.EMERALD);

    @EventHandler
    public void beaconFix(InventoryClickEvent event){
        if (!(event.getInventory() instanceof BeaconInventory)) return;
        if ((event.getClickedInventory() instanceof BeaconInventory)) {
            if ( BEACON_ITEMS.contains(event.getCursor().getType()) )  CoreProtect.getInstance().getAPI().logInteraction(event.getWhoClicked().getName(), event.getInventory().getLocation());
        }

    }

    @EventHandler
    public void on(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Trident)) return;
        Trident trident = (Trident) event.getDamager();
        if (!(trident.getShooter() instanceof Player)) return;
        Player shooter = (Player) trident.getShooter();
        if (trident.getItemStack().getEnchantments().get(Enchantment.CHANNELING) != null && event.getEntity().getWorld().isThundering()) {
            CoreProtect.getInstance().getAPI().logPlacement("trident[" + shooter.getName() + "]", event.getEntity().getLocation(), Material.FIRE, event.getEntity().getLocation().getBlock().getBlockData());
        }
    }


    @EventHandler
    public void on(RaidTriggerEvent event) {
        Queue.queueEntityKill("raidSpawn[" + event.getPlayer().getName() + "]", event.getPlayer().getLocation(), Collections.emptyList(), EntityType.VILLAGER);
    }

    @EventHandler
    public void on(EntityDeathEvent event) {
        if (event.getEntity().getLastDamageCause() != null) {
            if (event.getEntity().getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
                Player killer = event.getEntity().getKiller();
                if (killer != null) {
                    //Bukkit.broadcastMessage(String.valueOf(event.getEntity().getKiller().getName()));
                    if (event.getEntity().getType().equals(EntityType.PLAYER)) {
                        Queue.queuePlayerKill(killer.getName(), event.getEntity().getLocation(), ((Player) event.getEntity()).getName());
                    } else {
                        Queue.queueEntityKill("#firework[" + killer.getName() + "]", event.getEntity().getLocation(), Collections.emptyList(), event.getEntity().getType());
                    }

                }
            }
        }

    }
}
