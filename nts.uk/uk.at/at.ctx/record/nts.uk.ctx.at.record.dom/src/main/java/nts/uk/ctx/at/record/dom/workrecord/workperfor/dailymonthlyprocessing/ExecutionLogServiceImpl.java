package nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing;

import java.util.List;

import javax.ejb.Stateless;

import nts.arc.enums.EnumAdaptor;
import nts.arc.enums.EnumConstant;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionContent;


/**
 * 
 * @author hieult
 *
 */
@Stateless
public class ExecutionLogServiceImpl implements ExecutionLogService {

	public List<EnumConstant> getEnumContent(String companyID) {
		
		return EnumAdaptor.convertToValueNameList(ExecutionContent.class);
	}

}
