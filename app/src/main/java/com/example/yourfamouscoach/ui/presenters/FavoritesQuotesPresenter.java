package com.example.yourfamouscoach.ui.presenters;

import com.example.yourfamouscoach.domain.usecase.favoritequotes.DeleteSavedQuote;
import com.example.yourfamouscoach.ui.interfaces.IFavoritesQuotesPresenter;
import com.example.yourfamouscoach.ui.interfaces.IFavoritesView;
import com.example.yourfamouscoach.ui.mappers.PresentationMapper;

import java.util.List;

import com.example.yourfamouscoach.domain.model.Quote;
import com.example.yourfamouscoach.domain.usecase.favoritequotes.GetQuoteList;
import com.example.yourfamouscoach.ui.model.QuotePresentation;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoritesQuotesPresenter implements IFavoritesQuotesPresenter {

    private IFavoritesView view;
    private final GetQuoteList getQuoteListUseCase;

    private final DeleteSavedQuote deleteSavedQuoteUseCase;
    public FavoritesQuotesPresenter(IFavoritesView view,GetQuoteList getQuoteListUseCase,DeleteSavedQuote deleteSavedQuoteUseCase) {
        this.view = view;
        this.getQuoteListUseCase = getQuoteListUseCase;
        this.deleteSavedQuoteUseCase = deleteSavedQuoteUseCase;
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

    @Override
    public void onDeleteClicked(QuotePresentation quotePresentation) {
        deleteSavedQuoteUseCase.
                deleteSavedQuote(PresentationMapper.mapToDomainPresentationQuote(quotePresentation))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        view.showMessage("Deleted");
                        fetchQuotes();

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    @Override
    public void onShareClicked(String quote,String author) {
        view.shareQuoteSaved(quote,author);
    }

    @Override
    public void onCopyClicked(String quoteTextToCopy, String authorTextToCopy) {
        view.copyText(quoteTextToCopy,authorTextToCopy);
    }
}
