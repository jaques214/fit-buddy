package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.retrofit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import androidx.core.content.res.ResourcesCompat;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.MapActivity;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.R;

public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {
    private String url;
    private ImageView imageView;
    private MapActivity mapActivity;

    public ImageLoadTask(String url, ImageView imageView) {
        this.url = url;
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            URL urlConnection = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlConnection
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            //InputStream input = mapActivity.getApplicationContext().getResources().openRawResource(Integer.parseInt(url));
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            //Drawable d = new BitmapDrawable(mapActivity.getApplicationContext().getResources(), myBitmap);

            /*Bitmap resizedBitmap = getResizedBitmap(myBitmap, 300, 300);
            return resizedBitmap;*/
            //return d;
            return Bitmap.createScaledBitmap(myBitmap, 150, 150, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // create a matrix for the manipulation
        Matrix matrix = new Matrix();

        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);

        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        imageView.setImageBitmap(result);
    }

}