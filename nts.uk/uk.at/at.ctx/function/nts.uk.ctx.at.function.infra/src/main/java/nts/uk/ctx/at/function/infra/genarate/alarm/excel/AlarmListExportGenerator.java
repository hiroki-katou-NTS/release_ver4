package nts.uk.ctx.at.function.infra.genarate.alarm.excel;

import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.arc.layer.infra.file.export.WorkingFile;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.at.function.dom.alarm.AlarmPatternSetting;
import nts.uk.ctx.at.function.dom.alarm.AlarmPatternSettingRepository;
import nts.uk.ctx.at.function.dom.alarm.export.AlarmExportDto;
import nts.uk.ctx.at.function.dom.alarm.export.AlarmListGenerator;
import nts.uk.ctx.at.function.dom.alarm.sendemail.ValueExtractAlarmDto;
import nts.uk.shr.com.company.CompanyAdapter;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportContext;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportGenerator;

@Stateless
public class AlarmListExportGenerator extends AsposeCellsReportGenerator implements AlarmListGenerator {

	private static final String TEMPLATE_FILE = "report/KAL001-アラームリスト(個人別).xlsx";
	private static final String COMPANY_ERROR = "Company is not found!!!!";
	private static final String KAL001_151 = TextResource.localize("KAL001_151");
	private static final String KAL001_152 = TextResource.localize("KAL001_152");
	private static final String KAL001_153 = TextResource.localize("KAL001_153");
	private static final String KAL001_154 = TextResource.localize("KAL001_154");
	private static final String KAL001_155 = TextResource.localize("KAL001_155");
	private static final String KAL001_156 = TextResource.localize("KAL001_156");
	private static final String KAL001_157 = TextResource.localize("KAL001_157");
	private static final String KAL001_158 = TextResource.localize("KAL001_158");
	private static final String KAL001_101 = TextResource.localize("KAL001_101");
	private static final String KAL001_102 = TextResource.localize("KAL001_102");
	
	@Inject
	private CompanyAdapter company;
	
	@Inject
	private AlarmPatternSettingRepository alarmPatternSettingRepo;
	
	@Override
	public AlarmExportDto generate(FileGeneratorContext generatorContext, List<ValueExtractAlarmDto> dataSource,String currentAlarmCode) {

		
		try (AsposeCellsReportContext reportContext = this.createContext(TEMPLATE_FILE)) {
			setHeaderAndHeaderColumn(reportContext,currentAlarmCode);
			// set data source named "item"
			reportContext.setDataSource("item", dataSource);
			// process data binginds in template
			reportContext.processDesigner();

			// save as Excel file
			GeneralDateTime dateNow = GeneralDateTime.now();
			String dateTime = dateNow.toString("yyyyMMddHHmmss");
			String fileName = "AlarmList_" + dateTime + ".xlsx";
			OutputStream outputStream = this.createNewFile(generatorContext, fileName);
			reportContext.saveAsExcel(outputStream);
			WorkingFile workingFile = generatorContext.getWorkingFiles().get(0);
			AlarmExportDto alarmExportDto = new AlarmExportDto(workingFile.getTempFile().getPath(), fileName);
			return alarmExportDto;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void generateExcelScreen(FileGeneratorContext generatorContext, List<ValueExtractAlarmDto> dataSource,String currentAlarmCode) {
		try (AsposeCellsReportContext reportContext = this.createContext(TEMPLATE_FILE)) {
			setHeaderAndHeaderColumn(reportContext,currentAlarmCode);
			// set data source named "item"
			reportContext.setDataSource("item", dataSource);
			// process data binginds in template
			reportContext.processDesigner();
			// save as Excel file
			GeneralDateTime dateNow = GeneralDateTime.now();
			String dateTime = dateNow.toString("yyyyMMddHHmmss");
			String fileName = "AlarmList_" + dateTime + ".xlsx";
			OutputStream outputStream = this.createNewFile(generatorContext, fileName);
			reportContext.saveAsExcel(outputStream);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void setHeaderAndHeaderColumn(AsposeCellsReportContext reportContext,String currentAlarmCode) {
		String codeAlarm = "";
		String nameAlarm = "";
		Optional<AlarmPatternSetting> alarmPatternSetting =  alarmPatternSettingRepo.findByAlarmPatternCode(AppContexts.user().companyId(), currentAlarmCode);
		if(alarmPatternSetting.isPresent()) {
			codeAlarm = alarmPatternSetting.get().getAlarmPatternCD().v();
			nameAlarm = alarmPatternSetting.get().getAlarmPatternName().v();
		}
		String companyName = company.getCurrentCompany().orElseThrow(() -> new RuntimeException(COMPANY_ERROR))
				.getCompanyName();
		reportContext.getWorkbook().getWorksheets().get(0).getPageSetup().setHeader(0, "&9&\"MS ゴシック\"" + companyName);
		reportContext.getWorkbook().getWorksheets().get(0).getPageSetup().setHeader(1, "&16&\"MS ゴシック\"" + KAL001_101);
		DateTimeFormatter fullDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd  H:mm", Locale.JAPAN);
		reportContext.getWorkbook().getWorksheets().get(0).getPageSetup().setHeader(2,
				"&9&\"MS ゴシック\"" + LocalDateTime.now().format(fullDateTimeFormatter) + "\n"+KAL001_102+" &P");
		val cell = reportContext.getWorkbook().getWorksheets().get(0).getCells();
		cell.get(0, 0).setValue(TextResource.localize("KAL001_103",codeAlarm,nameAlarm));
		cell.get(1, 0).setValue(KAL001_151);
		cell.get(1, 1).setValue(KAL001_152);
		cell.get(1, 2).setValue(KAL001_153);
		cell.get(1, 3).setValue(KAL001_154);
		cell.get(1, 4).setValue(KAL001_155);
		cell.get(1, 5).setValue(KAL001_156);
		cell.get(1, 6).setValue(KAL001_157);
		cell.get(1, 7).setValue(KAL001_158);
	}
}