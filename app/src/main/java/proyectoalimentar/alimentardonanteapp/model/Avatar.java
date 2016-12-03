package proyectoalimentar.alimentardonanteapp.model;

import android.graphics.Bitmap;

public class Avatar {

    Bitmap original;
    Bitmap thumb;

    public Avatar(Bitmap original, Bitmap thumb){
        this.original = original;
        this.thumb = thumb;
    }

    public Bitmap getOriginal() {
        return original;
    }

    public Bitmap getThumb() {
        return thumb;
    }

    public void setOriginal(Bitmap original) {
        this.original = original;
    }

    public void setThumb(Bitmap thumb) {
        this.thumb = thumb;
    }
}
