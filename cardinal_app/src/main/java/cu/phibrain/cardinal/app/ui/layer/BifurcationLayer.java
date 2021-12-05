package cu.phibrain.cardinal.app.ui.layer;

import android.content.Context;
import android.content.SharedPreferences;
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
import org.oscim.layers.marker.ItemizedLayer;
import org.oscim.layers.marker.MarkerItem;
import org.oscim.layers.marker.MarkerSymbol;
import org.oscim.map.Layers;
import org.oscim.map.Map;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cu.phibrain.cardinal.app.CardinalApplication;
import cu.phibrain.cardinal.app.R;
import cu.phibrain.cardinal.app.injections.AppContainer;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Layer;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObject;
import cu.phibrain.plugins.cardinal.io.database.entity.model.WorkSession;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.WorkSessionOperations;
import eu.geopaparazzi.library.database.GPLog;
import eu.geopaparazzi.library.style.ColorUtilities;
import eu.geopaparazzi.library.util.GPDialogs;
import eu.geopaparazzi.library.util.IActivitySupporter;
import eu.geopaparazzi.library.util.LibraryConstants;
import eu.geopaparazzi.map.GPGeoPoint;
import eu.geopaparazzi.map.GPMapPosition;
import eu.geopaparazzi.map.GPMapView;
import eu.geopaparazzi.map.layers.interfaces.ISystemLayer;

import static eu.geopaparazzi.library.util.LibraryConstants.PREFS_KEY_NOTES_TEXT_VISIBLE;

public class BifurcationLayer extends ItemizedLayer<MarkerItem> implements ItemizedLayer.OnItemGestureListener<MarkerItem>, ISystemLayer, ICardinalBifurcation {
    private static final int TRANSP_WHITE = 0x80FFFFFF; // 50 percent white. AARRGGBB
    private static String NAME = null;

    private static Bitmap bifurcationBitmap;
    private WorkSession session;
    private GPMapView mapView;
    private IActivitySupporter activitySupporter;
    private static int textSize;
    private static String colorStr;
    private boolean showLabels;

    private SharedPreferences preferences;

    /*
     * Join visibility
     */
    public static String PREFS_KEY_BIFURCATION_VISIBLE = "PREFS_KEY_BIFURCATION_VISIBLE";

    public BifurcationLayer(GPMapView mapView, IActivitySupporter activitySupporter) {
        super(mapView.map(), getMarkerSymbol(mapView));
        this.mapView = mapView;
        getName(mapView.getContext());

        this.activitySupporter = activitySupporter;
        setOnItemGestureListener(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(mapView.getContext());
        SharedPreferences.Editor prefEditor = preferences.edit();
        prefEditor.putBoolean(PREFS_KEY_BIFURCATION_VISIBLE, isEnabled());
        prefEditor.commit();

        showLabels = preferences.getBoolean(PREFS_KEY_NOTES_TEXT_VISIBLE, true);
        AppContainer appContainer = ((CardinalApplication) CardinalApplication.getInstance()).getContainer();
        session = appContainer.getWorkSessionActive();

        boolean bifurcationVisible = preferences.getBoolean(PREFS_KEY_BIFURCATION_VISIBLE, true);

        try {
            if (bifurcationVisible)
                reloadData();
        } catch (IOException e) {
            GPLog.error(this, null, e);
        }


    }

    public static String getName(Context context) {
        if (NAME == null) {
            NAME = context.getString(R.string.layername_bifuration);
        }
        return NAME;
    }

    private static MarkerSymbol getMarkerSymbol(GPMapView mapView) {
        SharedPreferences peferences = PreferenceManager.getDefaultSharedPreferences(mapView.getContext());
        // notes type
        String textSizeStr = peferences.getString(LibraryConstants.PREFS_KEY_NOTES_TEXT_SIZE, LibraryConstants.DEFAULT_NOTES_SIZE + ""); //$NON-NLS-1$
        textSize = Integer.parseInt(textSizeStr);
        colorStr = peferences.getString(LibraryConstants.PREFS_KEY_NOTES_CUSTOMCOLOR, ColorUtilities.RED.getHex());
        Drawable bifurcationDrawable;

        String opacityStr = peferences.getString(LibraryConstants.PREFS_KEY_NOTES_OPACITY, "255"); //$NON-NLS-1$
        String sizeStr = peferences.getString(LibraryConstants.PREFS_KEY_NOTES_SIZE, LibraryConstants.DEFAULT_NOTES_SIZE + ""); //$NON-NLS-1$
        int noteSize = Integer.parseInt(sizeStr);
        float opacity = Integer.parseInt(opacityStr);

        OvalShape bifurcationShape = new OvalShape();
        android.graphics.Paint bifurcationPaint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG);
        bifurcationPaint.setStyle(android.graphics.Paint.Style.STROKE);
        bifurcationPaint.setColor(ColorUtilities.toColor(colorStr));
        bifurcationPaint.setAlpha((int) opacity);

        ShapeDrawable bifurcationShapeDrawable = new ShapeDrawable(bifurcationShape);
        android.graphics.Paint paint = bifurcationShapeDrawable.getPaint();
        paint.set(bifurcationPaint);
        bifurcationShapeDrawable.setIntrinsicHeight(noteSize);
        bifurcationShapeDrawable.setIntrinsicWidth(noteSize);
        bifurcationDrawable = bifurcationShapeDrawable;


        bifurcationBitmap = AndroidGraphics.drawableToBitmap(bifurcationDrawable);

        return new MarkerSymbol(bifurcationBitmap, MarkerSymbol.HotspotPlace.CENTER, false);
    }

