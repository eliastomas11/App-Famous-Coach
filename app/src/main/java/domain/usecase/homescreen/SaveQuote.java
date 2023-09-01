package domain.usecase.homescreen;

import android.util.Log;

import domain.interfaces.IQuotesRepository;
import domain.model.Quote;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;

public class SaveQuote {

    private final IQuotesRepository quotesRepository;

    public SaveQuote(IQuotesRepository quoteRepository) {
        this.quotesRepository = quoteRepository;
    }

    public @NonNull Completable saveQuote(Quote quote) {

        return quotesRepository.saveQuote(quote);
    }

}
