package ca.umanitoba.cs.zaheerd.persistence;

import ca.umanitoba.cs.zaheerd.model.Tracker;

public interface TrackerPersistence {

    Tracker saveTracker(Tracker tracker) throws PersistenceException;

    Tracker loadTracker() throws NotFoundException, PersistenceException;
}