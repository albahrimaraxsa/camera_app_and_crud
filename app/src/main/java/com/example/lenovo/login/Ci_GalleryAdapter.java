package com.example.lenovo.login;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import static com.example.lenovo.login.Ci_GetFilePath.getFilePath;
import static com.example.lenovo.login.Ci_GetFilePath.getFilePathVideo;


public class Ci_GalleryAdapter extends BaseAdapter{
    Context context;
    List<String> list;

    LayoutInflater vi;

    public Ci_GalleryAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
        vi = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            view = vi.inflate(R.layout.eachitem, null);
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        TextView textView = (TextView) view.findViewById(R.id.textView);

        if (getCount() > 0) {

            String filename = list.get(position);

            Bitmap bitmap = null;
            if(filename.substring(filename.length()-4).equalsIgnoreCase(".jpg")){
                String filepathname = getFilePath(filename);
                File image = new File(filepathname);
                bitmap = decodeSampledBitmapFromFile(image, 400, 200);
            }
            else if(filename.substring(filename.length()-4).equalsIgnoreCase(".mp4")){
                String filepathname = getFilePathVideo(filename);
                bitmap = ThumbnailUtils.createVideoThumbnail(filepathname,MediaStore.Video.Thumbnails.MINI_KIND);
            }
            imageView.setImageBitmap(bitmap);
            textView.setText(list.get(position));

        }


        return view;
    }



    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromFile(File image,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //BitmapFactory.decodeResource(res, resId, options);
        BitmapFactory.decodeFile(image.getAbsolutePath(),options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        //return BitmapFactory.decodeResource(res, resId, options);
        return BitmapFactory.decodeFile(image.getAbsolutePath(),options);
    }
}
