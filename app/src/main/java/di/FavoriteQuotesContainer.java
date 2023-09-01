package di;

import com.example.yourfamouscoach.ui.interfaces.IFavoritesView;
import com.example.yourfamouscoach.ui.presenters.FavoritesQuotesPresenter;

import data.repository.QuoteRepositoryImpl;
import domain.usecase.favoritequotes.GetQuoteList;

public class FavoriteQuotesContainer {
    private final GetQuoteList getQuoteListUseCase;
    public FavoriteQuotesContainer(QuoteRepositoryImpl quoteRepository) {
        getQuoteListUseCase = new GetQuoteList(quoteRepository);
    }

    public FavoritesQuotesPresenter providePresenter(IFavoritesView view){
        return new FavoritesQuotesPresenter(view,getQuoteListUseCase);
    }
}
