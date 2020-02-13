import engine.EngineCommon;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import outputClass.*;

import java.util.List;


public class MvpApiFlow {

    private String apiId;
    private String apiKey;
    private Object resultToInput;
    private ApiCallback apiCallback;
    private static final Logger logger = LoggerFactory.getLogger(MvpApiFlow.class);


    MvpApiFlow(){
        apiId = "";
        apiKey = "";
        resultToInput = null;
        apiCallback = null;
    }


    public interface ApiCallback{
        void onComplete(Object result);
        void onFail(String errMsg);
        Object onNext(SttOutput sttOutput);
        Object onNext(TtsOutput ttsOutput);
        Object onNext(NluOutput nluOutput);
        Object onNext(GptOutput gptOutput);
        Object onNext(DenoiseOutput denoiseOutput);
        Object onNext(DiarizationOutput diarizationOutput);
        Object onNext(EsrOutput esrOutput);
    }


    void callAPI(List<EngineCommon> playList) {

        for(EngineCommon engine : playList){
            logger.info("{} start!", engine.getClass().getSimpleName());

            engine.setInputData(resultToInput);
            CommonOutput output = null;

            try {
                output = engine.execApi(apiId, apiKey);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(output == null){
                apiCallback.onFail("**** execApi() Exception occur.");
            }else if(output.getStatus().equals("FAIL")){
                apiCallback.onFail("**** execApi() result status FAIL");
            }

            // Instance type casting
            resultToInput = callOnNext(output);

            if(resultToInput == null){
                logger.error("**** User error");
            }
        }
        apiCallback.onComplete(resultToInput);
    }

    private Object callOnNext(CommonOutput output){
        Object result = null;
        if(output instanceof SttOutput){
            logger.info("#### STT OUTPUT");
            result = apiCallback.onNext((SttOutput)output);
        }else if(output instanceof TtsOutput){
            logger.info("#### TTS OUTPUT");
            result = apiCallback.onNext((TtsOutput)output);
        }else if(output instanceof NluOutput){
            logger.info("#### NLU OUTPUT");
            result = apiCallback.onNext((NluOutput)output);
        }else if(output instanceof GptOutput){
            logger.info("#### GPT OUTPUT");
            result = apiCallback.onNext((GptOutput) output);
        }else if(output instanceof DenoiseOutput){
            logger.info("#### Denoise OUTPUT");
            result = apiCallback.onNext((DenoiseOutput) output);
        }else if(output instanceof DiarizationOutput){
            logger.info("#### Diarization OUTPUT");
            result = apiCallback.onNext((DiarizationOutput) output);
        }else if(output instanceof EsrOutput){
            logger.info("#### ESR OUTPUT");
            result = apiCallback.onNext((EsrOutput) output);
        }else{
            logger.error("**** EngineOutput type unmatched.");
        }
        return result;
    }


    /* Getter and Setter */
    public String getApiId(){
        return this.apiId;
    }
    private void setApiId(String apiId){
        this.apiId = apiId;
    }
    public String getApiKey(){
        return this.apiKey;
    }
    private void setApiKey(String apiKey){
        this.apiKey = apiKey;
    }

    void setResultToInput(Object resultToInput){
        this.resultToInput = resultToInput;
    }
    private void setCallback(ApiCallback newApiCallback){
        this.apiCallback = newApiCallback;
    }
}