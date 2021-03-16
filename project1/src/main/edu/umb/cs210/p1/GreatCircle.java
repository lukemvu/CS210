package edu.umb.cs210.p1;

import stdlib.StdOut;

public class GreatCircle {
    // calculates the great circle distance given two sets of coordinates
    protected static double calculateGreatCircleDistance(String[] args) {
        // Get angles lat1, lon1, lat2, and lon2 from command line as
        // doubles.
        double lat1 = Double.parseDouble(args[0]);
        double lon1 = Double.parseDouble(args[1]);
        double lat2 = Double.parseDouble(args[2]);
        double lon2 = Double.parseDouble(args[3]);

        // Convert the angles to radians.
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        // Calculate great-circle distance d.
        double a = Math.sin(lat1) * Math.sin(lat2);
        double b = Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2);
        double c = 111.0 * Math.acos(a + b);
        double d = Math.toDegrees(c);

        // Return d.
        return d;
    }

    // Entry point. [DO NOT EDIT]
    public static void main(String[] args) {
        StdOut.println(GreatCircle.calculateGreatCircleDistance(args));
    }
}
