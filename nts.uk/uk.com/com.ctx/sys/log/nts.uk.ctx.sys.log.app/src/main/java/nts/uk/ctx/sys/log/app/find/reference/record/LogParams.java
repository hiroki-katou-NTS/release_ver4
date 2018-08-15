package nts.uk.ctx.sys.log.app.find.reference.record;

import java.util.List;
import java.util.Map;

import lombok.Value;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.sys.log.app.find.reference.LogOutputItemDto;
import nts.uk.ctx.sys.log.app.find.reference.LogSetItemDetailDto;


@Value
public class LogParams {

	private List<String> listTagetEmployeeId;
	private List<String> listOperatorEmployeeId;
	private GeneralDate startDateTaget;
	private GeneralDate endDateTaget;
	private GeneralDate startDateOperator;
	private GeneralDate endDateOperator;
	private int recordType; //0: logLogin; 1: Log startup; 3 : Log data update persion ; 6: log data correct
	private List<LogOutputItemDto> lstHeaderDto;
	private List<LogOutputItemDto> lstSupHeaderDto;
	private List<LogBasicInfoDto> lstLogBasicInfoDto;
	private List<LogBasicInfoAllDto> listLogBasicInfoAllDto;
	private List<LogSetItemDetailDto> listLogSetItemDetailDto;
	private List<Map<String, Object>> listDataExport;
	private int targetDataType;
}
