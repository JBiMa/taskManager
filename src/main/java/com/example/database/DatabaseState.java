package com.example.database;

import java.io.*;

public class DatabaseState {
    private static DatabaseState instance;
    private static final String FILE_NAME = "databaseState.ser";

    private DatabaseState() {}

    public static synchronized DatabaseState getInstance() {
        if (instance == null) {
            instance = new DatabaseState();
        }
        return instance;
    }

    public boolean exists() {
        File file = new File(FILE_NAME);
        return file.exists();
    }

    public void saveState(DatabaseMOCK database) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(database);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DatabaseMOCK loadState() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("databaseState.ser"))) {
            Memento memento = (Memento) ois.readObject();  // Cast to Memento
            DatabaseMOCK database = new DatabaseMOCK();
            database.restoreFromMemento(memento);
            return database;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}