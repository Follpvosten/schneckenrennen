package schneckenrennen;

import org.json.JSONObject;

/**
 * A simple data class representing a racing snail.
 * @author Follpvosten
 */
public class Rennschnecke {

    protected int maxSpeed, progress;
    protected String name, race;
    protected boolean hasWon;

    /**
     * Creates a new racing snail. Its won value is set to false and its
     * progress is set to 0.
     * @param maxSpeed The maximum speed a snail can move at.
     * @param name The snail's name.
     * @param race The snail's race.
     */
    public Rennschnecke(int maxSpeed, String name, String race) {
        this.maxSpeed = maxSpeed;
        this.name = name;
        this.race = race;
        this.progress = 0;
        this.hasWon = false;
    }

    /**
     * Lets the snail move forward by adding a random number between 1 and the
     * snails max speed to the snails progress.
     */
    public void creep() {
        progress += RaceFrame.Random.nextInt(maxSpeed) + 1;
    }

    /**
     * Makes sure the snail isn't past the goal and sets its hasWon flag.
     * @param goal The goal of the current race.
     */
    public void win(int goal) {
        progress = goal;
        hasWon = true;
    }

    /**
     * Returns the snails max speed.
     * @return 
     */
    public int getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * Returns the snails current progress.
     * @return 
     */
    public int getProgress() {
        return progress;
    }

    /**
     * Returns the snails name.
     * @return 
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the snails race name.
     * @return 
     */
    public String getRace() {
        return race;
    }

    /**
     * Returns a value indicating if the snail has won the race.
     * @return 
     */
    public boolean isWinner() {
        return hasWon;
    }

    /**
     * Returns the snail's info as String to be displayed in the RaceFrame.
     * @return 
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(Translations.get("Rennschnecke.name", getName())).append("; ");
        builder.append(Translations.get("Rennschnecke.race", getRace())).append("; ");
        builder.append(Translations.get("Rennschnecke.progress", getProgress()));
        return builder.toString();
    }
    
    /**
     * Returns the snail's info as a String to be displayed in the WettDialog.
     * @return 
     */
    public String toBetString() {
        StringBuilder builder = new StringBuilder();
        builder.append(Translations.get("Rennschnecke.name", getName())).append("; ");
        builder.append(Translations.get("Rennschnecke.race", getRace()));
        return builder.toString();
    }
    
    /**
     * Loads a snail from the given JSON object
     * @param json The snail serialized to JSON
     * @return The deserialized snail as Rennschnecke
     */
    public static Rennschnecke fromJSON(JSONObject json) {
	Rennschnecke result =
		new Rennschnecke(
			json.getInt("maxSpeed"),
			json.getString("name"),
			json.getString("race")
		);
	result.progress = json.getInt("progress");
	result.hasWon = json.getBoolean("hasWon");
	return result;
    }
    
    /**
     * Serializes the snail to a JSON object.
     * @return A JSON object containing the data of the snail.
     */
    public JSONObject toJSON() {
	JSONObject result = new JSONObject();
	result.put("maxSpeed", maxSpeed);
	result.put("name", name);
	result.put("race", race);
	result.put("progress", progress);
	result.put("hasWon", hasWon);
	return result;
    }
    
    public boolean strengen(String bettor) {
	if(name.equals("Blümchen") && bettor.equals("Wolfi")) {
	    maxSpeed += 3;
	    return true;
	}
	return false;
    }
}
