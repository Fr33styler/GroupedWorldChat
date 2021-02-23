package ro.fr33styler.gwc.hooks;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import ro.fr33styler.gwc.GroupedWorldChat;
import ro.fr33styler.gwc.group.Group;

public class PapiHook {
    
    private final GWCExtension papi;

    public PapiHook(GroupedWorldChat plugin) {
        papi = new GWCExtension(plugin);
    }
    
    public void unregister() {
        papi.unregister();
    }

    public void register() {
        papi.register();
    }

    public String replace(Player p, String text) {
        return PlaceholderAPI.setPlaceholders(p, text);
    }

    private static class GWCExtension extends PlaceholderExpansion {

        private final GroupedWorldChat plugin;

        GWCExtension(GroupedWorldChat plugin) {
            this.plugin = plugin;
        }

        @Override
        public boolean persist() {
            return true;
        }

        @Override
        public boolean canRegister() {
            return true;
        }

        @Override
        public String getIdentifier() {
            return "gwc";
        }

        @Override
        public String getAuthor() {
            return "Fr33styler";
        }

        @Override
        public String getVersion() {
            return plugin.getDescription().getVersion();
        }

        @Override
        public String onRequest(OfflinePlayer offlinePlayer, String identifier) {
            if (offlinePlayer != null && offlinePlayer.isOnline()) {
                Group group = plugin.getGroups().get(((Player) offlinePlayer).getWorld().getUID());
                if (group != null) {
                    if (identifier.equals("group")) {
                        return group.getID();
                    } else if (identifier.equals("group_name")) {
                        return group.getName();
                    }
                }
            }
            return null;
        }
    }

}