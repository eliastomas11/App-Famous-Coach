package data.datasources.local.dataBase.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import data.datasources.local.dataBase.emotion.EmotionEntity;
import data.datasources.local.dataBase.quote.QuoteEntity;

public class QuoteWithEmotion {
    @Embedded public QuoteEntity quoteEntity;

    @Relation(parentColumn = "emotionId", entityColumn = "id")
    public EmotionEntity emotionEntity;
}
