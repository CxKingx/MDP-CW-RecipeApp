package cn.edu.nottingham.s20125628.recipecw;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ViewVideo extends AppCompatActivity {
    String VideoPath,VideoTitle;
    TextView VideoTitleView;
    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_video);
        Intent video=getIntent();
        VideoTitleView=findViewById(R.id.VideoTitle);
        VideoTitle=video.getStringExtra("VideoTitle");
        VideoTitleView.setText(VideoTitle);
        videoView = findViewById(R.id.videoView);

        PlayVideo();


    }
    public void PlayVideo(){
        MediaController mediaController = new MediaController(ViewVideo.this);
        mediaController.setAnchorView(videoView);
        getAllVideoPath(getApplicationContext());
        Uri newUri = Uri.parse(VideoPath);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(newUri);
        videoView.requestFocus();
        videoView.start();
    }
    // Load all video path and Find The correct video path based on title
    private void getAllVideoPath(Context context) {
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Log.d("TAG", "print all URis " + uri.toString());
        String[] projection = { MediaStore.Video.VideoColumns.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        ArrayList<String> pathArrList = new ArrayList<String>();

        int vidsCount = 0;
        if (cursor != null) {
            vidsCount = cursor.getCount();
            Log.d("TAG", "Total count of videos: " + vidsCount);
            while (cursor.moveToNext()) {
                pathArrList.add(cursor.getString(0));
                Log.d("TAG", cursor.getString(0));
            }
            cursor.close();
        }
        Log.d("TAG", "Out of videoloop " );
        for (String num : pathArrList) {
            if(num.contains(VideoTitle)){
                Log.d("TAG", "Gottem" );
                VideoPath=num;
            }
            Log.d("TAG", num );
        }
        //return pathArrList.toArray(new String[pathArrList.size()]);
    }
    /*
    public void PlayVideo(View view) {
        VideoView videoView =(VideoView)findViewById(R.id.videoView3);
        MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);
        //Uri newUri = Uri.fromFile(new File(testuri.getPath()));
        Uri newUri = Uri.parse(testuri.toString());
        //specify the location of media file
        //Uri uri=Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/media/1.mp4");

        //Setting MediaController and URI, then starting the videoView
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(newUri);
        videoView.requestFocus();
        videoView.start();
    }

 */
    /*
    private void LaunchVideoService() {
        VideoService videoService = new VideoService(getApplicationContext(),this);
        videoServiceIntent=new Intent(this, VideoService.class);
        videoServiceIntent.putExtra("VideoTitle",VideoTitle);
        videoServiceIntent.putExtra("videoView", (Parcelable) videoView);
        videoServiceIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT );
        Log.d("TAG","Starting Servcew");
        getApplicationContext().startService(videoServiceIntent);
    }

 */
}