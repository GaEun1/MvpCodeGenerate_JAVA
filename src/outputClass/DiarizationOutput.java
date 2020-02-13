package outputClass;

import org.json.simple.JSONObject;

public class DiarizationOutput extends CommonOutput {

    private JSONObject resultJson = null;

    public DiarizationOutput(){
        setEngineName("Diarization");
    }


    public JSONObject getResultJson() {
        return resultJson;
    }

    public void setResultJson(JSONObject resultJson) {
        this.resultJson = resultJson;
    }
}
