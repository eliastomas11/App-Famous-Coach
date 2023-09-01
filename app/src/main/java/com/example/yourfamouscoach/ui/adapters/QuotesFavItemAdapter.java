package com.example.yourfamouscoach.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourfamouscoach.databinding.QuoteItemBinding;
import com.example.yourfamouscoach.ui.model.QuotePresentation;

import java.util.List;

public class QuotesFavItemAdapter extends RecyclerView.Adapter<QuoteListItemViewHolder> {

    private final List<QuotePresentation> quotePresentationList;
    private final Context context;

    public QuotesFavItemAdapter(Context context, List<QuotePresentation> quotePresentationList) {
        this.context = context;
        this.quotePresentationList = quotePresentationList;
    }

    @NonNull
    @Override
    public QuoteListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        QuoteItemBinding binding = QuoteItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new QuoteListItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull QuoteListItemViewHolder holder, int position) {
        holder.bind(quotePresentationList.get(position));
    }

    @Override
    public int getItemCount() {
        return quotePresentationList.size();
    }


}

class QuoteListItemViewHolder extends RecyclerView.ViewHolder {

    private QuoteItemBinding binding;

    public QuoteListItemViewHolder(@NonNull QuoteItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(QuotePresentation quotePresentation) {
        binding.tvQuotePreview.setText(quotePresentation.getQuote());
        binding.tvQuoteAuthorPreview.setText(quotePresentation.getAuthor());
    }
}
