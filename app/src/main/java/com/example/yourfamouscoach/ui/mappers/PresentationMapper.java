package com.example.yourfamouscoach.ui.mappers;

import com.example.yourfamouscoach.ui.model.QuotePresentation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import domain.model.Quote;

public class PresentationMapper {

    public static List<QuotePresentation> mapToFavoriteQuotes(List<Quote> quoteList){
        return quoteList.stream().map(PresentationMapper::quoteToPresentation).collect(Collectors.toList());
    }

    private static QuotePresentation quoteToPresentation(Quote quote){
        return new QuotePresentation(quote.getQuote(),quote.getAuthor(),quote.getEmotion());
    }
}
