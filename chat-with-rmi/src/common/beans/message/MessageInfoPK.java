package common.beans.message;

import java.io.Serializable;
import java.util.Objects;

public class MessageInfoPK implements Serializable, Comparable<MessageInfoPK>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String fromId;
	private String toId;
	private long timeId;
	
	public long getTimeId() {
		return timeId;
	}
	public void setTimeId(long timeId) {
		this.timeId = timeId;
	}
	public String getFromId() {
		return fromId;
	}
	public void setFromId(String fromId) {
		this.fromId = fromId;
	}
	public String getToId() {
		return toId;
	}
	public void setToId(String toId) {
		this.toId = toId;
	}
	@Override
	public int hashCode() {
		return Objects.hash(fromId,toId );
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MessageInfoPK other = (MessageInfoPK) obj;
		return Objects.equals(fromId, other.fromId) && timeId == other.timeId && Objects.equals(toId, other.toId);
	}
	
	@Override
	public int compareTo(MessageInfoPK o) {
		int compare =0;
		if((compare=this.fromId.compareTo(o.fromId))!=0) {
			return compare;
		}
		if((compare=this.toId.compareTo(o.toId))!=0) {
			return compare;
		}
		if(this.timeId > o.getTimeId()) {
			return 1;
		}else if(this.timeId < o.getTimeId()) {
			return -1;
		}else {
			return 0;
		}
	}
	
}
