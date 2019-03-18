package nts.uk.ctx.at.function.app.export.alarm;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.function.dom.alarm.sendemail.ValueExtractAlarmDto;

/**
 * 
 * @author thuongtv
 *
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmExportQuery {
	
	/** Data export */
	private List<ValueExtractAlarmDto> data;
	
	private String currentAlarmCode;
}
