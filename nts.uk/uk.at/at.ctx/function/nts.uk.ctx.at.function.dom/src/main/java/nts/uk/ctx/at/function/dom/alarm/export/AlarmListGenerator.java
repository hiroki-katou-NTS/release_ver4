package nts.uk.ctx.at.function.dom.alarm.export;

import java.util.List;

import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.uk.ctx.at.function.dom.alarm.sendemail.ValueExtractAlarmDto;
/**
 * 
 * @author thuongtv
 *
 */

public interface AlarmListGenerator {

	/**
	 * Generate excel for send mail
	 *
	 * @param fileContext the file context
	 * @param exportData the export data
	 */
	AlarmExportDto generate(FileGeneratorContext generatorContext, List<ValueExtractAlarmDto> dataSource,String currentAlarmCode);
	
	/**
	 * Generate excel for screen
	 *
	 * @param fileContext the file context
	 * @param exportData the export data
	 * @return 
	 */
	 void generateExcelScreen(FileGeneratorContext generatorContext, List<ValueExtractAlarmDto> dataSource,String currentAlarmCode);
}
