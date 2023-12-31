package com.example.yourfamouscoach.data.datasources.local.dataBase.emotion;

import androidx.room.TypeConverter;

import com.example.yourfamouscoach.data.model.Emotions;

public class EmotionConverter {

    @TypeConverter
    public Emotions stringToEmotion(String emotion){
        return Emotions.valueOf(emotion.toUpperCase());
    }

    @TypeConverter
    public String emotionToString(Emotions emotions){
        return emotions.getName();
    }
}
