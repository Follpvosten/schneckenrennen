package schneckenrennen;

import java.util.ArrayList;

/**
 * A class representing a snail race. It holds a list of the snails that are
 * currently participating and provides the needed functions to let them
 * progress in the race.
 *
 * @author Follpvosten
 */
public class Rennen {

    /**
     * The name of the race. Is usually selected randomly from a list in the
     * used config file.
     */
    private final String name;
    /**
     * The distance a snail has to creep in order to win.
     */
    private final int goalDistance;
    /**
     * Indicates if the races is currently running.
     */
    private boolean raceRunning = false;
    /**
     * Indicates if the race has already ended. A race can't be continued if it
     * has ended completely.
     */
    private boolean raceEnded = false;

    /**
     * The list of snails participating in the race.
     */
    private final ArrayList<Rennschnecke> snails;

    /**
     * Creates a new snail race.
     *
     * @param name The name of the race.
     * @param goal The goal of the race.
     */
    public Rennen(String name, int goal) {
	this.name = name;
	goalDistance = goal;
	snails = new ArrayList<>();
    }

    /**
     * Lets all of the snails creep forward as far as they can. If the race ends
     * during the execution of this method, the snails will be sorted so the
     * winner is the first one on the list. Does nothing if the race isn't
     * running.
     */
    public void progress() {
	if (!raceRunning) {
	    return;
	}
	for (Rennschnecke schnegge : snails) {
	    schnegge.creep();
	    if (schnegge.getProgress() >= goalDistance) {
		schnegge.win(goalDistance);
		raceRunning = false;
		raceEnded = true;
	    }
	}
	if (raceEnded) {
	    snails.sort((Rennschnecke t, Rennschnecke t1) -> Integer.compare(t1.getProgress(), t.getProgress()));
	}
    }

    /**
     * Returns if the race is currently running.
     *
     * @return The value indicating if the race is currently running.
     */
    public boolean isRunning() {
	return raceRunning;
    }

    /**
     * Returns if the race has already ended. A race that has ended can't be
     * started or continued further.
     *
     * @return
     */
    public boolean hasEnded() {
	return raceEnded;
    }

    /**
     * Starts the race so snails can make progress.
     */
    public void start() {
	raceRunning = true;
    }

    /**
     * Stops the race and makes it impossible for snails to move forward.
     */
    public void stop() {
	raceRunning = false;
    }

    /**
     * Adds the given snail to the list of participants.
     *
     * @param schnegge The snail to participate in the race.
     */
    public void addRennschnecke(Rennschnecke schnegge) {
	snails.add(schnegge);
    }

    /**
     * Removes the snail with the given name from the list of participants.
     *
     * @param name The name of the snail to take out of the race.
     */
    public void removeRennschnecke(String name) {
	for (Rennschnecke schnegge : snails) {
	    if (schnegge.getName().equals(name)) {
		snails.remove(schnegge);
	    }
	}
    }

    /**
     * Returns the list of participating snails as an array.
     *
     * @return
     */
    public Rennschnecke[] getSchneckenArray() {
	return snails.toArray(new Rennschnecke[snails.size()]);
    }

    /**
     * Returns the number of snails currently participating.
     *
     * @return
     */
    public int getSchneckenCount() {
	return snails.size();
    }

    /**
     * Returns the name of the race.
     *
     * @return
     */
    public String getName() {
	return name;
    }

    /**
     * Returns the goal of the race.
     *
     * @return
     */
    public int getGoal() {
	return goalDistance;
    }
}
