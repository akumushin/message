package server;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import common.beans.message.MessageInfo;
import common.beans.message.MessageInfoPK;
import common.remote.ChatRemote;

public class ServerApp {
	private static Map<String, String> userInfoMap;
	private static Map<String, String> userTokenMap;
	private static Set<MessageInfo> messageSet;
	public static void main(String[] args) throws IOException, AlreadyBoundException {
		userInfoMap = new HashMap<String, String>();
		userTokenMap = new HashMap<String, String>();
		messageSet = new HashSet<MessageInfo>();
		userInfoMap.put("user1", "user1");
		userInfoMap.put("user2", "user2");
		userInfoMap.put("user3", "user3");
		userInfoMap.put("user4", "user4");
		
		Registry rs= LocateRegistry.createRegistry(8888);
		ChatRemote chatRemote = new ChatRemoteImpl();
		rs.rebind("http://localhost/chat", chatRemote);
		System.out.println("already create remote service:");
	}
	public static Map<String, String> getUserInfoMap() {
		return userInfoMap;
	}
	public static Map<String, String> getUserTokenMap() {
		return userTokenMap;
	}
	public static Set<MessageInfo> getMessageSet() {
		return messageSet;
	}

}
