package domain.usecase.homescreen;

import java.util.List;


import domain.interfaces.IQuotesRepository;
import domain.model.Quote;
import io.reactivex.rxjava3.core.Single;

public class GetQuotes {

    private final IQuotesRepository quotesRepository;

    public GetQuotes(IQuotesRepository quoteRepository) {
        this.quotesRepository = quoteRepository;
    }

    public Single<List<Quote>> getQuotes() {
        return quotesRepository.getQuotes();
    }


}
