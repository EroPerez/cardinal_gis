package cu.phibrain.cardinal.app.ui.layer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.hortonmachine.dbs.datatypes.EGeometryType;
import org.json.JSONException;
import org.json.JSONObject;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cu.phibrain.cardinal.app.CardinalApplication;
import cu.phibrain.cardinal.app.MapviewActivity;
import cu.phibrain.cardinal.app.R;
import cu.phibrain.cardinal.app.helpers.LatLongUtils;
import cu.phibrain.cardinal.app.helpers.NumberUtiles;
import cu.phibrain.cardinal.app.injections.AppContainer;
import cu.phibrain.cardinal.app.injections.UserMode;
import cu.phibrain.cardinal.app.ui.fragment.BarcodeReaderDialogFragment;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Layer;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjecType;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObject;
import cu.phibrain.plugins.cardinal.io.database.entity.model.RouteSegment;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.LayerOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.RouteSegmentOperations;
import cu.phibrain.plugins.cardinal.io.utils.ImageUtil;
import eu.geopaparazzi.library.GPApplication;
import eu.geopaparazzi.library.database.GPLog;
import eu.geopaparazzi.library.images.ImageUtilities;
import eu.geopaparazzi.library.style.ColorUtilities;
import eu.geopaparazzi.library.util.Compat;
import eu.geopaparazzi.library.util.GPDialogs;
import eu.geopaparazzi.library.util.IActivitySupporter;
import eu.geopaparazzi.library.util.LibraryConstants;
import eu.geopaparazzi.library.util.TextRunnable;
import eu.geopaparazzi.map.GPGeoPoint;
import eu.geopaparazzi.map.GPMapPosition;
import eu.geopaparazzi.map.GPMapView;
import eu.geopaparazzi.map.features.Feature;
import eu.geopaparazzi.map.layers.interfaces.IEditableLayer;
import eu.geopaparazzi.map.layers.interfaces.ISystemLayer;

import static eu.geopaparazzi.library.util.LibraryConstants.PREFS_KEY_NOTES_TEXT_VISIBLE;

public class CardinalPointLayer extends ItemizedLayer<MarkerItem> implements ItemizedLayer.OnItemGestureListener<MarkerItem>, ISystemLayer, IEditableLayer, ICardinalLayer {
    private static final int TRANSP_WHITE = 0x80FFFFFF; // 50 percent white. AARRGGBB
    private static String NAME = null;
    private static int textSize;
    private static String colorStr;
    public static final long SELECT_MARKER_UID = -1L;
    EGeometryType geometryMto;
    private Long ID;
    private GPMapView mapView;
    private IActivitySupporter activitySupporter;
    private AppContainer appContainer;
    private boolean showLabels;
    private static Bitmap notesBitmap;

