package gcov.beans;

public class GcovFile {
	private String fileName;
	private String sourcePath;
	private String gcnoPath;
	private String gcnaPath;

	public GcovFile(String fileName, String sourcePath, String gcnoPath, String gcnaPath) {
		this.fileName = fileName;
		this.sourcePath = sourcePath;
		this.gcnoPath = gcnoPath;
		this.gcnaPath = gcnaPath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getSourcePath() {
		return sourcePath;
	}

	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}

	public String getGcnoPath() {
		return gcnoPath;
	}

	public void setGcnoPath(String gcnoPath) {
		this.gcnoPath = gcnoPath;
	}

	public String getGcnaPath() {
		return gcnaPath;
	}

	public void setGcnaPath(String gcnaPath) {
		this.gcnaPath = gcnaPath;
	}

}
