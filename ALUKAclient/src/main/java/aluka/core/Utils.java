package aluka.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public interface Utils {
	
	public static final short TAMPON = 1024;
	
	public static String readFileContent(String path) throws IOException {

		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(path)))) {

			int l;
			byte[] readBuff = new byte[TAMPON];
			String content = "";
			while ((l = bis.read(readBuff)) > 0)
				content += new String(readBuff, 0, l);
			return content;

		} catch (IOException e) {
			throw e;
		}
	}

	public static void writeFileContent(String path, String content) throws IOException {
		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(path)))) {
			bos.write(content.getBytes());
		} catch (IOException e) {
			throw e;
		}
	}

	public static void pack(String sourceDirPath, String zipFilePath) throws IOException {
		Path p = Files.createFile(Paths.get(zipFilePath));
		try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
			Path pp = Paths.get(sourceDirPath);
			Files.walk(pp).filter(path -> !Files.isDirectory(path)).forEach(path -> {
				ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
				try {
					zs.putNextEntry(zipEntry);
					Files.copy(path, zs);
					zs.closeEntry();
				} catch (IOException e) {
					System.err.println(e);
				}
			});
		}
	}
}