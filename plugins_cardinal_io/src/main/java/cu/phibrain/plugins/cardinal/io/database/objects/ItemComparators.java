package cu.phibrain.plugins.cardinal.io.database.objects;

import java.util.Comparator;

import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectHasDefect;
import cu.phibrain.plugins.cardinal.io.database.entity.model.WorkSession;


public class ItemComparators {
    /**
     * Sort work session by id.
     */
    public static class WorkSessionComparator implements Comparator<WorkSession> {
        private boolean doInverse = false;

        /**
         *
         */
        public WorkSessionComparator() {
        }

        /**
         * @param doInverse invert comparator.
         */
        public WorkSessionComparator(boolean doInverse) {
            this.doInverse = doInverse;
        }

        public int compare(WorkSession m1, WorkSession m2) {
            Long id1 = m1.getId();
            Long id2 = m2.getId();

            int compareTo = id1.compareTo(id2);

            if (compareTo < 0) {
                return doInverse ? 1 : -1;
            } else if (compareTo > 0) {
                return doInverse ? -1 : 1;
            } else {
                return 0;
            }
        }
    }


    /**
     * Sort MapObjectHasDefect by defectId and map object.
     */
    public static class MapObjectHasDefectComparator implements Comparator<MapObjectHasDefect> {
        private boolean doInverse = false;

        /**
         *
         */
        public MapObjectHasDefectComparator() {
        }

        /**
         * @param doInverse invert comparator.
         */
        public MapObjectHasDefectComparator(boolean doInverse) {
            this.doInverse = doInverse;
        }

        public int compare(MapObjectHasDefect m1, MapObjectHasDefect m2) {
            Long mapObjectId = m1.getMapObjectDefectId();
            Long mapObjectDefectId = m2.getMapObjectDefectId();

            int compareTo = mapObjectId.compareTo(mapObjectDefectId);

            if (compareTo < 0) {
                return doInverse ? 1 : -1;
            } else if (compareTo > 0) {
                return doInverse ? -1 : 1;
            } else {
                return 0;
            }
        }
    }
}
