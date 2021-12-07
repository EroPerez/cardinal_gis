package cu.phibrain.cardinal.app.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import cu.phibrain.cardinal.app.helpers.NumberUtiles;
import cu.phibrain.cardinal.app.injections.AppContainer;
import cu.phibrain.cardinal.app.injections.UserMode;
import cu.phibrain.cardinal.app.ui.activities.CameraMapObjectActivity;
import cu.phibrain.cardinal.app.ui.adapter.LabelAutoCompleteAdapter;
import cu.phibrain.cardinal.app.ui.layer.BifurcationLayer;
import cu.phibrain.cardinal.app.ui.layer.CardinalEdgesLayer;
import cu.phibrain.cardinal.app.ui.layer.CardinalGPMapView;
import cu.phibrain.cardinal.app.ui.layer.CardinalJoinsLayer;
import cu.phibrain.cardinal.app.ui.layer.CardinalLineLayer;
import cu.phibrain.cardinal.app.ui.layer.CardinalSelectPointLayer;
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
import eu.geopaparazzi.library.images.ImageUtilities;
import eu.geopaparazzi.library.util.GPDialogs;
import eu.geopaparazzi.library.util.LibraryConstants;
import eu.geopaparazzi.library.util.PositionUtilities;
import eu.geopaparazzi.map.GPGeoPoint;
import eu.geopaparazzi.map.GPMapView;
import eu.geopaparazzi.map.features.editing.EditManager;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     BarcodeReaderDialogFragment.newInstance(mapView, points).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class BarcodeReaderDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    // TODO: Customize parameter argument names
    private final int RETURNCODE_FOR_TAKE_PICTURE = 666;

    LabelAutoCompleteAdapter labelAutoCompleteAdapter;
    private BottomSheetBehavior mBehavior;
    private AppContainer appContainer;
    //View Objects
    private ImageButton buttonScan;
    private ImageButton buttonSave;
    private AutoCompleteTextView autoCompleteTextViewCode;
    private CheckBox checkBoxNoLabel;
    //qr code scanner object
    private IntentIntegrator code128BarcodeScan;


    // object data
    private List<GPGeoPoint> coordinates;
    private GPMapView mapView;
    private LabelSubLot label;
    private WorkSession currentSession;
    private long grade;
    private boolean compositeMode;
    private Button continueButton;
    private MapObject currentObj;

    public BarcodeReaderDialogFragment() {
    }

    public BarcodeReaderDialogFragment(GPMapView mapView, List<GPGeoPoint> points, long grade) {
        super();
        this.mapView = mapView;
        this.coordinates = points;
        this.grade = grade;
        this.compositeMode = false;
    }

    // TODO: Customize parameters
    public static BarcodeReaderDialogFragment newInstance(GPMapView mapView, List<GPGeoPoint> points, long grade) {
        final BarcodeReaderDialogFragment fragment = new BarcodeReaderDialogFragment(mapView, points, grade);
        final Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = new ObjectInspectorDialogFragment.MyBottomSheetDialog(
        BarcodeReaderDialogFragment.this.getContext(), cu.phibrain.cardinal.app.R.style.BottomSheetDialogTheme
        );

        View view = View.inflate(BarcodeReaderDialogFragment.this.getContext(), R.layout.fragment_barcode_reader_dialog_list_dialog, null);
        this.appContainer = ((CardinalApplication) CardinalApplication.getInstance()).getContainer();
        this.currentSession = BarcodeReaderDialogFragment.this.appContainer.getWorkSessionActive();

        //View objects
        this.buttonScan = view.findViewById(R.id.scanBtn);
        this.buttonSave = view.findViewById(R.id.imgBtnSave);
        this.buttonSave.setVisibility(View.GONE);
        this.autoCompleteTextViewCode = view.findViewById(R.id.autoCompleteTextViewCode);
        this.autoCompleteTextViewCode.setThreshold(3);
        this.labelAutoCompleteAdapter = new LabelAutoCompleteAdapter(
                this.getContext(), R.layout.spinner_inv, R.id.tvSpinnerValue,
                LabelSubLotOperations.getInstance().loadAll(currentSession.getId(), false)
        );
        this.autoCompleteTextViewCode.setAdapter(this.labelAutoCompleteAdapter);
        this. autoCompleteTextViewCode.setOnItemClickListener((adapterView, view1, position, id) -> {
            //this is the way to find selected object/item
            this.label = (LabelSubLot) adapterView.getItemAtPosition(position);
            if (this.label != null) {
                this.buttonSave.setVisibility(View.VISIBLE);
            } else {
                this.buttonSave.setVisibility(View.GONE);
            }

        });

        //intializing scan object
        this.code128BarcodeScan = IntentIntegrator.forSupportFragment(BarcodeReaderDialogFragment.this); // `this` is the current Fragment
        this.code128BarcodeScan.setDesiredBarcodeFormats(IntentIntegrator.CODE_128);
        this.code128BarcodeScan.setPrompt(getString(R.string.scan_map_object_barcode_message));
        this.code128BarcodeScan.setOrientationLocked(false);
        //no label checkbox
        this.checkBoxNoLabel = view.findViewById(R.id.checkBoxWithoutTag);
        this.checkBoxNoLabel.setEnabled(this.appContainer.getCurrentMapObject() != null);

        //attaching onclick listener
        this.buttonScan.setOnClickListener(BarcodeReaderDialogFragment.this);
        this.buttonSave.setOnClickListener(BarcodeReaderDialogFragment.this);
        this.checkBoxNoLabel.setOnCheckedChangeListener(BarcodeReaderDialogFragment.this);

        dialog.setContentView(view);
        this.mBehavior = BottomSheetBehavior.from((View) view.getParent());
        return dialog;
    }


    @Override
    public void onStart() {
        super.onStart();
        this.mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
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
                this.label = LabelSubLotOperations.getInstance().load(this.currentSession.getId(), barcode);
                if (this.label != null && !this.label.getGeolocated()) { //setting values to textviews
                    this.autoCompleteTextViewCode.setText(this.label.toString(), true);
                    this.buttonSave.setVisibility(View.VISIBLE);
                } else {
                    this.buttonSave.setVisibility(View.GONE);
                    GPDialogs.toast(getActivity(), String.format(getString(R.string.barcode_notavailable_message), barcode), Toast.LENGTH_LONG);
                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            FragmentActivity activity = getActivity();
            if (activity == null) return;
            switch (requestCode) {
                case (RETURNCODE_FOR_TAKE_PICTURE):
                    if (resultCode == Activity.RESULT_OK) {
                        this.currentObj.resetImages();
                        if (this.currentObj.getImages().size() >= LatLongUtils.getMinImageToTake()) {
                            this.continueButton.setEnabled(true);
                            this.continueButton.setVisibility(View.VISIBLE);
                        } else {
                            this.continueButton.setEnabled(false);
                            this.continueButton.setVisibility(View.GONE);
                        }
                    }
                    break;

            }
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
            this.code128BarcodeScan.initiateScan();
        } else if (i == R.id.imgBtnSave) {
            FragmentActivity activity = getActivity();
            boolean terminalFound = false;
            String mapObjectCode = this.getMapObjectCode();
            try {
                MapObjecType currentSelectedObjectType = this.appContainer.getMapObjecTypeActive();
                MapObject previousObj = this.appContainer.getCurrentMapObject();

                if (this.appContainer.getRouteSegmentActive() == null) {
                    Layer currentSelectedObjectTypeLayer = currentSelectedObjectType.getLayerObj();

                    this.currentObj = new MapObject();
                    this.currentObj.setCode(mapObjectCode);
                    this.currentObj.setCoord(this.coordinates);
                    this.currentObj.setMapObjectTypeId(currentSelectedObjectType.getId());

                    this.grade = currentSelectedObjectType.getIsTerminal() ? 1 : this.grade;

                    this.currentObj.setNodeGrade(this.grade);
                    this.currentObj.setSessionId(this.currentSession.getId());
                    this.currentObj.setIsCompleted(false);
                    this.currentObj.setCreatedAt(new Date());
                    this.currentObj.setElevation(this.mapView.getMapPosition().getZoomLevel() + 0.0f);
                    if (this.compositeMode && previousObj != null) {
                        this.currentObj.setJoinId(previousObj.getId());
                    }
                    MapObjectOperations.getInstance().save(this.currentObj);
                    if (!this.currentObj.isComponent()) {
                        launchPickupPhoto(this.currentObj);
                    }

                    if (!this.currentObj.isTerminal() && !this.compositeMode) {
                        this.appContainer.setCurrentMapObject(this.currentObj);
                    } else if (this.currentObj.isTerminal()) {
                        this.appContainer.setCurrentMapObject(null);
                        this.appContainer.setMapObjecTypeActive(null);
                        this.appContainer.setNetworksActive(null);
                        terminalFound = true;
                    }
                    this.appContainer.setMode(UserMode.NONE);
                    reloadLayer(currentSelectedObjectTypeLayer);
                    GPDialogs.quickInfo(this.mapView, getString(R.string.map_object_saved_message));

//                LatLongUtils.showTip(activity, LatLongUtils.distance(previousObj, currentObj));

                    if (
                            !this.currentObj.belongToTopoLayer()
                                    || previousObj == null
                                    || !previousObj.belongToTopoLayer()
                                    || previousObj.getIsCompleted()
                                    || this.compositeMode
                    ) {
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
                                    BarcodeReaderDialogFragment.this.appContainer.setCurrentMapObject(null);
                                    refreshUI(tf);

                                }), () -> activity.runOnUiThread(() -> {
                                    // no
                                    GPLog.addLogEntry(String.format(activity.getString(R.string.max_distance_threshold_broken_message),
                                            LatLongUtils.getMaxDistance()));

                                    RouteSegment edge = new RouteSegment(null, previousObj.getId(), BarcodeReaderDialogFragment.this.currentObj.getId());
                                    RouteSegmentOperations.getInstance().save(edge);
                                    try {
                                        mapView.reloadLayer(CardinalEdgesLayer.class);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        GPLog.error(BarcodeReaderDialogFragment.this, null, e);
                                    }

                                    refreshUI(tf);
                                })
                        );
                    } else {
                        RouteSegment edge = new RouteSegment(null, previousObj.getId(), this.currentObj.getId());
                        RouteSegmentOperations.getInstance().save(edge);
                        this.mapView.reloadLayer(CardinalEdgesLayer.class);
                        refreshUI(terminalFound);
                    }

                } else { // convertir segmento de ruta a objeto
                    this.currentObj = new MapObject();
                    this.currentObj.setCode(mapObjectCode);
                    this.currentObj.setCoord(this.coordinates);
                    this.currentObj.setMapObjectTypeId(currentSelectedObjectType.getId());

                    this.currentObj.setNodeGrade(this.grade);
                    this.currentObj.setSessionId(this.currentSession.getId());
                    this.currentObj.setIsCompleted(false);
                    this.currentObj.setCreatedAt(new Date());
                    this.currentObj.setElevation(this.mapView.getMapPosition().getZoomLevel() + 0.0f);
                    if (this.compositeMode && previousObj != null) {
                        this.currentObj.setJoinId(previousObj.getId());
                    }

                    MapObjectOperations.getInstance().save(this.currentObj);
                    launchPickupPhoto(this.currentObj);
                    this.appContainer.setRouteSegmentActive(null);
                    this.mapView.reloadLayer(CardinalLineLayer.class);

                    refreshUI(terminalFound);

                }


            } catch (Exception e) {
                GPLog.error(this, e.getLocalizedMessage(), e);
                e.printStackTrace();

                refreshUI(terminalFound);

            }

        }
    }

    private String getMapObjectCode() {
        String code = "";
        if (this.compositeMode) {
            MapObject mapObject = this.appContainer.getCurrentMapObject();
            int subIndex = mapObject.getJoinedList().size() + 1;
            code = String.format("%s-%s",
                    mapObject.getCode(),
                    NumberUtiles.lPadZero(subIndex, 2)
            );
            for (MapObject mapObjectJoin :
                    mapObject.getJoinedList()) {
                subIndex++;
                if (mapObjectJoin.getCode().equals(code)) {
                    code = String.format("%s-%s",
                            mapObject.getCode(),
                            NumberUtiles.lPadZero(subIndex, 2)
                    );
                }

            }
        } else {
            code = this.label.toString();
        }

        return code;
    }

    void reloadLayer(Layer layer) throws Exception {

        EditManager.INSTANCE.getEditLayer().reloadData();
        ((CardinalGPMapView) this.mapView).reloadLayer(layer.getId());
        this.mapView.reloadLayer(CardinalSelectPointLayer.class);
        if (this.compositeMode) {
            this.mapView.reloadLayer(CardinalJoinsLayer.class);
        }
        this.mapView.reloadLayer(BifurcationLayer.class);
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

    /**
     * Called when the checked state of a compound button has changed.
     *
     * @param buttonView The compound button view whose state has changed.
     * @param isChecked  The new checked state of buttonView.
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        FragmentActivity activity = getActivity();
        if (isChecked == true) {
            autoCompleteTextViewCode.setVisibility(View.GONE);
            buttonScan.setVisibility(View.GONE);
            buttonSave.setVisibility(View.VISIBLE);
            compositeMode = true;
            GPDialogs.toast(activity,
                    String.format(activity.getString(R.string.map_object_aggregation_mode_actived_message)),
                    Toast.LENGTH_LONG);
        } else {
            autoCompleteTextViewCode.setVisibility(View.VISIBLE);
            buttonScan.setVisibility(View.VISIBLE);
            buttonSave.setVisibility(View.GONE);
            compositeMode = false;
            GPDialogs.toast(activity,
                    String.format(activity.getString(R.string.map_object_aggregation_mode_disabled_message)),
                    Toast.LENGTH_LONG);
        }
    }


    private void launchPickupPhoto(MapObject currentObj) {
        FragmentActivity activity = this.getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View v = getLayoutInflater().inflate(R.layout.pickup_quick_photo, null);
        builder.setView(v);
        AlertDialog ad = builder.create();
        ad.setCancelable(false);
//        ((TextView) v.findViewById(R.id.tvalertPhoto)).setText(
//                String.format(
//                        activity.getString(R.string.map_object_must_have_a_image_number_taken),
//                        LatLongUtils.getMinImageToTake()
//                )
//        );


        continueButton = v.findViewById(R.id.cancelbutton);
        if (currentObj.getImages().size() < LatLongUtils.getMinImageToTake()) {
            continueButton.setEnabled(false);
            continueButton.setVisibility(View.GONE);
        }

        ImageButton cameraButton = v.findViewById(R.id.launchButton);
        cameraButton.setOnClickListener(v1 -> {

            currentObj.resetImages();
            if (currentObj.getImages().size() >= LatLongUtils.getMinImageToTake() - 1) {
                continueButton.setEnabled(true);
                continueButton.setVisibility(View.VISIBLE);
            } else {
                continueButton.setEnabled(false);
                continueButton.setVisibility(View.GONE);
            }

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
            double[] gpsLocation = PositionUtilities.getGpsLocationFromPreferences(preferences);

            String imageName = ImageUtilities.getCameraImageName(null);
            Intent cameraIntent = new Intent(activity, CameraMapObjectActivity.class);
            cameraIntent.putExtra(LibraryConstants.PREFS_KEY_CAMERA_IMAGENAME, imageName);
            cameraIntent.putExtra(LibraryConstants.DATABASE_ID, currentObj.getId());

            if (gpsLocation != null) {
                cameraIntent.putExtra(LibraryConstants.LATITUDE, gpsLocation[1]);
                cameraIntent.putExtra(LibraryConstants.LONGITUDE, gpsLocation[0]);
                cameraIntent.putExtra(LibraryConstants.ELEVATION, gpsLocation[2]);
            }

            //Investigarbcomo notificar al frame
            activity.startActivityForResult(cameraIntent, RETURNCODE_FOR_TAKE_PICTURE);

        });


        continueButton.setOnClickListener(v1 -> {
            currentObj.resetImages();
            if (currentObj.getImages().size() >= LatLongUtils.getMinImageToTake()) {
                ad.cancel();
            }
//                GPDialogs.yesNoMessageDialog(activity,
//                        String.format(
//                                activity.getString(R.string.map_object_must_have_a_image_number_taken_exit_confirm),
//                                LatLongUtils.getMinImageToTake()
//                        ),
//                        () -> activity.runOnUiThread(() -> {
//                            // yes
//                            ad.cancel();
//
//                        }),
//                        null
//                );
//            } else{

//            }

        });


        ad.show();
    }
}

