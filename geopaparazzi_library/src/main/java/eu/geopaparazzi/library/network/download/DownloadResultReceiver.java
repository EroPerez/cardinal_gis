package eu.geopaparazzi.library.network.download;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.widget.ProgressBar;
import android.widget.TextView;

import eu.geopaparazzi.library.core.dialogs.ProgressBarDialogFragment;

@SuppressWarnings("ALL")
abstract public class DownloadResultReceiver extends ResultReceiver {
    public static final int RESULT_CODE = 1;
    public static final String DOWNLOAD_ACTION = "eu.geopaparazzi.library.network.download.action";
    public static final String PROGRESS_KEY = "progress";
    public static final String PROGRESS_MESSAGE_KEY = "progress_message";
    public static final String PROGRESS_ENDED_KEY = "progress_ended";
    public static final String PROGRESS_ERRORED_KEY = "progress_errored";
    public static final String EXTRA_KEY = "download_reciever";
    public static final String EXTRA_FILES_KEY = "download_files";
    public static final int max = 100;

    public DownloadResultReceiver(Handler handler) {
        super(handler);
    }

    public abstract ProgressBar getProgressBar();

    public abstract TextView getProgressView();

    public abstract ProgressBarDialogFragment.IProgressChangeListener getProgressChangeListener();


    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (resultCode == RESULT_CODE) {
            ProgressBar progressBar = getProgressBar();
            if (progressBar != null) {
                int percentage = resultData.getInt(PROGRESS_KEY);
                if (percentage != 0)
                    progressBar.setProgress(percentage);
            }

            TextView progressView = getProgressView();
            if (progressView != null) {
                String msg = resultData.getString(PROGRESS_MESSAGE_KEY);
                if (msg != null)
                    progressView.setText(msg);
            }

            ProgressBarDialogFragment.IProgressChangeListener progressChangeListener = getProgressChangeListener();
            if (progressChangeListener != null) {
                String errorMsg = resultData.getString(PROGRESS_ERRORED_KEY);
                if (errorMsg != null) {
                    progressChangeListener.onProgressError(errorMsg);
                } else {
                    String doneString = resultData.getString(PROGRESS_ENDED_KEY);
                    if (doneString != null) {
                        progressChangeListener.onProgressDone(doneString);
                    }
                }
            }
        }
    }
}