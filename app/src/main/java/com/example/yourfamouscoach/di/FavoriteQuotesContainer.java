package com.example.yourfamouscoach.di;

import androidx.fragment.app.FragmentManager;

import com.example.yourfamouscoach.domain.usecase.favoritequotes.DeleteSavedQuote;
import com.example.yourfamouscoach.ui.interfaces.IFavoritesView;
import com.example.yourfamouscoach.ui.presenters.FavoritesQuotesPresenter;

import com.example.yourfamouscoach.data.repository.QuoteRepositoryImpl;
import com.example.yourfamouscoach.domain.usecase.favoritequotes.GetQuoteList;

import dagger.hilt.android.flags.FragmentGetContextFix;

public class FavoriteQuotesContainer {
    private final GetQuoteList getQuoteListUseCase;
    private final DeleteSavedQuote deleteSavedQuoteUseCase;
    public FavoriteQuotesContainer(QuoteRepositoryImpl quoteRepository) {
        getQuoteListUseCase = new GetQuoteList(quoteRepository);
        deleteSavedQuoteUseCase = new DeleteSavedQuote(quoteRepository);
    }

    public FavoritesQuotesPresenter providePresenter(IFavoritesView view){
        return new FavoritesQuotesPresenter(view,getQuoteListUseCase,deleteSavedQuoteUseCase);
    }
}
