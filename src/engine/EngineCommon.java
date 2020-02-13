package engine;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import outputClass.CommonOutput;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public abstract class EngineCommon {

    private static final Logger logger = LoggerFactory.getLogger(EngineCommon.class);

    public abstract CommonOutput execApi(String apiId, String apiKey) throws ParseException;
    public abstract void setInputData(Object resultToInput);


    ContentBody makeContentBody(Object inputData, String className){
        ContentBody cb = null;

        if(inputData instanceof Map){
            Map<String, Object> map = (HashMap<String, Object>)inputData;
            byte[] dataArr = (byte[]) map.get("data");
            cb =  new InputStreamBody(new ByteArrayInputStream(dataArr),className + "." +map.get("type"));
        }else if(inputData instanceof File){
            cb = new FileBody((File)inputData);
        }else if(inputData instanceof String){
            cb = new StringBody((String)inputData, ContentType.TEXT_PLAIN.withCharset("UTF-8"));
        }else{
            logger.error("makeContentBody() :: inputData type이 매치되지 않았습니다. ("+ className +")");
        }
        return cb;
    }


    Object sendPostReq(String url, Map<String, String> headers, HttpEntity entity){ /// return 타입 수정 필요

        try{
            HttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);

            // header setting
            if(headers != null){
                for(String key : headers.keySet()){
                    post.setHeader(key, headers.get(key));
                }
            }

            post.setEntity(entity);
            HttpResponse response = client.execute(post);
            HttpEntity respEntity = response.getEntity();

            ContentType contentType = ContentType.getOrDefault(respEntity);
            logger.info("Resp MimeType : {}  Resp CharSet : {}", contentType.getMimeType(), contentType.getCharset());
            String mimeType = contentType.getMimeType();

            int responseCode = response.getStatusLine().getStatusCode();
            String type = mimeType.substring(mimeType.indexOf('/') + 1);
            if(responseCode == 200){
                if(mimeType.contains("application/json")){
                    return procJsonResult(respEntity);
                }else if(mimeType.contains("audio/")){
                    if(type.contains("wav")){ // TTS는 x-wav 임
                        type = "wav";
                    }
                    return procByteArrResult(respEntity, type);
                }else if(mimeType.contains("image/")){
                    return procByteArrResult(respEntity, type);
                }
            }else{
                logger.error("API call error : \nResponseCode : "+responseCode+", reason : "+ respEntity.getContent());
            }

        }catch(IOException e){
            logger.error("sendPost IOException : "+ e);
        }

        return null;

    }


    private String procJsonResult(HttpEntity respEntity){

        StringBuilder result = new StringBuilder();
        BufferedReader br;

        try {
            br = new BufferedReader(new InputStreamReader(respEntity.getContent(), "utf-8"));
            String line;
            while ((line = br.readLine()) != null) {
                result.append(line.replace("\\n",""));
            }
            br.close();
        } catch (IOException e) {
            logger.error("procJsonResult() exception : "+ e);
            e.printStackTrace();
            return null;
        }

        return result.toString();

    }


    private Map<String, Object> procByteArrResult(HttpEntity respEntity, String fileType){
        Map<String, Object> returnMap =  new HashMap<>();
        InputStream in;

        try {
            in = respEntity.getContent();
            returnMap.put("type", fileType);
            returnMap.put("data", IOUtils.toByteArray(in));
            return returnMap;

        } catch (IOException e) {
            logger.error("procByteArrResult() exception : "+ e);
            e.printStackTrace();
            return null;
        }
    }



}