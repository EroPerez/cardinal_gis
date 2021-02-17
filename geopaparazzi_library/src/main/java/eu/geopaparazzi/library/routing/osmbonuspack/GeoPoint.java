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

// Created by plusminus on 21:28:12 - 25.09.2008
package eu.geopaparazzi.library.routing.osmbonuspack;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @author Nicolas Gramlich
 * @author Theodore Hong
 */
public class GeoPoint implements IGeoPoint, Parcelable, Serializable, Cloneable {

    public static final int RADIUS_EARTH_METERS = 6378137; // http://en.wikipedia.org/wiki/Earth_radius#Equatorial_radius
    public static final double METERS_PER_STATUTE_MILE = 1609.344; // http://en.wikipedia.org/wiki/Mile
    public static final double METERS_PER_NAUTICAL_MILE = 1852; // http://en.wikipedia.org/wiki/Nautical_mile
    public static final double FEET_PER_METER = 3.2808399; // http://en.wikipedia.org/wiki/Feet_%28unit_of_length%29
    public static final int EQUATORCIRCUMFENCE = (int) (2 * Math.PI * RADIUS_EARTH_METERS);
    public static final float DEG2RAD = (float) (Math.PI / 180.0);
    public static final float RAD2DEG = (float) (180.0 / Math.PI);

    public static final float PI = (float) Math.PI;
    public static final float PI_2 = PI / 2.0f;
    public static final float PI_4 = PI / 4.0f;

    // ===========================================================
    // Constants
    // ===========================================================

    static final long serialVersionUID = 1L;

    // ===========================================================
    // Fields
    // ===========================================================

    private int mLongitudeE6;
    private int mLatitudeE6;
    private int mAltitude;

    // ===========================================================
    // Constructors
    // ===========================================================

    public GeoPoint(final int aLatitudeE6, final int aLongitudeE6) {
        this.mLatitudeE6 = aLatitudeE6;
        this.mLongitudeE6 = aLongitudeE6;
    }

    public GeoPoint(final int aLatitudeE6, final int aLongitudeE6, final int aAltitude) {
        this.mLatitudeE6 = aLatitudeE6;
        this.mLongitudeE6 = aLongitudeE6;
        this.mAltitude = aAltitude;
    }

    public GeoPoint(final double aLatitude, final double aLongitude) {
        this.mLatitudeE6 = (int) (aLatitude * 1E6);
        this.mLongitudeE6 = (int) (aLongitude * 1E6);
    }

    public GeoPoint(final double aLatitude, final double aLongitude, final double aAltitude) {
        this.mLatitudeE6 = (int) (aLatitude * 1E6);
        this.mLongitudeE6 = (int) (aLongitude * 1E6);
        this.mAltitude = (int) aAltitude;
    }

    public GeoPoint(final Location aLocation) {
        this(aLocation.getLatitude(), aLocation.getLongitude(), aLocation.getAltitude());
    }

    public GeoPoint(final GeoPoint aGeopoint) {
        this.mLatitudeE6 = aGeopoint.mLatitudeE6;
        this.mLongitudeE6 = aGeopoint.mLongitudeE6;
        this.mAltitude = aGeopoint.mAltitude;
    }

    public static GeoPoint fromDoubleString(final String s, final char spacer) {
        final int spacerPos1 = s.indexOf(spacer);
        final int spacerPos2 = s.indexOf(spacer, spacerPos1 + 1);

        if (spacerPos2 == -1) {
            return new GeoPoint(
                    (int) (Double.parseDouble(s.substring(0, spacerPos1)) * 1E6),
                    (int) (Double.parseDouble(s.substring(spacerPos1 + 1)) * 1E6));
        } else {
            return new GeoPoint(
                    (int) (Double.parseDouble(s.substring(0, spacerPos1)) * 1E6),
                    (int) (Double.parseDouble(s.substring(spacerPos1 + 1, spacerPos2)) * 1E6),
                    (int) Double.parseDouble(s.substring(spacerPos2 + 1)));
        }
    }

