package domain.usecase.homescreen;

import domain.interfaces.IQuotesRepository;
import domain.model.Quote;
import io.reactivex.rxjava3.core.Single;

public class SpecificQuote {
    private final IQuotesRepository quotesRepository;

    public SpecificQuote(IQuotesRepository quoteRepository) {
        this.quotesRepository = quoteRepository;
    }

    public Single<Quote> getAnswer(String emotion){
        return quotesRepository.getQuoteWithEmotion(emotion);
    }

}
