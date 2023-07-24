package ro.fr33styler.groupedworldchat.listeners;

import org.bukkit.entity.Player;
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
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Group group = plugin.getGroups().get(player.getWorld().getUID());
        if (group != null) {
            String format = group.getFormat();
            if (plugin.getPapi() != null) {
                format = plugin.getPapi().replace(player, format);
            }
            try {
                event.setFormat(format);
            } catch (Exception ignored) {
                plugin.getLogger().severe("The format couldn't be parsed: " + format);
                event.setCancelled(true);
            }
            event.getRecipients().removeIf(recipient -> !(recipient.hasPermission("gwc.admin") || plugin.getGroups().get(recipient.getWorld().getUID()) == group));
        }
    }

}
