package ca.umanitoba.cs.zaheerd;

import ca.umanitoba.cs.zaheerd.persistence.TrackerPersistence;
import ca.umanitoba.cs.zaheerd.persistence.json.TrackerPersistenceJson;
import ca.umanitoba.cs.zaheerd.ui.TrackerDisplay;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        Path storageFile = Paths.get("tracker.json");
        TrackerPersistence persistence = new TrackerPersistenceJson(storageFile);

        TrackerDisplay display = new TrackerDisplay(persistence);
        display.run();
    }
}