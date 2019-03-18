package nts.uk.ctx.at.function.app.export.alarm;

import java.util.List;

import lombok.Value;
import nts.uk.ctx.at.function.dom.alarm.sendemail.ValueExtractAlarmDto;

/**
 * 
 * @author thuongtv
 *
 */

@Value
public class AlarmExportQuery {
	
	/** Data export */
	private List<ValueExtractAlarmDto> data;
	
	private String currentAlarmCode;
}
