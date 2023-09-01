package domain.usecase.favoritequotes;

import java.util.List;

import domain.interfaces.IQuotesRepository;
import domain.model.Quote;
import io.reactivex.rxjava3.core.Single;

public class GetQuoteList {

    private IQuotesRepository quotesRepository;

    public GetQuoteList(IQuotesRepository quotesRepository) {
        this.quotesRepository = quotesRepository;
    }

    public Single<List<Quote>> getQuoteList(){
        return quotesRepository.getFavoritesQuotes();
    }

}
