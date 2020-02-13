package engine;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import outputClass.CommonOutput;
import outputClass.GptOutput;

import java.util.HashMap;
import java.util.Map;

public class EngineGPT extends EngineCommon{

    private String lang;
    private Object inputData;
    private static final Logger logger = LoggerFactory.getLogger(EngineGPT.class);


    /* Constructor */
    public EngineGPT(){ }
    public EngineGPT(String lang){
        this.lang = lang;
    }


    /* api call function */
    @Override
    public CommonOutput execApi(String apiId, String apiKey) throws ParseException {

        GptOutput gptOutput = new GptOutput();

        String url = "https://dev-api.maum.ai" + "/api/gpt/";
        Map<String, String> headers = new HashMap<>();
        JSONObject json = new JSONObject();

        headers.put("Accept", "application/json");
        headers.put("Content-type", "application/json");
        headers.put("Accept-Charset", "UTF-8");

        json.put("apiId", apiId);
        json.put("apiKey", apiKey);
        json.put("lang", lang);
        json.put("context", (String)inputData);

        HttpEntity entity = new StringEntity(json.toString(),"utf-8");

        // send POST
        String result = (String)super.sendPostReq(url, headers, entity);

        if(result == null){
            logger.debug("**** post result is null.");
            gptOutput.setStatus("FAIL");
        }else{
            JSONParser parser = new JSONParser();
            JSONObject jObj = (JSONObject)parser.parse(result);

            gptOutput.setStatus("SUCCESS");
            gptOutput.setResultJson(jObj);
            logger.debug("GPT result : {}", gptOutput.getResultJson());
        }
        return gptOutput;


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