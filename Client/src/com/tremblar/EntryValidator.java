package com.tremblar;

public class EntryValidator {
	static boolean verifyIpAddress(String address) {
		String[] addressBytes = address.split("\\.");
		if (addressBytes.length != 4) {
			System.out.println("Server IP must have 4 bytes");
			return false;
		}
		
		boolean isValidAddress = true;
		for (int i = 0; i < addressBytes.length; i++) {
			try {
				isValidAddress &= Integer.parseInt(addressBytes[i]) >= 0;
				isValidAddress &= Integer.parseInt(addressBytes[i]) <= 255;
			} catch (NumberFormatException e) {
				System.out.println("Not all entries were numbers - " + e);
				isValidAddress = false;
			}
			if (!isValidAddress) {
				System.out.println("Server IP can only contain numbers between 0 and 255");
				break;
			}
		}
		return isValidAddress;
	}
	
	static boolean verifyPort(int port) {
		final int PORT_MIN = 5000;
		final int PORT_MAX = 5050;
		
		if (port < PORT_MIN || port > PORT_MAX) {
			System.out.println("Server port must be between 5000 and 5050");
			return false;
		}
		
		return true;
	}
}
