package outputClass;

public class DenoiseOutput extends CommonOutput {
    private String contentType;
    private byte[] resultByteArray;

    public DenoiseOutput(){
        setEngineName("Denoise");
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
