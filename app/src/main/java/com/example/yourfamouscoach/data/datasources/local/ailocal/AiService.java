package com.example.yourfamouscoach.data.datasources.local.ailocal;

import java.util.List;

import com.example.yourfamouscoach.data.datasources.remote.ai.AiResponseModel;
import com.example.yourfamouscoach.data.model.QuoteDto;
import io.reactivex.rxjava3.core.Single;

public interface AiService {

    Single<AiResponseModel> getAnswer(String emotions, List<QuoteDto> quoteList);
}
