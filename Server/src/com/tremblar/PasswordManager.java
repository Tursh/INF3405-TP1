package com.tremblar;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser; 
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class PasswordManager{
	private JSONObject passwordFile;
	private JSONParser jsonReader = new JSONParser();
	private String filePath;
	
	// set this to true if a client has been added or removed
	public boolean needsSaving = false;
	
	PasswordManager(String filePath) throws FileNotFoundException, IOException{
		// if the JSON file does exist, read it
		this.filePath = filePath;
		try{
			passwordFile = (JSONObject)jsonReader.parse(new FileReader(filePath));
		}catch(ParseException e){
			System.out.println(e);
			passwordFile = new JSONObject();
			needsSaving = true;
		}
	}
	
	boolean clientOk(String clientName, String password){
		if(!passwordFile.containsKey(clientName)) {
			return false;
		}
		try{
			String registeredPassword = (String)passwordFile.get(clientName);
			return registeredPassword.equals(password);
		}catch(Exception e){
			// get throws an exception if client is not found
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	boolean insertClient(String clientName, String password){
		
		if(!passwordFile.containsKey(clientName)) {
			passwordFile.put(clientName, password);
			saveFile();
			return true;
		}
			return false;
		
	}
	
	void removeClient(String clientName){
		if(passwordFile.containsKey(clientName))
		{
			passwordFile.remove(clientName);
			saveFile();
		}
	}
	
	void saveFile() {
        try {
    		FileWriter file = new FileWriter(filePath);
			file.write(passwordFile.toJSONString());
	        file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}