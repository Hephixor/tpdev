package com.indahouse.skylab.calendarupmc.com.indahouse.skylab.calendarupmc.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import biweekly.util.org.apache.commons.codec.binary.Base64;

public class ICSDownloader {

	public ICSDownloader() {}

	public File downloadICS(String urls) {
		URL url;
		File file = null;
		try {


			try {
				url = new URL(urls);
				file = new File("./cal.ics");
				if(!file.exists()) {
					if (file.createNewFile()){
						System.out.println("File created");
						getCalAndWriteFile(url, file,false);
						
					}
					else{
						System.out.println("Could not create file.");
					}
				}
				else {
					System.out.println("File already exists, overwriting");
					getCalAndWriteFile(url, file,true);
					
				}

			} 
			catch (MalformedURLException e) {
				e.printStackTrace();
			}

		} 
		catch (IOException e) {
			e.printStackTrace();
		}

		return file;

	}
	
	public void getCalAndWriteFile(URL url, File file, boolean overwrite) throws IOException {
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		String user_pass = "student.master:guest";
		String encoded_pass = Base64.encodeBase64String(user_pass.getBytes());
		con.setConnectTimeout(30000);
		con.setRequestMethod("GET");
		con.setRequestProperty("Content-Type",  "text/calendar");
		con.setDoOutput(true);		
		con.setRequestProperty("Authorization", "Basic " + encoded_pass);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer content = new StringBuffer();
		while((inputLine = in.readLine()) != null) {
			content.append(inputLine);
			content.append("\r\n");
		}

		in.close();
		con.disconnect();
		BufferedWriter writer = null;
		if(overwrite) {
		 writer = new BufferedWriter(new FileWriter(file.getName(),false));
		 //writer.write("----------------------------OVERWRITING-------------------------\n\r");
		}
		else {
		 writer = new BufferedWriter(new FileWriter(file.getName(),true));	
		 
		}
		writer.write(content.toString());
		writer.flush();
		writer.close();
	}



}
