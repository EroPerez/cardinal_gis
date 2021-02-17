package eu.geopaparazzi.core.profiles.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;

import eu.geopaparazzi.core.R;
import eu.geopaparazzi.library.core.ResourcesManager;
import eu.geopaparazzi.library.core.activities.DirectoryBrowserActivity;
import eu.geopaparazzi.library.profiles.Profile;
import eu.geopaparazzi.library.util.GPDialogs;
import eu.geopaparazzi.library.util.LibraryConstants;

public class ProjectFragment extends Fragment {
    private static final String ARG_PROFILE = "profile";//NON-NLS
    public static final int RETURNCODE_BROWSE = 666;

    private EditText nameEdittext;
    private EditText pathEdittext;
    private Profile profile;

    public ProjectFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ProjectFragment newInstance(Profile profile) {
        ProjectFragment fragment = new ProjectFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PROFILE, profile);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profilesettings_project, container, false);

        profile = getArguments().getParcelable(ARG_PROFILE);

        nameEdittext = rootView.findViewById(R.id.projectNameEditText);
        nameEdittext.setOnLongClickListener(v -> {
            GPDialogs.yesNoMessageDialog(getActivity(), getString(R.string.profiles_want_clear_project_form), new Runnable() {
                @Override
                public void run() {
                    ProfileSettingsActivity activity = (ProfileSettingsActivity) getActivity();
                    activity.onProjectPathChanged("");

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            nameEdittext.setText("");
                            pathEdittext.setText("");
                        }
                    });

                }
            }, null);
            return true;
        });

        pathEdittext = rootView.findViewById(R.id.projectPathEditText);
        if (profile != null && profile.profileProject != null && profile.getFile(profile.profileProject.getRelativePath()).exists()) {
            setProjectData(profile.getFile(profile.profileProject.getRelativePath()).getAbsolutePath());
        }
        FloatingActionButton addFormButton = rootView.findViewById(R.id.addProjectButton);
        addFormButton.setOnClickListener(v -> {
            try {
                File sdcardDir = ResourcesManager.getInstance(getContext()).getMainStorageDir();
                Intent browseIntent = new Intent(getContext(), DirectoryBrowserActivity.class);
                browseIntent.putExtra(DirectoryBrowserActivity.EXTENSIONS, new String[]{"gpap"});//NON-NLS
                browseIntent.putExtra(DirectoryBrowserActivity.STARTFOLDERPATH, sdcardDir.getAbsolutePath());
                startActivityForResult(browseIntent, RETURNCODE_BROWSE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return rootView;
    }

    private void setProjectData(String path) {
        try {
            File file = new File(path);
            nameEdittext.setText(file.getName());
            pathEdittext.setText(file.getParentFile().getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (RETURNCODE_BROWSE): {
                if (resultCode == Activity.RESULT_OK) {
                    String path = data.getStringExtra(LibraryConstants.PREFS_KEY_PATH);
                    if (path != null && new File(path).exists()) {
                        String sdcardPath = profile.getSdcardPath();

                        if (!path.contains(sdcardPath)) {
                            GPDialogs.warningDialog(getActivity(), getString(R.string.data_need_to_reside_in_same_path), null);
                            return;
                        }

                        String relativePath = path.replaceFirst(sdcardPath, "");

                        setProjectData(path);
                        ProfileSettingsActivity activity = (ProfileSettingsActivity) getActivity();
                        activity.onProjectPathChanged(relativePath);
                    }
                }
            }
        }
    }
}