package nts.uk.shr.com.program;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author manhnd
 *
 *	Program
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Program {
	
	/**
	 * Web app Id
	 */
	private WebAppId appId;
	
	/**
	 * Program Id
	 */
	private String pId;
	
	/**
	 * Program name
	 */
	private String pName;
	
	/**
	 * Program path
	 */
	private String pPath;

	@Override
	public int hashCode() {
		int result = 0;
		result += ((pId == null) ? 0 : pId.hashCode());
		result += ((appId == null) ? 0 : appId.name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Program other = (Program) obj;
		if (this.appId != other.appId || !this.pId.equals(other.getPId())) return false;
		return true;
	}
}
