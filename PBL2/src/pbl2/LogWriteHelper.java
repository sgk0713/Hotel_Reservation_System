package pbl2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class LogWriteHelper {
	private String fileName = "";
	private File file;
	public LogWriteHelper(String fileName) {
		this.setFileName(fileName);
		this.file = new File(fileName);
	}
	
	public boolean append(String txt) {
		try {
			FileWriter fw = new FileWriter(file, true);
			fw.write(txt+"\r\n");
			fw.close();
			return true;
		} catch (IOException e) {
			System.out.println("FAILED WRITEING LOG... (X) ");
			return false;
		}
	}
	
	public boolean overWrite(String txt) {
		try {
			FileWriter fw = new FileWriter(file);
			fw.write(txt+"\r\n");
			fw.close();
			return true;
		} catch (IOException e) {
			System.out.println("FAILED WRITEING LOG... (X) ");
			return false;
		}
	}
	
	
	public String getFileName() {
		return fileName;
	}
	private void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
	
}
