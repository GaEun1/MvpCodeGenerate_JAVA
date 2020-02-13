package engine;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import outputClass.CommonOutput;
import outputClass.TtsOutput;

import java.util.Map;

public class EngineTTS extends EngineCommon{

    private Object inputData;
    private String voiceName;
    private static final Logger logger = LoggerFactory.getLogger(EngineTTS.class);

    /* Constructor */
    public EngineTTS(){ }
    public EngineTTS(String voiceName){
        this.voiceName = voiceName;
    }


    /* api call function */
    @Override
    public CommonOutput execApi(String apiId, String apiKey) {

        logger.info("TTS text : {}", (String)inputData);

        TtsOutput ttsOutput =  new TtsOutput();

        String url = "https://dev-api.maum.ai" + "/tts/stream";

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addPart("apiId", new StringBody(apiId, ContentType.TEXT_PLAIN.withCharset("UTF-8")));
        builder.addPart("apiKey",new StringBody(apiKey, ContentType.TEXT_PLAIN.withCharset("UTF-8")));
        builder.addPart("text", new StringBody((String)inputData, ContentType.TEXT_PLAIN.withCharset("UTF-8")));
        builder.addPart("voiceName", new StringBody(voiceName, ContentType.TEXT_PLAIN.withCharset("UTF-8")));

        HttpEntity entity = builder.build();
        // send POST
        Map<String, Object> result = (Map<String, Object>) super.sendPostReq(url, null, entity);

        if(result == null){
            logger.debug("**** post result is null.");
            ttsOutput.setStatus("FAIL");
        }else{
            ttsOutput.setStatus("SUCCESS");
            ttsOutput.setContentType(result.get("type").toString());
            ttsOutput.setResultByteArray((byte[])result.get("data"));
            logger.debug("TTS result: {}", result.get("type"));
        }
        return ttsOutput;


    }


    /* Getter and Setter */
    @Override
    public void setInputData(Object inputData) {
        this.inputData = inputData;
    }

    public Object getInputData() {
        return inputData;
    }

    public String getVoiceName() {
        return voiceName;
    }

    public void setVoiceName(String voiceName) {
        this.voiceName = voiceName;
    }
}
