package engine;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import outputClass.CommonOutput;
import outputClass.NluOutput;

import java.util.HashMap;
import java.util.Map;

public class EngineNLU extends EngineCommon{

    private String lang;
    private Object inputData;
    private static final Logger logger = LoggerFactory.getLogger(EngineNLU.class);


    /* Constructor */
    public EngineNLU(){ }
    public EngineNLU(String lang){
        this.lang = lang;
    }


    /* api call function */
    @Override
    public CommonOutput execApi(String apiId, String apiKey) throws ParseException {

        NluOutput nluOutput = new NluOutput();

        String url = "https://dev-api.maum.ai" + "/api/nlu";
        Map<String, String> headers = new HashMap<>();
        JSONObject json = new JSONObject();

        headers.put("Accept", "application/json");
        headers.put("Content-type", "application/json");
        headers.put("Accept-Charset", "UTF-8");

        json.put("apiId", apiId);
        json.put("apiKey", apiKey);
        json.put("lang", lang);
        json.put("reqText", (String)inputData);

        HttpEntity entity = new StringEntity(json.toString(),"utf-8");

        // send POST
        String result = (String)super.sendPostReq(url, headers, entity);

        if(result == null){
            logger.debug("**** post result is null.");
            nluOutput.setStatus("FAIL");
        }else{
            JSONParser parser = new JSONParser();
            JSONObject jObj = (JSONObject)parser.parse(result);

            nluOutput.setStatus("SUCCESS");
            nluOutput.setResultJson(jObj);
            logger.debug("NLU result : {}", nluOutput.getResultJson());
        }
        return nluOutput;


    }


    /* Getter and Setter */
    @Override
    public void setInputData(Object inputData) {
        this.inputData = inputData;
    }

    public Object getInputData() {
        return inputData;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
