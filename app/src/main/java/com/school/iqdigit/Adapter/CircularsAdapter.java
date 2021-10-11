package com.school.iqdigit.Adapter;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.school.iqdigit.Activity.PdfWebViewActivity;
import com.school.iqdigit.Model.Circular;
import com.school.iqdigit.Model.Image;
import com.school.iqdigit.R;

import java.util.List;
import java.util.StringTokenizer;

import static com.school.iqdigit.Api.RetrofitClient.BASE_URL2;


public class CircularsAdapter extends RecyclerView.Adapter<CircularsAdapter.AssSubHolder> {
    private Context mCtx;
    private List<Circular> circularList;
    DownloadManager downloadManager;

    public CircularsAdapter(Context mCtx, List<Circular> circularList) {
        this.mCtx = mCtx;
        this.circularList = circularList;
    }

    @NonNull
    @Override
    public AssSubHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recycler_all_circulars, viewGroup, false);
        return new AssSubHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final AssSubHolder assSubHolder, int i) {
        final Circular circular = circularList.get(i);
        assSubHolder.title.setText(circular.getTitle());
        assSubHolder.description.setText(circular.getDescription());
        Log.d("circular.getImg(): ",circular.getImg());
        assSubHolder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (circular.getImg().endsWith("pdf")) {
                    download(circular.getImg());
                } else {
                    downloadManager = (DownloadManager) mCtx.getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri;
                    if (circular.getImg().contains("https")) {
                        uri = Uri.parse(circular.getImg());
                    } else {
                        uri = Uri.parse(BASE_URL2 + circular.getImg());
                    }

                    DownloadManager downloadmanager = (DownloadManager) mCtx.getSystemService(Context.DOWNLOAD_SERVICE);
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    Long refrence = downloadManager.enqueue(request);
                    final String urldata = circular.getImg();
                    String last = urldata.substring(urldata.lastIndexOf("/") + 1);
                    request.setTitle(last);
                    request.setDescription("Downloading");
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, last);
                    downloadmanager.enqueue(request);
                    Toast.makeText(mCtx, "File is downloaded successfully and saved in downloads", Toast.LENGTH_LONG).show();
                }
            }
        });
        assSubHolder.btnViewCircular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCtx, PdfWebViewActivity.class);
                intent.putExtra("pdf",circular.getImg());
                intent.putExtra("title","Circular");
                mCtx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return circularList.size();
    }

    class AssSubHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        ImageView download;
        Button btnViewCircular;

        public AssSubHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.circular_title);
            description = itemView.findViewById(R.id.circular_description);
            download = itemView.findViewById(R.id.circular_download);
            btnViewCircular = itemView.findViewById(R.id.btnViewCircular);
        }
    }
    public void download(String path) {
        DownloadManager downloadmanager = (DownloadManager) mCtx.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(path);
        final String urldata = path;
        String last = urldata.substring(urldata.lastIndexOf("/") + 1);
        StringTokenizer tokens = new StringTokenizer(last, ".");
        String filename = tokens.nextToken();
        String extension = tokens.nextToken();
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(last);
        request.setDescription("Downloading");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, last);
        downloadmanager.enqueue(request);
        Toast.makeText(mCtx, "File is downloaded successfully and saved in downloads", Toast.LENGTH_LONG).show();
    }
}
