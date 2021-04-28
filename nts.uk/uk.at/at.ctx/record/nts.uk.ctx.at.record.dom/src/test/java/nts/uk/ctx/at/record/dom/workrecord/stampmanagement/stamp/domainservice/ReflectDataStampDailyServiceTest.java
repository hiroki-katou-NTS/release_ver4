package nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.domainservice;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Injectable;
import mockit.integration.junit4.JMockit;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.ExecutionTypeDaily;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.createdailyoneday.EmbossingExecutionFlag;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.createdailyresults.OutputCreateDailyOneDay;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.Stamp;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.StampHelper;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.domainservice.ReflectDataStampDailyService.Require;
import nts.uk.ctx.at.shared.dom.adapter.generalinfo.dtoimport.EmployeeGeneralInfoImport;
import nts.uk.ctx.at.shared.dom.dailyperformanceprocessing.output.PeriodInMasterList;
import nts.uk.ctx.at.shared.dom.scherec.appreflectprocess.appreflectcondition.reflectprocess.ScheduleRecordClassifi;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.breakouting.breaking.BreakTimeOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.dailyattendancework.IntegrationOfDaily;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.function.algorithm.ChangeDailyAttendance;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.workinfomation.algorithmdailyper.OutputTimeReflectForWorkinfo;
import nts.uk.ctx.at.shared.dom.workrecord.workperfor.dailymonthlyprocessing.ErrorMessageInfo;
/**
 * 
 * @author tutk
 *
 */

@RunWith(JMockit.class)
public class ReflectDataStampDailyServiceTest {
	
	String employeeId = "employeeId";
	String cid = "cid";
	GeneralDate date = GeneralDate.today();
	List<ErrorMessageInfo> errorMessageInfos = new ArrayList<>();
	ReflectDataStampDailyServiceHelper helper = new ReflectDataStampDailyServiceHelper();
	
	ChangeDailyAttendance changeDailyAtt = new  ChangeDailyAttendance(true,
			true, 
			true,
			true,
			ScheduleRecordClassifi.RECORD);
	
	OutputTimeReflectForWorkinfo info = new OutputTimeReflectForWorkinfo();

	@Injectable
	private Require require;
	
	
	// Test all date is error
	@Test
	public void test_all_date_is_error() {
		
	
		Stamp stamp = StampHelper.getStampDefault();
		
		OutputCreateDailyOneDay resultData = helper.getErrorsNotNull(stamp);
		
		new Expectations() {
			{
				require.createDailyResult(cid,
						employeeId,
						(GeneralDate)any,
						ExecutionTypeDaily.CREATE,
						EmbossingExecutionFlag.ALL,
						(EmployeeGeneralInfoImport)any,
						(PeriodInMasterList)any,
						new IntegrationOfDaily(
								employeeId,
								(GeneralDate)any,
								null, 
								null, 
								null,
								Optional.empty(), 
								new ArrayList<>(), 
								Optional.empty(), 
								new BreakTimeOfDailyAttd(), 
								Optional.empty(), 
								Optional.empty(), 
								Optional.empty(), 
								Optional.empty(), 
								Optional.empty(), 
								Optional.empty(), 
								new ArrayList<>(),
								Optional.empty(),
								new ArrayList<>(),
								Optional.empty()));
				
				result = resultData;
				
			}
		};
		
		Optional<GeneralDate> optional = ReflectDataStampDailyService.getJudgment(require, cid, employeeId, stamp);
		
		assertThat(optional.isPresent()).isFalse();

	}
	
	
	// Test all date is not error and not date is true
	@Test
	public void test2() {
		
	
		Stamp stamp = StampHelper.getStampDefault();
		
		OutputCreateDailyOneDay resultData = helper.getErrorsNull(stamp);
		new Expectations() {
			{
				require.createDailyResult(cid,
						employeeId,
						(GeneralDate)any,
						ExecutionTypeDaily.CREATE,
						EmbossingExecutionFlag.ALL,
						(EmployeeGeneralInfoImport)any,
						(PeriodInMasterList)any,
						new IntegrationOfDaily(
								employeeId,
								(GeneralDate)any,
								null, 
								null, 
								null,
								Optional.empty(), 
								new ArrayList<>(), 
								Optional.empty(), 
								new BreakTimeOfDailyAttd(), 
								Optional.empty(), 
								Optional.empty(), 
								Optional.empty(), 
								Optional.empty(), 
								Optional.empty(), 
								Optional.empty(), 
								new ArrayList<>(),
								Optional.empty(),
								new ArrayList<>(),
								Optional.empty()));
				
				result = resultData;
				
				require.get(cid,
						employeeId,
						date,
						resultData.getIntegrationOfDaily().getWorkInformation());
				
				result = info;
				
				require.reflectStamp(stamp,
						info.getStampReflectRangeOutput(),
						resultData.getIntegrationOfDaily(),
						changeDailyAtt);
				
				result = errorMessageInfos;
				
			}
		};
		
		Optional<GeneralDate> optional = ReflectDataStampDailyService.getJudgment(require, cid, employeeId, stamp);
		
		assertThat(optional.isPresent()).isFalse();
	}
		
	// Test date is not error and not date is true
	@Test
	public void test3() {
	
		Stamp stamp = StampHelper.getStampDefaultIsTrue();
		
		OutputCreateDailyOneDay resultData = helper.getErrorsNull(stamp);
		new Expectations() {
			{
				require.createDailyResult(cid,
						employeeId,
						(GeneralDate)any,
						ExecutionTypeDaily.CREATE,
						EmbossingExecutionFlag.ALL,
						(EmployeeGeneralInfoImport)any,
						(PeriodInMasterList)any,
						new IntegrationOfDaily(
								employeeId,
								(GeneralDate)any,
								null, 
								null, 
								null,
								Optional.empty(), 
								new ArrayList<>(), 
								Optional.empty(), 
								new BreakTimeOfDailyAttd(), 
								Optional.empty(), 
								Optional.empty(), 
								Optional.empty(), 
								Optional.empty(), 
								Optional.empty(), 
								Optional.empty(), 
								new ArrayList<>(),
								Optional.empty(),
								new ArrayList<>(),
								Optional.empty()));
				
				result = resultData;
				
				require.get(cid,
						employeeId,
						date,
						resultData.getIntegrationOfDaily().getWorkInformation());
				
				result = info;
				
				require.reflectStamp(stamp,
						info.getStampReflectRangeOutput(),
						resultData.getIntegrationOfDaily(),
						changeDailyAtt);
				
				result = errorMessageInfos;
				
			}
		};
		
		Optional<GeneralDate> optional = ReflectDataStampDailyService.getJudgment(require, cid, employeeId, stamp);
		
		assertThat(optional.isPresent()).isTrue();
		GeneralDate date = GeneralDate.today().addDays(-2);
		assertThat(optional.get()).isEqualTo(date);

	}
}