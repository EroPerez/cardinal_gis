package cu.phibrain.cardinal.app.ui.activities;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cu.phibrain.cardinal.app.R;
import cu.phibrain.cardinal.app.ui.GridSpacingItemDecoration;
import cu.phibrain.cardinal.app.ui.adapter.MapObjectDefectsImagesAdapter;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectHasDefectHasImages;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectHasDefectHasImagesOperations;
import eu.geopaparazzi.library.util.LibraryConstants;

public class MapObjectDefectImageGalleryActivity extends AppCompatActivity {

    private Long defectId;
    private String defectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_object_defect_image_gallery);

        // getting data
        Bundle extras = getIntent().getExtras();
        defectId = extras.getLong(LibraryConstants.DATABASE_ID, -1);
        defectName= extras.getString(LibraryConstants.PREFS_KEY_TEXT,"");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(String.format(getResources().getString(R.string.activity_defect_images_label), defectName));

        if (null != toolbar) {
            toolbar.setNavigationOnClickListener(v -> NavUtils.navigateUpFromSameTask(MapObjectDefectImageGalleryActivity.this));

        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        RecyclerView recyclerView = findViewById(R.id.rv_defects_images);
        RecyclerView.LayoutManager  llm = new GridLayoutManager(getApplicationContext(), 2, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(llm);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_layout_margin);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, true, 0, false));

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        List<MapObjectHasDefectHasImages> defectHasImages = (defectId != -1) ? MapObjectHasDefectHasImagesOperations.getInstance().getImages(defectId) : new ArrayList();

        MapObjectDefectsImagesAdapter mapObjectDefectsImagesAdapter = new MapObjectDefectsImagesAdapter(this, defectHasImages);
        recyclerView.setAdapter(mapObjectDefectsImagesAdapter);
        recyclerView.setHasFixedSize(true);
    }
}
