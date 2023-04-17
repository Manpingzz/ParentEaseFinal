package edu.northeastern.myapplication.recylerView;

import edu.northeastern.myapplication.R;
import edu.northeastern.myapplication.entity.Tip;
import edu.northeastern.myapplication.tip.SingleTipActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHolder> {

    private Context context;
    private static List<Tip> tipDataList;
    private Map<String, Bitmap> userImageCache = new HashMap<>();

    public CardViewAdapter(Context context, List<Tip> tipDataList) {
        this.context = context;
        this.tipDataList = tipDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.activity_card_view, parent,false);
        final ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Tip currentTip = tipDataList.get(position);
        String titleText = currentTip.getTitle();

        // set display the max length of title
        int maxTitleLength = 36;
        if (titleText.length() > maxTitleLength) {
            titleText = titleText.substring(0, maxTitleLength) + "...";
        }
        holder.title.setText(titleText);

        holder.username.setText(currentTip.getUserName());

        holder.userAvatarImageView.setImageResource(R.drawable.default_profile_image);

        String imageUrl = currentTip.getPictureUrl();
        String userId = currentTip.getUserId();

        holder.userAvatarImageView.setImageResource(R.drawable.default_profile_image);

        Glide.with(context)
                .load(imageUrl)
                .centerCrop()
                .into(holder.image);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View tipView;
        public ImageView image;
        public ImageView userAvatarImageView;
        public TextView title;
        public TextView username;
        public View cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            tipView = itemView;
            image = itemView.findViewById(R.id.imageView);
            userAvatarImageView = itemView.findViewById(R.id.userAvatarImageView);
            title = itemView.findViewById(R.id.titleView);
            username = itemView.findViewById(R.id.usernameView);
            cardView = itemView.findViewById(R.id.cardView);

            // Set an OnClickListener on the card view
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the position of the clicked item
                    int position = getAdapterPosition();
                    // Get the data at the clicked position
                    Tip tip = tipDataList.get(position);
                    // Start the new activity
                    Intent intent = new Intent(v.getContext(), SingleTipActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("tip", tip);
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return tipDataList.size();
    }

    /**
     * Update data of the tipDataList
     *
     * @param tipDataList
     */

    public void setTipDataList(List<Tip> tipDataList) {
        this.tipDataList = tipDataList;
        notifyDataSetChanged();
    }
}

