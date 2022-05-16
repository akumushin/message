package common.remote;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import common.beans.auth.AuthenticateInfo;
import common.beans.message.MessageInfo;
import common.beans.message.MessageInfoPK;

public interface ChatRemote extends Remote, Serializable {
	/**
	 * 
	 * @param username
	 * @param password
	 * @return login token
	 */
	ChatResult<AuthenticateInfo> login(String username, String password) throws RemoteException;
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @return login token
	 */
	ChatResult<String> logout(AuthenticateInfo auth) throws RemoteException;
	
	ChatResult<MessageInfo> sendMessage(AuthenticateInfo auth, String to, String message) 
			throws RemoteException;
	
	ChatResult<List<MessageInfo>> receiveMessage(AuthenticateInfo auth, String fromId, long timeIdGreaterThan) 
			throws RemoteException;
	ChatResult<List<MessageInfo>> receiveMessageLast(AuthenticateInfo auth, String fromId, int pageNo, int pageSize) 
			throws RemoteException;
}
