
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import engine.*;
import outputClass.*;

public class MvpClient {

    private static final Logger logger = LoggerFactory.getLogger(MvpClient.class);


    public void APICall(){

        MvpApiFlow mvpApiFlow = new MvpApiFlow();
        List<EngineCommon> engineList = new ArrayList<>();

        mvpApiFlow.setApiId("");
        mvpApiFlow.setApiKey("");

        // 첫 번째 input setting 및 apiFlow 생성
        mvpApiFlow.setResultToInput(new File("음성 파일 path"));
        engineList.add(new EngineDenoise());
        engineList.add(new EngineSTT("baseline", "16000", "kor"));
        engineList.add(new EngineGPT("kor"));
        engineList.add(new EngineTTS("baseline_kor"));



        mvpApiFlow.setCallback(new MvpApiFlow.ApiCallback(){
            @Override
            // MVP flow 모두 완료 --> 최종 output customising 가능
            public void onComplete(Object result){
                logger.info("onSuccess");
            }

            @Override
            // MVP flow 실행중 에러 발생
            public void onFail(String errMsg){
                logger.info("onFail :: {}", errMsg);
            }

            Map<String, Object> convertToMap(String contentType, byte[] array){
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("type", contentType);
                resultMap.put("data", array);
                return resultMap;
            }

            //============= 각 API 실행 완료 --> output customising 가능 ==========//
            @Override
            public Object onNext(SttOutput sttOutput){
                logger.info("onNext :: {}", sttOutput.getEngineName());
                String lastOutput = sttOutput.getResultJson().get("data").toString();
                return lastOutput;
            }

            @Override
            public Object onNext(TtsOutput ttsOutput){
                logger.info("onNext :: {}", ttsOutput.getEngineName());
                String contentType = ttsOutput.getContentType();
                byte[] resultByteArray = ttsOutput.getResultByteArray();

//                String fileName = "/TTS";
//                String fileType = "." + contentType.substring(contentType.indexOf('/')+1);
//                ttsOutput.saveFile(resultByteArray, "", fileName + fileType);

                return convertToMap(contentType, resultByteArray);
            }

            @Override
            public Object onNext(NluOutput nluOutput){
                logger.info("onNext :: {}", nluOutput.getEngineName());
                return nluOutput.getResultJson();
            }

            @Override
            public Object onNext(GptOutput gptOutput){
                logger.info("onNext :: {}", gptOutput.getEngineName());
                String lastOutput = gptOutput.getResultJson().get("result").toString();
                return lastOutput;
            }

            @Override
            public Object onNext(DenoiseOutput denoiseOutput){
                logger.info("onNext :: {}", denoiseOutput.getEngineName());
                String contentType = denoiseOutput.getContentType();
                byte[] resultByteArray = denoiseOutput.getResultByteArray();

//                String fileName = "/Denoise";
//                String fileType = "." + contentType.substring(contentType.indexOf('/')+1);
//                denoiseOutput.saveFile(resultByteArray, "", fileName + fileType);

                return convertToMap(contentType, resultByteArray);
            }

            @Override
            public Object onNext(DiarizationOutput diarizationOutput){
                logger.info("onNext :: {}", diarizationOutput.getEngineName());
                return diarizationOutput.getResultJson();
            }

            @Override
            public Object onNext(EsrOutput esrOutput){
                logger.info("onNext :: {}", esrOutput.getEngineName());
                String contentType = esrOutput.getContentType();
                byte[] resultByteArray = esrOutput.getResultByteArray();

//                String fileName = "/ESR";
//                String fileType = "." + contentType.substring(contentType.indexOf('/')+1);
//                esrOutput.saveFile(resultByteArray, "", fileName + fileType);

                return convertToMap(contentType, resultByteArray);
            }

        });

        // Thread start
        Thread thread = new Thread(() -> mvpApiFlow.callAPI(engineList));
        thread.start();


    }

}
