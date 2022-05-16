package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

import common.beans.auth.AuthenticateInfo;
import common.beans.message.MessageInfo;
import common.remote.ChatRemote;
import common.remote.ChatResult;
import common.utils.MessageUtil;

public class ChatClient {
	private ChatRemote chatRemote;
	private AuthenticateInfo auth;
	private Scanner sc = new Scanner(System.in);
	public ChatClient() {
		try {
			Registry registry = LocateRegistry.getRegistry(8888);
			chatRemote= (ChatRemote) registry.lookup("http://localhost/chat");
		}catch (Exception e) {
			//error
			throw new RuntimeException("cant access to server");
		}
	}
	public void run() {
		boolean loop = false;
		String control = "";
		//control login
		do {
			this.println("// Control //");
			this.println("1:Login\t2.Exit");
			control = this.readLine();
			switch(control) {
			case "1":
				//đăng nhập
				loop = !login();
				break;
			case "2":
				//thoát
				return;
			default:
				this.println("Input error. Try");
				loop= true;
			}
		}while (loop);
		//chat control
		do {
			this.println("// Control //");
			this.println("1:Chat\t2.Logout");
			control = this.readLine();
			switch(control) {
			case "1":
				//đăng nhập
				loop = chat();
				break;
			case "2":
				//Đăng xuất
				logout();
				return;
			default:
				this.println("Input error. Try");
				loop= true;
			}
		}while (loop);
	}
	public boolean login() {
		try {
			
			// login
			this.println("---login---");
			this.printf("username:");
			String username = this.readLine();
			this.printf("password:");
			String password = this.readLine();
			ChatResult<AuthenticateInfo> chatResult = chatRemote.login(username, password);
			if(MessageUtil.isError(chatResult.getDetailCode())) {
				this.println(MessageUtil.getMessage(chatResult.getDetailCode()));
				return false;
			}
			this.auth = chatResult.getResult();
			return true;
		}catch(Exception e) {
			this.println("system error");
			return false;
		}
		
	}
	public boolean logout() {
		try {
			ChatResult<String> chatResult =this.chatRemote.logout(auth);
			if(MessageUtil.isError(chatResult.getDetailCode())) {
				this.println(MessageUtil.getMessage(chatResult.getDetailCode()));
				return false;
			}
			return true;
		} catch (RemoteException e) {
			//
			this.println("system error");
			return false;
		}
	}
	public boolean chat() {
		//get message initial
		ChatResult<MessageInfo> chatResultSingle = null;
		ChatResult<List<MessageInfo>> chatResultList = null;
		try {
			this.printf("Your friend:");
			String frient = this.readLine();
			chatResultList = this.chatRemote.receiveMessageLast(auth, frient, 1, 20);
			if(MessageUtil.isError(chatResultList.getDetailCode())) {
				this.println(MessageUtil.getMessage(chatResultList.getDetailCode()));
				return true;
			}
			chatResultList.getResult().forEach(mess->{
				this.printf("%s>>%s\r\n", mess.getId().getFromId(), mess.getMessage());
			});
			do {
				// nhập tin nhắn
				this.printf(auth.getUsername()+">>");
				String message = this.readLine();
				if(message.equals("EXIT")) {
					return true;
				}
				chatResultSingle = chatRemote.sendMessage(auth, frient, message);
				// nếu xảy ra lỗi thì kết thúc
				if(MessageUtil.isError(chatResultSingle.getDetailCode())) {
					this.println(MessageUtil.getMessage(chatResultSingle.getDetailCode()));
					return false;
				}
				long lastTimeId = chatResultList.getResult().get(chatResultList.getResult().size()-1).getId().getTimeId();
				chatResultList = chatRemote.receiveMessage(auth, frient, lastTimeId);
				chatResultList.getResult().forEach(mess->{
					if(!auth.getUsername().equals(mess.getId().getFromId())) {
						this.printf("%s>>%s\r\n", mess.getId().getFromId(), mess.getMessage());
					}
				});
			}while(true);
		} catch (Exception e) {
			this.println("system error");
			return false;
		}
		
	}
	private void printf(String format, Object...args) {
		System.out.printf(format, args);
	}
	private void println(Object obj) {
		System.out.println(obj);
	}
	private String readLine() {
		sc = new Scanner(System.in);
		String val = sc.nextLine();
		return val;
	}
}
