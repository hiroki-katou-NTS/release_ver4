package nts.uk.ctx.at.shared.app.command.specialholiday.grantrelationship;

import lombok.Getter;
import lombok.Setter;
/**
 * insert grant relationship command
 * 
 * @author yennth
 *
 */
@Getter
@Setter
public class InsertGrantRelationshipCommand {
	/**コード**/
	private int specialHolidayCode;
	/**コード**/
	private String relationshipCode;
	/** 付与日数 **/
	private int grantRelationshipDay;
	/** 喪主時加算日数 **/
	private Integer morningHour;
}