    public CardinalPointLayer(GPMapView mapView, IActivitySupporter activitySupporter, Long ID) throws IOException {
        super(mapView.map(), getMarkerSymbol(mapView, ID));
        this.mapView = mapView;
        this.ID = ID;
        this.setEnabled(true);
        getName(mapView.getContext());
        geometryMto = EGeometryType.POINT;
        this.activitySupporter = activitySupporter;
        setOnItemGestureListener(this);
        appContainer = ((CardinalApplication) CardinalApplication.getInstance()).getContainer();
        try {
            reloadData();
        } catch (IOException e) {
            GPLog.error(this, null, e);
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(GPApplication.getInstance());
        showLabels = preferences.getBoolean(PREFS_KEY_NOTES_TEXT_VISIBLE, true);

    }

    public CardinalPointLayer(Map map, MarkerSymbol defaultMarker) {
        super(map, defaultMarker);
    }

    public static String getName(Context context) {
        if (NAME == null) {
            NAME = context.getString(R.string.layername_mot);
        }
        return NAME;
    }

    private static MarkerSymbol getMarkerSymbol(GPMapView mapView, Long _ID) throws IOException {
        SharedPreferences peferences = PreferenceManager.getDefaultSharedPreferences(mapView.getContext());
        // notes type
        boolean doCustom = peferences.getBoolean(LibraryConstants.PREFS_KEY_NOTES_CHECK, true);
        String textSizeStr = peferences.getString(LibraryConstants.PREFS_KEY_NOTES_TEXT_SIZE, LibraryConstants.DEFAULT_NOTES_SIZE + ""); //$NON-NLS-1$
        textSize = (int) (Integer.parseInt(textSizeStr) / 1.5);
        colorStr = peferences.getString(LibraryConstants.PREFS_KEY_NOTES_CUSTOMCOLOR, ColorUtilities.ALMOST_BLACK.getHex());
        Drawable notesDrawable;
        if (doCustom) {
            String opacityStr = peferences.getString(LibraryConstants.PREFS_KEY_NOTES_OPACITY, "255"); //$NON-NLS-1$
            String sizeStr = peferences.getString(LibraryConstants.PREFS_KEY_NOTES_SIZE, LibraryConstants.DEFAULT_NOTES_SIZE + ""); //$NON-NLS-1$
            int noteSize = Integer.parseInt(sizeStr);
            float opacity = Integer.parseInt(opacityStr);

            OvalShape notesShape = new OvalShape();
            android.graphics.Paint notesPaint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG);
            notesPaint.setStyle(android.graphics.Paint.Style.FILL);
            notesPaint.setColor(ColorUtilities.toColor(colorStr));
            notesPaint.setAlpha((int) opacity);

            ShapeDrawable notesShapeDrawable = new ShapeDrawable(notesShape);
            android.graphics.Paint paint = notesShapeDrawable.getPaint();
            paint.set(notesPaint);
            notesShapeDrawable.setIntrinsicHeight(noteSize);
            notesShapeDrawable.setIntrinsicWidth(noteSize);
            notesDrawable = notesShapeDrawable;
        } else {
            notesDrawable = Compat.getDrawable(mapView.getContext(), eu.geopaparazzi.library.R.drawable.ic_place_accent_24dp);
        }
        notesBitmap = AndroidGraphics.drawableToBitmap(notesDrawable);

        return new MarkerSymbol(notesBitmap, MarkerSymbol.HotspotPlace.CENTER, false);
    }


    private GPGeoPoint centerPoint(MapObject mapObject) {
        return LatLongUtils.centerPoint(mapObject.getCoord(), mapObject.getObjectType().getGeomType());
    }

    @Override
    public void reloadData() throws IOException {
        cu.phibrain.plugins.cardinal.io.database.entity.model.Layer cardinalLayer = LayerOperations.getInstance().load(ID);
        GPMapPosition mapPosition = mapView.getMapPosition();
        int zoom = mapPosition.getZoomLevel();

        List<MarkerItem> markerItems = new ArrayList<>();
        removeAllItems();
        if (cardinalLayer.getEnabled() && zoom >= cardinalLayer.getViewZoomLevel()) {

            for (MapObjecType mtoMapObjcType : cardinalLayer.getMapobjectypes()) {
                byte[] icon = ImageUtil.getScaledBitmapAsByteArray(
                        ImageUtilities.getImageFromImageData(
                                mtoMapObjcType.getIconAsByteArray()
                        ),
                        48, 48, false);

                Bitmap _mtoBitmap = AndroidGraphics.decodeBitmap(new ByteArrayInputStream(icon));

                mtoMapObjcType.resetMapObjects();
                List<MapObject> mapObjects = mtoMapObjcType.getMapObjects();

                for (MapObject mapObject : mapObjects) {
                    String text = mtoMapObjcType.getCaption();
                    GPGeoPoint ct = centerPoint(mapObject);
                    MarkerItem mi = new MarkerItem(mapObject.getId(), mapObject.getCode(), text, ct);
                    mi.setMarker(createAdvancedSymbol(mi, _mtoBitmap));
                    markerItems.add(mi);
                }
            }
        }
        addItems(markerItems);
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
        appContainer.setRouteSegmentActive(null);
        MapObject previousObj = appContainer.getCurrentMapObject();
        MapObject currentObj = MapObjectOperations.getInstance().load((Long) item.getUid());
        if (appContainer.getMode() == UserMode.OBJECT_ADDING_EDGE)
            addEdge(item, currentObj, previousObj);
        else if (appContainer.getMode() == UserMode.OBJECT_DOCK)
            joinMo(item, currentObj, previousObj);

        return true;

    }

