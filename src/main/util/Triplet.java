package util;

// Represents a data model which is a 3-tuple, useful for passing data of several data types
public class Triplet<X, Y, Z> {
    public final X first;
    public final Y second;
    public final Z third;

    //EFFECTS: constructs a Triplet whose first element is x, second element is y, and third element is z
    public Triplet(X x, Y y, Z z) {
        this.first = x;
        this.second = y;
        this.third = z;
    }

    @Override
    public boolean equals(Object o) {
        //modeled after: https://stackoverflow.com/questions/30855198/setting-objects-equal-to-eachother-java
//        if (! (o instanceof Triplet)) {
//            return false; //Triplet cannot be equal to an object which is not a triplet
//        }

        Triplet otherTriplet = (Triplet) o;
        return (this.first == otherTriplet.first && this.second == otherTriplet.second
                && this.third == otherTriplet.third);
    }
}
