package cu.phibrain.cardinal.app.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.IOException;
import java.util.List;

import cu.phibrain.cardinal.app.CardinalApplication;
import cu.phibrain.cardinal.app.MapviewActivity;
import cu.phibrain.cardinal.app.R;
import cu.phibrain.cardinal.app.injections.AppContainer;
import cu.phibrain.cardinal.app.ui.SpacesItemDecoration;
import cu.phibrain.cardinal.app.ui.activities.CameraMapObjectActivity;
import cu.phibrain.cardinal.app.ui.adapter.LabelSubLotAdapter;
import cu.phibrain.cardinal.app.ui.adapter.MapObjectAttrAdapter;
import cu.phibrain.cardinal.app.ui.adapter.MapObjectDefectsAdapter;
import cu.phibrain.cardinal.app.ui.adapter.MapObjectImagesAdapter;
import cu.phibrain.cardinal.app.ui.adapter.MapObjectStatesAdapter;
import cu.phibrain.cardinal.app.ui.adapter.StockAutoCompleteAdapter;
import cu.phibrain.cardinal.app.ui.layer.CardinalPointLayer;
import cu.phibrain.cardinal.app.ui.layer.EdgesLayer;
import cu.phibrain.plugins.cardinal.io.database.entity.model.LabelSubLot;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjecTypeDefect;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjecTypeState;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObject;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectImages;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectMetadata;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Stock;
import cu.phibrain.plugins.cardinal.io.database.entity.model.WorkSession;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.LabelSubLotOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjecTypeOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectImagesOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.StockOperations;
import eu.geopaparazzi.library.images.ImageUtilities;
import eu.geopaparazzi.library.util.GPDialogs;
import eu.geopaparazzi.library.util.LibraryConstants;
import eu.geopaparazzi.library.util.PositionUtilities;
import eu.geopaparazzi.map.GPMapView;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     ObjectInspectorDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class ObjectInspectorDialogFragment extends BottomSheetDialogFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_MAP_OBJECT_ID = "MAP_OBJECT_ID";
    private final int RETURNCODE_FOR_TAKE_PICTURE = 666;
    private final int RETURNCODE_FOR_TAKE_DEFECT_PICTURE = 667;

    private BottomSheetBehavior mBehavior;

    private AppContainer appContainer;

    private GPMapView mapView;

    protected MapObjectImagesAdapter imagesAdapter;

    // TODO: Customize parameters
    public static ObjectInspectorDialogFragment newInstance(GPMapView mapView, long objectId) {
        final ObjectInspectorDialogFragment fragment = new ObjectInspectorDialogFragment(mapView);
        final Bundle args = new Bundle();
        args.putLong(ARG_MAP_OBJECT_ID, objectId);
        fragment.setArguments(args);
        return fragment;
    }

    public static class MyBottomSheetDialog extends BottomSheetDialog {

        public MyBottomSheetDialog(@NonNull Context context) {
            super(context);
        }

        protected MyBottomSheetDialog(@NonNull Context context, final boolean cancelable,
                                      OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);
        }

        public MyBottomSheetDialog(@NonNull Context context, @StyleRes final int theme) {
            super(context, theme);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getWindow().setLayout((int) (getScreenWidth() * 95 / 100) /*our width*/, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    public ObjectInspectorDialogFragment(GPMapView mapView) {
        super();
        this.mapView = mapView;
    }

    public ObjectInspectorDialogFragment() {
        super();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = new MyBottomSheetDialog(this.getContext(), cu.phibrain.cardinal.app.R.style.BottomSheetDialogTheme);

        View view = View.inflate(getContext(), R.layout.fragment_object_inspector_dialog_list_dialog, null);

        Long objectId = getArguments().getLong(ARG_MAP_OBJECT_ID);

        MapObject objectSelected = MapObjectOperations.getInstance().load(objectId);

        appContainer = ((CardinalApplication) CardinalApplication.getInstance()).appContainer;
        appContainer.setCurrentMapObject(objectSelected);
        appContainer.setMapObjecTypeActive(objectSelected.getObjectType());

        //Update ui
        Intent intent = new Intent(MapviewActivity.ACTION_UPDATE_UI);
        intent.putExtra("update_map_object_active", true);
        intent.putExtra("update_map_object_type_active", true);
        getActivity().sendBroadcast(intent);


        try {
            // Basic attribute section
            initBasicAttribute(view, objectSelected);
            // MapObject images section
            initMapObjectImages(view, objectSelected);
            // MapObject defects section
            initMapObjectDefects(view, objectSelected);
            //MapObject states section
            initMapObjectStates(view, objectSelected);
            //MapObject attr section
            initMapObjectMeta(view, objectSelected);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLiteConstraintException e) {
            GPDialogs.errorDialog(this.getContext(), e, null);
        }

        dialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        return dialog;
    }


    @Override
    public void onStart() {
        super.onStart();
        mBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
    }

    @Override
    public void dismiss() {

        try {
            mapView.reloadLayer(CardinalPointLayer.class);
            mapView.reloadLayer(EdgesLayer.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.dismiss();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FragmentActivity activity = getActivity();
        if (activity == null) return;
        switch (requestCode) {
            case (RETURNCODE_FOR_TAKE_PICTURE):
                if (resultCode == Activity.RESULT_OK) {
                    MapObject object = appContainer.getCurrentMapObject();
                    List<MapObjectImages> images = MapObjectImagesOperations.getInstance().loadAll(object.getId());
                    imagesAdapter.setMapObjectImages(images);
                    Log.d("TAKE_PICTURE size:", " " + images.size());
                }
                break;
            case RETURNCODE_FOR_TAKE_DEFECT_PICTURE:

                break;
        }
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    private void initBasicAttribute(View view, MapObject object) throws IOException {

        Spinner edtCode = view.findViewById(R.id.edtCode);
        EditText edtCodeFake = view.findViewById(R.id.edtCodeFake);

        WorkSession session = appContainer.getWorkSessionActive();

        // Codigo del nodo
        if (session.getId() != object.getSessionId()) {
            edtCode.setVisibility(View.GONE);
            edtCodeFake.setVisibility(View.VISIBLE);
            edtCodeFake.setText(object.getCode());
        } else {
            edtCodeFake.setVisibility(View.GONE);
            edtCode.setVisibility(View.VISIBLE);

            LabelSubLotAdapter lotAdapter = new LabelSubLotAdapter(
                    this.getContext(), R.layout.spinner_inv,
                    LabelSubLotOperations.getInstance().loadAll(session.getId())
            );

            edtCode.setAdapter(lotAdapter);
            if (object.getCode() != null) {
                edtCode.setSelection(lotAdapter.select(object.getCode(), session.getId()), true);
            }

            edtCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    // Get the selected value
                    Spinner spinner = (Spinner) parent;
                    LabelSubLot data = (LabelSubLot) spinner.getItemAtPosition(position);
                    try {
                        if (data != null && data.getLabelObj().getCode() != object.getCode()) {
                            object.setCode(data.getLabelObj().getCode());
                            MapObjectOperations.getInstance().save(object);
                            //Notify dataSet Changed
                            lotAdapter.reload(session.getId());
                        }
                    } catch (SQLiteConstraintException ex) {
                        GPDialogs.quickInfo(mapView, String.format(getString(R.string.map_object_duplicate_code_messsage), data.getLabelObj().getCode()));
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        // Grado del nodo
        Boolean isTopological = object.getLayer().getIsTopology();

        if (!isTopological) {
            view.findViewById(R.id.tvGrade).setVisibility(View.GONE);
            view.findViewById(R.id.edtGrade).setVisibility(View.GONE);
            view.findViewById(R.id.row_node_grade).setVisibility(View.GONE);
            object.setNodeGrade(0);
            MapObjectOperations.getInstance().save(object);
        } else {
            EditText edtGrade = view.findViewById(R.id.edtGrade);
            edtGrade.setText(String.valueOf(object.getNodeGrade()));
            edtGrade.setOnEditorActionListener((v, actionId, event) -> {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE && v != null) {
                    Long value = Long.parseLong(String.valueOf(v.getText()));
                    object.setNodeGrade(value > 0 ? value : value * -1);
                    MapObjectOperations.getInstance().save(object);
                    handled = true;
                    //close keyboard
                    edtGrade.clearFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                }
                return handled;
            });
        }


//        Spinner spnInv = view.findViewById(R.id.spnInv);
//        StockAdapter stockAdapter = new StockAdapter(this.getContext(), R.layout.spinner_inv, appContainer.getProjectActive().getStocks());
//        spnInv.setAdapter(stockAdapter);
//        if (object.getStockCode() != null) {
//            spnInv.setSelection(stockAdapter.select(object.getStockCode()));
//        }
//        spnInv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
////                if (position == 0) {
////                    return;
////                } else {
////                    position = position - 1;
////                }
//                // Get the selected value
//                Spinner spinner = (Spinner) parent;
//                Stock data = (Stock) spinner.getItemAtPosition(position);
//                object.setStockCode(data);
//                MapObjectOperations.getInstance().update(object);
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        // Nro de inv
        AutoCompleteTextView autoCompleteTextViewInv = view.findViewById(R.id.autoCompleteTextViewInv);
        StockAutoCompleteAdapter stockAdapter = new StockAutoCompleteAdapter(
                this.getContext(), R.layout.spinner_inv, R.id.tvSpinnerValue,
                StockOperations.getInstance().loadAll(appContainer.getProjectActive().getId())
        );
        autoCompleteTextViewInv.setAdapter(stockAdapter);
        if (object.getStockCode() != null)
            autoCompleteTextViewInv.setText(object.getStockCode().getCode());

        autoCompleteTextViewInv.setOnItemClickListener((adapterView, view1, position, id) -> {
            //this is the way to find selected object/item
            Stock data = (Stock) adapterView.getItemAtPosition(position);
            if (data != null) {
                Log.d("MapObjectInv", data.getCode());
                object.setStockCodeId(data.getId());
                data.setLocated(true);

            }

        });

        autoCompleteTextViewInv.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_DONE && v != null) {
                MapObjectOperations.getInstance().save(object);
                stockAdapter.notifyDataSetChanged();
                handled = true;

                //close keyboard
                autoCompleteTextViewInv.clearFocus();
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
            }
            return handled;
        });

//Type
        EditText edtType = view.findViewById(R.id.edtType);
        edtType.setText(object.getObjectType().getCaption());
//Geometry
        EditText edtGeometry = view.findViewById(R.id.edtGeometry);
        edtGeometry.setText(object.getObjectType().getGeomType().name());
// Observaciones
        EditText edtObservation = view.findViewById(R.id.edtObservation);
        String obsv = object.getObservation();
        if (obsv != null && !obsv.isEmpty())
            edtObservation.setText(Html.fromHtml(object.getObservation()));
        edtObservation.setRawInputType(InputType.TYPE_CLASS_TEXT);
        edtObservation.setImeActionLabel(getResources().getString(R.string.done), EditorInfo.IME_ACTION_DONE);
        edtObservation.setImeOptions(EditorInfo.IME_ACTION_DONE);

        edtObservation.setOnEditorActionListener((v, actionId, event) -> {
            if (event == null) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Capture soft enters in a singleLine EditText that is the last EditText
                    // This one is useful for the new list case, when there are no existing ListItems
                    edtObservation.clearFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

                    String value = String.valueOf(v.getText());
                    object.setObservation(value);
                    MapObjectOperations.getInstance().save(object);

                } else if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    // Capture soft enters in other singleLine EditTexts
                } else if (actionId == EditorInfo.IME_ACTION_GO) {
                } else {
                    // Let the system handle all other null KeyEvents
                    return false;
                }
            } else if (actionId == EditorInfo.IME_NULL) {
                // Capture most soft enters in multi-line EditTexts and all hard enters;
                // They supply a zero actionId and a valid keyEvent rather than
                // a non-zero actionId and a null event like the previous cases.
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    // We capture the event when the key is first pressed.
                } else {
                    // We consume the event when the key is released.
                    return true;
                }
            } else {
                // We let the system handle it when the listener is triggered by something that
                // wasn't an enter.
                return false;
            }
            return true;
        });

        // Delete node button
        ImageButton deleteObject = view.findViewById(R.id.imgBtnDelete);
        deleteObject.setVisibility(View.VISIBLE);
        if (session.getId() == object.getSessionId()) {
            deleteObject.setOnClickListener(v -> {
                final FragmentActivity activity = getActivity();
                if (activity == null)
                    return;

                GPDialogs.yesNoMessageDialog(getActivity(), getString(R.string.do_you_want_to_delete_this_map_object),
                        () -> activity.runOnUiThread(() -> {
                            // stop logging
                            MapObjectOperations.getInstance().delete(object);
                            ObjectInspectorDialogFragment.this.dismiss();

                        }), null
                );

            });
        } else deleteObject.setVisibility(View.INVISIBLE);

    }


    private void initMapObjectImages(View view, MapObject object) {

        FragmentActivity activity = getActivity();

        ImageButton imgBtnAddPhoto = view.findViewById(R.id.imgBtnAddPhoto);
        imgBtnAddPhoto.setOnClickListener(v -> {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
            double[] gpsLocation = PositionUtilities.getGpsLocationFromPreferences(preferences);

            String imageName = ImageUtilities.getCameraImageName(null);
            Intent cameraIntent = new Intent(activity, CameraMapObjectActivity.class);
            cameraIntent.putExtra(LibraryConstants.PREFS_KEY_CAMERA_IMAGENAME, imageName);
            cameraIntent.putExtra(LibraryConstants.DATABASE_ID, object.getId());

            if (gpsLocation != null) {
                cameraIntent.putExtra(LibraryConstants.LATITUDE, gpsLocation[1]);
                cameraIntent.putExtra(LibraryConstants.LONGITUDE, gpsLocation[0]);
                cameraIntent.putExtra(LibraryConstants.ELEVATION, gpsLocation[2]);
            }

            startActivityForResult(cameraIntent, RETURNCODE_FOR_TAKE_PICTURE);
        });

        RecyclerView recyclerView = view.findViewById(R.id.rvObjectPhotos);

        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_layout_margin);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        List<MapObjectImages> images = MapObjectImagesOperations.getInstance().loadAll(object.getId());

        imagesAdapter = new MapObjectImagesAdapter(activity, images);
        recyclerView.setAdapter(imagesAdapter);

    }

    private void initMapObjectDefects(View view, MapObject object) {

        FragmentActivity activity = getActivity();

        List<MapObjecTypeDefect> defects = MapObjecTypeOperations.getInstance().getDefects(object.getMapObjectTypeId());

        if (defects.size() > 0) {
            RecyclerView recyclerView = view.findViewById(R.id.rvObjectDefects);
            LinearLayoutManager llm = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(llm);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    ((LinearLayoutManager) recyclerView.getLayoutManager()).getOrientation());
            recyclerView.addItemDecoration(dividerItemDecoration);

            MapObjectDefectsAdapter defectsAdapter = new MapObjectDefectsAdapter(activity, defects, object);
            recyclerView.setAdapter(defectsAdapter);
            recyclerView.setHasFixedSize(true);

        } else {
            view.findViewById(R.id.defects_header_container).setVisibility(View.GONE);
            view.findViewById(R.id.defects_body_container).setVisibility(View.GONE);
        }
    }


    private void initMapObjectStates(View view, MapObject object) {

        FragmentActivity activity = getActivity();

        List<MapObjecTypeState> states = MapObjecTypeOperations.getInstance().getStates(object.getMapObjectTypeId());

        if (states.size() > 0) {
            RecyclerView recyclerView = view.findViewById(R.id.rvObjectSates);
            LinearLayoutManager llm = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(llm);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    ((LinearLayoutManager) recyclerView.getLayoutManager()).getOrientation());
            recyclerView.addItemDecoration(dividerItemDecoration);

            MapObjectStatesAdapter statesAdapter = new MapObjectStatesAdapter(states, object);
            recyclerView.setAdapter(statesAdapter);
            recyclerView.setHasFixedSize(true);

        } else {
            view.findViewById(R.id.states_header_container).setVisibility(View.GONE);
            view.findViewById(R.id.states_body_container).setVisibility(View.GONE);
        }
    }

    private void initMapObjectMeta(View view, MapObject object) {

        FragmentActivity activity = getActivity();

        List<MapObjectMetadata> metadata = object.getMetadata();

        if (metadata.size() > 0) {
            RecyclerView recyclerView = view.findViewById(R.id.rvObjectAttributes);
            LinearLayoutManager llm = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(llm);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    ((LinearLayoutManager) recyclerView.getLayoutManager()).getOrientation());
            recyclerView.addItemDecoration(dividerItemDecoration);

            MapObjectAttrAdapter statesAdapter = new MapObjectAttrAdapter(activity, metadata);
            recyclerView.setAdapter(statesAdapter);
            recyclerView.setHasFixedSize(true);

        } else {
            view.findViewById(R.id.attr_header_container).setVisibility(View.GONE);
            view.findViewById(R.id.attr_body_container).setVisibility(View.GONE);
        }
    }
}