    public BifurcationLayer(Map map, MarkerSymbol defaultMarker) {
        super(map, defaultMarker);
    }

    public void reloadData() throws IOException {

        if (!isEnabled()) {
            return;
        }

        List<MapObject> mapObjects = WorkSessionOperations.getInstance().getMapObjects(session.getId());
        GPMapPosition mapPosition = mapView.getMapPosition();
        int zoom = mapPosition.getZoomLevel();

        List<MarkerItem> markerItems = new ArrayList<>();
        removeAllItems();
        for (MapObject mapObject : mapObjects) {
            Layer cardinalLayer = mapObject.getLayer();
            if (cardinalLayer != null
                    && cardinalLayer.getEnabled()
                    && zoom >= cardinalLayer.getViewZoomLevel()
                    && mapObject.getNodeGrade() > 2
            ) {
                long pendingRoute = mapObject.getNodeGrade() - MapObjectOperations.getInstance().getRouteSegmentsCount(mapObject.getId());
                String title = String.format("P   %d", pendingRoute);
                GPGeoPoint ct = mapObject.getCentroid();
                MarkerItem mi = new MarkerItem(mapObject.getId(), title, "", ct);
                mi.setMarker(createAdvancedSymbol(mi, bifurcationBitmap));
                markerItems.add(mi);
            }
        }
        addItems(markerItems);
        update();
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled == isEnabled())
            return;

        super.setEnabled(enabled);

        // update preferences to show/hide blue circle
        SharedPreferences.Editor prefEditor = preferences.edit();
        prefEditor.putBoolean(PREFS_KEY_BIFURCATION_VISIBLE, isEnabled());
        prefEditor.commit();

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
            GPDialogs.quickInfo(mapView, String.format("%s - (%s)", getName(mapView.getContext()), item.getTitle()));
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
        final Paint textPainter = CanvasAdapter.newPaint();
        textPainter.setStyle(Paint.Style.FILL);
        int textColor = ColorUtilities.toColor(colorStr);
        textPainter.setColor(textColor);
        textPainter.setTextSize(textSize);
        textPainter.setTypeface(Paint.FontFamily.MONOSPACE, Paint.FontStyle.NORMAL);

        final Paint haloTextPainter = CanvasAdapter.newPaint();
        haloTextPainter.setStyle(Paint.Style.FILL);
        haloTextPainter.setColor(Color.WHITE);
        haloTextPainter.setTextSize(textSize);
        haloTextPainter.setTypeface(Paint.FontFamily.MONOSPACE, Paint.FontStyle.BOLD);

        int bitmapHeight = poiBitmap.getHeight();
        int margin = 3;
        int dist2symbol = (int) Math.round(bitmapHeight * 1.5);

        int titleWidth = ((int) haloTextPainter.getTextWidth(item.title) + 2 * margin);
        int titleHeight = (int) (haloTextPainter.getTextHeight(item.title) + textPainter.getFontDescent() + 2 * margin);

        int symbolWidth = poiBitmap.getWidth();

        int xSize = Math.max(titleWidth, symbolWidth);
        int ySize = titleHeight + symbolWidth + dist2symbol;

        // markerCanvas, the drawing area for all: title, description and symbol
        Bitmap markerBitmap = CanvasAdapter.newBitmap(xSize, ySize, 0);
        org.oscim.backend.canvas.Canvas markerCanvas = CanvasAdapter.newCanvas();
        markerCanvas.setBitmap(markerBitmap);

        // titleCanvas for the title text
        Bitmap titleBitmap = CanvasAdapter.newBitmap(titleWidth + margin, titleHeight + margin, 0);
        org.oscim.backend.canvas.Canvas titleCanvas = CanvasAdapter.newCanvas();
        titleCanvas.setBitmap(titleBitmap);

        titleCanvas.fillRectangle(0, 0, titleWidth, titleHeight, TRANSP_WHITE);
        titleCanvas.drawText(item.title, margin, titleHeight - margin - textPainter.getFontDescent(), haloTextPainter);
        titleCanvas.drawText(item.title, margin, titleHeight - margin - textPainter.getFontDescent(), textPainter);

        if (showLabels)
            markerCanvas.drawBitmap(titleBitmap, xSize * 0.5f - (titleWidth * 0.5f), ySize * 0.5f - (symbolWidth * 0.5f));
        markerCanvas.drawBitmap(poiBitmap, xSize * 0.5f - (symbolWidth * 0.5f), ySize * 0.5f - (symbolWidth * 0.5f));

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
        return toDefaultJson();
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

}
