package nts.uk.ctx.sys.log.app.find.reference.record;

import java.util.List;

import lombok.Value;
import nts.uk.ctx.sys.log.app.find.reference.LogOutputItemDto;

@Value
public class LogScreenIParam {
	
	private LogParams logParams;
	
	private ParamOutputItem paramOutputItem;

	private List<LogOutputItemDto> lstHeaderDto;
	
	private List<LogOutputItemDto> lstSubHeaderDto;
}
