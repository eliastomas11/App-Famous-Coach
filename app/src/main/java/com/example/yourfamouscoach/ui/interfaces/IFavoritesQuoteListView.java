package com.example.yourfamouscoach.ui.interfaces;

import com.example.yourfamouscoach.ui.model.QuotePresentation;

public interface IFavoritesQuoteListView {

    void deleteClicked(QuotePresentation quotePresentation);

    void shareClicked(String quote,String author);

    void copyClicked(String quote,String author);
}
