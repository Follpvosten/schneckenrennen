package schneckenrennen;

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
        builder.append("Name: ").append(getName()).append("; ");
        builder.append("Rasse: ").append(getRace()).append("; ");
        builder.append("Fortschritt: ").append(getProgress());
        return builder.toString();
    }
    
    /**
     * Returns the snail's info as a String to be displayed in the WettDialog.
     * @return 
     */
    public String toBetString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Name: ").append(getName()).append("; ");
        builder.append("Rasse: ").append(getRace());
        return builder.toString();
    }
}
