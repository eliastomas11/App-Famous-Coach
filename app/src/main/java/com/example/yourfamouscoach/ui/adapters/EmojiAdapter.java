package com.example.yourfamouscoach.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.yourfamouscoach.R;
import com.example.yourfamouscoach.ui.resources.Emojis;

import java.util.ArrayList;
import java.util.HashMap;

public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.EmojiViewHolder> {

    private ViewPager2 viewPager2;
    private final ArrayList<Emojis> emojiList;

    public EmojiAdapter(ArrayList<Emojis> emojiList) {
        this.emojiList = emojiList;
    }

    @NonNull
    @Override
    public EmojiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EmojiViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.emoji_carrousel,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull EmojiViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return emojiList.size();
    }

    class EmojiViewHolder extends RecyclerView.ViewHolder{

        public EmojiViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        void bind(int position){
            ImageView emoji = itemView.findViewById(R.id.ivEmoji);
            TextView emotion = itemView.findViewById(R.id.tvEmotionName);
            emoji.setImageResource(emojiList.get(position).emojiResource);
            emotion.setText(emojiList.get(position).toString());

        }
    }
}

