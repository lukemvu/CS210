package edu.umb.cs210.p3;

import stdlib.StdOut;

import java.util.Arrays;

// An immutable type representing a location on Earth.
public class Location implements Comparable<Location> {
    private final String loc; // location name
    private final double lat; // latitude
    private final double lon; // longitude

    // Construct a new location given its name, latitude, and
    // longitude.
    public Location(String loc, double lat, double lon) {
        this.loc = loc;
        this.lat = lat;
        this.lon = lon;
    }

    // The great-circle distance between this location and that.
    public double distanceTo(Location that) {
        // Convert lat/lon to radians
        double lat1 = Math.toRadians(this.lat);
        double lon1 = Math.toRadians(this.lon);
        double lat2 = Math.toRadians(that.lat);
        double lon2 = Math.toRadians(that.lon);
        // Distance formula as radians
        double a = Math.sin(lat1) * Math.sin(lat2);
        double b = Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2);
        double c = 111 * Math.toDegrees(Math.acos(a + b));
        // Return distance
        return c;
    }

    // Is this location the same as that?
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null) return false;
        if (this.getClass() != that.getClass()) return false;
        Location thatLocation = (Location) that;
        // Compare lat and long of two Locations
        return this.lat == thatLocation.lat && this.lon == thatLocation.lon;
    }

    // -1, 0, or 1 depending on whether the distance of this 
    // location to the origin (Parthenon, Athens, Greece @
    // 37.971525, 23.726726) is less than, equal to, or greater
    // than the distance of that location to the origin.
    public int compareTo(Location that) {
        Location origin = new Location("Parthenon", 37.971525, 23.726726);
        if (this.distanceTo(origin) < that.distanceTo(origin)) return -1;
        if (this.distanceTo(origin) == that.distanceTo(origin)) return 0;
        if (this.distanceTo(origin) > that.distanceTo(origin)) return 1;
        return 0;
    }

    // A string representation of the location, in
    // "loc (lat, lon)" format.
    public String toString() {
        return this.loc + " (" + this.lat + ", " + this.lon + ")";
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        int rank = Integer.parseInt(args[0]);
        double lat = Double.parseDouble(args[1]);
        double lon = Double.parseDouble(args[2]);        
        Location[] wonders = new Location[7];
        wonders[0] = new Location("The Great Wall of China (China)", 
                                  40.6769, 117.2319);
        wonders[1] = new Location("Petra (Jordan)", 30.3286, 35.4419);
        wonders[2] = new Location("The Colosseum (Italy)", 41.8902, 12.4923);
        wonders[3] = new Location("Chichen Itza (Mexico)", 20.6829, -88.5686);
        wonders[4] = new Location("Machu Picchu (Peru)", -13.1633, -72.5456);
        wonders[5] = new Location("Taj Mahal (India)", 27.1750, 78.0419);
        wonders[6] = new Location("Christ the Redeemer (Brazil)",
                                  22.9519, -43.2106);
        Arrays.sort(wonders);
        for (Location wonder : wonders) {
            StdOut.println(wonder);
        }
        Location loc = new Location("", lat, lon);
        StdOut.println(wonders[rank].equals(loc));
    }
}
