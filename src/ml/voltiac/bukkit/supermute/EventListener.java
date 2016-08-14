package ml.voltiac.bukkit.supermute;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import net.minecraft.server.v1_10_R1.IChatBaseComponent;
import net.minecraft.server.v1_10_R1.PacketPlayOutChat;
import net.minecraft.server.v1_10_R1.IChatBaseComponent.ChatSerializer;

public class EventListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		if (Main.getInstance().getConfig().getBoolean("chatStatus")) {
			return;
		} else {
			sendActionBar(p, ChatColor.RED + "" + ChatColor.BOLD + "Hey!" + ChatColor.GRAY
					+ " Chat has been disabled! You may not speak.");
			e.setMessage("");
			e.setFormat("");
			e.setCancelled(true);
		}

	}

	public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		if (Main.getInstance().getConfig().getBoolean("chatStatus")) {
			return;
		} else {
			if (e.getMessage().contains("me") || e.getMessage().contains("say")
					|| e.getMessage().equalsIgnoreCase("say") || e.getMessage().equalsIgnoreCase("me")) {
				sendActionBar(p, ChatColor.RED + "" + ChatColor.BOLD + "Hey!" + ChatColor.GRAY
						+ " Chat has been disabled! You may not do that command.");
				e.setCancelled(true);
			}
		}

	}

	public static void sendActionBar(Player player, String message) {
		CraftPlayer p = (CraftPlayer) player;
		IChatBaseComponent cbc = ChatSerializer.a("{\"text\": \"" + message + "\"}");
		PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(ppoc);
	}

}
