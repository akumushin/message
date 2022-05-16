package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import common.beans.auth.AuthenticateInfo;
import common.beans.message.MessageInfo;
import common.beans.message.MessageInfoPK;
import common.remote.ChatRemote;
import common.remote.ChatResult;

public class ChatRemoteImpl extends UnicastRemoteObject implements  ChatRemote {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected ChatRemoteImpl() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public ChatResult<AuthenticateInfo> login(String username, String password) throws RemoteException {
		ChatResult<AuthenticateInfo> chatResult = new ChatResult<AuthenticateInfo>();
		String passwordConf = ServerApp.getUserInfoMap().get(username);
		if(passwordConf == null) {
			chatResult.setDetailCode("100001");// khong ton tai
		}else if( !passwordConf.equals(password)) {
			chatResult.setDetailCode("100002");// mat khau sai
		}else {
			AuthenticateInfo authenticateInfo = new AuthenticateInfo();
			authenticateInfo.setUsername(username);
			authenticateInfo.setToken(UUID.randomUUID().toString());
			ServerApp.getUserTokenMap().put(username, authenticateInfo.getToken());
			chatResult.setDetailCode("000000");
			chatResult.setResult(authenticateInfo);
		} 
		return chatResult;
	}

	@Override
	public ChatResult<String> logout(AuthenticateInfo auth) throws RemoteException {
		ChatResult<String> chatResult = new ChatResult<String>();
		if(checkAuthenticate(auth, chatResult)) {
			ServerApp.getUserTokenMap().remove(auth.getUsername());
		}else {
			chatResult.setResult("NG");
		}
		return chatResult;
	}

	@Override
	public ChatResult<MessageInfo> sendMessage(AuthenticateInfo auth, String to, String message)
			throws RemoteException {
		ChatResult<MessageInfo> chatResult = new ChatResult<MessageInfo>();
		if(! checkAuthenticate(auth, chatResult)) {
			return chatResult;
		}
		if(!ServerApp.getUserInfoMap().containsKey(to)) {
			chatResult.setDetailCode("200001");
			return chatResult;
		}
		
		MessageInfoPK pk = new MessageInfoPK();
		pk.setFromId(auth.getUsername());
		pk.setToId(to);
		pk.setTimeId(System.currentTimeMillis());
		
		MessageInfo messageInfo = new MessageInfo();
		messageInfo.setId(pk);
		messageInfo.setMessage(message);
		messageInfo.setCreateAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(pk.getTimeId()), 
                TimeZone.getDefault().toZoneId()));
		ServerApp.getMessageSet().add(messageInfo);
		chatResult.setResult(messageInfo);
		return chatResult;
	}

	@Override
	public ChatResult<List<MessageInfo>> receiveMessage(final AuthenticateInfo auth,final String from,final long lastTimeId)
			throws RemoteException {
		ChatResult<List<MessageInfo>> chatResult = new ChatResult<List<MessageInfo>>();
		if(! checkAuthenticate(auth, chatResult)) {
			return chatResult;
		}
		if(!ServerApp.getUserInfoMap().containsKey(from)) {
			chatResult.setDetailCode("200001");
			return chatResult;
		}
		
		Stream<MessageInfo> stream = ServerApp.getMessageSet().stream().filter(mess->{
			return mess.getId().getFromId().equals(from) 
					&& mess.getId().getToId().equals(auth.getUsername())
					&& mess.getId().getTimeId() > lastTimeId
					|| mess.getId().getFromId().equals(auth.getUsername()) 
					&& mess.getId().getToId().equals(from)
					&& mess.getId().getTimeId() > lastTimeId;
		});
		
		chatResult.setResult(stream.collect(Collectors.toList()));
		return chatResult;
	}



	@Override
	public ChatResult<List<MessageInfo>> receiveMessageLast(AuthenticateInfo auth, String from, int pageNo,
			int pageSize) throws RemoteException {
		ChatResult<List<MessageInfo>> chatResult = new ChatResult<List<MessageInfo>>();
		if(! checkAuthenticate(auth, chatResult)) {
			return chatResult;
		}
		if(!ServerApp.getUserInfoMap().containsKey(from)) {
			chatResult.setDetailCode("200001");
			return chatResult;
		}
		
		Stream<MessageInfo> stream = ServerApp.getMessageSet().stream().filter(mess->{
			return mess.getId().getFromId().equals(from) 
					&& mess.getId().getToId().equals(auth.getUsername())
					|| mess.getId().getFromId().equals(auth.getUsername()) 
					&& mess.getId().getToId().equals(from);
			})	.sorted((m1, m2)->m2.getId().compareTo(m1.getId()))
				.limit(pageSize)
				.sorted((m1, m2)->m1.getId().compareTo(m2.getId()));
		
		chatResult.setResult(stream.collect(Collectors.toList()));
		return chatResult;
	}
	
	private boolean checkAuthenticate(AuthenticateInfo auth, ChatResult<?> chatResult) {
		String tokenConf = ServerApp.getUserTokenMap().get(auth.getUsername());
		if(tokenConf == null) {
			chatResult.setDetailCode("100001");// khong ton tai
			return false;
		}else if( !tokenConf.equals(auth.getToken())) {
			chatResult.setDetailCode("100003");// token  sai
			return false;
		}else {
			chatResult.setDetailCode("000000");
			return true;
		} 
	}
}
