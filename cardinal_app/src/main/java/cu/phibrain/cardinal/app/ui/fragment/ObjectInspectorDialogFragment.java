package cu.phibrain.cardinal.app.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.IOException;

import cu.phibrain.cardinal.app.CardinalApplication;
import cu.phibrain.cardinal.app.R;
import cu.phibrain.cardinal.app.injections.AppContainer;
import cu.phibrain.cardinal.app.ui.adapter.LabelSubLotAdapter;
import cu.phibrain.cardinal.app.ui.adapter.StockAdapter;
import cu.phibrain.plugins.cardinal.io.database.entity.model.LabelSubLot;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObject;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Stock;
import cu.phibrain.plugins.cardinal.io.database.entity.model.WorkSession;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectOperations;

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

    private BottomSheetBehavior mBehavior;

    private AppContainer appContainer;

    // TODO: Customize parameters
    public static ObjectInspectorDialogFragment newInstance(long objectId) {
        final ObjectInspectorDialogFragment fragment = new ObjectInspectorDialogFragment();
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

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = new MyBottomSheetDialog(this.getContext(), cu.phibrain.cardinal.app.R.style.BottomSheetDialogTheme);

        View view = View.inflate(getContext(), R.layout.fragment_object_inspector_dialog_list_dialog, null);

        LinearLayout linearLayout = view.findViewById(R.id.root);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        params.height = getScreenHeight();
        linearLayout.setLayoutParams(params);

        Long objectId = getArguments().getLong(ARG_MAP_OBJECT_ID);

        MapObject objectSelected = MapObjectOperations.getInstance().load(objectId);

        appContainer = ((CardinalApplication) CardinalApplication.getInstance()).appContainer;
        appContainer.setMapObjectActive(objectSelected);

        // Basic attribute section
        try {
            initBasicAttribute(view, objectSelected);
        } catch (IOException e) {
            e.printStackTrace();
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

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    private void initBasicAttribute(View view, MapObject object) throws IOException {

        Spinner edtCode = view.findViewById(R.id.edtCode);
        WorkSession session = appContainer.getWorkSessionActive();
        WorkSession objectSession = object.getSession();
        LabelSubLotAdapter lotAdapter = new LabelSubLotAdapter(this.getContext(), R.layout.spinner_inv, session.getLabels());

        if (session.getId() != object.getSessionId()) edtCode.setEnabled(false);
        else edtCode.setEnabled(true);

        edtCode.setAdapter(lotAdapter);
        if (object.getCode() != null) {
            edtCode.setSelection(lotAdapter.select(object.getCode(), session.getId()), true);
        }

        edtCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Get the selected value
                Spinner spinner = (Spinner) parent;
                try {
                    LabelSubLot data = (LabelSubLot) spinner.getItemAtPosition(position);
                    if (data != null && data.getLabelObj().getCode() != object.getCode()) {
                        object.setCode(data.getLabelObj().getCode());
                        MapObjectOperations.getInstance().update(object);
                    }
                } catch (android.database.SQLException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        EditText edtGrade = view.findViewById(R.id.edtGrade);
        edtGrade.setText(String.valueOf(object.getNodeGrade()));
        edtGrade.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_DONE && v != null) {
                Long value = Long.parseLong(String.valueOf(v.getText()));
                object.setNodeGrade(value);
                MapObjectOperations.getInstance().update(object);
                handled = true;
            }
            return handled;
        });


        Spinner spnInv = view.findViewById(R.id.spnInv);
        StockAdapter stockAdapter = new StockAdapter(this.getContext(), R.layout.spinner_inv, appContainer.getProjectActive().getStocks());
        spnInv.setAdapter(stockAdapter);
        if (object.getStockCode() != null) {
            spnInv.setSelection(stockAdapter.select(object.getStockCode()));
        }
        spnInv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    return;
                } else {
                    position = position - 1;
                }
                // Get the selected value
                Spinner spinner = (Spinner) parent;
                Stock data = (Stock) spinner.getItemAtPosition(position);
                object.setStockCode(data);
                MapObjectOperations.getInstance().update(object);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        EditText edtType = view.findViewById(R.id.edtType);
        edtType.setText(object.getObjectType().getCaption());

        EditText edtGeometry = view.findViewById(R.id.edtGeometry);
        edtGeometry.setText(object.getObjectType().getGeomType().name());

        EditText edtObservation = view.findViewById(R.id.edtObservation);
        String obsv = object.getObservation();
        if (obsv != null && !obsv.isEmpty())
            edtObservation.setText(Html.fromHtml(object.getObservation()));
        edtObservation.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_DONE && v != null) {
                String value = String.valueOf(v.getText());
                object.setObservation(value);
                MapObjectOperations.getInstance().update(object);
                handled = true;
            }
            return handled;
        });

        // Delete
     ImageButton deleteObject =  view.findViewById(R.id.imgBtnDelete);
        deleteObject.setOnClickListener(v -> {
            MapObjectOperations.getInstance().delete(object);
            CardinalApplication.doRestart(ObjectInspectorDialogFragment.this.getContext());
        });

    }


}


//

//Using this code will allow you to set up a multi-line EditText where pressing the Return key on the virtual keyboard will remove focus from the EditText, thereby preventing the user from entering a newline character.
//
//        For mContent we set the raw input type as TYPE_CLASS_TEXT, IME Options as IME_ACTION_DONE to set up the Return key on the virtual keyboard as a DONE action. We also label the return key using setImeActionLabel.
//
//        The OnEditorActionListener lets us listen for actions on the keyboard. The giant nested if-statement is mostly empty because we only really need to check for a null event and IME_ACTION_DONE, since we set it earlier in the IME Options. I left the statements in to show you how it would look if you wanted to handle things differently.
//
//        NOTE: This has not been tested for hardware keyboards, but if you have tested it I would love to know if you found that it works.

// mContent.setRawInputType(InputType.TYPE_CLASS_TEXT);
//        mContent.setImeActionLabel(getResources().getString(R.string.done), EditorInfo.IME_ACTION_DONE);
//        mContent.setImeOptions(EditorInfo.IME_ACTION_DONE);
//
//        mContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//@Override
//public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//        if (event == null) {
//        if (actionId == EditorInfo.IME_ACTION_DONE) {
//        // Capture soft enters in a singleLine EditText that is the last EditText
//        // This one is useful for the new list case, when there are no existing ListItems
//        mContent.clearFocus();
//        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
//        } else if (actionId == EditorInfo.IME_ACTION_NEXT) {
//        // Capture soft enters in other singleLine EditTexts
//        } else if (actionId == EditorInfo.IME_ACTION_GO) {
//        } else {
//        // Let the system handle all other null KeyEvents
//        return false;
//        }
//        } else if (actionId == EditorInfo.IME_NULL) {
//        // Capture most soft enters in multi-line EditTexts and all hard enters;
//        // They supply a zero actionId and a valid keyEvent rather than
//        // a non-zero actionId and a null event like the previous cases.
//        if (event.getAction() == KeyEvent.ACTION_DOWN) {
//        // We capture the event when the key is first pressed.
//        } else {
//        // We consume the event when the key is released.
//        return true;
//        }
//        } else {
//        // We let the system handle it when the listener is triggered by something that
//        // wasn't an enter.
//        return false;
//        }
//        return true;
//        }
//        });
