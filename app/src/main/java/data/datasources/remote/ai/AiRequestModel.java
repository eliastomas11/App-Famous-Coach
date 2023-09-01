package data.datasources.remote.ai;

public class AiRequestModel {

    private String inputs;

    public String getInputs() {
        return inputs;
    }

    public void setInputs(String inputs) {
        this.inputs = inputs;
    }

    public AiRequestModel(String inputs) {
        this.inputs = inputs;
    }
}
