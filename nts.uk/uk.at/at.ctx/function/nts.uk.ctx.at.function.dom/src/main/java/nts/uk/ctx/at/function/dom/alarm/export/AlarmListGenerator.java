package nts.uk.ctx.at.function.dom.alarm.export;

import java.io.InputStream;
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
	 * Generate.
	 *
	 * @param fileContext the file context
	 * @param exportData the export data
	 */
	InputStream generate(FileGeneratorContext generatorContext, List<ValueExtractAlarmDto> dataSource);
}
