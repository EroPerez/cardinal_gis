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
package cu.phibrain.cardinal.app.ui.map.tools;


import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import cu.phibrain.cardinal.app.CardinalApplication;
import cu.phibrain.cardinal.app.MapviewActivity;
import cu.phibrain.cardinal.app.injections.AppContainer;
import cu.phibrain.cardinal.app.injections.UserMode;
import cu.phibrain.plugins.cardinal.io.utils.ImageUtil;
import eu.geopaparazzi.library.GPApplication;
import eu.geopaparazzi.library.util.Compat;
import eu.geopaparazzi.library.util.IActivitySupporter;
import eu.geopaparazzi.map.GPMapView;
import eu.geopaparazzi.map.R;
import eu.geopaparazzi.map.features.editing.EditManager;
import eu.geopaparazzi.map.features.tools.interfaces.Tool;
import eu.geopaparazzi.map.features.tools.interfaces.ToolGroup;
import eu.geopaparazzi.map.layers.interfaces.IEditableLayer;

/**
 * The main polygon layer editing tool group, which just shows the tool palette.
 *
 * @author Andrea Antonello (www.hydrologis.com)
 */
public class CardinalPolygonMainEditingToolGroup implements ToolGroup, OnClickListener, OnTouchListener {

    private AppContainer appContainer;
    private GPMapView mapView;

    private int selectionColor;
    private ImageButton createFeatureButton;
    private ImageButton commitButton;

    private ImageButton undoButton;
    private ImageButton editButton;
    private ImageButton editCoordButton;
    private ImageButton deleteButton;

    // To handle edit action
    private static IActivitySupporter activitySupporter;

    public static void setActivitySupporter(IActivitySupporter activity) {
        if (activitySupporter == null) {
            activitySupporter = activity;
        }
    }

    /**
     * Constructor.
     *
     * @param mapView the map view.
     * @param activitySupporter the map view activity
     */
    public CardinalPolygonMainEditingToolGroup(GPMapView mapView, IActivitySupporter activitySupporter) {
        this.mapView = mapView;

        LinearLayout parent = EditManager.INSTANCE.getToolsLayout();
        selectionColor = Compat.getColor(parent.getContext(), R.color.main_selection);

        appContainer = ((CardinalApplication) GPApplication.getInstance()).getContainer();

        setActivitySupporter(activitySupporter);
    }
    /**
     * Constructor user en create tool.
     *
     * @param mapView the map view.
     */
    public CardinalPolygonMainEditingToolGroup(GPMapView mapView) {
        this.mapView = mapView;

        LinearLayout parent = EditManager.INSTANCE.getToolsLayout();
        selectionColor = Compat.getColor(parent.getContext(), R.color.main_selection);

        appContainer = ((CardinalApplication) GPApplication.getInstance()).getContainer();

        setActivitySupporter(null);
    }

    public void activate() {
        if (mapView != null)
            mapView.setClickable(true);
    }

    public void initUI() {

        LinearLayout parent = EditManager.INSTANCE.getToolsLayout();
        Context context = parent.getContext();
        IEditableLayer editLayer = EditManager.INSTANCE.getEditLayer();
        int padding = 2;

        if (editLayer != null) {

            createFeatureButton = new ImageButton(context);
            createFeatureButton.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));
            createFeatureButton.setBackground(Compat.getDrawable(context, R.drawable.ic_editing_create_polygon_24dp));
            createFeatureButton.setPadding(0, padding, 0, padding);
            createFeatureButton.setOnClickListener(this);
            createFeatureButton.setOnTouchListener(this);
            parent.addView(createFeatureButton);

            undoButton = new ImageButton(context);
            undoButton.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            undoButton.setBackground(Compat.getDrawable(context, R.drawable.ic_editing_undo_24dp));
            undoButton.setPadding(0, padding, 0, padding);
            undoButton.setOnTouchListener(this);
            undoButton.setOnClickListener(this);
            parent.addView(undoButton);
            undoButton.setVisibility(View.GONE);

