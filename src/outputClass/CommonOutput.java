package outputClass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;

public abstract class CommonOutput {

    private String status = "FAIL";
    private String engineName = "";
    private static final Logger logger = LoggerFactory.getLogger(CommonOutput.class);

    public File saveFile(byte[] result, String filePath, String fileName){
        File resultFile = new File(filePath);
        try {
            if(!resultFile.exists()){
                resultFile.mkdirs();
                logger.info("==== Directory 생성 ====");
                logger.info(resultFile.getPath());
            }
            try (FileOutputStream fos = new FileOutputStream(resultFile + fileName)) {
                fos.write(result);
                fos.flush();

                return new File(resultFile+fileName);
            }
        }catch (Exception e){
            e.getStackTrace();
            logger.error("saveFile error : {0}",e);
            return null;
        }
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEngineName() {
        return engineName;
    }

    private void setEngineName(String engineName) {
        this.engineName = engineName;
    }
}
