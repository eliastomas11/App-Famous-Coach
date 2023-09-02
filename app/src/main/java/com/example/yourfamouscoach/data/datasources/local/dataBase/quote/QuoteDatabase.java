package com.example.yourfamouscoach.data.datasources.local.dataBase.quote;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.yourfamouscoach.data.datasources.local.dataBase.emotion.EmotionsDao;
import com.example.yourfamouscoach.data.datasources.local.dataBase.emotion.EmotionConverter;
import com.example.yourfamouscoach.data.datasources.local.dataBase.emotion.EmotionEntity;

@Database(entities = {QuoteEntity.class, EmotionEntity.class}, version = 1,exportSchema = true)
@TypeConverters({EmotionConverter.class})
public abstract class QuoteDatabase extends RoomDatabase {

    public abstract QuoteDao getQuoteDao();

    public abstract EmotionsDao getEmotionDao();
}
