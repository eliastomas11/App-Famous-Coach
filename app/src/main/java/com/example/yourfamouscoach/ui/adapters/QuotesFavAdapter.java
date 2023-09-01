package com.example.yourfamouscoach.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourfamouscoach.databinding.QuoteListItemBinding;
import com.example.yourfamouscoach.ui.model.QuotePresentation;
import com.example.yourfamouscoach.ui.resources.Emojis;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QuotesFavAdapter extends RecyclerView.Adapter<QuotesFavAdapter.QuoteViewHolder> {

    private final List<Emojis> emojiList;
    private List<QuotePresentation> quotesList;

    private Context context;

    public QuotesFavAdapter(List<QuotePresentation> quotesList, List<Emojis> emojisList, Context context) {
        this.quotesList = quotesList;
        this.emojiList = emojisList;
        this.emojiList.removeIf(emojis -> quotesList.stream().noneMatch(quotePresentation -> emojis.toString().equalsIgnoreCase(quotePresentation.getEmotion())));
        this.context = context;
    }

    @NonNull
    @Override
    public QuoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        QuoteListItemBinding binding = QuoteListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new QuoteViewHolder(binding, context);
    }

    @Override
    public void onBindViewHolder(@NonNull QuoteViewHolder holder, int position) {
        holder.bind(emojiList.get(position), quotesList.stream().filter(quotePresentation ->
                        quotePresentation.getEmotion().equalsIgnoreCase(emojiList.get(position).toString()))
                .collect(Collectors.toList()));
    }

    @Override
    public int getItemCount() {
        return emojiList.size();
    }

    static class QuoteViewHolder extends RecyclerView.ViewHolder {

        QuoteListItemBinding quoteListItemBinding;
        QuotesFavItemAdapter quotesFavItemAdapter;
        Context context;

        public QuoteViewHolder(@NonNull QuoteListItemBinding quoteListItemBinding, Context context) {
            super(quoteListItemBinding.getRoot());
            this.quoteListItemBinding = quoteListItemBinding;
            this.context = context;
        }


        public void bind(Emojis emoji, List<QuotePresentation> quoteList) {
            quotesFavItemAdapter = new QuotesFavItemAdapter(context, quoteList);
            quoteListItemBinding.rvFavQuoteEmotion.setText(emoji.toString());
            quoteListItemBinding.rvFavFilterQuote.setAdapter(quotesFavItemAdapter);
            quoteListItemBinding.rvFavFilterQuote.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        }
    }
}
