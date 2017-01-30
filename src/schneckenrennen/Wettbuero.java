package schneckenrennen;

import java.util.ArrayList;


public class Wettbuero {

    public static class Wette {

        public double einsatz;
        public String spielerName;
        public Rennschnecke schnecke;

        public Wette(double einsatz, String spieler, Rennschnecke schnecke) {
            this.einsatz = einsatz;
            this.spielerName = spieler;
            this.schnecke = schnecke;
        }
    }

    private ArrayList<Wette> wetten;
    private Rennen rennen;
    private final double factor;

    public Wettbuero(double factor) {
        this.factor = factor;
    }

    public void assignNewRennen(Rennen newRennen) {
        wetten = new ArrayList<>();
        rennen = newRennen;
    }

    public void placeBet(double betrag, String spielerName, Rennschnecke schnecke) {
        wetten.add(new Wette(betrag, spielerName, schnecke));
    }
    
    public void placeBet(Wette wette) {
        wetten.add(wette);
    }

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
}
