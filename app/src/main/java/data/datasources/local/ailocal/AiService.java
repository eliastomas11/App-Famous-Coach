package data.datasources.local.ailocal;

import java.util.List;

import data.datasources.remote.ai.AiResponseModel;
import data.model.QuoteDto;
import io.reactivex.rxjava3.core.Single;

public interface AiService {

    Single<List<AiResponseModel>> getAnswer(String emotions, List<QuoteDto> quoteList);
}
