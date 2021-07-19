
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
import android.graphics.PorterDuff.Mode;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.List;

import eu.geopaparazzi.library.database.GPLog;
import eu.geopaparazzi.library.util.Compat;
import eu.geopaparazzi.library.util.GPDialogs;
import eu.geopaparazzi.map.GPMapView;
import eu.geopaparazzi.map.R;
import eu.geopaparazzi.map.features.editing.EditManager;
import eu.geopaparazzi.map.features.tools.impl.InfoTool;
import eu.geopaparazzi.map.features.tools.impl.LineCreateFeatureToolGroup;
import eu.geopaparazzi.map.features.tools.impl.SelectionTool;
import eu.geopaparazzi.map.features.tools.interfaces.Tool;
import eu.geopaparazzi.map.features.tools.interfaces.ToolGroup;
import eu.geopaparazzi.map.layers.LayerManager;
import eu.geopaparazzi.map.layers.interfaces.IEditableLayer;

/**
 * The main line layer editing tool group, which just shows the tool palette.
 *
 * @author Andrea Antonello (www.hydrologis.com)
 */
public class CardinalLineMainEditingToolGroup implements ToolGroup, OnClickListener, OnTouchListener {

    private ImageButton selectAllButton;
    private GPMapView mapView;

    private ImageButton selectEditableButton;
    private int selectionColor;
    private ImageButton createFeatureButton;
    private ImageButton commitButton;

    private ImageButton undoButton;

    /**
     * Constructor.
     *
     * @param mapView the map view.
     */
    public CardinalLineMainEditingToolGroup(GPMapView mapView) {
        this.mapView = mapView;

        LinearLayout parent = EditManager.INSTANCE.getToolsLayout();
        selectionColor = Compat.getColor(parent.getContext(), R.color.main_selection);
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
            createFeatureButton.setBackground(Compat.getDrawable(context, R.drawable.ic_editing_create_line_24dp));
            createFeatureButton.setPadding(0, padding, 0, padding);
            createFeatureButton.setOnClickListener(this);
            createFeatureButton.setOnTouchListener(this);
            parent.addView(createFeatureButton);

            selectEditableButton = new ImageButton(context);
            selectEditableButton.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));
            selectEditableButton.setBackground(Compat.getDrawable(context, R.drawable.ic_editing_select_editable_24dp));
            selectEditableButton.setPadding(0, padding, 0, padding);
            selectEditableButton.setOnClickListener(this);
            selectEditableButton.setOnTouchListener(this);
            parent.addView(selectEditableButton);
        }

        selectAllButton = new ImageButton(context);
        selectAllButton.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        Tool activeTool = EditManager.INSTANCE.getActiveTool();
        if (activeTool instanceof InfoTool) {
            selectAllButton.setBackground(Compat.getDrawable(context, R.drawable.ic_editing_select_all_active_24dp));
        } else {
            selectAllButton.setBackground(Compat.getDrawable(context, R.drawable.ic_editing_select_all_24dp));
        }
        selectAllButton.setPadding(0, padding, 0, padding);
        selectAllButton.setOnClickListener(this);
        selectAllButton.setOnTouchListener(this);
        parent.addView(selectAllButton);

        if (editLayer != null) {
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
    }

    public void disable() {
        EditManager.INSTANCE.setActiveTool(null);
        LinearLayout parent = EditManager.INSTANCE.getToolsLayout();
        if (parent != null)
            parent.removeAllViews();
    }

    public void onClick(View v) {
        if (v == selectAllButton) {
            Tool currentTool = EditManager.INSTANCE.getActiveTool();
            if (currentTool != null && currentTool instanceof InfoTool) {
                // if the same tool is re-selected, it is disabled
                EditManager.INSTANCE.setActiveTool(null);
            } else {
                // check maps enablement
                try {
                    List<IEditableLayer> editableLayers = LayerManager.INSTANCE.getEditableLayers(mapView);
                    final Context context = EditManager.INSTANCE.getEditingView().getContext();
                    if (editableLayers.size() == 0) {
                        LinearLayout parent = EditManager.INSTANCE.getToolsLayout();
                        if (parent != null) {
                            GPDialogs.warningDialog(context, context.getString(R.string.no_queriable_layer_is_visible), null);
                        }
                        return;
                    }
                } catch (Exception e) {
                    GPLog.error(this, null, e);
                }

                Tool activeTool = new InfoTool(this, mapView);
                EditManager.INSTANCE.setActiveTool(activeTool);
            }
        } else if (v == selectEditableButton) {
            Tool currentTool = EditManager.INSTANCE.getActiveTool();
            if (currentTool instanceof SelectionTool) {
                // if the same tool is re-selected, it is disabled
                EditManager.INSTANCE.setActiveTool(null);
            } else {
                Tool activeTool = new SelectionTool(mapView);
                EditManager.INSTANCE.setActiveTool(activeTool);
            }
        } else if (v == createFeatureButton) {
            ToolGroup createFeatureToolGroup = new LineCreateFeatureToolGroup(mapView, null);
            EditManager.INSTANCE.setActiveToolGroup(createFeatureToolGroup);
        } else if (v == undoButton) {
//            if (cutExtendProcessedFeature != null) {
//                EditManager.INSTANCE.setActiveTool(null);
//                commitButton.setVisibility(View.GONE);
//                undoButton.setVisibility(View.GONE);
//                EditManager.INSTANCE.invalidateEditingView();
//            }
        }

        handleToolIcons(v);
    }

    @SuppressWarnings("deprecation")
    private void handleToolIcons(View activeToolButton) {
        Context context = activeToolButton.getContext();
        Tool currentTool = EditManager.INSTANCE.getActiveTool();
        if (selectEditableButton != null) {
            if (currentTool != null && activeToolButton == selectEditableButton) {
                selectEditableButton.setBackground(Compat.getDrawable(context,
                        R.drawable.ic_editing_select_editable_active_24dp));
            } else {
                selectEditableButton.setBackground(Compat.getDrawable(context,
                        R.drawable.ic_editing_select_editable_24dp));
            }
        }
        if (selectAllButton != null)
            if (currentTool != null && activeToolButton == selectAllButton) {
                selectAllButton
                        .setBackground(Compat.getDrawable(context, R.drawable.ic_editing_select_all_active_24dp));
            } else {
                selectAllButton.setBackground(Compat.getDrawable(context, R.drawable.ic_editing_select_all_24dp));
            }
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                v.getBackground().setColorFilter(selectionColor, Mode.SRC_ATOP);
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

