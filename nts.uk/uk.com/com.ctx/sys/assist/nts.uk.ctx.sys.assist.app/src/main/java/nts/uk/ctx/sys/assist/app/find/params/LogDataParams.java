package nts.uk.ctx.sys.assist.app.find.params;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.sys.assist.app.find.logdataresult.Condition;


@Data
@Builder
public class LogDataParams {
	
	private int systemType;
	
	private int recordType;
	
	private GeneralDateTime startDateOperator;
	
	private GeneralDateTime endDateOperator;
	
	private List<String>  listOperatorEmployeeId;
	
	private List<Condition> listCondition;
}
