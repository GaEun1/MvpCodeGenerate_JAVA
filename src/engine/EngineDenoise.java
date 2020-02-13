package engine;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import outputClass.CommonOutput;
import outputClass.DenoiseOutput;

import java.util.Map;

public class EngineDenoise extends EngineCommon{

    private Object inputData;
    private static final Logger logger = LoggerFactory.getLogger(EngineDenoise.class);


    /* Constructor */
    public EngineDenoise(){ }


    /* api call function */
    @Override
    public CommonOutput execApi(String apiId, String apiKey) {

        ContentBody cb = makeContentBody(inputData, this.getClass().getSimpleName());

        DenoiseOutput denoiseOutput = new DenoiseOutput();

        String url = "https://dev-api.maum.ai" + "/api/dap/denoise";

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addPart("file", cb);
        builder.addPart("apiId", new StringBody(apiId, ContentType.MULTIPART_FORM_DATA));
        builder.addPart("apiKey", new StringBody(apiKey, ContentType.MULTIPART_FORM_DATA));

        HttpEntity entity = builder.build();

        // send POST
        Map<String, Object> result = (Map<String, Object>) super.sendPostReq(url, null, entity);

        if(result == null){
            logger.debug("**** post result is null.");
            denoiseOutput.setStatus("FAIL");
        }else{
            denoiseOutput.setStatus("SUCCESS");
            denoiseOutput.setContentType(result.get("type").toString());
            denoiseOutput.setResultByteArray((byte[])result.get("data"));
            logger.debug("Denoise result: {}", result.get("type"));
        }
        return denoiseOutput;

    }

    /* Getter and Setter */
    @Override
    public void setInputData(Object inputData) {
        this.inputData = inputData;
    }
}
