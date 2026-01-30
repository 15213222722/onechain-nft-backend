package io.xone.chain.onenft;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class s {
	
	public static void main(String[] args) {
		loadImage("https://statics.newxone.com/final/3bg.png");

	}

	public static byte[] loadImage(String url) {
		try {
			URL imageUrl = new URL(url);
			URLConnection connection = imageUrl.openConnection();
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			connection.setRequestProperty("User-Agent", "Java/1.8.0_401");			
		 
			System.out.println("--- Request Headers ---");
			Map<String, List<String>> requestProperties = connection.getRequestProperties();
			for (Map.Entry<String, List<String>> entry : requestProperties.entrySet()) {
				System.out.println("Key: " + entry.getKey() + " ,Value: " + entry.getValue());
			}

			System.out.println("--- Response Headers ---");
			java.util.Map<String, java.util.List<String>> headerFields = connection.getHeaderFields();
			System.out.println(headerFields);
			for (java.util.Map.Entry<String, java.util.List<String>> entry : headerFields.entrySet()) {
				System.out.println("Key: " + entry.getKey() + " ,Value: " + entry.getValue());
			}
//			System.out.println("Content-Type: " + connection.getContentType());
//			headerFields.get("")
//			headerFields.put("User-Agent", "");
			try (InputStream inputStream = connection.getInputStream()) {
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				byte[] buffer = new byte[4096];
				int bytesRead;
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
				}
				byte[] ret = outputStream.toByteArray();
				return ret;
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to load image from URL: " + url, e);
		}
	}

}