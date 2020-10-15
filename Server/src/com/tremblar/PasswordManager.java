package com.tremblar;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser; 
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class PasswordManager{
	private static JSONObject passwordFile;
	private static JSONParser jsonReader = new JSONParser();
	// set this to true if a client has been added or removed
	public boolean needsSaving = false;
	PasswordManager(String filepath) throws FileNotFoundException, IOException{
		// if the JSON file does exist, read it
		try{
			passwordFile = (JSONObject)jsonReader.parse(new FileReader(filepath));
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
			needsSaving = true;
			return true;
		}else {
			return false;
		}
		
	}
	
	void removeClient(String clientName){
		if(passwordFile.containsKey(clientName))
			passwordFile.remove(clientName);
		needsSaving = true;
	}
	
	void saveFile(String filepath) throws IOException{
		FileWriter file = new FileWriter(filepath);
        file.write(passwordFile.toJSONString());
        file.close();
	}
}