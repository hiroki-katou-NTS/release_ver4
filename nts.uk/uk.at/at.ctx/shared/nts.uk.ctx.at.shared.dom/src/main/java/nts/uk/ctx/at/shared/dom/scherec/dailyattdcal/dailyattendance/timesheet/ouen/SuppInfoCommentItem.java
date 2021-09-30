package nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.timesheet.ouen;

import lombok.Getter;
import nts.arc.layer.dom.objecttype.DomainObject;

/**
 * ValueObject 補足情報のコメント項目
 * @author tutt
 *
 */
@Getter
public class SuppInfoCommentItem implements DomainObject {
	
	/** 補足情報NO: 作業補足情報NO	*/
	private SuppInfoNo suppInfoNo;
	
	/** 補足コメント: 作業補足コメント */			
	private WorkSuppComment workSuppComment;
}
