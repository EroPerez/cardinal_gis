package cu.phibrain.cardinal.app.ui.layer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.preference.PreferenceManager;
import android.widget.Toast;

import org.hortonmachine.dbs.datatypes.EGeometryType;
import org.json.JSONException;
import org.json.JSONObject;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.oscim.android.canvas.AndroidGraphics;
import org.oscim.backend.CanvasAdapter;
import org.oscim.backend.canvas.Bitmap;
import org.oscim.backend.canvas.Color;
import org.oscim.layers.marker.ItemizedLayer;
import org.oscim.layers.marker.MarkerItem;
import org.oscim.layers.marker.MarkerSymbol;
import org.oscim.layers.vector.geometries.CircleDrawable;
import org.oscim.layers.vector.geometries.Style;
import org.oscim.map.Layers;
import org.oscim.map.Map;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cu.phibrain.cardinal.app.CardinalApplication;
import cu.phibrain.cardinal.app.MapviewActivity;
import cu.phibrain.cardinal.app.R;
import cu.phibrain.cardinal.app.helpers.LatLongUtils;
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
import eu.geopaparazzi.library.database.GPLog;
import eu.geopaparazzi.library.images.ImageUtilities;
import eu.geopaparazzi.library.style.ColorUtilities;
import eu.geopaparazzi.library.util.Compat;
import eu.geopaparazzi.library.util.GPDialogs;
import eu.geopaparazzi.library.util.IActivitySupporter;
import eu.geopaparazzi.library.util.LibraryConstants;
import eu.geopaparazzi.map.GPGeoPoint;
import eu.geopaparazzi.map.GPMapPosition;
import eu.geopaparazzi.map.GPMapView;
import eu.geopaparazzi.map.features.Feature;
import eu.geopaparazzi.map.layers.interfaces.IEditableLayer;
import eu.geopaparazzi.map.layers.interfaces.ISystemLayer;

public class CardinalSelectPointLayer extends ItemizedLayer<MarkerItem> implements ItemizedLayer.OnItemGestureListener<MarkerItem>, ISystemLayer {
    public static final String NONFORMSTART = "@";
    public static final int FORMUPDATE_RETURN_CODE = 669;
    private static final int FG_COLOR = 0xFF000000; // 100 percent black. AARRGGBB
    private static final int BG_COLOR = 0x80FF69B4; // 50 percent pink. AARRGGBB
    private static final int TRANSP_WHITE = 0x80FFFFFF; // 50 percent white. AARRGGBB
    private static String NAME = null;
    private static Bitmap mtoBitmap;
    private static int textSize;
    private static String colorStr;
    public static final long SELECT_MARKER_UID = -1L;
    private GPMapView mapView;
    private IActivitySupporter activitySupporter;
    private  MarkerItem selectMarker = null;
    private  MarkerItem joinMarker =null;
    //    List<MapObject> mapObjectsList;
    private AppContainer appContainer;

    public CardinalSelectPointLayer(GPMapView mapView, IActivitySupporter activitySupporter) throws IOException {
        super(mapView.map(), getMarkerSymbol(mapView));
        this.mapView = mapView;
        getName(mapView.getContext());
        this.activitySupporter = activitySupporter;
        setOnItemGestureListener(this);
        appContainer = ((CardinalApplication) CardinalApplication.getInstance()).getContainer();
        try {
            reloadData();
        } catch (IOException e) {
            GPLog.error(this, null, e);
        }


    }

    public CardinalSelectPointLayer(Map map, MarkerSymbol defaultMarker) {
        super(map, defaultMarker);
    }

    public static String getName(Context context) {
        if (NAME == null) {
            NAME = context.getString(R.string.layername_mot);
        }
        return NAME;
    }

    private static MarkerSymbol getMarkerSymbol(GPMapView mapView) throws IOException {
        SharedPreferences peferences = PreferenceManager.getDefaultSharedPreferences(mapView.getContext());
        //String textSizeStr = peferences.getString(LibraryConstants.PREFS_KEY_NOTES_TEXT_SIZE, LibraryConstants.DEFAULT_NOTES_SIZE + ""); //$NON-NLS-1$
        colorStr = peferences.getString(LibraryConstants.PREFS_KEY_NOTES_CUSTOMCOLOR, ColorUtilities.ALMOST_BLACK.getHex());
        Drawable imagesDrawable = Compat.getDrawable(mapView.getContext(), eu.geopaparazzi.library.R.drawable.ic_bookmarks_48dp);

        mtoBitmap = AndroidGraphics.drawableToBitmap(imagesDrawable);
        //byte [] icon = mtoMapObjecType.getIconAsByteArray();
        //mtoBitmap = AndroidGraphics.decodeBitmap(new ByteArrayInputStream(icon));

        return new MarkerSymbol(mtoBitmap, MarkerSymbol.HotspotPlace.UPPER_LEFT_CORNER, false);
    }

    private GPGeoPoint centerPoint(MapObject mapObject) {
        return LatLongUtils.centerPoint(mapObject.getCoord(), mapObject.getObjectType().getGeomType());
    }

    @Override
    public void reloadData() throws IOException {

        MapObject currentMo = appContainer.getCurrentMapObject();
        removeAllItems();
        List<MarkerItem> markerItems = new ArrayList<>();
        if(currentMo!=null) {
            selectMarker = new MarkerItem(1, "asdas", "22", centerPoint(currentMo));
            Drawable imagesDrawable = Compat.getDrawable(mapView.getContext(), R.drawable.long_select_mto);
            mtoBitmap = AndroidGraphics.drawableToBitmap(imagesDrawable);
            selectMarker.setMarker(new MarkerSymbol(mtoBitmap, MarkerSymbol.HotspotPlace.CENTER, false));


            //joinMarker.setMarker(markerJoin);
            //probar esto
            //imagesDrawable.setLevel()
            ShapeDrawable joinCircle= new ShapeDrawable( new OvalShape());
            joinCircle.setIntrinsicHeight( 200 );
            joinCircle.setIntrinsicWidth( 200);
            joinCircle.setBounds(new Rect(0, 0, 200, 200));
            joinCircle.getPaint().setColor(Color.BLUE);
            joinCircle.getPaint().setStyle(Paint.Style.STROKE);
            joinCircle.getPaint().setStrokeWidth(2f);
            joinCircle.getPaint().setStrokeCap(Paint.Cap.ROUND);

//            CircleDrawable circle = new CircleDrawable(centerPoint(currentMo),1, Style.builder()
//                    .strokeColor(android.graphics.Color.YELLOW)
//                    .strokeWidth(2f)
//                    .cap(org.oscim.backend.canvas.Paint.Cap.ROUND)
//                    .build());
            joinMarker = new MarkerItem(2, "", "", centerPoint(currentMo));
            joinMarker.setMarker(new MarkerSymbol(AndroidGraphics.drawableToBitmap(joinCircle), MarkerSymbol.HotspotPlace.CENTER, false));
            markerItems.add(selectMarker);
            markerItems.add(joinMarker);
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

        return true;

    }

    @Override
    public boolean onItemLongPress(int index, MarkerItem item) {

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
        //jo.put(LAYERID_TAG, 1L);
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

}
