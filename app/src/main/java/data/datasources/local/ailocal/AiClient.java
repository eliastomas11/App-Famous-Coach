package data.datasources.local.ailocal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import data.datasources.remote.ai.AiRequestModel;
import data.datasources.remote.ai.AiResponseModel;
import data.datasources.remote.ai.AiService;
import data.datasources.remote.ai.AiTest;
import data.model.QuoteDto;
import io.reactivex.rxjava3.core.Single;


public class AiClient implements data.datasources.local.ailocal.AiService {


    private final String input;

    public AiClient() {
        input = "each different quote starts with ',',which of these quotes sounds";
    }


    @Override
    public Single<List<AiResponseModel>> getAnswer(String emotions, List<QuoteDto> quoteList) {

        AiService service = new AiTest().getRetrofit();
        int bound1 = new Random().nextInt(7);
        int bound2 = 10 + new Random().nextInt( (quoteList.size() - 1) - 10);
        return service.aiAnswer(new AiRequestModel(input + emotions + getQuoteStringList(quoteList).subList(bound1,bound2))).map(aiResponse -> {
            aiResponse.get(0).setGenerated_text(cleanAnswer(aiResponse.get(0).getGenerated_text()));
            return aiResponse;
        });
    }

    private String cleanAnswer(String quote){
        if(!quote.endsWith(".")){
            quote = quote.concat(".");
        }
        return quote;
    }


    private List<String> getQuoteStringList(List<QuoteDto> quoteDtos) {
        List<String> quoteList = new ArrayList<>();
        quoteDtos.forEach(a -> quoteList.add(a.getQuote() + "\n"));
        return quoteList;
    }
}
