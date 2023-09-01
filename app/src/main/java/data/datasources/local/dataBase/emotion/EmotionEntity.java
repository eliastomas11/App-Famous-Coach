package data.datasources.local.dataBase.emotion;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


import data.model.Emotions;

@Entity(tableName = "emotion")
public class EmotionEntity {
    @PrimaryKey(autoGenerate = true)
    int id;
    Emotions name;

    public EmotionEntity(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Emotions getName() {
        return name;
    }

    public void setName(Emotions name) {
        this.name = name;
    }
}
