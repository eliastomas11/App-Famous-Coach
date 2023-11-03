package com.example.yourfamouscoach.data.mapper;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.yourfamouscoach.data.datasources.local.dataBase.quote.QuoteCache;
import com.example.yourfamouscoach.data.datasources.local.dataBase.quote.QuoteEntity;
import com.example.yourfamouscoach.data.datasources.remote.ai.AiResponseModel;
import com.example.yourfamouscoach.data.model.QuoteDto;
import com.example.yourfamouscoach.domain.model.Quote;

public class QuoteDataMapper {

    public QuoteDataMapper() {
    }

    public List<Quote> dtoToDomain(List<QuoteDto> quoteDtos, String emotion) {
        List<Quote> quoteList = new ArrayList<>();
        quoteDtos.forEach(quoteDto -> quoteList.add(transformDto(quoteDto, emotion)));
        return quoteList;
    }

    public Quote transformDto(QuoteDto quoteDto, String emotion) {
        return new Quote(quoteDto.getQuote(), quoteDto.getAuthor(), emotion);
    }

    public List<QuoteEntity> domainToDb(List<Quote> quoteList) {
        List<QuoteEntity> quoteEntities = new ArrayList<>();
        quoteList.forEach(quote -> quoteEntities.add(transformDomain(quote)));
        return quoteEntities;
    }

    public QuoteEntity transformDomain(Quote quote) {
        QuoteEntity quoteEntity = new QuoteEntity();
        quoteEntity.setQuote(quote.getQuote());
        quoteEntity.setAuthor(quote.getAuthor());
        //quoteEntity.setId(Emotions.valueOf(quote.getEmotion()).getId());
        return quoteEntity;
    }

    public Quote aiToDomain(AiResponseModel aiModel, String author, String emotion) {
        return new Quote(aiModel.getGenerated_text(), author, emotion);
    }

    public Quote transformEntity(QuoteEntity quoteEntity, String emotion) {
        return new Quote(quoteEntity.getQuote(), quoteEntity.getAuthor(), emotion);
    }

    public List<Quote> dbToDomain(List<QuoteEntity> quoteEntities, List<String> emotions) {
        ArrayList<Quote> quoteList = new ArrayList<>();
        for (int i = 0; i < quoteEntities.size(); i++) {
            quoteList.add(transformEntity(quoteEntities.get(i), emotions.get(i)));
        }
        return quoteList;
    }

    public List<QuoteCache> dtoToLocalCache(List<QuoteDto> quoteDtoList) {
        return quoteDtoList.stream().map(quoteDto -> new QuoteCache(quoteDto.getQuote(),quoteDto.getAuthor())).collect(Collectors.toList());
    }

    public List<QuoteDto> cacheToDto(List<QuoteCache> quotes) {
        return quotes.stream().map(quoteCache -> new QuoteDto(quoteCache.getQuote(), quoteCache.getAuthor())).collect(Collectors.toList());
    }
}
