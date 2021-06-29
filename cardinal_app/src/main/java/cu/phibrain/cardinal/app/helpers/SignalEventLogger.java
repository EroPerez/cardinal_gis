package cu.phibrain.cardinal.app.helpers;

import java.util.Date;

import cu.phibrain.plugins.cardinal.io.database.entity.operations.SignalEventsOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.model.SignalEvents;

public class SignalEventLogger {

    /**
     * Add a log entry for cardinal signals event: GPS, STORAGE, POWER  or GSM
     *
     * @param types
     * @param sessionId
     * @param level
     * @param currentDate Current date when event do
     * @param gpsPosition Current gps positions if it on.
     */


    public static void addEventLogEntry(SignalEvents.SignalTypes types, long sessionId, long level, Date currentDate, double[] gpsPosition) {
        double gpsLat = 0.0, gpsLon = 0.0;

        if (gpsPosition != null) {
            gpsLat = gpsPosition[1];
            gpsLon = gpsPosition[0];
        }

        SignalEvents event = new SignalEvents(types, currentDate, currentDate, level, sessionId, gpsLat, gpsLon);

        SignalEventsOperations.getInstance().insert(event);
    }
}