    public static GeoPoint fromInvertedDoubleString(final String s, final char spacer) {
        final int spacerPos1 = s.indexOf(spacer);
        final int spacerPos2 = s.indexOf(spacer, spacerPos1 + 1);

        if (spacerPos2 == -1) {
            return new GeoPoint(
                    (int) (Double.parseDouble(s.substring(spacerPos1 + 1)) * 1E6),
                    (int) (Double.parseDouble(s.substring(0, spacerPos1)) * 1E6));
        } else {
            return new GeoPoint(
                    (int) (Double.parseDouble(s.substring(spacerPos1 + 1, spacerPos2)) * 1E6),
                    (int) (Double.parseDouble(s.substring(0, spacerPos1)) * 1E6),
                    (int) Double.parseDouble(s.substring(spacerPos2 + 1)));

        }
    }

    public static GeoPoint fromIntString(final String s) {
        final int commaPos1 = s.indexOf(',');
        final int commaPos2 = s.indexOf(',', commaPos1 + 1);

        if (commaPos2 == -1) {
            return new GeoPoint(
                    Integer.parseInt(s.substring(0, commaPos1)),
                    Integer.parseInt(s.substring(commaPos1 + 1)));
        } else {
            return new GeoPoint(
                    Integer.parseInt(s.substring(0, commaPos1)),
                    Integer.parseInt(s.substring(commaPos1 + 1, commaPos2)),
                    Integer.parseInt(s.substring(commaPos2 + 1))
            );
        }
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public int getLongitudeE6() {
        return this.mLongitudeE6;
    }

    public int getLatitudeE6() {
        return this.mLatitudeE6;
    }

    public double getLongitude() {
        return this.mLongitudeE6 * 1E-6;
    }

    public double getLatitude() {
        return this.mLatitudeE6 * 1E-6;
    }

    public int getAltitude() {
        return this.mAltitude;
    }

    public void setLongitudeE6(final int aLongitudeE6) {
        this.mLongitudeE6 = aLongitudeE6;
    }

    public void setLatitudeE6(final int aLatitudeE6) {
        this.mLatitudeE6 = aLatitudeE6;
    }

    public void setAltitude(final int aAltitude) {
        this.mAltitude = aAltitude;
    }

    public void setCoordsE6(final int aLatitudeE6, final int aLongitudeE6) {
        this.mLatitudeE6 = aLatitudeE6;
        this.mLongitudeE6 = aLongitudeE6;
    }

    // ===========================================================
    // Methods from SuperClass/Interfaces
    // ===========================================================

    @Override
    public GeoPoint clone() {
        return new GeoPoint(this.mLatitudeE6, this.mLongitudeE6, this.mAltitude);
    }

    @Override
    public String toString() {
        return new StringBuilder().append(this.mLatitudeE6).append(",").append(this.mLongitudeE6).append(",").append(this.mAltitude)
                .toString();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        final GeoPoint rhs = (GeoPoint) obj;
        return rhs.mLatitudeE6 == this.mLatitudeE6 && rhs.mLongitudeE6 == this.mLongitudeE6 && rhs.mAltitude == this.mAltitude;
    }

    @Override
    public int hashCode() {
        return 37 * (17 * mLatitudeE6 + mLongitudeE6) + mAltitude;
    }

    // ===========================================================
    // Parcelable
    // ===========================================================
    private GeoPoint(final Parcel in) {
        this.mLatitudeE6 = in.readInt();
        this.mLongitudeE6 = in.readInt();
        this.mAltitude = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel out, final int flags) {
        out.writeInt(mLatitudeE6);
        out.writeInt(mLongitudeE6);
        out.writeInt(mAltitude);
    }

    public static final Creator<GeoPoint> CREATOR = new Creator<GeoPoint>() {
        @Override
        public GeoPoint createFromParcel(final Parcel in) {
            return new GeoPoint(in);
        }

        @Override
        public GeoPoint[] newArray(final int size) {
            return new GeoPoint[size];
        }
    };

    // ===========================================================
    // Methods
    // ===========================================================

    /**
     * @return distance in meters
     * @see <a href="http://www.geocities.com/DrChengalva/GPSDistance.html">GPSDistance.html</a>
     */
    public int distanceTo(final GeoPoint other) {

        final double a1 = DEG2RAD * this.mLatitudeE6 / 1E6;
        final double a2 = DEG2RAD * this.mLongitudeE6 / 1E6;
        final double b1 = DEG2RAD * other.getLatitudeE6() / 1E6;
        final double b2 = DEG2RAD * other.getLongitudeE6() / 1E6;

        final double cosa1 = Math.cos(a1);
        final double cosb1 = Math.cos(b1);

        final double t1 = cosa1 * Math.cos(a2) * cosb1 * Math.cos(b2);

        final double t2 = cosa1 * Math.sin(a2) * cosb1 * Math.sin(b2);

        final double t3 = Math.sin(a1) * Math.sin(b1);

        final double tt = Math.acos(t1 + t2 + t3);

        return (int) (RADIUS_EARTH_METERS * tt);
    }

    /**
     * @return bearing in degrees
     * @see <a href="http://groups.google.com/group/osmdroid/browse_thread/thread/d22c4efeb9188fe9/bc7f9b3111158dd">discussion</a>
     */
    public double bearingTo(final GeoPoint other) {
        final double lat1 = Math.toRadians(this.mLatitudeE6 / 1E6);
        final double long1 = Math.toRadians(this.mLongitudeE6 / 1E6);
        final double lat2 = Math.toRadians(other.getLatitudeE6() / 1E6);
        final double long2 = Math.toRadians(other.getLongitudeE6() / 1E6);
        final double delta_long = long2 - long1;
        final double a = Math.sin(delta_long) * Math.cos(lat2);
        final double b = Math.cos(lat1) * Math.sin(lat2) -
                Math.sin(lat1) * Math.cos(lat2) * Math.cos(delta_long);
        final double bearing = Math.toDegrees(Math.atan2(a, b));
        final double bearing_normalized = (bearing + 360) % 360;
        return bearing_normalized;
    }

    /**
     * Calculate a point that is the specified distance and bearing away from this point.
     *
     * @see <a href="http://www.movable-type.co.uk/scripts/latlong.html">latlong.html</a>
     * @see <a href="http://www.movable-type.co.uk/scripts/latlon.js">latlon.js</a>
     */
    public GeoPoint destinationPoint(final double aDistanceInMeters, final float aBearingInDegrees) {

        // convert distance to angular distance
        final double dist = aDistanceInMeters / RADIUS_EARTH_METERS;

        // convert bearing to radians
        final float brng = DEG2RAD * aBearingInDegrees;

        // get current location in radians
        final double lat1 = DEG2RAD * getLatitudeE6() / 1E6;
        final double lon1 = DEG2RAD * getLongitudeE6() / 1E6;

        final double lat2 = Math.asin(Math.sin(lat1) * Math.cos(dist) + Math.cos(lat1)
                * Math.sin(dist) * Math.cos(brng));
        final double lon2 = lon1
                + Math.atan2(Math.sin(brng) * Math.sin(dist) * Math.cos(lat1), Math.cos(dist)
                - Math.sin(lat1) * Math.sin(lat2));

        final double lat2deg = lat2 / DEG2RAD;
        final double lon2deg = lon2 / DEG2RAD;

        return new GeoPoint(lat2deg, lon2deg);
    }

    public static GeoPoint fromCenterBetween(final GeoPoint geoPointA, final GeoPoint geoPointB) {
        return new GeoPoint((geoPointA.getLatitudeE6() + geoPointB.getLatitudeE6()) / 2,
                (geoPointA.getLongitudeE6() + geoPointB.getLongitudeE6()) / 2);
    }

    public String toDoubleString() {
        return new StringBuilder().append(this.mLatitudeE6 / 1E6).append(",")
                .append(this.mLongitudeE6 / 1E6).append(",").append(this.mAltitude).toString();
    }

    public String toInvertedDoubleString() {
        return new StringBuilder().append(this.mLongitudeE6 / 1E6).append(",")
                .append(this.mLatitudeE6 / 1E6).append(",").append(this.mAltitude).toString();
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}