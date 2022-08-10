package engine.tools;

public class SingleClientEntry {

    private final String username;
    private final long time;
    private final int time_unit;

    public SingleClientEntry(String username, int timeUnit) {
        this.username = username;
        this.time = System.currentTimeMillis();
        this.time_unit = timeUnit;

    }


    public long getTime() {
        return time;
    }

    public long getTimeUnit() {
        return time_unit;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return (username != null ? username + ": " : "");
    }
}
