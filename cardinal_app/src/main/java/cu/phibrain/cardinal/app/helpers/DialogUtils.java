package cu.phibrain.cardinal.app.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.material.textfield.TextInputLayout;

import cu.phibrain.cardinal.app.R;
import cu.phibrain.cardinal.app.ui.TextChangedListener;
import eu.geopaparazzi.library.util.TextRunnable;

public class DialogUtils {
    /**
     * Execute a message dialog in an {@link AsyncTask}.
     *
     * @param activity     the {@link Activity} to use.
     * @param message      a message to show.
     * @param defaultValue a default text to fill in.
     * @param minValue     a min value to decrease
     * @param maxValue     a max value to increase
     * @param textRunnable optional {@link TextRunnable} to trigger after ok was pressed.
     */
    public static void inputNumberDialog(final Activity activity, final String message,
                                         final Long defaultValue,
                                         final Long minValue, final Long maxValue,
                                         final TextRunnable textRunnable) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = activity.getLayoutInflater().inflate(
                R.layout.number_input_dialog, null);
        builder.setView(view);

        final TextInputLayout textInputLayout = view.findViewById(R.id.dialogTextLayout);
        textInputLayout.setHint(message);
        textInputLayout.setLabelFor(R.id.dialogEdittext);
        textInputLayout.setHelperText(message);

        final EditText editText = view.findViewById(R.id.dialogEdittext);
        if (defaultValue != null) {
            editText.setText(String.valueOf(defaultValue));
        }
        final ImageButton addButton = view.findViewById(R.id.imageButtonAdd);
        final ImageButton minusButton = view.findViewById(R.id.imageButtonMinus);

        try {
            builder.setPositiveButton(activity.getString(R.string.continue_fuck),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Editable value = editText.getText();
                            String newText = value.toString();
                            if (newText.length() < 1) {
                                newText = String.valueOf(defaultValue);
                            }
                            dialog.dismiss();
                            if (textRunnable != null) {
                                textRunnable.setText(newText);
                                new Thread(textRunnable).start();
                            }
                        }
                    }
            );

            builder.setNegativeButton(activity.getString(android.R.string.cancel), null);

            addButton.setOnClickListener(v -> {
                Editable value = editText.getText();
                String newText = value.toString();
                long currentValue = NumberUtiles.parseStringToLong(newText, defaultValue);
                if (currentValue >= maxValue) {
                    currentValue = maxValue;
                    addButton.setEnabled(false);
                } else {
                    currentValue++;
                }
                minusButton.setEnabled(true);

                editText.setText(String.valueOf(currentValue));
            });

            minusButton.setOnClickListener(v -> {
                Editable value = editText.getText();
                String newText = value.toString();
                long currentValue = NumberUtiles.parseStringToLong(newText, defaultValue);
                if (currentValue <= minValue) {
                    currentValue = minValue;
                    minusButton.setEnabled(false);
                } else {
                    currentValue--;
                }
                addButton.setEnabled(true);

                editText.setText(String.valueOf(currentValue));
            });

            editText.addTextChangedListener(new TextChangedListener<EditText>(editText) {
                @Override
                public void onTextChanged(EditText target, Editable s) {
                    String newText = s.toString();
                    if (newText.length() < 1) {
                        target.setText(String.valueOf(defaultValue));
                        addButton.setEnabled(true);
                        minusButton.setEnabled(true);
                    } else {
                        long currentValue = NumberUtiles.parseStringToLong(newText, defaultValue);
                        if (currentValue < minValue) {
                            target.setText(String.valueOf(minValue));
                            minusButton.setEnabled(false);
                            addButton.setEnabled(true);
                        } else if (currentValue > maxValue) {
                            target.setText(String.valueOf(maxValue));
                            addButton.setEnabled(false);
                            minusButton.setEnabled(true);
                        } else if(currentValue > minValue  && currentValue < maxValue){
                            addButton.setEnabled(true);
                            minusButton.setEnabled(true);
                        }
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
