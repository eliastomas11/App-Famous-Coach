package com.example.yourfamouscoach.ui.interfaces;

import com.example.yourfamouscoach.ui.model.QuotePresentation;

import java.util.List;

public interface IFavoritesQuotesPresenter {

    void fetchQuotes();

    void onDeleteClicked(QuotePresentation quotePresentation);

    void onShareClicked(String quote,String author);
    void onCopyClicked(String quoteTextToCopy, String authorTextToCopy);
}
