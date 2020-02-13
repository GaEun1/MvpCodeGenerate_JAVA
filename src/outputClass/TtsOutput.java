package outputClass;

public class TtsOutput extends CommonOutput {
    private String contentType;
    private byte[] resultByteArray;

    public TtsOutput(){
        setEngineName("TTS");
    }



    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getResultByteArray() {
        return resultByteArray;
    }

    public void setResultByteArray(byte[] resultByteArray) {
        this.resultByteArray = resultByteArray;
    }
}
