package cn.edu.nottingham.s20125628.recipecw;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class VideoService extends Service  {
    String VideoTitle,VideoPath;
    Activity activity;
    Context context;
    VideoView videoView;
    public VideoService(Context context,Activity activity){
        this.context=context;
        this.activity=activity;

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();


    }
    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        //Log.d("1","Start Command");
        VideoTitle=intent.getStringExtra("VideoTitle");
        videoView=intent.getParcelableExtra("videoView=");
        startVideo();

        return START_NOT_STICKY;
    }

    private void startVideo() {
        //VideoView videoView = activity.findViewById(R.id.videoView);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        getAllVideoPath(getApplicationContext());
        Uri newUri = Uri.parse(VideoPath);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(newUri);
        videoView.requestFocus();
        videoView.start();
    }
    private void getAllVideoPath(Context context) {
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        //Log.d("TAG", "print all URis " + uri.toString());
        String[] projection = { MediaStore.Video.VideoColumns.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        ArrayList<String> pathArrList = new ArrayList<String>();

        int vidsCount = 0;
        if (cursor != null) {
            vidsCount = cursor.getCount();
            //Log.d("TAG", "Total count of videos: " + vidsCount);
            while (cursor.moveToNext()) {
                pathArrList.add(cursor.getString(0));
               // Log.d("TAG", cursor.getString(0));
            }
            cursor.close();
        }
        //Log.d("TAG", "Out of videoloop " );
        for (String num : pathArrList) {
            if(num.contains(VideoTitle)){
                //Log.d("TAG", "Gottem" );
                VideoPath=num;
            }
           // Log.d("TAG", num );
        }
        //return pathArrList.toArray(new String[pathArrList.size()]);
    }
    @Override
    public void onDestroy() {

    }
    @Override
    public void onTaskRemoved(Intent rootIntent) {

        //stop service
        stopSelf();
    }

}
