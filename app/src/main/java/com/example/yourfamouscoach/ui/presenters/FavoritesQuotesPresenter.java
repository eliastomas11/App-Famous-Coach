package com.example.yourfamouscoach.ui.presenters;

import com.example.yourfamouscoach.ui.interfaces.IFavoritesQuotesPresenter;
import com.example.yourfamouscoach.ui.interfaces.IFavoritesView;
import com.example.yourfamouscoach.ui.mappers.PresentationMapper;
import com.example.yourfamouscoach.ui.model.QuotePresentation;

import java.util.List;

import domain.model.Quote;
import domain.usecase.favoritequotes.GetQuoteList;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import utils.QuoteMapper;

public class FavoritesQuotesPresenter implements IFavoritesQuotesPresenter {

    private IFavoritesView view;
    private GetQuoteList getQuoteListUseCase;
    public FavoritesQuotesPresenter(IFavoritesView view,GetQuoteList getQuoteListUseCase) {
        this.view = view;
        this.getQuoteListUseCase = getQuoteListUseCase;
    }

    @Override
    public void fetchQuotes() {
        view.showProgressBar();
        getQuoteListUseCase.getQuoteList().
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Quote>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<Quote> quoteList) {
                        view.hideProgressBar();
                        if(quoteList.isEmpty()){
                            view.showEmptyMessage("Empty");
                        }
                        view.showQuoteList(PresentationMapper.mapToFavoriteQuotes(quoteList));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        view.hideProgressBar();
                        view.showEmptyMessage(e.getMessage());
                    }
                });
    }
}
