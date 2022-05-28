package aluka.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import aluka.core.ServerManager;

public class DownloadTor implements Runnable{
	
	private String os;
	
	public DownloadTor(String os) {
		this.os = os;
	}

	@Override
	public void run() {
		System.out.println("[ + ] - Installing tor browser JOB STARTED");
		
		ServerManager server = ServerManager.getInstance();
		File file = server.readFile();
		unzip(file,new File("tor"));
		
		if(os.equals("windows")) {
			
		}else if(os.equals("macos")) {
			
		}else {
			
		}
		
	}
	
	private static void unzip(File zipFile, File dir) {
        // create output directory if it doesn't exist
        if(!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFile);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while(ze != null){
                String fileName = ze.getName();
                File newFile = new File(dir.getAbsolutePath() + File.separator + fileName);
                System.out.println("Unzipping to "+newFile.getAbsolutePath());
                //create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
                }
                fos.close();
                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
	
}
