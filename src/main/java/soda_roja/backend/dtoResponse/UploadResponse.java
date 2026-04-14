package soda_roja.backend.dtoResponse;

public class UploadResponse {
	 private String filePath;
	    private String message;

	    public UploadResponse(String filePath, String message) {
	        this.filePath = filePath;
	        this.message = message;
	    }

	    public String getFilePath() {
	        return filePath;
	    }

	    public String getMessage() {
	        return message;
	    }

}
