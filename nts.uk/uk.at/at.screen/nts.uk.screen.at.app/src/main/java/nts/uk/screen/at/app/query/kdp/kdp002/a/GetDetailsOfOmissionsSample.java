package nts.uk.screen.at.app.query.kdp.kdp002.a;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import nts.arc.layer.app.cache.CacheCarrier;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.record.dom.stamp.application.StampPromptAppRepository;
import nts.uk.ctx.at.record.dom.stamp.application.StampPromptApplication;
import nts.uk.ctx.at.record.dom.stamp.card.management.personalengraving.AppDisplayNameAdapter;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerErrorRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErAlApplication;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErAlApplicationRepository;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.domainservice.CheckAttdErrorAfterStampService;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.ButtonPositionNo;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.PageNo;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.StampButton;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.StampSetPerRepository;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.StampSettingPerson;
import nts.uk.ctx.at.shared.dom.adapter.employment.ShareEmploymentAdapter;
import nts.uk.ctx.at.shared.dom.dailyattdcal.dailyattendance.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosurePeriod;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.shr.com.context.AppContexts;

/**
 * UKDesign.UniversalK.就業.KDP_打刻.KDP002_打刻入力(個人打刻).A:打刻入力(個人).メニュー別OCD.打刻漏れの内容を取得する.打刻漏れの内容を取得する
 * @author lamvt
 *
 */
public class GetDetailsOfOmissionsSample {
	
	@Inject
	private AppDisplayNameAdapter appDisplayAdapter;
	@Inject
	private StampPromptAppRepository stamPromptAppRepo;

	@Inject
	private ErAlApplicationRepository erAlApplicationRepo;

	@Inject
	private EmployeeDailyPerErrorRepository employeeDailyPerErrorRepo;

	@Inject
	private StampSetPerRepository stampSetPerRepo;
	
	@Inject
	private ClosureRepository closureRepo;
	@Inject
	private ClosureEmploymentRepository closureEmploymentRepo;
	@Inject
	private ShareEmploymentAdapter shareEmploymentAdapter;
	
	public GetDetailsOfOmissionsSampleDto get(int pageNo, int buttonDisNo) {
		
		//1: 取得する(@Require, 社員ID, 打刻手段, 打刻ボタン)
		//Viết code cho xử lý 1
		
		//2: <call>
		//Viết code cho xử lý 2
		String employeeId = AppContexts.user().employeeId();
		CheckAttdErrorAfterStampRequiredImpl required = new CheckAttdErrorAfterStampRequiredImpl();
		StampButton stampButton = new StampButton(new PageNo(pageNo), new ButtonPositionNo(buttonDisNo));

		return new GetDetailsOfOmissionsSampleDto(CheckAttdErrorAfterStampService.get(required, employeeId, stampButton), appDisplayAdapter.getAppDisplay());
	}
	
	private class CheckAttdErrorAfterStampRequiredImpl implements CheckAttdErrorAfterStampService.Require {

		@Override
		public Optional<StampPromptApplication> getStampSet() {
			return stamPromptAppRepo.getStampPromptApp(AppContexts.user().companyId());
		}

		@Override
		public DatePeriod findClosurePeriod(String employeeId, GeneralDate baseDate) {
			return ClosureService.findClosurePeriod(
					ClosureService.createRequireM3(closureRepo, closureEmploymentRepo, shareEmploymentAdapter),
					new CacheCarrier(), employeeId, baseDate);
		}

		@Override
		public Optional<ClosurePeriod> getClosurePeriod(String employeeId, GeneralDate baseDate) {
			Closure closure = ClosureService.getClosureDataByEmployee(
					ClosureService.createRequireM3(closureRepo, closureEmploymentRepo, shareEmploymentAdapter),
					new CacheCarrier(), employeeId, baseDate);
			if (closure == null)
				return Optional.empty();
			Optional<ClosurePeriod> closurePeriodOpt = closure.getClosurePeriodByYmd(baseDate);
			return closurePeriodOpt;
		}

		@Override
		public Optional<ErAlApplication> getAllErAlAppByEralCode(String errorAlarmCode) {
			return erAlApplicationRepo.getAllErAlAppByEralCode(AppContexts.user().companyId(), errorAlarmCode);
		}

		@Override
		public List<EmployeeDailyPerError> findByPeriodOrderByYmd(String employeeId, DatePeriod datePeriod) {
			return employeeDailyPerErrorRepo.findByPeriodOrderByYmd(employeeId, datePeriod);
		}

		@Override
		public Optional<StampSettingPerson> getStampSetPer() {
			return stampSetPerRepo.getStampSetting(AppContexts.user().companyId());
		}

	}
}
