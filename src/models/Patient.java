package models;

public class Patient {
    private final int id;

    public Patient(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "P" + id;
    }
}
