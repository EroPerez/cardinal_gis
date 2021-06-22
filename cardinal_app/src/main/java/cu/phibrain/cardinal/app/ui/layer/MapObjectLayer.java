package cu.phibrain.cardinal.app.ui.layer;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.oscim.android.canvas.AndroidGraphics;
import org.oscim.backend.CanvasAdapter;
import org.oscim.backend.canvas.Bitmap;
import org.oscim.layers.marker.ItemizedLayer;
import org.oscim.layers.marker.MarkerItem;
import org.oscim.layers.marker.MarkerSymbol;
import org.oscim.map.Layers;
import org.oscim.map.Map;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cu.phibrain.plugins.cardinal.io.R;
import cu.phibrain.plugins.cardinal.io.database.entity.MapObjecTypeOperations;
import cu.phibrain.plugins.cardinal.io.model.MapObjecType;
import cu.phibrain.plugins.cardinal.io.model.MapObject;
import eu.geopaparazzi.library.database.GPLog;
import eu.geopaparazzi.library.style.ColorUtilities;
import eu.geopaparazzi.library.util.IActivitySupporter;
import eu.geopaparazzi.library.util.LibraryConstants;
import eu.geopaparazzi.map.GPGeoPoint;
import eu.geopaparazzi.map.GPMapView;
import eu.geopaparazzi.map.layers.interfaces.ISystemLayer;

public class MapObjectLayer extends ItemizedLayer<MarkerItem> implements ItemizedLayer.OnItemGestureListener<MarkerItem>, ISystemLayer, ICardinalLayer {
    private static final int FG_COLOR = 0xFF000000; // 100 percent black. AARRGGBB
    private static final int BG_COLOR = 0x80FF69B4; // 50 percent pink. AARRGGBB
    private static final int TRANSP_WHITE = 0x80FFFFFF; // 50 percent white. AARRGGBB
    private static String NAME = null;
    public static final String NONFORMSTART = "@";
    private Long ID;
    public static final int FORMUPDATE_RETURN_CODE = 669;
    private static Bitmap mtoBitmap;
    private GPMapView mapView;
    private IActivitySupporter activitySupporter;
    private static int textSize;
    private static String colorStr;
    private MapObjecType currentObjectype;

    public MapObjectLayer(GPMapView mapView, IActivitySupporter activitySupporter, Long ID) throws IOException {
        super(mapView.map(), getMarkerSymbol(mapView, ID));
        this.mapView = mapView;
        this.ID = ID;
        currentObjectype = MapObjecTypeOperations.getInstance().load(this.ID);
        NAME = currentObjectype.getCaption();
        NAME = getName(mapView.getContext());

        this.activitySupporter = activitySupporter;
        setOnItemGestureListener(this);
        try {
            reloadData();
        } catch (IOException e) {
            GPLog.error(this, null, e);
        }


    }

    public static String getName(Context context) {
        if (NAME == null) {
            NAME = context.getString(R.string.layername_mot);
        }
        return NAME;
    }


    private static MarkerSymbol getMarkerSymbol(GPMapView mapView, Long _ID) throws IOException {
        MapObjecType mtoMapObjecType = MapObjecTypeOperations.getInstance().load(_ID);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mapView.getContext());
        colorStr = preferences.getString(LibraryConstants.PREFS_KEY_NOTES_CUSTOMCOLOR, ColorUtilities.ALMOST_BLACK.getHex());

        byte[] icon = mtoMapObjecType.getIconAsByteArray();
        mtoBitmap = AndroidGraphics.decodeBitmap(new ByteArrayInputStream(icon));

