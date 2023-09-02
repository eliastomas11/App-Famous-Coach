package com.example.yourfamouscoach.data.mapper;

import java.util.ArrayList;
import java.util.List;

import com.example.yourfamouscoach.data.datasources.local.dataBase.quote.QuoteEntity;
import com.example.yourfamouscoach.data.datasources.remote.ai.AiResponseModel;
import com.example.yourfamouscoach.data.model.QuoteDto;
import com.example.yourfamouscoach.domain.model.Quote;

public class QuoteDataMapper {

    public QuoteDataMapper() {
    }

    public List<Quote> dtoToDomain(List<QuoteDto> quoteDtos,String emotion){
        List<Quote> quoteList = new ArrayList<>();
        quoteDtos.forEach(quoteDto -> quoteList.add(transformDto(quoteDto, emotion)));
        return quoteList;
    }

    public Quote transformDto(QuoteDto quoteDto, String emotion){
        return new Quote(quoteDto.getQuote(),quoteDto.getAuthor(),emotion);
    }

    public List<QuoteEntity> domainToDb(List<Quote> quoteList){
        List<QuoteEntity> quoteEntities = new ArrayList<>();
        quoteList.forEach(quote -> quoteEntities.add(transformDomain(quote)));
        return quoteEntities;
    }

    public QuoteEntity transformDomain(Quote quote){
        QuoteEntity quoteEntity = new QuoteEntity();
        quoteEntity.setQuote(quote.getQuote());
        quoteEntity.setAuthor(quote.getAuthor());
        //quoteEntity.setId(Emotions.valueOf(quote.getEmotion()).getId());
        return quoteEntity;
    }

    public Quote aiToDomain(List<AiResponseModel> response,String author){
        return transformAi(response.get(0),author);
    }

    public Quote transformAi(AiResponseModel aiModel,String author){
        return new Quote(aiModel.getGenerated_text(),author,"");
    }

    public Quote transformEntity(QuoteEntity quoteEntity,String emotion){
        return new Quote(quoteEntity.getQuote(),quoteEntity.getAuthor(),emotion);
    }

    public List<Quote> dbToDomain(List<QuoteEntity> quoteEntities,List<String> emotions){
        ArrayList<Quote> quoteList = new ArrayList<>();
        for(int i= 0;i<quoteEntities.size();i++){
            quoteList.add(transformEntity(quoteEntities.get(i),emotions.get(i)));
        }
        return quoteList;
    }

}
