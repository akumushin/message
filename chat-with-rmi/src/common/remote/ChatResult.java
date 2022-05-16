package common.remote;

import java.io.Serializable;

public class ChatResult<R> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String detailCode;
	private R result;
	public String getDetailCode() {
		return detailCode;
	}
	public void setDetailCode(String detailCode) {
		this.detailCode = detailCode;
	}
	public R getResult() {
		return result;
	}
	public void setResult(R result) {
		this.result = result;
	}
	
}
