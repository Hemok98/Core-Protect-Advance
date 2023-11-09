package hemok98.coreprotectadvance;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CoreProtectAdvance extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(new FixListener(),this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
