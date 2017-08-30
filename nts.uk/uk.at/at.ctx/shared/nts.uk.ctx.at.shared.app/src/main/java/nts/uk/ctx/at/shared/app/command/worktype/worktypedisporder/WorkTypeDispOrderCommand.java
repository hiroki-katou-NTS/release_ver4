package nts.uk.ctx.at.shared.app.command.worktype.worktypedisporder;

import lombok.Value;
import nts.uk.ctx.at.shared.dom.worktype.worktypedisporder.WorkTypeDispOrder;
import nts.uk.shr.com.context.AppContexts;

/**
 * 
 * @author TanLV
 *
 */
@Value
public class WorkTypeDispOrderCommand {
	/** The company id. */
	/*会社ID*/
	private String companyId;
	
	/** The work type code. */
	/*勤務種類コード*/
	private String workTypeCode;
	
	/*並び順*/
	private int disporder;
	
	/**
	 * Convert to domain object
	 * @return
	 */
	public WorkTypeDispOrder toDomain() {
		String companyId = AppContexts.user().companyId();
				
		return  WorkTypeDispOrder.createFromJavaType(companyId, workTypeCode, disporder);
	}
}
