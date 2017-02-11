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
 * A class responsible for loading the snail names, snail race names and general
 * race names ("the values") from config files.
 *
 * @author Follpvosten
 */
public final class ConfigManager {

    /**
     * The default config file path.
     */
    public static final String DEFAULT_CONFIG = "./config.json";

    /**
     * The key used for the double value of the WettBuero's factor.
     */
    public static final String BETFACTOR_KEY = "betfactor";
    /**
     * The key used for the JSON array of snail names.
     */
    public static final String SNAILNAMES_KEY = "snailnames";
    /**
     * The key used for the JSON array of snail races.
     */
    public static final String SNAILRACES_KEY = "snailraces";
    /**
     * The key used for the JSON array of race names.
     */
    public static final String RACENAMES_KEY = "racenames";

    // Data loaded from the config file
    private static ArrayList<String> snailNames, snailRaces, raceNames;
    private static double betFactor;

    /**
     * Exception to be thrown when there are less than 4 snail names
     */
    private static class NotEnoughNamesException extends Exception {}

    /**
     * Loads values from the default config file. In case of an error, the
     * default config values will be generated saved to the default config file,
     * if it doesn't already exist.
     */
    public static void loadDefaultConfigFile() {
	try {
	    loadConfigFile(DEFAULT_CONFIG);
	} catch (FileNotFoundException ex) {
	    Logger.getLogger(ConfigManager.class.getName())
		    .log(Level.INFO, TranslationManager.getTranslation("Config.error.fileNotFound"), ex);
	    applyDefaults();
	} catch (IOException ex) {
	    Logger.getLogger(ConfigManager.class.getName())
		    .log(Level.SEVERE, TranslationManager.getTranslation("Config.error.ioError"), ex);
	    applyDefaults();
	} catch (JSONException ex) {
	    Logger.getLogger(ConfigManager.class.getName())
		    .log(Level.SEVERE, TranslationManager.getTranslation("Config.error.jsonInvalid"), ex);
	    applyDefaults();
	} catch (NotEnoughNamesException ex) {
	    Logger.getLogger(ConfigManager.class.getName())
		    .log(Level.SEVERE, TranslationManager.getTranslation("Config.error.notEnoughNames"), ex);
	    applyDefaults();
	}
    }

    /**
     * Loads values from the specified config file.
     * @param filename The path of the config file to get settings from.
     * @return An empty String if everything went well, errors otherwise
     */
    public static String loadSpecificConfigFile(String filename) {
	String result = "";
	try {
	    loadConfigFile(filename);
	} catch (FileNotFoundException ex) {
	    String message = TranslationManager.getTranslation("Config.error.selectedFileNotFound");
	    Logger.getLogger(ConfigManager.class.getName())
		    .log(Level.INFO, message, ex);
	    result = message;
	} catch (IOException ex) {
	    String message = TranslationManager.getTranslation("Config.error.ioError");
	    Logger.getLogger(ConfigManager.class.getName()).log(Level.SEVERE, message, ex);
	    result = message;
	} catch(JSONException ex) {
	    String message = TranslationManager.getTranslation("Config.error.jsonInvalid");
	    Logger.getLogger(ConfigManager.class.getName()).log(Level.SEVERE, message, ex);
	    result = message;
	    loadDefaultConfigFile();
	} catch (NotEnoughNamesException ex) {
	    String message = TranslationManager.getTranslation("Config.error.notEnoughNames");
	    Logger.getLogger(ConfigManager.class.getName()).log(Level.SEVERE, message, ex);
	    result = message;
	}
	return result;
    }

