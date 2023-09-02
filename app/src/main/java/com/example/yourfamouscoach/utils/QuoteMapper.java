package com.example.yourfamouscoach.utils;

import com.example.yourfamouscoach.ui.model.QuotePresentation;

import java.util.ArrayList;
import java.util.List;

import com.example.yourfamouscoach.data.datasources.local.dataBase.quote.QuoteEntity;
import com.example.yourfamouscoach.data.model.QuoteDto;
import com.example.yourfamouscoach.domain.model.Quote;

public class QuoteMapper {

    public static List<Quote> mapDtoToDomain(List<QuoteDto> quoteDtos){
        ArrayList<Quote> quotes = new ArrayList<>();
        quoteDtos.forEach(quoteDto -> quotes.add(new Quote(quoteDto.getQuote(),quoteDto.getAuthor(),"")));
        return quotes;
    }

    public static List<QuoteEntity> mapDtoToDataBase(List<QuoteDto> quoteDtos) {
        ArrayList<QuoteEntity> quotesEntitys = new ArrayList<>();
        quoteDtos.forEach(quoteDto -> {
            QuoteEntity quoteEntity = new QuoteEntity();
            quoteEntity.setQuote(quoteDto.getQuote());
            quoteEntity.setAuthor(quoteDto.getAuthor());
            quotesEntitys.add(quoteEntity);
        });
        return quotesEntitys;
    }

    public static List<Quote> mapDbToDomain(List<QuoteEntity> quoteEntities){
        ArrayList<Quote> quotes = new ArrayList<>();
        quoteEntities.forEach(quoteDb -> {
            Quote quote = new Quote(quoteDb.getQuote(),quoteDb.getAuthor(),"");
            quotes.add(quote);
        });
        return quotes;
    }

    public static QuotePresentation mapDomainToPresentation(Quote quote){
        return new QuotePresentation(quote.getQuote(),quote.getAuthor());
    }

    public static Quote mapPresentationToDomain(QuotePresentation quotePresentation,String emotion){
        return new Quote(quotePresentation.getQuote(),quotePresentation.getAuthor(),emotion);
    }

    public static List<QuotePresentation> DomainToPresentation(List<Quote> quotes){
        ArrayList<QuotePresentation> quotePresentations = new ArrayList<>();
        quotes.forEach(quote -> quotePresentations.add(mapDomainToPresentation(quote)));
        return quotePresentations;
    }
}
