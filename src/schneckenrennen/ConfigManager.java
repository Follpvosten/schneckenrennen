package schneckenrennen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A class responsible for loading the snail names, snail race names and
 * general race names ("the values") from the config file.
 * @author Follpvosten
 */
public final class ConfigManager {

    public static final String DEFAULT_CONFIG = "config.json";
    public static final String SNAILNAMES_KEY = "snailnames";
    public static final String SNAILRACES_KEY = "snailraces";
    public static final String RACENAMES_KEY = "racenames";

    private static ArrayList<String> snailNames, snailRaces, raceNames;
    
    private static class NotEnoughNamesException extends Exception {}

    /**
     * Loads the values from the saved config file, if existent.
     * If the config file is missing, a new one is generated.
     */
    public static void loadConfigFile() {
	File file = new File("./" + DEFAULT_CONFIG);
	if (file.exists()) {
	    try {
		FileInputStream fis = new FileInputStream(file);
		byte[] data = new byte[(int) file.length()];
		fis.read(data);
		fis.close();
		loadConfigJSON(new String(data, "UTF-8"));
	    } catch (FileNotFoundException ex) {
		Logger.getLogger(ConfigManager.class.getName())
			.log(Level.SEVERE, "Someone deleted a file. Like, really, really fast.", ex);
		System.out.println("Someone deleted a file. Like, really, really fast.");
		applyDefaultLists();
	    } catch (IOException ex) {
		Logger.getLogger(ConfigManager.class.getName())
			.log(Level.SEVERE, "Could not load file content for some reason.", ex);
		System.out.println("Could not load file content for some reason.");
		applyDefaultLists();
	    } catch (NotEnoughNamesException ex) {
		Logger.getLogger(ConfigManager.class.getName())
			.log(Level.SEVERE, "Not enough snail names in the config file!", ex);
		System.out.println("Not enough snail names in the config file!");
		applyDefaultLists();
	    }
	} else {
	    applyDefaultLists();
	}
    }

    /**
     * Generates the default config values and saves them to the config file.
     */
    private static void applyDefaultLists() {
	snailNames = new ArrayList<>(Arrays.asList(new String[]{
	    "Winfried", "Bertha", "Hannibal", "Hüftgelenk",
	    "Goliath", "Moses", "Petrus", "Nikolaus",
	    "Python", "Baumhardt", "Felix", "Osiris",
	    "Waldi", "Luna", "Schnurri", "Sushi",
	    "Deidre", "Wolfi", "Blümchen", "Bo-bobo",
	    "Günther", "Horst-Dieter", "Kevin", "Moritz",
	    "Anthony", "Buchwirt", "Linda", "Eckhardt"
	}));
	raceNames = new ArrayList<>(Arrays.asList(new String[]{
	    "Rennen des Jahres", "Irgendein Rennen", "Rennen Nummer Drei",
	    "Das große Kriechen", "Grand Kriech", "Marathon",
	    "Entenlauf", "Schneckenrennen", "Wettkriechen",
	    "Aus die Schneck' - Der letzte Tag", "Wanderschnecken auf Eilkurs",
	    "Ein puitziges Rennen"
	}));
	snailRaces = new ArrayList<>(Arrays.asList(new String[]{
	    "Audi", "LKW", "Airbus", "Schäferhund",
	    "Bernhardiner", "Dackel", "Perser", "Siam",
	    "Chihuahua", "Pago-Pago", "Moai", "Baum",
	    "Kaninchen", "Hase", "Pui", "Mäuschen",
	    "Pudel", "Affenbrotbaum", "Goldfisch", "U-Boot",
	    "Bim", "Tram", "Zug", "Kreuzfahrtschiff",
	    "UFO", "Banane", "Meerschweinchen", "Treppenstufe",
	    "Schienenersatzverkehr", "Wanderer", "Schwimmer", "Knirps",
	    "Tänzerin", "Aromalady"
	}));

	saveLists();
    }

    /**
     * Currently only used to save the default config.
     */
    public static void saveLists() {
	try {
	    JSONObject configJSON = new JSONObject();
	    
	    {
		JSONArray currentArray = new JSONArray();
		for (String s : snailNames) {
		    currentArray.put(s);
		}
		configJSON.put(SNAILNAMES_KEY, currentArray);

		currentArray = new JSONArray();
		for (String s : snailRaces) {
		    currentArray.put(s);
		}
		configJSON.put(SNAILRACES_KEY, currentArray);

		currentArray = new JSONArray();
		for (String s : raceNames) {
		    currentArray.put(s);
		}
		configJSON.put(RACENAMES_KEY, currentArray);
	    }
	    
	    File file = new File(DEFAULT_CONFIG);
	    FileWriter fw = new FileWriter(file, false);
	    fw.write(configJSON.toString(2));
	    fw.flush();
	    fw.close();
	} catch (JSONException ex) {
	    Logger.getLogger(ConfigManager.class.getName())
		    .log(Level.SEVERE, "Somehow failed to write JSON object.", ex);
	    System.out.println("Somehow failed to write JSON object.");
	} catch (IOException ex) {
	    Logger.getLogger(ConfigManager.class.getName())
		    .log(Level.SEVERE, "Failed to write file.", ex);
	    System.out.print("Failed to write file.");
	}
    }

    /**
     * Loads the values from a config JSON string.
     * @param jsonString The JSON string from the config file
     */
    private static void loadConfigJSON(String jsonString) throws NotEnoughNamesException {
	try {
	    JSONObject json = new JSONObject(jsonString);
	    {
		snailNames = new ArrayList<>();
		JSONArray namesJsonArray = json.getJSONArray(SNAILNAMES_KEY);
		if(namesJsonArray.length() < 4) {
		    throw new NotEnoughNamesException();
		}
		for (int i = 0; i < namesJsonArray.length(); i++) {
		    snailNames.add(namesJsonArray.getString(i));
		}
	    }
	    {
		snailRaces = new ArrayList<>();
		JSONArray racesJsonArray = json.getJSONArray(SNAILRACES_KEY);
		for (int i = 0; i < racesJsonArray.length(); i++) {
		    snailRaces.add(racesJsonArray.getString(i));
		}
	    }
	    {
		raceNames = new ArrayList<>();
		JSONArray racesJsonArray = json.getJSONArray(RACENAMES_KEY);
		for (int i = 0; i < racesJsonArray.length(); i++) {
		    raceNames.add(racesJsonArray.getString(i));
		}
	    }
	} catch (JSONException ex) {
	    Logger.getLogger(ConfigManager.class.getName())
		    .log(Level.SEVERE, "Somehow failed to read JSON object.", ex);
	    System.out.println("Somehow failed to read JSON object.");
	}
    }

    public static ArrayList<String> getSnailNames() {
	return snailNames;
    }

    public static String getRandomSnailName(Random random) {
	return snailNames.get(random.nextInt(snailNames.size()));
    }

    public static String getRandomSnailRace(Random random) {
	return snailRaces.get(random.nextInt(snailRaces.size()));
    }

    public static String getRandomRaceName(Random random) {
	return raceNames.get(random.nextInt(raceNames.size()));
    }

}
