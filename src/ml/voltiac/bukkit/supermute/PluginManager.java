package ml.voltiac.bukkit.supermute;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PluginManager {
		
	// Config file
	public String pluginStoreFilePath = "coc_store/";
	public String configFileName = "config.yml";
	public String logFileName = "log.log";
	
	// Options
	public String prefix;
	public String colorPrefix;
	
	
	
	public void log(int code, boolean console, boolean log, String msg) {

		// Codes:
		// - 0 = No formatting
		// - 1 = Info
		// - 2 = Error
		// - 3 = Ran Command
		// - 4 = Game info
		// - 5 = lobby info
		// - 6 = Warning
		// - 99 = other

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date)); // 2014/08/06 15:59:48

		String logFormat = "[" + dateFormat.format(date) + "] %m";
		String consoleFormat = "[ClashOfClans] %m";

		if (code == 1) {
			logFormat = "[" + dateFormat.format(date) + "] <INFO> %m";
			consoleFormat = "[ClashOfClans] <INFO> %m";
		} else if (code == 2) {
			logFormat = "[" + dateFormat.format(date) + "] <ERROR> %m";
			consoleFormat = "[ClashOfClans] <ERROR> %m";
		} else if (code == 3) {
			logFormat = "[" + dateFormat.format(date) + "] <COMMAND> %m";
			consoleFormat = "[ClashOfClans] <COMMAND> %m";
		} else if (code == 4) {
			logFormat = "[" + dateFormat.format(date) + "] <GAME> %m";
			consoleFormat = "[ClashOfClans] <GAME> %m";
		} else if (code == 5) {
			logFormat = "[" + dateFormat.format(date) + "] <LOBBY> %m";
			consoleFormat = "[ClashOfClans] <LOBBY> %m";
		} else if (code == 6) {
			logFormat = "[" + dateFormat.format(date) + "] <WARNING> %m";
			consoleFormat = "[ClashOfClans] <WARNING> %m";
		} else if (code == 99) {
			logFormat = "[" + dateFormat.format(date) + "] <OTHER> %m";
			consoleFormat = "[ClashOfClans] <OTHER> %m";
		} else {
			logFormat = "[" + dateFormat.format(date) + "] %m";
			consoleFormat = "%m";
		}

		String logMessage = logFormat.replaceAll("%m", msg);
		String consoleMessage = consoleFormat.replaceAll("%m", msg);

		if (console) {
			System.out.print(consoleMessage);
		}
		boolean temp = false;
		if (log && temp) {
			try {
				String data = logMessage + "\n";

				File file = new File(pluginStoreFilePath + logFileName);

				// if file doesnt exists, then create it
				if (!file.exists()) {
					file.mkdir();
					file.createNewFile();
				}

				// true = append file
				FileWriter fileWritter = new FileWriter(file.getName(), true);
				BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
				bufferWritter.write(data);
				bufferWritter.close();

				// System.out.println("Done");

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
