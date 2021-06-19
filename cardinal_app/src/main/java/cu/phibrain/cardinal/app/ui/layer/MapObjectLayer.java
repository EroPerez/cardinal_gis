package cu.phibrain.cardinal.app.ui.layer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.oscim.android.canvas.AndroidGraphics;
import org.oscim.backend.CanvasAdapter;
import org.oscim.backend.canvas.Bitmap;
import org.oscim.backend.canvas.Color;
import org.oscim.backend.canvas.Paint;
import org.oscim.core.GeoPoint;
import org.oscim.layers.marker.ItemizedLayer;
import org.oscim.layers.marker.MarkerItem;
import org.oscim.layers.marker.MarkerSymbol;
import org.oscim.map.Layers;
import org.oscim.map.Map;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cu.phibrain.cardinal.app.CardinalApplication;
import cu.phibrain.cardinal.app.injections.AppContainer;
import cu.phibrain.plugins.cardinal.io.database.entity.MapObjecTypeOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.MapObjectOperations;
import cu.phibrain.plugins.cardinal.io.model.MapObjecType;
import cu.phibrain.plugins.cardinal.io.model.MapObject;
import eu.geopaparazzi.library.GPApplication;
import eu.geopaparazzi.library.database.ANote;
import eu.geopaparazzi.library.database.DefaultHelperClasses;
import eu.geopaparazzi.library.database.GPLog;
import eu.geopaparazzi.library.database.TableDescriptions;
import eu.geopaparazzi.library.forms.FormActivity;
import eu.geopaparazzi.library.forms.FormInfoHolder;
import eu.geopaparazzi.library.style.ColorUtilities;
import eu.geopaparazzi.library.util.Compat;
import eu.geopaparazzi.library.util.GPDialogs;
import eu.geopaparazzi.library.util.IActivitySupporter;
import eu.geopaparazzi.library.util.LibraryConstants;
import eu.geopaparazzi.library.util.TimeUtilities;
import eu.geopaparazzi.map.GPMapView;
import cu.phibrain.plugins.cardinal.io.R;
import eu.geopaparazzi.map.layers.interfaces.ISystemLayer;

import static eu.geopaparazzi.library.util.LibraryConstants.PREFS_KEY_NOTES_TEXT_VISIBLE;
import static eu.geopaparazzi.library.util.LibraryConstants.PREFS_KEY_NOTES_VISIBLE;
public class MapObjectLayer extends ItemizedLayer<MarkerItem> implements ItemizedLayer.OnItemGestureListener<MarkerItem>, ISystemLayer, ICardinalLayer {
    private static final int FG_COLOR = 0xFF000000; // 100 percent black. AARRGGBB
    private static final int BG_COLOR = 0x80FF69B4; // 50 percent pink. AARRGGBB
    private static final int TRANSP_WHITE = 0x80FFFFFF; // 50 percent white. AARRGGBB
    private static String NAME = null;
    public static final String NONFORMSTART = "@";
    private Long ID;
    public static final int FORMUPDATE_RETURN_CODE = 669;
    private static Bitmap notesBitmap;
    private GPMapView mapView;
    private IActivitySupporter activitySupporter;
    private static int textSize;
    private static String colorStr;
    public MapObjectLayer(GPMapView mapView, IActivitySupporter activitySupporter, Long ID) throws IOException {
        super(mapView.map(), getMarkerSymbol(mapView, ID));
        this.mapView = mapView;
        this.ID = ID;
        getName(mapView.getContext());

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
        SharedPreferences peferences = PreferenceManager.getDefaultSharedPreferences(mapView.getContext());
        String textSizeStr = peferences.getString(LibraryConstants.PREFS_KEY_NOTES_TEXT_SIZE, LibraryConstants.DEFAULT_NOTES_SIZE + ""); //$NON-NLS-1$
        colorStr = peferences.getString(LibraryConstants.PREFS_KEY_NOTES_CUSTOMCOLOR, ColorUtilities.ALMOST_BLACK.getHex());
        Drawable imagesDrawable = Compat.getDrawable(mapView.getContext(), eu.geopaparazzi.library.R.drawable.ic_bookmarks_48dp);

        //notesBitmap = AndroidGraphics.drawableToBitmap(imagesDrawable);
        byte [] icon = mtoMapObjecType.getIconAsByteArray();
        notesBitmap = AndroidGraphics.decodeBitmap(new ByteArrayInputStream(icon));

        return new MarkerSymbol(notesBitmap, MarkerSymbol.HotspotPlace.UPPER_LEFT_CORNER, false);
    }


    public MapObjectLayer(Map map, MarkerSymbol defaultMarker) {
        super(map, defaultMarker);
    }

    public void reloadData() throws IOException {
        MapObjecType mtoMapObjecType = MapObjecTypeOperations.getInstance().load(this.getID());
        List<MapObject> mapObjects = mtoMapObjecType.getMapObjects();
        List<MarkerItem> pts = new ArrayList<>();
        for (MapObject mapObject : mapObjects) {
            String[] coord = mapObject.getCoord().split(",");
            double lat = Double.parseDouble(coord[0]);
            double lon = Double.parseDouble(coord[1]);
            String text = mapObject.getObjectType().getCaption();
            pts.add(new MarkerItem(mapObject.getId(), text, mapObject.getObjectType().getDescription(), new GeoPoint(lat, lon)));
        }
        for (MarkerItem mi : pts) {
            mi.setMarker(createAdvancedSymbol(mi, notesBitmap));
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
        if (item != null) {
            String description = item.getSnippet();
            if (description.startsWith(NONFORMSTART)) {
                GPDialogs.infoDialog(mapView.getContext(), description.substring(1), null);
            } else {
                try {
                    long uid = (long) item.getUid();
                    ANote note = DefaultHelperClasses.getDefaulfNotesHelper().getNoteById(uid);

                    GeoPoint point = item.getPoint();
                    double lat = point.getLatitude();
                    double lon = point.getLongitude();
                    Intent formIntent = new Intent(mapView.getContext(), FormActivity.class);
                    FormInfoHolder formInfoHolder = new FormInfoHolder();
                    formInfoHolder.sectionName = item.title;
                    formInfoHolder.formName = null;
                    formInfoHolder.noteId = note.getId();
                    formInfoHolder.longitude = lon;
                    formInfoHolder.latitude = lat;
                    formInfoHolder.sectionObjectString = item.getSnippet();
                    formInfoHolder.objectExists = true;
                    formIntent.putExtra(FormInfoHolder.BUNDLE_KEY_INFOHOLDER, formInfoHolder);

                    activitySupporter.startActivityForResult(formIntent, FORMUPDATE_RETURN_CODE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
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
        int bitmapHeight = poiBitmap.getHeight();
        int margin = 3;
        int dist2symbol = (int) Math.round(bitmapHeight / 2.0);

        int symbolWidth = poiBitmap.getWidth();

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
        jo.put("ID", this.getID());
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
