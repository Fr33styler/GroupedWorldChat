package ro.fr33styler.gwc;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import ro.fr33styler.gwc.hooks.PapiHook;
import ro.fr33styler.gwc.group.Group;
import ro.fr33styler.gwc.listeners.ChatEvent;

import java.util.*;

public class GroupedWorldChat extends JavaPlugin {

    private PapiHook papi;
    private final Map<UUID, Group> groups = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        for (String id : config.getConfigurationSection("Groups").getKeys(false)) {

            String name = config.getString("Groups." + id + ".Name");
            String format = config.getString("Groups." + id + ".Format");

            Group group = new Group(id, name, format);
            for (String worldName : config.getStringList("Groups." + id + ".Worlds")) {

                World world = Bukkit.getWorld(worldName);

                if (world == null) {
                    getLogger().warning("The world " + worldName + " could not be found!");
                } else {
                    if (groups.containsKey(world.getUID())) {
                        getLogger().warning("The world " + worldName + " is duplicated in the group " + id + "!");
                    } else {
                        groups.put(world.getUID(), group);
                    }
                }

            }
        }
        Bukkit.getPluginManager().registerEvents(new ChatEvent(this), this);
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            papi = new PapiHook(this);
            papi.register();
        }
    }

    @Override
    public void onDisable() {
        groups.clear();
        papi.unregister();
        papi = null;
    }

    public Map<UUID, Group> getGroups() {
        return groups;
    }

    public PapiHook getPapi() {
        return papi;
    }

}
