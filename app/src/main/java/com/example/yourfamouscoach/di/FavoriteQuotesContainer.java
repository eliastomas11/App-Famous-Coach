package com.example.yourfamouscoach.di;

import com.example.yourfamouscoach.ui.interfaces.IFavoritesView;
import com.example.yourfamouscoach.ui.presenters.FavoritesQuotesPresenter;

import com.example.yourfamouscoach.data.repository.QuoteRepositoryImpl;
import com.example.yourfamouscoach.domain.usecase.favoritequotes.GetQuoteList;

public class FavoriteQuotesContainer {
    private final GetQuoteList getQuoteListUseCase;
    public FavoriteQuotesContainer(QuoteRepositoryImpl quoteRepository) {
        getQuoteListUseCase = new GetQuoteList(quoteRepository);
    }

    public FavoritesQuotesPresenter providePresenter(IFavoritesView view){
        return new FavoritesQuotesPresenter(view,getQuoteListUseCase);
    }
}
