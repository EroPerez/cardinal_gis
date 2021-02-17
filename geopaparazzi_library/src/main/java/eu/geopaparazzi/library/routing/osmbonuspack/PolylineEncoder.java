/*
 * Geopaparazzi - Digital field mapping on Android based devices
 * Copyright (C) 2016  HydroloGIS (www.hydrologis.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.geopaparazzi.library.routing.osmbonuspack;

import java.util.ArrayList;

/**
 * Methods to encode and decode a polyline with Google polyline encoding/decoding scheme.
 *
 * @see <a href="https://developers.google.com/maps/documentation/utilities/polylinealgorithm">Google polyline algorithm</a>
 */
public class PolylineEncoder {

    private static StringBuffer encodeSignedNumber(int num) {
        int sgn_num = num << 1;
        if (num < 0) {
            sgn_num = ~(sgn_num);
        }
        return (encodeNumber(sgn_num));
    }

    private static StringBuffer encodeNumber(int num) {
        StringBuffer encodeString = new StringBuffer();
        while (num >= 0x20) {
            int nextValue = (0x20 | (num & 0x1f)) + 63;
            encodeString.append((char) (nextValue));
            num >>= 5;
        }
        num += 63;
        encodeString.append((char) (num));
        return encodeString;
    }

    /**
     * Encode a polyline with Google polyline encoding method
     *
     * @param polyline  the polyline
     * @param precision 1 for a 6 digits encoding, 10 for a 5 digits encoding.
     * @return the encoded polyline, as a String
     */
    public static String encode(ArrayList<GeoPoint> polyline, int precision) {
        StringBuffer encodedPoints = new StringBuffer();
        int prev_lat = 0, prev_lng = 0;
        for (GeoPoint trackpoint : polyline) {
            int lat = trackpoint.getLatitudeE6() / precision;
            int lng = trackpoint.getLongitudeE6() / precision;
            encodedPoints.append(encodeSignedNumber(lat - prev_lat));
            encodedPoints.append(encodeSignedNumber(lng - prev_lng));
            prev_lat = lat;
            prev_lng = lng;
        }
        return encodedPoints.toString();
    }

    /**
     * Decode a "Google-encoded" polyline
     *
     * @param encodedString
     * @param precision     1 for a 6 digits encoding of lat and lon, 10 for a 5 digits encoding.
     * @param hasAltitude   if the polyline also contains altitude (GraphHopper routes, with altitude in cm).
     * @return the polyline.
     */
    public static ArrayList<GeoPoint> decode(String encodedString, int precision, boolean hasAltitude) {
        int index = 0;
        int len = encodedString.length();
        int lat = 0, lng = 0, alt = 0;
        ArrayList<GeoPoint> polyline = new ArrayList<>(len / 3);
        //capacity estimate: polyline size is roughly 1/3 of string length for a 5digits encoding, 1/5 for 10digits.

        while (index < len) {
            int b, shift, result;
            shift = result = 0;
            do {
                b = encodedString.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = result = 0;
            do {
                b = encodedString.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            if (hasAltitude) {
                shift = result = 0;
                do {
                    b = encodedString.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dalt = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                alt += dalt;
            }

            GeoPoint p = new GeoPoint(lat * precision, lng * precision, alt / 100);
            polyline.add(p);
        }

        //Log.d("BONUSPACK", "decode:string="+len+" points="+polyline.size());

        return polyline;
    }
}
