package com.is3106.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.springframework.web.multipart.MultipartFile;

import com.is3106.exception.FileServerException;

public class FileHelper {



	public static String appendPath(String gFileName) {
		return "/" + gFileName;
	}
	
	public static String filePathTofileName(String gFilePath) {
		String[] splitter =  gFilePath.split("/");
		return splitter[splitter.length-1];
	}

	public static File convertMultiPartToFile(MultipartFile file) throws FileServerException {
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(convFile);
			fos.write(file.getBytes());
			fos.close();
			return convFile;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new FileServerException(e.getMessage());
		}
	}
	
	public static byte[] recoverImageFromUrl(String urlText) throws Exception {
		URL url = new URL(urlText);
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		try (InputStream inputStream = url.openStream()) {
			int n = 0;
			byte[] buffer = new byte[1024];
			while (-1 != (n = inputStream.read(buffer))) {
				output.write(buffer, 0, n);
			}
		}
		return output.toByteArray();
	}

}