    /**
     * Loads the values from the given config file. Does not make any checks and
     * throws Exceptions for every error.
     * @param filename The name of the file to be loaded
     * @throws java.io.FileNotFoundException When the file does not exist
     * @throws java.io.IOException When the file can't be read for some reason
     * @throws schneckenrennen.ConfigManager.NotEnoughNamesException
     */
    public static void loadConfigFile(String filename)
	    throws FileNotFoundException, IOException,
		   JSONException, NotEnoughNamesException {
	File file = new File(filename);
	FileInputStream fis = new FileInputStream(file);
	byte[] data = new byte[(int) file.length()];
	fis.read(data);
	fis.close();
	loadConfigJSON(new String(data, "UTF-8"));
    }

    /**
     * Generates the default config values and saves them to the config file if
     * it doesn't exist.
     */
    private static void applyDefaults() {
	betFactor = 1.5;
	snailNames = new ArrayList<>(Arrays.asList(
		"Winfried", "Bertha", "Hannibal", "Hüftgelenk",
		"Goliath", "Moses", "Petrus", "Nikolaus",
		"Python", "Baumhardt", "Felix", "Osiris",
		"Waldi", "Luna", "Schnurri", "Sushi",
		"Deidre", "Wolfi", "Blümchen", "Bo-bobo",
		"Günther", "Horst-Dieter", "Kevin", "Moritz",
		"Anthony", "Buchwirt", "Linda", "Eckhardt"
	));
	raceNames = new ArrayList<>(Arrays.asList(
		"Rennen des Jahres", "Irgendein Rennen", "Rennen Nummer Drei",
		"Das große Kriechen", "Grand Kriech", "Marathon",
		"Entenlauf", "Schneckenrennen", "Wettkriechen",
		"Aus die Schneck' - Der letzte Tag", "Wanderschnecken auf Eilkurs",
		"Ein puitziges Rennen"
	));
	snailRaces = new ArrayList<>(Arrays.asList(
		"Audi", "LKW", "Airbus", "Schäferhund",
		"Bernhardiner", "Dackel", "Perser", "Siam",
		"Chihuahua", "Pago-Pago", "Moai", "Baum",
		"Kaninchen", "Hase", "Pui", "Mäuschen",
		"Pudel", "Affenbrotbaum", "Goldfisch", "U-Boot",
		"Bim", "Tram", "Zug", "Kreuzfahrtschiff",
		"UFO", "Banane", "Meerschweinchen", "Treppenstufe",
		"Schienenersatzverkehr", "Wanderer", "Schwimmer", "Knirps",
		"Tänzerin", "Aromalady"
	));

	if (!new File(DEFAULT_CONFIG).exists()) {
	    saveConfig();
	}
    }

    /**
     * Currently only used to save the default config.
     */
    public static void saveConfig() {
	try {
	    JSONObject configJSON = new JSONObject();
	    configJSON.put(BETFACTOR_KEY, betFactor);

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
     *
     * @param jsonString The JSON string from the config file
     */
    private static void loadConfigJSON(String jsonString)
	    throws JSONException, NotEnoughNamesException {
	JSONObject json = new JSONObject(jsonString);
	betFactor = json.optDouble(BETFACTOR_KEY, 1.5);
	{
	    snailNames = new ArrayList<>();
	    JSONArray namesJsonArray = json.getJSONArray(SNAILNAMES_KEY);
	    if (namesJsonArray.length() < 4) {
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
    }

    /**
     * Returns the currently loaded snail names.
     * @return 
     */
    public static ArrayList<String> getSnailNames() {
	return snailNames;
    }

    /**
     * Returns a random race from the currently loaded list of snail races
     * @param random The {@link Random} object to be used.
     * @return 
     */
    public static String getRandomSnailRace(Random random) {
	return snailRaces.get(random.nextInt(snailRaces.size()));
    }

    /**
     * Returns a random name from the currently loaded list of race names
     * @param random The {@link Random} object to be used.
     * @return 
     */
    public static String getRandomRaceName(Random random) {
	return raceNames.get(random.nextInt(raceNames.size()));
    }

    /**
     * Returns the currently loaded bet factor for the Wettbuero.
     * @return 
     */
    public static double getBetFactor() {
	return betFactor;
    }
}
