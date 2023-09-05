package com.example.yourfamouscoach.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourfamouscoach.databinding.QuoteItemBinding;
import com.example.yourfamouscoach.ui.interfaces.IFavoritesQuoteListView;
import com.example.yourfamouscoach.ui.model.QuotePresentation;

import java.util.List;

public class QuotesFavItemAdapter extends RecyclerView.Adapter<QuoteListItemViewHolder> {

    private final List<QuotePresentation> quotePresentationList;
    private final Context context;

    private final IFavoritesQuoteListView favoritesQuoteListView;

    public QuotesFavItemAdapter(Context context, List<QuotePresentation> quotePresentationList,IFavoritesQuoteListView favoritesQuoteListView) {
        this.context = context;
        this.quotePresentationList = quotePresentationList;
        this.favoritesQuoteListView = favoritesQuoteListView;
    }

    @NonNull
    @Override
    public QuoteListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        QuoteItemBinding binding = QuoteItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new QuoteListItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull QuoteListItemViewHolder holder, int position) {
        holder.bind(quotePresentationList.get(position),favoritesQuoteListView);
    }

    @Override
    public int getItemCount() {
        return quotePresentationList.size();
    }


}

