package models;

public class Doctor {
    private final int id;

    public Doctor(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Doctor-" + id;
    }
}
