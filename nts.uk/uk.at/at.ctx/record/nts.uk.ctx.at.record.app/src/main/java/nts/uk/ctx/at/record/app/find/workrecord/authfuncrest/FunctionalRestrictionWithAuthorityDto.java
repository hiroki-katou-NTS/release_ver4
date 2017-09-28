/**
 * 
 */
package nts.uk.ctx.at.record.app.find.workrecord.authfuncrest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author danpv
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FunctionalRestrictionWithAuthorityDto {

	private String roleID;

	/**
	 * 日別実績の機能NO
	 */
	private int functionNo;

	/**
	 * 日別実績の機能名
	 */
	private String displayName;

	/**
	 * 利用区分
	 */
	private boolean availability;

	/**
	 * 日別実績の機能説明文
	 */
	private String description;

	public FunctionalRestrictionWithAuthorityDto(String roleId, int functionNo, String displayname,
			String description) {
		this.roleID = roleId;
		this.functionNo = functionNo;
		this.displayName = displayname;
		this.description = description;
	}
}