    @Override
    public boolean onItemLongPress(int index, MarkerItem item) {
        appContainer.setRouteSegmentActive(null);
        MarkerItem selectMarker = getMarkerById(CardinalPointLayer.SELECT_MARKER_UID);
        if (selectMarker != null)
            removeItem(selectMarker);

        if (item != null && Long.parseLong("" + item.getUid()) != CardinalPointLayer.SELECT_MARKER_UID) {
            MapObject objectSelected = MapObjectOperations.getInstance().load((Long) item.getUid());
            appContainer.setCurrentMapObject(objectSelected);
            appContainer.setMapObjecTypeActive(objectSelected.getObjectType());
            try {
                mapView.reloadLayer(CardinalSelectPointLayer.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Update ui
            Intent intent = new Intent(MapviewActivity.ACTION_UPDATE_UI);
            intent.putExtra("update_map_object_active", true);
            intent.putExtra("update_map_object_type_active", true);
            ((MapviewActivity) this.activitySupporter).sendBroadcast(intent);

        }
        return true;

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
        int dist2symbol = (int) Math.round(bitmapHeight * 1.7);

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
            markerCanvas.drawBitmap(titleBitmap, xSize * 0.5f - (titleWidth * 0.5f), 0);
        markerCanvas.drawBitmap(poiBitmap, xSize * 0.5f - (symbolWidth * 0.5f), ySize * 0.5f - (symbolWidth * 0.5f));

        return (new MarkerSymbol(markerBitmap, MarkerSymbol.HotspotPlace.CENTER, true));
    }

    @Override
    public String getId() {
        return getName() + "_" + ID;
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

    @Override
    public boolean isEditable() {
        return true;
    }

    @Override
    public boolean isInEditingMode() {
        return false;
    }

    @Override
    public List<Feature> getFeatures(Envelope env) throws Exception {
        return null;
    }

    @Override
    public void deleteFeatures(List<Feature> features) throws Exception {
        GPDialogs.yesNoMessageDialog(this.activitySupporter.getContext(),
                ((MapviewActivity) this.activitySupporter).getString(cu.phibrain.cardinal.app.R.string.do_you_want_to_delete_this_map_object),
                () -> ((MapviewActivity) this.activitySupporter).runOnUiThread(() -> {
                    // stop logging
                    MapObjectOperations.getInstance().delete(appContainer.getCurrentMapObject());
                    appContainer.setCurrentMapObject(null);
                    try {
                        this.reloadData();
                        mapView.reloadLayer(CardinalEdgesLayer.class);
                        mapView.reloadLayer(CardinalSelectPointLayer.class);
                        mapView.reloadLayer(CardinalJoinsLayer.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(MapviewActivity.ACTION_UPDATE_UI);
                    intent.putExtra("update_map_object_active", true);
                    intent.putExtra("update_map_object_type_active", true);

                    ((MapviewActivity) this.activitySupporter).sendBroadcast(intent);

                }), null
        );
        appContainer.setMode(UserMode.NONE);
    }

    @Override
    public void addNewFeatureByGeometry(Geometry geometry, int srid) throws Exception {
        AppCompatActivity activity = (MapviewActivity) this.activitySupporter;

        GPDialogs.inputMessageDialog(activity, activity.getString(R.string.inspector_object_grade), "2", new TextRunnable() {
            @Override
            public void run() {
                long grade = NumberUtiles.parseStringToLong(theTextToRunOn, 0L);
                BarcodeReaderDialogFragment.newInstance(
                        mapView, LatLongUtils.toGpGeoPoints(geometry), grade
                ).show(
                        activity.getSupportFragmentManager(),
                        "dialog"
                );
            }
        });

    }

    @Override
    public void updateFeatureGeometry(Feature feature, Geometry geometry, int geometrySrid) throws Exception {
        MapObject currentMO = appContainer.getCurrentMapObject();
        MapObjecType oldSelectedObjectType = null;

        if (appContainer.getMode() == UserMode.OBJECT_COORD_EDITION) {

            currentMO.setCoord(LatLongUtils.toGpGeoPoints(geometry));
            MapObjectOperations.getInstance().save(currentMO);

        } else if (appContainer.getMode() == UserMode.OBJECT_EDITION) {
            //Do the clone process here
            currentMO.setCoord(LatLongUtils.toGpGeoPoints(geometry));
            MapObjecType newSelectedObjectType = appContainer.getMapObjecTypeActive();
            oldSelectedObjectType = currentMO.getObjectType();
            currentMO.setMapObjectTypeId(newSelectedObjectType.getId());
            MapObjectOperations.getInstance().clone(currentMO);

            appContainer.setCurrentMapObject(currentMO);

        }

        appContainer.setMode(UserMode.NONE);
        //Reload layers associated
        this.reloadData();
        mapView.reloadLayer(CardinalEdgesLayer.class);
        mapView.reloadLayer(CardinalSelectPointLayer.class);
        mapView.reloadLayer(CardinalLineLayer.class);
        mapView.reloadLayer(CardinalPolygonLayer.class);
        mapView.reloadLayer(CardinalJoinsLayer.class);
        //Reload current point layers
        if (oldSelectedObjectType != null) {
            Layer layer = oldSelectedObjectType.getLayerObj();
            ((CardinalGPMapView) mapView).reloadLayer(layer.getId());
        }

        GPDialogs.quickInfo(mapView, ((MapviewActivity) activitySupporter).getString(cu.phibrain.cardinal.app.R.string.map_object_saved_message));

        Intent intent = new Intent(MapviewActivity.ACTION_UPDATE_UI);
        intent.putExtra("update_map_object_active", true);
        intent.putExtra("update_map_object_type_active", true);

        ((MapviewActivity) this.activitySupporter).sendBroadcast(intent);
    }


    @Override
    public EGeometryType getGeometryType() {
        return geometryMto;
    }

    @Override
    public Long getLayerId() {
        return ID;
    }

    private boolean addEdge(MarkerItem item, MapObject currentObj, MapObject previousObj) {
        MapviewActivity activity = (MapviewActivity) this.activitySupporter;
        if (item != null &&
                Long.parseLong("" + item.getUid()) != -1L &&
                appContainer.getMode() == UserMode.OBJECT_ADDING_EDGE &&
                !previousObj.equals(currentObj)
        ) {


            if (!currentObj.belongToTopoLayer() || !previousObj.belongToTopoLayer()) {
                GPDialogs.toast(this.activitySupporter.getContext(), R.string.no_topology_layer, Toast.LENGTH_SHORT);
                return true;
            } else if (currentObj.getIsCompleted() || previousObj.getIsCompleted()) {
                GPDialogs.toast(this.activitySupporter.getContext(), R.string.obj_destination_completed, Toast.LENGTH_SHORT);
                return true;
            }

            if (LatLongUtils.soFar(previousObj, LatLongUtils.getMaxDistance(), currentObj)) {

                GPDialogs.yesNoMessageDialog((MapviewActivity) this.activitySupporter,
                        String.format(activity.getString(cu.phibrain.cardinal.app.R.string.max_distance_threshold_broken_message_edge),
                                LatLongUtils.getMaxDistance()),
                        () -> activity.runOnUiThread(() -> {
                            // yes
                            GPLog.addLogEntry(String.format(activity.getString(cu.phibrain.cardinal.app.R.string.max_distance_threshold_broken_message_edge),
                                    LatLongUtils.getMaxDistance()));

                            RouteSegment edge = new RouteSegment(null, previousObj.getId(), currentObj.getId());
                            RouteSegmentOperations.getInstance().save(edge);
                            appContainer.setMode(UserMode.NONE);
                            mapView.releaseMapBlock();
                            try {
                                mapView.reloadLayer(CardinalEdgesLayer.class);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }), () -> activity.runOnUiThread(() -> {
                            // no


                        })
                );
            } else {

                RouteSegment edge = new RouteSegment(null, previousObj.getId(), currentObj.getId());
                RouteSegmentOperations.getInstance().save(edge);
                appContainer.setMode(UserMode.NONE);
                mapView.releaseMapBlock();
                try {
                    mapView.reloadLayer(CardinalEdgesLayer.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            //Update ui
            Intent intent = new Intent(MapviewActivity.ACTION_UPDATE_UI);
            intent.putExtra("update_map_object_active", true);
            activity.sendBroadcast(intent);

        }
        return true;
    }

    private boolean joinMo(MarkerItem item, MapObject currentObj, MapObject previousObj) {
        MapviewActivity activity = (MapviewActivity) this.activitySupporter;
        if (item != null &&
                Long.parseLong("" + item.getUid()) != CardinalPointLayer.SELECT_MARKER_UID &&
                appContainer.getMode() == UserMode.OBJECT_DOCK &&
                !previousObj.equals(currentObj) &&
                currentObj.getJoinObj() == null
        ) {
            if (previousObj.getJoinId() != null && previousObj.getJoinId() == currentObj.getId()) { // para evitar un ciclo en la agregación
                GPDialogs.quickInfo(mapView, activity.getString(cu.phibrain.cardinal.app.R.string.map_object_already_docked_to_that_mapobject_message));
                return false;
            }

            if (LatLongUtils.soFar(previousObj, LatLongUtils.getRadiusJoinMo(), currentObj)) {
                GPDialogs.yesNoMessageDialog((MapviewActivity) this.activitySupporter,
                        String.format(activity.getString(cu.phibrain.cardinal.app.R.string.max_distance_threshold_broken_message_jointo),
                                LatLongUtils.getRadiusJoinMo()),
                        () -> activity.runOnUiThread(() -> {
                            // yes
                            GPLog.addLogEntry(String.format(activity.getString(cu.phibrain.cardinal.app.R.string.max_distance_threshold_broken_message_jointo),
                                    LatLongUtils.getRadiusJoinMo()));

                            currentObj.setJoinObj(previousObj);
                            MapObjectOperations.getInstance().save(currentObj);
                            appContainer.setMode(UserMode.NONE);
                            mapView.releaseMapBlock();
                            try {
                                mapView.reloadLayer(CardinalJoinsLayer.class);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }), () -> activity.runOnUiThread(() -> {
                            // no


                        })
                );
            } else {
                currentObj.setJoinObj(previousObj);
                MapObjectOperations.getInstance().save(currentObj);
                appContainer.setMode(UserMode.NONE);
                mapView.releaseMapBlock();
                try {
                    mapView.reloadLayer(CardinalJoinsLayer.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //Update ui
            Intent intent = new Intent(MapviewActivity.ACTION_UPDATE_UI);
            intent.putExtra("update_map_object_active", true);
            activity.sendBroadcast(intent);
            return true;
        } else if (currentObj.getJoinObj() != null) {
            GPDialogs.quickInfo(mapView, activity.getString(R.string.map_object_already_docked_message));
        }
        return false;
    }


    private MarkerItem getMarkerById(Long id) {
        for (MarkerItem item : getItemList()) {
            if (id.equals(Long.parseLong("" + item.getUid())))
                return item;
        }
        return null;
    }
}
