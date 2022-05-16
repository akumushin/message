package client;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import common.beans.auth.AuthenticateInfo;
import common.remote.ChatRemote;
import common.remote.ChatResult;

public class ClientApp {
	public static void main(String[] args) throws IOException, NotBoundException, InterruptedException {
		ChatClient client = new ChatClient();
		client.run();
		
		
	}
		

}
