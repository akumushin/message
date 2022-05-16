package common.beans.message;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class MessageInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private MessageInfoPK id;
	
	private String message;
	
	private LocalDateTime createAt;

	public MessageInfoPK getId() {
		return id;
	}

	public void setId(MessageInfoPK id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getCreateAt() {
		return createAt;
	}

	public void setCreateAt(LocalDateTime createAt) {
		this.createAt = createAt;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MessageInfo other = (MessageInfo) obj;
		return Objects.equals(id, other.id);
	}
	
	

}
