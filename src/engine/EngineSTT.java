package engine;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import outputClass.CommonOutput;
import outputClass.SttOutput;

public class EngineSTT extends EngineCommon {

    private Object inputData;
    private String level;
    private String samplingRate;
    private String lang;
    private static final Logger logger = LoggerFactory.getLogger(EngineSTT.class);


    /* Constructor */
    public EngineSTT(){  }
    public EngineSTT(String level, String samplingRate, String lang){
        this.level = level;
        this.samplingRate = samplingRate;
        this.lang = lang;
    }


    /* api call function */
    @Override
    public CommonOutput execApi(String apiId, String apiKey) throws ParseException {

        ContentBody cb = makeContentBody(inputData, this.getClass().getSimpleName());

        SttOutput sttOutput = new SttOutput();

        String url = "https://dev-api.maum.ai" + "/api/stt/";

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addPart("file", cb);
        builder.addPart("lang", new StringBody(lang, ContentType.MULTIPART_FORM_DATA));
        builder.addPart("level", new StringBody(level, ContentType.MULTIPART_FORM_DATA));
        builder.addPart("sampling", new StringBody(samplingRate, ContentType.MULTIPART_FORM_DATA));
        builder.addPart("ID", new StringBody(apiId, ContentType.MULTIPART_FORM_DATA));
        builder.addPart("key", new StringBody(apiKey, ContentType.MULTIPART_FORM_DATA));
        builder.addPart("cmd", new StringBody("runFileStt", ContentType.MULTIPART_FORM_DATA));

        HttpEntity entity = builder.build();

        // send POST
        String result = (String)super.sendPostReq(url, null, entity);

        if(result == null){
            logger.debug("**** post result is null.");
            sttOutput.setStatus("FAIL");
        }else{
            JSONParser parser = new JSONParser();
            JSONObject jObj = (JSONObject)parser.parse(result);

            sttOutput.setStatus("SUCCESS");
            sttOutput.setResultJson(jObj);
            logger.debug("STT result : {}", sttOutput.getResultJson());
        }
        return sttOutput;


    }




/* Getter and Setter */
    @Override
    public void setInputData(Object inputData) {
        this.inputData = inputData;
    }

    public Object getInputData() {
        return inputData;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSamplingRate() {
        return samplingRate;
    }

    public void setSamplingRate(String samplingRate) {
        this.samplingRate = samplingRate;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
