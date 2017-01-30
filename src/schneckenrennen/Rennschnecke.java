package schneckenrennen;


public class Rennschnecke {

    protected int maxSpeed, progress;
    protected String name, race;
    protected boolean hasWon;

    public Rennschnecke(int maxSpeed, String name, String race) {
        this.maxSpeed = maxSpeed;
        this.name = name;
        this.race = race;
        this.progress = 0;
        this.hasWon = false;
    }

    public void creep() {
        progress += RaceFrame.Random.nextInt(maxSpeed) + 1;
    }

    public void win(int goal) {
        progress = goal;
        hasWon = true;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public int getProgress() {
        return progress;
    }

    public String getName() {
        return name;
    }

    public String getRace() {
        return race;
    }

    public boolean isWinner() {
        return hasWon;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Name: ").append(getName()).append("; ");
        builder.append("Rasse: ").append(getRace()).append("; ");
        builder.append("Fortschritt: ").append(getProgress());
        return builder.toString();
    }
    
    public String toBetString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Name: ").append(getName()).append("; ");
        builder.append("Rasse: ").append(getRace());
        return builder.toString();
    }
}