        return new MarkerSymbol(mtoBitmap, MarkerSymbol.HotspotPlace.UPPER_LEFT_CORNER, false);
    }


    public MapObjectLayer(Map map, MarkerSymbol defaultMarker) {
        super(map, defaultMarker);
    }


    public void reloadData(Long id) throws IOException {
        MapObjecType maoMapObjcType = MapObjecTypeOperations.getInstance().load(id);
        maoMapObjcType.refresh();
        List<MapObject> mapObjects = maoMapObjcType.getMapObjects();

        byte[] icon = maoMapObjcType.getIconAsByteArray();
        Bitmap _mtoBitmap = AndroidGraphics.decodeBitmap(new ByteArrayInputStream(icon));

        List<MarkerItem> pts = new ArrayList<>();
        for (MapObject mapObject : mapObjects) {
            GPGeoPoint coord = mapObject.getCoord().get(0);
            String text = mapObject.getObjectType().getCaption();
            pts.add(new MarkerItem(mapObject.getId(), text, mapObject.getObjectType().getDescription(), coord));
        }
        for (MarkerItem mi : pts) {
            mi.setMarker(createAdvancedSymbol(mi, _mtoBitmap));
        }
        addItems(pts);
        update();
    }

    public void reloadData() throws IOException {
        //MapObjecType mtoMapObjecType = MapObjecTypeOperations.getInstance().load(ID);
        currentObjectype.resetMapObjects();
        List<MapObject> mapObjects = currentObjectype.getMapObjects();
        List<MarkerItem> pts = new ArrayList<>();
        for (MapObject mapObject : mapObjects) {

            GPGeoPoint coord = mapObject.getCoord().get(0);
            String text = mapObject.getCode();
            pts.add(new MarkerItem(mapObject.getId(), text, mapObject.getObservation(), coord));
        }
        for (MarkerItem mi : pts) {
            mi.setMarker(createAdvancedSymbol(mi, mtoBitmap));
        }
        addItems(pts);
        update();
    }

    public void disable() {
        setEnabled(false);
    }


    public void enable() {
        setEnabled(true);
    }

    @Override
    public boolean onItemSingleTapUp(int index, MarkerItem item) {
        if (item != null)
            Toast.makeText(this.activitySupporter.getContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
//        if (item != null) {
//            String description = item.getSnippet();
//            if (description.startsWith(NONFORMSTART)) {
//                GPDialogs.infoDialog(mapView.getContext(), description.substring(1), null);
//            } else {
//                try {
//                    long uid = (long) item.getUid();
//                    ANote note = DefaultHelperClasses.getDefaulfNotesHelper().getNoteById(uid);
//
//                    GeoPoint point = item.getPoint();
//                    double lat = point.getLatitude();
//                    double lon = point.getLongitude();
//                    Intent formIntent = new Intent(mapView.getContext(), FormActivity.class);
//                    FormInfoHolder formInfoHolder = new FormInfoHolder();
//                    formInfoHolder.sectionName = item.title;
//                    formInfoHolder.formName = null;
//                    formInfoHolder.noteId = note.getId();
//                    formInfoHolder.longitude = lon;
//                    formInfoHolder.latitude = lat;
//                    formInfoHolder.sectionObjectString = item.getSnippet();
//                    formInfoHolder.objectExists = true;
//                    formIntent.putExtra(FormInfoHolder.BUNDLE_KEY_INFOHOLDER, formInfoHolder);
//
//                    activitySupporter.startActivityForResult(formIntent, FORMUPDATE_RETURN_CODE);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
        return false;
    }

    @Override
    public boolean onItemLongPress(int index, MarkerItem item) {
        return false;
    }


    /**
     * Creates a transparent symbol with text and description.
     * PREFS_KEY_IMAGES_TEXT_VISIBLE
     *
     * @param item      -> the MarkerItem to process, containing title and description
     *                  if description starts with a '#' the first line of the description is drawn.
     * @param poiBitmap -> poi bitmap for the center
     * @return MarkerSymbol with title, description and symbol
     */


    private MarkerSymbol createAdvancedSymbol(MarkerItem item, Bitmap poiBitmap) {
        int bitmapHeight = 48;//poiBitmap.getHeight();
        int margin = 3;
        int dist2symbol = (int) Math.round(bitmapHeight / 2.0);

        int symbolWidth = 48;//poiBitmap.getWidth();

        int xSize = symbolWidth;
        int ySize = symbolWidth + dist2symbol;

        // markerCanvas, the drawing area for all: title, description and symbol
        Bitmap markerBitmap = CanvasAdapter.newBitmap(xSize, ySize, 0);
        org.oscim.backend.canvas.Canvas markerCanvas = CanvasAdapter.newCanvas();
        markerCanvas.setBitmap(markerBitmap);

        markerCanvas.drawBitmap(poiBitmap, xSize * 0.5f - (symbolWidth * 0.25f), ySize * 0.5f - (symbolWidth * 0.25f));

        return (new MarkerSymbol(markerBitmap, MarkerSymbol.HotspotPlace.CENTER, true));
    }

    @Override
    public String getId() {
        return getName();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public GPMapView getMapView() {
        return mapView;
    }

    @Override
    public void load() {
        Layers layers = map().layers();
        layers.add(this, CardinalLayerGroups.GROUP_CARDINALLAYERS.getGroupId());
    }

    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject jo = toDefaultJson();
        jo.put(LAYERID_TAG, this.getID());
        return jo;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }
}
