package fr.frogdevelopment.bibluelle.gallery.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.DrawableImageViewTarget;

import java.util.List;

import fr.frogdevelopment.bibluelle.GlideRequest;
import fr.frogdevelopment.bibluelle.GlideRequests;
import fr.frogdevelopment.bibluelle.R;
import fr.frogdevelopment.bibluelle.data.entities.BookPreview;

public class CarouselBooksAdapter extends RecyclerView.Adapter<CarouselBooksAdapter.ViewHolder> {

    private final Context mContext;
    private final List<BookPreview> mItems;
    private final OnBookClickListener mListener;
    private final GlideRequest<Drawable> mRequestBuilder;
    private final LayoutInflater mLayoutInflater;

    public CarouselBooksAdapter(Context context, List<BookPreview> previews, OnBookClickListener listener, GlideRequests glideRequests) {
        this.mContext = context;
        this.mItems = previews;
        this.mListener = listener;
        this.mLayoutInflater = LayoutInflater.from(mContext);

        this.mRequestBuilder = glideRequests
                .asDrawable()
                .fitCenter();

        setHasStableIds(true);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public long getItemId(int position) {
        return mItems.get(position).isbn.hashCode();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_carousel, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        BookPreview preview = mItems.get(position);

        viewHolder.itemView.setOnClickListener(v -> mListener.onBookClick(v, preview.isbn));

        mRequestBuilder
                .clone()
                .load(mContext.getFileStreamPath(preview.getCoverFile()))
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(new DrawableImageViewTarget(viewHolder.mCoverView) {

                    @Override
                    protected void setResource(@Nullable Drawable resource) {
                        RippleDrawable rippledImage = new RippleDrawable(ColorStateList.valueOf(mContext.getColor(R.color.colorPrimaryDark)), resource, null);
                        super.setResource(rippledImage);
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView mCoverView;

        ViewHolder(View itemView) {
            super(itemView);

            mCoverView = itemView.findViewById(R.id.item_cover);
        }
    }
}