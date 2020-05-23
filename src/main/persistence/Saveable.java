package persistence;

import java.io.PrintWriter;

//code taken from TellerApp from CPSC210
// Represents data that can be saved to file
public interface Saveable {
    // MODIFIES: printWriter
    // EFFECTS: writes the saveable to printWriter
    void save(PrintWriter printWriter);
}