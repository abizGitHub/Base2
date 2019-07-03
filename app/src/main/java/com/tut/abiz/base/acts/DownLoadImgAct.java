package com.tut.abiz.base.acts;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.tut.abiz.base.Consts;
import com.tut.abiz.base.R;
import com.tut.abiz.base.model.Confiq;
import com.tut.abiz.base.model.GeneralModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by abiz on 5/28/2019.
 */

public class DownLoadImgAct extends BaseActivity {

    private ImageView imageView;
    private ImageButton btn;
    private Bitmap bitmap;
    public static final String DOWNLOAD_DIR = "abiz";
    private static final int PERM_REQ_WRITE = 1111;
    private static final int PERM_REQ_READ = 2222;
    private GeneralModel generalModel;
    private String imageName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_downloadimg);
        generalModel = (GeneralModel) getIntent().getExtras().getSerializable(Consts.GENERALMODEL);
        imageName = "img" + getSelectedTable() + "_" + generalModel.getId() + ".jpg";
        Toast.makeText(this, imageName, Toast.LENGTH_SHORT).show();
        imageView = (ImageView) findViewById(R.id.imageView);
        btn = (ImageButton) findViewById(R.id.dwnld);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(DownLoadImgAct.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // request for permission
                ActivityCompat.requestPermissions(DownLoadImgAct.this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE}, PERM_REQ_READ);
            }
        }
        final File file = getFile();
        //btn.setEnabled(false);
        downloadImage(file.getAbsolutePath(), false);
        imageView.setImageBitmap(bitmap);

        if (bitmap == null || bitmap.getByteCount() < 999) {
            //btn.setEnabled(true);
            GetXMLTask task = new GetXMLTask();
            Confiq confiq = getDbHelper().getBriefConfiq();
            String rnd = (new Random().nextInt(999) * 31) + "-" +
                    (new Random().nextInt(999) * 17) + "-" +
                    (new Random().nextInt(999) * 13);
            String url = Consts.SERVERADDRESS + Consts.ADDRESSIMG + "/" +
                    getSelectedTable() + "/" + confiq.getUserId() + "/" +
                    rnd + "/" + generalModel.getId() + "/" + confiq.getUserName();
            task.execute(new String[]{url});
        }
        if (bitmap == null || bitmap.getByteCount() < 999) {
            btn.setVisibility(View.VISIBLE);
        } else
            btn.setVisibility(View.GONE);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (bitmap != null) {
                    try {
                        if (getFile().exists())
                            return;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (ActivityCompat.checkSelfPermission(DownLoadImgAct.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {
                                // request for permission
                                ActivityCompat.requestPermissions(DownLoadImgAct.this, new String[]{
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERM_REQ_WRITE);
                            }
                        }
                        File outFile = file;
                        OutputStream output = new FileOutputStream(outFile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
                        output.flush();
                        output.close();
                        Toast.makeText(DownLoadImgAct.this, "saved in " + DOWNLOAD_DIR + "/" + imageName, Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {
                        //log.e("<<", e.getMessage());
                        e.printStackTrace();
                    } catch (IOException e) {
                        //log.e("<<", e.getMessage());
                        e.printStackTrace();
                    }
                } else
                    Toast.makeText(DownLoadImgAct.this, "download first", Toast.LENGTH_SHORT).show();
            }

        });

    }

    @NonNull
    private File getFile() {
        String filename = imageName;
        File outFile = new File(
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + DOWNLOAD_DIR,
                filename);
        new File(Environment.getExternalStorageDirectory().getAbsolutePath()).mkdir();
        new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + DOWNLOAD_DIR).mkdir();
        if (!outFile.getParentFile().exists()) {
            outFile.getParentFile().mkdirs();
            outFile.getParentFile().mkdir();
        }
        return outFile;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERM_REQ_WRITE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "permission denied. closing application", Toast.LENGTH_SHORT).show();
                finish();
            }
            return;
        }
        if (requestCode == PERM_REQ_READ) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "permission denied. closing application", Toast.LENGTH_SHORT).show();
                finish();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public Bitmap downloadImage(String url, boolean fromNet) {
        bitmap = null;
        InputStream stream = null;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 1;

        try {
            if (fromNet) {
                stream = getHttpConnection(url);
                bitmap = BitmapFactory.
                        decodeStream(stream, null, bmOptions);
                if (stream != null)
                    stream.close();
            } else {
                bitmap = BitmapFactory.
                        decodeFile(url);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return bitmap;
    }

    private InputStream getHttpConnection(String urlString) throws IOException {
        InputStream stream = null;
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();

        try {
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();

            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = httpConnection.getInputStream();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return stream;
    }

    private class GetXMLTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap map = null;
            for (String url : urls) {
                map = downloadImage(url, true);
            }
            return map;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }

    }


    @Override
    protected void doStaredTasks() {
    }

    @Override
    protected ArrayAdapter getListAdapter() {
        return null;
    }

    @Override
    protected ArrayList<String> getGeneralTitles() {
        return null;
    }

    @Override
    protected ArrayList<GeneralModel> getGeneralList() {
        return null;
    }

    @Override
    public void onStarChanged(int position, boolean checked) {
    }
}
