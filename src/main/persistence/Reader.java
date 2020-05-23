package persistence;

import model.Maze;
import model.Tile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//code and idea taken from TellerApp from CPSC210
// A reader that can read account data from a file
public class Reader {
    public static final String DELIMITER = ",";

    // EFFECTS: returns a maze parsed from given file; throws
    // IOException if an exception is raised when opening / reading from file
    public static Maze readMaze(File file) throws IOException {
        List<String> fileContent = readFile(file);
        return parseContent(splitString(fileContent.get(0)));
    }

    // EFFECTS: returns content of file as a list of strings, each string
    // containing the content of one row of the file
    private static List<String> readFile(File file) throws IOException {
        return Files.readAllLines(file.toPath());
    }

    // EFFECTS: returns a maze object parsed from list of strings
    private static Maze parseContent(List<String> fileContent) {
        int x = Integer.parseInt(fileContent.get(0));
        int y = Integer.parseInt(fileContent.get(1));
        ArrayList<Tile.TileType> tileTypes = toTileTypes(fileContent, x, y);

        return new Maze(tileTypes, x, y);
    }

    // EFFECTS: returns a list of strings obtained by splitting line on DELIMITER
    private static ArrayList<String> splitString(String line) {
        String[] splits = line.split(DELIMITER);
        return new ArrayList<>(Arrays.asList(splits));
    }

    //EFFECTS: returns list of tile types obtained by a list of
    //         strings of the associated enum name
    private static ArrayList<Tile.TileType> toTileTypes(List<String> strings, int x, int y) {
        ArrayList<Tile.TileType> tileTypes = new ArrayList<>(x * y);
        for (int i = 2; i < strings.size(); i++) {
            tileTypes.add(toTileType(strings.get(i)));
        }

        return tileTypes;
    }

    //EFFECTS: converts name of enum of the enum object itself
    private static Tile.TileType toTileType(String string) {
        return Tile.TileType.valueOf(string);
    }
}