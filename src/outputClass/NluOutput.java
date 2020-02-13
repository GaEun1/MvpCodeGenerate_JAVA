package outputClass;

import org.json.simple.JSONObject;

public class NluOutput extends CommonOutput{

    private JSONObject resultJson = null;

    public NluOutput(){ setEngineName("NLU");}

    public JSONObject getResultJson() {
        return resultJson;
    }

    public void setResultJson(JSONObject resultJson) {
        this.resultJson = resultJson;
    }
}
