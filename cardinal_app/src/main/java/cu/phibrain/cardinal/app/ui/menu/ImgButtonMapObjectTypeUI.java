package cu.phibrain.cardinal.app.ui.menu;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams;
import cu.phibrain.cardinal.app.R;

public class ImgButtonMapObjectTypeUI extends AppCompatImageButton {
    private MapObjectTypeUI adapterMot;
    private Animation fabClose;
    private Animation fabOpen;
    private Animation fbMove;

    public ImgButtonMapObjectTypeUI(@NonNull Context context, MapObjectTypeUI adapterMot2) {
        super(context);
        setAdapterMot(adapterMot2);
        this.fabOpen = AnimationUtils.loadAnimation(context, R.anim.fab_open);
        this.fabClose = AnimationUtils.loadAnimation(context, R.anim.fab_close);
        this.fbMove = AnimationUtils.loadAnimation(context, R.anim.fab_move);
        LayoutParams params = new LayoutParams(-2, -2);
        setVisibility(4);
        setLayoutParams(params);
        setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent_mot)));
    }

    public void closeFab() {
        startAnimation(this.fabClose);
        setEnabled(false);
    }

    public void moveFab() {
        startAnimation(this.fbMove);
    }

    public void openFab() {
        setEnabled(true);
        startAnimation(this.fabOpen);
        bringToFront();
    }

    public MapObjectTypeUI getAdapterMot() {
        return this.adapterMot;
    }

    public void setAdapterMot(MapObjectTypeUI adapterMot2) {
        this.adapterMot = adapterMot2;
    }

    public void themeParent() {
        LayoutParams params = new LayoutParams(-2, -2);
        params.setMargins(0, 16, 0, 5);
        params.gravity = 81;
        setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent_mot)));
        setImageBitmap(toRoundCorner(Bitmap.createScaledBitmap(BitmapFactory.decodeFile("/storage/emulated/0/geopaparazzi/1.jpg"), 120, 120, true), 120));
        setLayoutParams(params);
    }

    public void themeChildren(int levelMargin) {
        LayoutParams params = new LayoutParams(-2, -2);
        params.setMargins(0, 0, 0, levelMargin * 130);
        params.gravity = 81;
        setLayoutParams(params);
        setImageResource(R.drawable.ic_mapview_mot_children_24dp);
        setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent_mot)));
    }

    public void themeWhitChildren() {
        setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent_mot)));
        setImageResource(R.drawable.ic_mapview_mot_parent_24dp);
    }

    private static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint(1);
        RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()));
        float roundPx = (float) pixels;
        paint.setStyle(Style.FILL);
        paint.setColor(-1);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        rectF.set(rectF.left + 5.0f, rectF.top + 5.0f, rectF.right - 5.0f, rectF.bottom - 5.0f);
        Path clipPath = new Path();
        clipPath.addRoundRect(rectF, roundPx, roundPx, Direction.CW);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.clipPath(clipPath);
        canvas.drawBitmap(bitmap, null, rectF, paint);
        return output;
    }
}