            commitButton = new ImageButton(context);
            commitButton.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            commitButton.setBackground(Compat.getDrawable(context, R.drawable.ic_editing_commit_24dp));
            commitButton.setPadding(0, padding, 0, padding);
            commitButton.setOnTouchListener(this);
            commitButton.setOnClickListener(this);
            parent.addView(commitButton);
            commitButton.setVisibility(View.GONE);
        }

        if (editLayer != null && appContainer.getCurrentMapObject() != null) {
            //edit
            editButton = new ImageButton(context);
            editButton.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            if (appContainer.getMode() == UserMode.OBJECT_EDITION)
                editButton
                        .setBackground(Compat.getDrawable(context, cu.phibrain.cardinal.app.R.drawable.ic_mapview_toggle_editing_on_24dp));
            else
                editButton
                        .setBackground(Compat.getDrawable(context, cu.phibrain.cardinal.app.R.drawable.ic_mapview_toggle_editing_off_24dp));

            editButton.setPadding(0, padding, 0, padding);
            editButton.setOnTouchListener(this);
            editButton.setOnClickListener(this);
            parent.addView(editButton);
            if (activitySupporter == null)
                editButton.setVisibility(View.GONE);

            //edit coord
            editCoordButton = new ImageButton(context);
            editCoordButton.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            editCoordButton.setImageBitmap(ImageUtil.getBitmap(context, cu.phibrain.cardinal.app.R.drawable.ic_marker_map_24dp));
            editCoordButton
                    .setBackground(Compat.getDrawable(context, cu.phibrain.cardinal.app.R.drawable.button_background_states));
            editCoordButton.setPadding(0, padding, 0, padding);
            editCoordButton.setOnTouchListener(this);
            editCoordButton.setOnClickListener(this);
            parent.addView(editCoordButton);

            //delete
            deleteButton = new ImageButton(context);
            deleteButton.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            deleteButton.setImageBitmap(ImageUtil.getBitmap(context, cu.phibrain.cardinal.app.R.drawable.ic_delete_24dp));
            deleteButton
                    .setBackground(Compat.getDrawable(context, cu.phibrain.cardinal.app.R.drawable.button_background_states));
            deleteButton.setPadding(0, padding, 0, padding);
            deleteButton.setOnTouchListener(this);
            deleteButton.setOnClickListener(this);
            parent.addView(deleteButton);

        }
    }

    public void disable() {
        EditManager.INSTANCE.setActiveTool(null);
        LinearLayout parent = EditManager.INSTANCE.getToolsLayout();
        if (parent != null)
            parent.removeAllViews();
    }

    public void onClick(View v) {
        if (v == createFeatureButton) {
            ToolGroup createFeatureToolGroup = new CardinalPolygonCreateFeatureToolGroup(mapView, UserMode.OBJECT_COORD_EDITION);
            EditManager.INSTANCE.setActiveToolGroup(createFeatureToolGroup);
        }  else if (v == undoButton) {
            editCoordButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
            //appContainer.setMode(UserMode.NONE);
        } else if (v == editButton) {
            if (appContainer.getMode() == UserMode.NONE) {
                appContainer.setMode(UserMode.OBJECT_EDITION);
                ((MapviewActivity)activitySupporter).onMenuMTO();
                deleteButton.setVisibility(View.GONE);
                editCoordButton.setVisibility(View.GONE);
            } else {
                appContainer.setMode(UserMode.NONE);
                editCoordButton.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);
            }
        } else if (v == deleteButton) {
            IEditableLayer editLayer = EditManager.INSTANCE.getEditLayer();
            appContainer.setMode(UserMode.OBJECT_DELETION);
            try {
                editLayer.deleteFeatures(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (v == editCoordButton) {
            appContainer.setMode(UserMode.OBJECT_COORD_EDITION);
            ToolGroup createFeatureToolGroup = new CardinalPolygonCreateFeatureToolGroup(mapView, UserMode.OBJECT_COORD_EDITION);
            EditManager.INSTANCE.setActiveToolGroup(createFeatureToolGroup);
        }

        handleToolIcons(v);
    }

    @SuppressWarnings("deprecation")
    private void handleToolIcons(View activeToolButton) {
        Context context = activeToolButton.getContext();
        Tool currentTool = EditManager.INSTANCE.getActiveTool();
        if (editButton != null)
            if (appContainer.getMode() == UserMode.OBJECT_EDITION && activeToolButton == editButton) {
                editButton
                        .setBackground(Compat.getDrawable(context, cu.phibrain.cardinal.app.R.drawable.ic_mapview_toggle_editing_on_24dp));
            } else {
                editButton.setBackground(Compat.getDrawable(context, cu.phibrain.cardinal.app.R.drawable.ic_mapview_toggle_editing_off_24dp));
            }
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                v.invalidate();
                break;
            }
            case MotionEvent.ACTION_UP: {
                v.getBackground().clearColorFilter();
                v.invalidate();
                break;
            }
        }
        return false;
    }

    public void onToolFinished(Tool tool) {

    }

    public void onToolDraw(Canvas canvas) {
        // nothing to draw
    }

    public boolean onToolTouchEvent(MotionEvent event) {
        return false;
    }

    public void onGpsUpdate(double lon, double lat) {
        // ignore
    }
}
