package schneckenrennen;

import java.util.ArrayList;

/**
 * A class for taking bets, multiplying the money by a given factor and giving
 * the money to the winners of the bets.
 * @author Follpvosten
 */
public class Wettbuero {

    /**
     * A class representing a simple bet.
     */
    public static class Wette {

	/**
	 * The bet.
	 */
        public double einsatz;
	/**
	 * The bettor's name.
	 */
        public String spielerName;
	/**
	 * The snail the bet it on.
	 */
        public Rennschnecke schnecke;

	/**
	 * Creates a new bet.
	 * @param einsatz The bet.
	 * @param spieler The bettor's name.
	 * @param schnecke The snail the bet is on.
	 */
        public Wette(double einsatz, String spieler, Rennschnecke schnecke) {
            this.einsatz = einsatz;
            this.spielerName = spieler;
            this.schnecke = schnecke;
        }
    }

    /**
     * A list of the currently placed bets.
     */
    private ArrayList<Wette> wetten;
    /**
     * The race this Wettbuero is supervising.
     */
    private Rennen rennen;
    /**
     * The factor the winner's bets are multiplied with.
     */
    private final double factor;

    /**
     * Creates a new Wettbuero.
     * @param factor The factor the winner's bets will be multiplied with.
     */
    public Wettbuero(double factor) {
        this.factor = factor;
    }

    /**
     * Initiates the Wettbuero for a new race.
     * @param newRennen The race to take bets for.
     */
    public void assignNewRennen(Rennen newRennen) {
        wetten = new ArrayList<>();
        rennen = newRennen;
    }

    /**
     * Places a new bet.
     * @param betrag The bet.
     * @param spielerName The bettor's name.
     * @param schnecke The snail the bet is on.
     */
    public void placeBet(double betrag, String spielerName, Rennschnecke schnecke) {
        wetten.add(new Wette(betrag, spielerName, schnecke));
    }
    
    /**
     * Adds a new bet to the list of placed bets.
     * @param wette The bet to be placed.
     */
    public void placeBet(Wette wette) {
        wetten.add(wette);
    }

    /**
     * Creates a single String to display the outcome of the supervised race,
     * including the placing of all the snails and the bets that were won.
     * @return The generated String.
     */
    public String generateOutcome() {
        if (!rennen.hasEnded()) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        Rennschnecke[] schneggen = rennen.getSchneckenArray();
        for (int i = 0; i < schneggen.length; i++) {
            int place = schneggen[i].isWinner() ? 1 : i+1;
            builder.append(place).append(".: ").append(schneggen[i].toString());
            if (schneggen[i].isWinner()) {
                for (Wette wette : wetten) {
                    if (wette.schnecke == schneggen[i]) {
                        builder.append(" (").append(wette.spielerName).append(" gewinnt ");
                        builder.append(String.format("%.2f", wette.einsatz * factor)).append("€)");
                    }
                }
            }
            builder.append("\n");
        }
        StringBuilder loserBuilder = new StringBuilder("\n(Verlierer:");
        int loserCount = 0;
        for (Wette wette : wetten) {
            if (!wette.schnecke.isWinner()) {
                loserBuilder.append(" ").append(wette.spielerName).append(" (");
                loserBuilder.append(String.format("%.2f", wette.einsatz)).append("€);");
                loserCount++;
            }
        }
        loserBuilder.append(")");
        if(loserCount > 0) builder.append(loserBuilder);

        return builder.toString();
    }

    /**
     * Returns the factor the winner's bet is multiplied with.
     * @return 
     */
    public double getFactor() {
	return factor;
    }
}
