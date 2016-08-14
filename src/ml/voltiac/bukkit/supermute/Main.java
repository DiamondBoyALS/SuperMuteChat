package ml.voltiac.bukkit.supermute;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_10_R1.IChatBaseComponent;
import net.minecraft.server.v1_10_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_10_R1.PacketPlayOutChat;

public class Main extends JavaPlugin {
	public EventListener listener = new EventListener();
	public ml.voltiac.bukkit.supermute.PluginManager PluginManager = new ml.voltiac.bukkit.supermute.PluginManager();

	@Override
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new EventListener(), this);

		PluginManager.log(0, false, true, "+---------------------New Log-----------------------+");
		PluginManager.log(1, true, true, "Plugin Enabled");
		if (!getConfig().contains("chatStatus")) {
			getConfig().set("chatStatus", true);
		}
		if (!getConfig().contains("password")) {
			getConfig().set("password", "Password11");

		}
	}

	@Override
	public void onDisable() {
		PluginManager.log(1, true, true, "Plugin Disabled");

	}

	public FileConfiguration Config = getConfig();

	public static Main getInstance() {
		return (Main) Bukkit.getPluginManager().getPlugin("SuperMuteChat");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player p = (Player) sender;

		if (label.equalsIgnoreCase("muteChat") || label.equalsIgnoreCase("unMuteChat")) {
			if (args.length == 0) {
				p.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH
						+ "+---------------------------------------------------+");
				p.sendMessage(ChatColor.GOLD + "/muteChat <password>" + ChatColor.GRAY + " - " + ChatColor.AQUA
						+ "Mutes Chat > Password Protected!");
				p.sendMessage(ChatColor.GOLD + "/unMuteChat <password>" + ChatColor.GRAY + " - " + ChatColor.AQUA
						+ "Unmutes Chat > Password Protected!");
				p.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH
						+ "+---------------------------------------------------+");
				return true;
			} else if (args[0].equalsIgnoreCase("help")) {
				p.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH
						+ "+---------------------------------------------------+");
				p.sendMessage(ChatColor.GOLD + "/muteChat <password>" + ChatColor.GRAY + " - " + ChatColor.AQUA
						+ "Mutes Chat > Password Protected!");
				p.sendMessage(ChatColor.GOLD + "/unMuteChat <password>" + ChatColor.GRAY + " - " + ChatColor.AQUA
						+ "Unmutes Chat > Password Protected!");
				p.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH
						+ "+---------------------------------------------------+");
				return true;
			} else if (label.equalsIgnoreCase("muteChat")) {
				if (args[0].equalsIgnoreCase("getMuted")) {
					p.sendMessage(ChatColor.AQUA + "Chat: " + ChatColor.GOLD
							+ String.valueOf(getConfig().getBoolean("chatStatus")));
					return true;
				} else if (args[0].equalsIgnoreCase("reload")) {
					if (args.length != 2) {
						p.sendMessage(ChatColor.AQUA + "Usage: " + ChatColor.GOLD + "/muteChat reload <password>");
					} else if (args[1].equals(getConfig().getString("password"))) {
						loadConfiguration();
						p.sendMessage(ChatColor.GREEN + "SuperMuteChat has been reloaded!");
					} else {
						p.sendMessage(ChatColor.RED + "INCORRECT PASSWORD!");
					}
					return true;
				} else if (args[0].equalsIgnoreCase("changepassword")) {
					if (args.length == 4) {
						if (args[0].equals(getConfig().getString("password"))) {
							if (!args[3].equalsIgnoreCase(args[4].toString())) {
								getConfig().set("password", args[3]);
								p.sendMessage(ChatColor.AQUA + "Password changed to: " + ChatColor.GOLD + args[3]);
								saveConfig();
								return true;
							} else {
								p.sendMessage(ChatColor.RED + "Passwords do not match!");
							}
						} else {
							p.sendMessage(ChatColor.RED + "INNCORECT PASSWORD!");
						}
					} else {
						p.sendMessage(ChatColor.AQUA + "Usage: " + ChatColor.GOLD
								+ "/muteChat changepassword <curentPassword> <newPassword> <repeatPassword>");
						return true;
					}
				}
				if (args.length != 1) {
					p.sendMessage(ChatColor.AQUA + "Usage: " + ChatColor.GOLD + "/muteChat <password>");
				} else if (args[0].equals(getConfig().getString("password"))) {
					getConfig().set("chatStatus", false);
					saveConfig();
					p.sendMessage(ChatColor.RED + "You have muted chat!");
					Bukkit.broadcastMessage(ChatColor.RED + "Chat has been muted for all players!");
				} else {
					p.sendMessage(ChatColor.RED + "INCORRECT PASSWORD!");
				}
				return true;

			} else if (label.equalsIgnoreCase("unMuteChat")) {
				if (args.length != 1) {
					p.sendMessage(ChatColor.AQUA + "Usage: " + ChatColor.GOLD + "/muteChat <password>");
					return true;
				} else if (args[0].equals(getConfig().getString("password"))) {
					getConfig().set("chatStatus", true);
					saveConfig();
					p.sendMessage(ChatColor.GREEN + "You have unmuted chat!");
					Bukkit.broadcastMessage(ChatColor.GREEN + "Chat has been unmuted for all players!");
					return true;
				} else {
					p.sendMessage(ChatColor.RED + "INCORRECT PASSWORD!");
					return true;
				}

			} else {
				p.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH
						+ "+---------------------------------------------------+");
				p.sendMessage(ChatColor.GOLD + "/muteChat <password>" + ChatColor.GRAY + " - " + ChatColor.AQUA
						+ "Mutes Chat > Password Protected!");
				p.sendMessage(ChatColor.GOLD + "/unMuteChat <password>" + ChatColor.GRAY + " - " + ChatColor.AQUA
						+ "Unmutes Chat > Password Protected!");
				p.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH
						+ "+---------------------------------------------------+");
				return true;
			}
		}

		return false;
	}

	public static void sendActionBar(Player player, String message) {
		CraftPlayer p = (CraftPlayer) player; // Don't run if player is not on
												// 1.8
		IChatBaseComponent cbc = ChatSerializer.a("{\"text\": \"" + message + "\"}");
		PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(ppoc);
	}

	public void loadConfiguration() {

		getConfig().getDefaults();

		reloadConfig();

		if (getConfig().getInt("config") != 2) {
			PluginManager.log(2, true, true, "-=-=-=-=-=-=-=-=ERROR IN CONFIG!=-=-=-=-=-=-=-=-");
			PluginManager.log(2, true, true, "Your config version is not the same as the plugin version!");
			PluginManager.log(2, true, true, "Plugin will now be disabled untill problem is fixed!");
			PluginManager.log(2, true, true, "Please delete your config and restart the server!");
			PluginManager.log(2, true, true, "-=-=-=-=-=-=-=-=ERROR IN CONFIG!=-=-=-=-=-=-=-=-");
			getServer().getPluginManager().disablePlugin(this);
		}

		PluginManager.log(1, true, true, "Configuation Loaded");
	}

	public void saveConfiguration() {
		saveConfig();
		PluginManager.log(1, true, true, "Configuation Saved");
	}

	public boolean getChatStatus() {
		return getConfig().getBoolean("chatStatus");
	}

}
