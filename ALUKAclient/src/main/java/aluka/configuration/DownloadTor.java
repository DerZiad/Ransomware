package aluka.configuration;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import aluka.core.ServerManager;

public class DownloadTor implements Runnable {

	private String os;

	public DownloadTor(String os) {
		this.os = os;
	}

	@Override
	public void run() {
		System.out.println("[ + ] - Installing tor browser JOB STARTED");

		// Connection with principal Server
		Short port = null;
		while (true) {
			
			Socket socket = null;
			DataInputStream dis = null;
			
			try {
				System.out.println("[+] - Connecting with principal server");
				socket = new Socket(InetAddress.getByName("localhost"), 8080);
				dis = new DataInputStream(socket.getInputStream());
				port = dis.readShort();
				socket.close();
				break;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// Connecting to slave
		while (true) {
			Socket socket = null;
			try {
				socket = new Socket(InetAddress.getByName("localhost"), port);
				PrintWriter pw = new PrintWriter(socket.getOutputStream());
				pw.println(os);
				pw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					socket.close();
					break;
				} catch (IOException e) {
				}
			}
		}

	}

	private static void unzip(File zipFile, File dir) {
		// create output directory if it doesn't exist
		if (!dir.exists())
			dir.mkdirs();
		FileInputStream fis;
		// buffer for read and write data to file
		byte[] buffer = new byte[1024];
		try {
			fis = new FileInputStream(zipFile);
			ZipInputStream zis = new ZipInputStream(fis);
			ZipEntry ze = zis.getNextEntry();
			while (ze != null) {
				String fileName = ze.getName();
				File newFile = new File(dir.getAbsolutePath() + File.separator + fileName);
				System.out.println("Unzipping to " + newFile.getAbsolutePath());
				// create directories for sub directories in zip
				new File(newFile.getParent()).mkdirs();
				FileOutputStream fos = new FileOutputStream(newFile);
				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
				// close this ZipEntry
				zis.closeEntry();
				ze = zis.getNextEntry();
			}
			// close last ZipEntry
			zis.closeEntry();
			zis.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
