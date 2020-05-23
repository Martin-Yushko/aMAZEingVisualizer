package util;

// Represents a data model which is a 2-tuple, useful for passing data of several data types
public class Pair<X, Y> {
    public final X first;
    public final Y second;

    //EFFECTS: constructs a Pair whose first element is x, and whose second element is y
    public Pair(X x, Y y) {
        this.first = x;
        this.second = y;
    }

    @Override
    public boolean equals(Object o) {
        //modeled after: https://stackoverflow.com/questions/30855198/setting-objects-equal-to-eachother-java
//        if (! (o instanceof Pair)) {
//            return false; //Pair cannot be equal to an object which is not a pair
//        }

        Pair otherPair = (Pair) o;
        return this.first == otherPair.first && this.second == otherPair.second;
    }
}
