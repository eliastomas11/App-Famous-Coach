package data.datasources.local.cache;

import java.util.List;

import data.model.QuoteDto;

public class QuoteMemCache {

    private List<QuoteDto> quoteList;

    public List<QuoteDto> getQuotes(){
        return quoteList;
    }

    public void saveQuotes(List<QuoteDto> quoteDtos){
        quoteList = quoteDtos;
    }
}
