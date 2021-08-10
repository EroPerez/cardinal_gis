package cu.phibrain.cardinal.app.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Date;
import java.util.List;

import cu.phibrain.cardinal.app.CardinalApplication;
import cu.phibrain.cardinal.app.MapviewActivity;
import cu.phibrain.cardinal.app.R;
import cu.phibrain.cardinal.app.helpers.LatLongUtils;
import cu.phibrain.cardinal.app.injections.AppContainer;
import cu.phibrain.cardinal.app.injections.UserMode;
import cu.phibrain.cardinal.app.ui.adapter.LabelAutoCompleteAdapter;
import cu.phibrain.cardinal.app.ui.layer.CardinalGPMapView;
import cu.phibrain.cardinal.app.ui.layer.CardinalSelectPointLayer;
import cu.phibrain.cardinal.app.ui.layer.EdgesLayer;
import cu.phibrain.plugins.cardinal.io.database.entity.model.LabelSubLot;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Layer;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjecType;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObject;
import cu.phibrain.plugins.cardinal.io.database.entity.model.RouteSegment;
import cu.phibrain.plugins.cardinal.io.database.entity.model.WorkSession;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.LabelSubLotOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.RouteSegmentOperations;
import eu.geopaparazzi.library.database.GPLog;
import eu.geopaparazzi.library.util.GPDialogs;
import eu.geopaparazzi.map.GPGeoPoint;
import eu.geopaparazzi.map.GPMapView;
import eu.geopaparazzi.map.features.editing.EditManager;
import eu.geopaparazzi.map.layers.interfaces.IGpLayer;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     BarcodeReaderDialogFragment.newInstance(mapView, points).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class BarcodeReaderDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    // TODO: Customize parameter argument names


    LabelAutoCompleteAdapter labelAutoCompleteAdapter;
    private BottomSheetBehavior mBehavior;
    private AppContainer appContainer;
    //View Objects
    private ImageButton buttonScan;
    private ImageButton buttonSave;
    private AutoCompleteTextView autoCompleteTextViewCode;
    //qr code scanner object
    private IntentIntegrator code128BarcodeScan;


    // object data
    private List<GPGeoPoint> coordinates;
    private GPMapView mapView;
    private LabelSubLot label;
    private WorkSession currentSession;
    private long grade;

    public BarcodeReaderDialogFragment() {
    }

    public BarcodeReaderDialogFragment(GPMapView mapView, List<GPGeoPoint> points, long grade) {
        super();
        this.mapView = mapView;
        this.coordinates = points;
        this.grade = grade;
    }

    // TODO: Customize parameters
    public static BarcodeReaderDialogFragment newInstance(GPMapView mapView, List<GPGeoPoint> points, long grade) {
        final BarcodeReaderDialogFragment fragment = new BarcodeReaderDialogFragment(mapView, points, grade);
        final Bundle args = new Bundle();
//        args.putLong(ARG_ITEM_COUNT, itemCount);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = new ObjectInspectorDialogFragment.MyBottomSheetDialog(this.getContext(), cu.phibrain.cardinal.app.R.style.BottomSheetDialogTheme);

        View view = View.inflate(getContext(), R.layout.fragment_barcode_reader_dialog_list_dialog, null);


        appContainer = ((CardinalApplication) CardinalApplication.getInstance()).getContainer();
        currentSession = appContainer.getWorkSessionActive();

        //View objects
        buttonScan = (ImageButton) view.findViewById(R.id.scanBtn);
        buttonSave = (ImageButton) view.findViewById(R.id.imgBtnSave);
        buttonSave.setVisibility(View.GONE);
        autoCompleteTextViewCode = view.findViewById(R.id.autoCompleteTextViewCode);
        labelAutoCompleteAdapter = new LabelAutoCompleteAdapter(
                this.getContext(), R.layout.spinner_inv, R.id.tvSpinnerValue,
                LabelSubLotOperations.getInstance().loadAll(currentSession.getId(), false)
        );
        autoCompleteTextViewCode.setAdapter(labelAutoCompleteAdapter);
        autoCompleteTextViewCode.setOnItemClickListener((adapterView, view1, position, id) -> {
            //this is the way to find selected object/item
            label = (LabelSubLot) adapterView.getItemAtPosition(position);
            if (label != null) {
                buttonSave.setVisibility(View.VISIBLE);
            } else {
                buttonSave.setVisibility(View.GONE);
            }

        });

        //intializing scan object
        code128BarcodeScan = IntentIntegrator.forSupportFragment(this); // `this` is the current Fragment
        code128BarcodeScan.setDesiredBarcodeFormats(IntentIntegrator.CODE_128);
        code128BarcodeScan.setPrompt(getString(R.string.scan_map_object_barcode_message));
        code128BarcodeScan.setOrientationLocked(false);

        //attaching onclick listener
        buttonScan.setOnClickListener(this);
        buttonSave.setOnClickListener(this);


        dialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        return dialog;
    }


    @Override
    public void onStart() {
        super.onStart();
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void dismiss() {

        super.dismiss();

    }


    //Getting the scan results
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                GPDialogs.toast(getActivity(), getString(R.string.barcode_scannig_error_message), Toast.LENGTH_LONG);
            } else {
                //if qr contains data
                String barcode = result.getContents();
                label = LabelSubLotOperations.getInstance().load(currentSession.getId(), barcode);
                if (label != null && !label.getGeolocated()) { //setting values to textviews
                    autoCompleteTextViewCode.setText(label.toString(), true);
                    buttonSave.setVisibility(View.VISIBLE);
                } else {
                    buttonSave.setVisibility(View.GONE);
                    GPDialogs.toast(getActivity(), String.format(getString(R.string.barcode_notavailable_message), barcode), Toast.LENGTH_LONG);
                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.scanBtn) {
            //initiating the qr code scan
            code128BarcodeScan.initiateScan();
        } else if (i == R.id.imgBtnSave) {
            FragmentActivity activity = getActivity();
            boolean terminalFound = false;
            try {
                MapObjecType currentSelectedObjectType = appContainer.getMapObjecTypeActive();
                MapObject previousObj = appContainer.getCurrentMapObject();
                Layer currentSelectedObjectTypeLayer = currentSelectedObjectType.getLayerObj();

                MapObject currentObj = new MapObject();
                currentObj.setCode(label.toString());
                currentObj.setCoord(this.coordinates);
                currentObj.setMapObjectTypeId(currentSelectedObjectType.getId());

                grade = currentSelectedObjectType.getIsTerminal() ? 1 : grade;

                currentObj.setNodeGrade(grade);
                currentObj.setSessionId(currentSession.getId());
                currentObj.setIsCompleted(false);
                currentObj.setCreatedAt(new Date());
                currentObj.setElevation(mapView.getMapPosition().getZoomLevel() + 0.0f);

                MapObjectOperations.getInstance().save(currentObj);

                if (!currentObj.isTerminal()) {
                    appContainer.setCurrentMapObject(currentObj);
                } else {
                    appContainer.setCurrentMapObject(null);
                    appContainer.setMapObjecTypeActive(null);
                    appContainer.setNetworksActive(null);
                    terminalFound = true;
                }
                appContainer.setMode(UserMode.NONE);
                reloadLayer(currentSelectedObjectTypeLayer);
                GPDialogs.quickInfo(mapView, getString(R.string.map_object_saved_message));

//                LatLongUtils.showTip(activity, LatLongUtils.distance(previousObj, currentObj));

                if (!currentObj.belongToTopoLayer() ||
                        previousObj == null ||
                        !previousObj.belongToTopoLayer() ||
                        previousObj.getIsCompleted()) {
                    refreshUI(terminalFound);
                    return;
                }


                if (LatLongUtils.soFar(previousObj, LatLongUtils.getMaxDistance(), currentObj)) {
                    final boolean tf = terminalFound;
                    GPDialogs.yesNoMessageDialog(activity,
                            String.format(getString(R.string.max_distance_threshold_broken_message),
                                    LatLongUtils.getMaxDistance()),
                            () -> activity.runOnUiThread(() -> {
                                // yes
                                appContainer.setCurrentMapObject(null);
                                refreshUI(tf);

                            }), () -> activity.runOnUiThread(() -> {
                                // no
                                GPLog.addLogEntry(String.format(activity.getString(R.string.max_distance_threshold_broken_message),
                                        LatLongUtils.getMaxDistance()));

                                RouteSegment edge = new RouteSegment(null, previousObj.getId(), currentObj.getId(), new Date());
                                RouteSegmentOperations.getInstance().save(edge);
                                try {
                                    mapView.reloadLayer(EdgesLayer.class);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                refreshUI(tf);
                            })
                    );
                } else {

                    RouteSegment edge = new RouteSegment(null, previousObj.getId(), currentObj.getId(), new Date());
                    RouteSegmentOperations.getInstance().save(edge);
                    mapView.reloadLayer(EdgesLayer.class);
                    refreshUI(terminalFound);
                }




            } catch (Exception e) {
                GPLog.error(this, e.getLocalizedMessage(), e);
                e.printStackTrace();

                refreshUI(terminalFound);

            }

        }
    }

    void reloadLayer(Layer layer) throws Exception {

        ((IGpLayer) EditManager.INSTANCE.getEditLayer()).reloadData();
        ((CardinalGPMapView) mapView).reloadLayer(layer.getId());
        ((CardinalGPMapView) mapView).reloadLayer(CardinalSelectPointLayer.class);

    }

    void refreshUI(boolean terminalFound) {
        //Update ui
        Intent intent = new Intent(MapviewActivity.ACTION_UPDATE_UI);
        intent.putExtra("update_map_object_active", true);
        intent.putExtra("update_map_object_type_active", true);
        intent.putExtra("is_map_object_terminal", terminalFound);
        getActivity().sendBroadcast(intent);


        dismiss();
    }

}

