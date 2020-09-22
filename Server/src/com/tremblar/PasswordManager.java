package com.tremblar;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser; 
import org.json.simple.parser.ParseException;

import java.io.FileWriter;
import java.io.IOException;


public class PasswordManager{
	private static JSONObject passwordFile;
	private static JSONParser jsonReader = new JSONParser();
	// set this to true if a client has been added or removed
	public boolean needsSaving = false;
	PasswordManager(String filepath){
		// if the JSON file does exist, read it
		try{
			passwordFile = (JSONObject)jsonReader.parse(filepath);
		}catch(ParseException e){
			// if it does not exist: create it
			passwordFile = new JSONObject();
			needsSaving = true;
		}
	}
	
	boolean clientOk(String clientName, String password){
		if(!passwordFile.containsKey(clientName))
			return false;
		try{
			String registeredPassword = (String)passwordFile.get(clientName);
			if(registeredPassword != password){
				return false;
			}
			return true;
		}catch(Exception e){
			// get throws an exception if client is not found
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	void insertClient(String clientName, String password){
		if(!passwordFile.containsKey(clientName))
			passwordFile.put(clientName, password);
		needsSaving = true;
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