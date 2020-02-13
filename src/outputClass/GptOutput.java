package outputClass;

import org.json.simple.JSONObject;

public class GptOutput extends CommonOutput {

    private JSONObject resultJson = null;

    public GptOutput(){
        setEngineName("GPT");
    }


    public JSONObject getResultJson() {
        return resultJson;
    }

    public void setResultJson(JSONObject resultJson) {
        this.resultJson = resultJson;
    }
}
