package ro.fr33styler.groupedworldchat;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import ro.fr33styler.groupedworldchat.group.Group;
import ro.fr33styler.groupedworldchat.hook.PapiHook;
import ro.fr33styler.groupedworldchat.listeners.ChatEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GroupedWorldChat extends JavaPlugin {

    private PapiHook papi;
    private final Map<UUID, Group> groups = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        ConfigurationSection groupsSection = getConfig().getConfigurationSection("groups");
        for (String id : groupsSection.getKeys(false)) {

            ConfigurationSection idSection = groupsSection.getConfigurationSection(id);

            String name = idSection.getString("name");
            String format = idSection.getString("format");

            Group group = new Group(id, name, format);
            for (String worldName : idSection.getStringList("worlds")) {

                World world = Bukkit.getWorld(worldName);

                if (world == null) {
                    getLogger().warning("The world " + worldName + " could not be found!");
                } else if (groups.containsKey(world.getUID())) {
                    getLogger().warning("The world " + worldName + " is duplicated in the group " + id + "!");
                } else {
                    groups.put(world.getUID(), group);
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

    public PapiHook getPapi() {
        return papi;
    }

    public Map<UUID, Group> getGroups() {
        return groups;
    }

}
