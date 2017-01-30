package schneckenrennen;

import java.util.ArrayList;
import java.util.Comparator;


public class Rennen {

    private final String name;
    private final int goalDistance;
    private boolean raceRunning = false;
    private boolean raceEnded = false;

    private final ArrayList<Rennschnecke> snails;

    public Rennen(String name, int goal) {
        this.name = name;
        goalDistance = goal;
        snails = new ArrayList<>();
    }

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
        if(raceEnded) {
            snails.sort(new Comparator<Rennschnecke>() {
                @Override
                public int compare(Rennschnecke t, Rennschnecke t1) {
                    return Integer.compare(t1.getProgress(), t.getProgress());
                }
            });
        }
    }

    public boolean isRunning() {
        return raceRunning;
    }
    
    public boolean hasEnded() {
        return raceEnded;
    }

    public void start() {
        raceRunning = true;
    }

    public void stop() {
        raceRunning = false;
    }

    public void addRennschnecke(Rennschnecke schnegge) {
        snails.add(schnegge);
    }

    public void removeRennschnecke(String name) {
        for (Rennschnecke schnegge : snails) {
            if (schnegge.getName().equals(name)) {
                snails.remove(schnegge);
            }
        }
    }

    public Rennschnecke[] getSchneckenArray() {
        return snails.toArray(new Rennschnecke[snails.size()]);
    }
    
//    public ArrayList<Rennschnecke> getWinners() {
//        ArrayList<Rennschnecke> winners = new ArrayList<>();
//        snails.forEach((Rennschnecke schnegge) -> { if(schnegge.isWinner()) winners.add(schnegge); });
//        return winners;
//    }

    public int getSchneckenCount() {
        return snails.size();
    }

    public String getName() {
        return name;
    }

    public int getGoal() {
        return goalDistance;
    }
}
