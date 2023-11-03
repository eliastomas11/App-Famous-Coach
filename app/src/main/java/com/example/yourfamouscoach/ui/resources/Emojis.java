package com.example.yourfamouscoach.ui.resources;

import androidx.annotation.NonNull;

import com.example.yourfamouscoach.R;

public enum Emojis {
    RANDOM(R.drawable.random_emoji),
    HAPPY(R.drawable.happy_emoj),
    SAD(R.drawable.sad_emoji),
    WORRIED(R.drawable.worried_emoji),
    EXCITED(R.drawable.excited_emoji),
    ANGRY(R.drawable.angry_emoji);

    public final int emojiResource;
    private Emojis(int emojiResource) {
        this.emojiResource = emojiResource;
    }

    @NonNull
    @Override
    public String toString() {
        switch (this) {
            case RANDOM: return "random";
            case HAPPY: return "happy";
            case SAD: return "sad";
            case WORRIED: return "worried";
            case EXCITED: return "excited";
            case ANGRY: return "angry";
            default: return "";
        }
    }
}
