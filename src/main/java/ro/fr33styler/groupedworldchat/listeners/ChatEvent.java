package ro.fr33styler.groupedworldchat.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import ro.fr33styler.groupedworldchat.GroupedWorldChat;
import ro.fr33styler.groupedworldchat.group.Group;

public class ChatEvent implements Listener {

    private final GroupedWorldChat plugin;

    public ChatEvent(GroupedWorldChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onChat(AsyncPlayerChatEvent e) {
        Group group = plugin.getGroups().get(e.getPlayer().getWorld().getUID());
        if (group != null) {
            String format = group.getFormat();
            if (plugin.getPapi() != null) {
                format = plugin.getPapi().replace(e.getPlayer(), format);
            }
            try {
                e.setFormat(format);
            } catch (Exception ignored) {
                plugin.getLogger().severe("The format couldn't be parsed: " + format);
                e.setCancelled(true);
            }
            e.getRecipients().removeIf(p -> !(p.hasPermission("gwc.admin") || plugin.getGroups().get(p.getWorld().getUID()) == group));
        }
    }

}
