package com.example.yourfamouscoach.data.datasources.local.cache;

import com.example.yourfamouscoach.data.model.QuoteDto;

import java.util.List;

public class QuoteMemCache {

    private List<QuoteDto> quoteList;

    public List<QuoteDto> getQuotes(){
        return quoteList;
    }

    public void saveQuotes(List<QuoteDto> quoteDtos){
        quoteList = quoteDtos;
    }

}
