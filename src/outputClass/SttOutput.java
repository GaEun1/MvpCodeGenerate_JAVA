package outputClass;

import org.json.simple.JSONObject;

public class SttOutput extends CommonOutput {

    private JSONObject resultJson = null;

    public SttOutput(){
        setEngineName("STT");
    }


    public JSONObject getResultJson() {
        return resultJson;
    }

    public void setResultJson(JSONObject resultJson) {
        this.resultJson = resultJson;
    }

}
