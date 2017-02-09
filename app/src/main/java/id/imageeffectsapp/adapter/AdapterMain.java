package id.imageeffectsapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import id.imageeffectsapp.R;
import id.imageeffectsapp.model.ImageGallery;
import id.imageeffectsapp.views.activity.ImageDetailActivity;

/**
 * Created by Bayu WPP on 1/1/2017.
 */

public class AdapterMain extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    static Context context;
    List<ImageGallery> images;

    public AdapterMain(Context context, List<ImageGallery> images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new ImagesHolder(inflater.inflate(R.layout.adapter_main,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ImagesHolder)holder).bindData(images.get(position));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    private class ImagesHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ImagesHolder(final View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.image_main_adapter);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(itemView.getContext(), ImageDetailActivity.class);
                    in.putExtra("imageDetail",images.get(getAdapterPosition()).getFileName());
                    itemView.getContext().startActivity(in);
                }
            });

        }
        void bindData(ImageGallery image){
            imageView.setImageBitmap(image.getBitmap());
        }
    }
}
