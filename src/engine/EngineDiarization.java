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
import outputClass.DiarizationOutput;


public class EngineDiarization extends EngineCommon{

    private Object inputData;
    private static final Logger logger = LoggerFactory.getLogger(EngineDiarization.class);


    /* Constructor */
    public EngineDiarization(){ }


    /* api call function */
    @Override
    public CommonOutput execApi(String apiId, String apiKey) throws ParseException {

        ContentBody cb = makeContentBody(inputData, this.getClass().getSimpleName());

        DiarizationOutput diarizationOutput = new DiarizationOutput();

        String url = "https://dev-api.maum.ai" + "/api/dap/diarize";

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addPart("reqVoice", cb);
        builder.addPart("apiId", new StringBody(apiId, ContentType.MULTIPART_FORM_DATA));
        builder.addPart("apiKey", new StringBody(apiKey, ContentType.MULTIPART_FORM_DATA));

        HttpEntity entity = builder.build();

        // send POST
        String result = (String)super.sendPostReq(url, null, entity);

        if(result == null){
            logger.debug("**** post result is null.");
            diarizationOutput.setStatus("FAIL");
        }else{
            JSONParser parser = new JSONParser();
            JSONObject jObj = (JSONObject)parser.parse(result);

            diarizationOutput.setStatus("SUCCESS");
            diarizationOutput.setResultJson(jObj);
            logger.debug("Diarization result : {}", diarizationOutput.getResultJson());
        }
        return diarizationOutput;

    }

    /* Getter and Setter */
    @Override
    public void setInputData(Object inputData) {
        this.inputData = inputData;
    }
}
