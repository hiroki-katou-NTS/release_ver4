package nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.Getter;
import nts.arc.layer.app.cache.CacheCarrier;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.shared.dom.adapter.employment.BsEmploymentHistoryImport;
import nts.uk.ctx.at.shared.dom.adapter.employment.EmploymentHistShareImport;
import nts.uk.ctx.at.shared.dom.adapter.employment.ShareEmploymentAdapter;
import nts.uk.ctx.at.shared.dom.adapter.holidaymanagement.CompanyAdapter;
import nts.uk.ctx.at.shared.dom.adapter.holidaymanagement.CompanyDto;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.DigestionAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange.param.BreakDayOffRemainMngRefactParam;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange.param.SubstituteHolidayAggrResult;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimBreakDayOffMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimBreakDayOffMngRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimBreakMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimDayOffMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemain;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemainRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.DataManagementAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.RemainType;
import nts.uk.ctx.at.shared.dom.remainingnumber.subhdmana.ComDayOffManaDataRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.subhdmana.CompensatoryDayOffManaData;
import nts.uk.ctx.at.shared.dom.remainingnumber.subhdmana.LeaveManaDataRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.subhdmana.LeaveManagementData;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensLeaveComSetRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensLeaveEmSetRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryLeaveComSetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryLeaveEmSetting;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmployment;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;

@Stateless
public class NumberRemainVacationLeaveRangeProcess {

	@Inject
	private ComDayOffManaDataRepository comDayOffManaDataRepository;

	@Inject
	private LeaveManaDataRepository leaveManaDataRepository;

	@Inject
	private ShareEmploymentAdapter shareEmploymentAdapter;

	@Inject
	private CompensLeaveEmSetRepository compensLeaveEmSetRepository;

	@Inject
	private CompensLeaveComSetRepository compensLeaveComSetRepository;

	@Inject
	private InterimRemainRepository interimRemainRepository;

	@Inject
	private InterimBreakDayOffMngRepository interimBreakDayOffMngRepository;

	@Inject
	private ClosureRepository closureRepo;

	@Inject
	private ClosureEmploymentRepository closureEmpRepo;
	
	@Inject
	private ShareEmploymentAdapter shrEmpAdapter;

	@Inject
	private CompanyAdapter companyAdapter;

	public SubstituteHolidayAggrResult getBreakDayOffMngInPeriod(BreakDayOffRemainMngRefactParam inputParam) {

		RequireImpl requireImpl = new RequireImpl.RequireImplBuilder(comDayOffManaDataRepository,
				leaveManaDataRepository, shareEmploymentAdapter, compensLeaveEmSetRepository,
				compensLeaveComSetRepository, closureRepo, closureEmpRepo, shrEmpAdapter)
						.interimRemainRepo(interimRemainRepository)
						.interimBreakDayOffMngRepo(interimBreakDayOffMngRepository)
						.companyAdapter(companyAdapter).build();

		return NumberRemainVacationLeaveRangeQuery.getBreakDayOffMngInPeriod(requireImpl, inputParam);
	}

	@Getter
	public static class RequireImpl implements NumberRemainVacationLeaveRangeQuery.Require {

		private final ComDayOffManaDataRepository comDayOffManaDataRepository;

		private final LeaveManaDataRepository leaveManaDataRepository;

		private final ShareEmploymentAdapter shareEmploymentAdapter;

		private final CompensLeaveEmSetRepository compensLeaveEmSetRepository;

		private final CompensLeaveComSetRepository compensLeaveComSetRepository;

		private final InterimRemainRepository interimRemainRepository;

		private final InterimBreakDayOffMngRepository interimBreakDayOffMngRepository;

		private final CompanyAdapter companyAdapter;
		
		private ClosureRepository closureRepo;
		
		private ClosureEmploymentRepository closureEmpRepo;
		
		private ShareEmploymentAdapter shrEmpAdapter;

		public RequireImpl(RequireImplBuilder builder) {
			this.comDayOffManaDataRepository = builder.comDayOffManaDataRepository;
			this.leaveManaDataRepository = builder.leaveManaDataRepository;
			this.shareEmploymentAdapter = builder.shareEmploymentAdapter;
			this.compensLeaveEmSetRepository = builder.compensLeaveEmSetRepository;
			this.compensLeaveComSetRepository = builder.compensLeaveComSetRepository;
			this.interimRemainRepository = builder.interimRemainRepository;
			this.interimBreakDayOffMngRepository = builder.interimBreakDayOffMngRepository;
			this.companyAdapter = builder.companyAdapter;
			this.closureEmpRepo = builder.closureEmpRepo;
			this.closureRepo = builder.closureRepo;
			this.shrEmpAdapter = builder.shrEmpAdapter;

		}

		@Override
		public List<CompensatoryDayOffManaData> getBySidYmd(String companyId, String employeeId,
				GeneralDate startDateAggr) {
			return comDayOffManaDataRepository.getBySidYmd(companyId, employeeId, startDateAggr);
		}

		@Override
		public List<LeaveManagementData> getBySidYmd(String cid, String sid, GeneralDate ymd, DigestionAtr state) {
			return leaveManaDataRepository.getBySidYmd(cid, sid, ymd, state);
		}

		@Override
		public Optional<BsEmploymentHistoryImport> findEmploymentHistory(String companyId, String employeeId,
				GeneralDate baseDate) {
			return shareEmploymentAdapter.findEmploymentHistory(companyId, employeeId, baseDate);
		}

		@Override
		public CompensatoryLeaveEmSetting findComLeavEmpSet(String companyId, String employmentCode) {
			return compensLeaveEmSetRepository.find(companyId, employmentCode);
		}

		@Override
		public CompensatoryLeaveComSetting findComLeavComSet(String companyId) {
			return compensLeaveComSetRepository.find(companyId);
		}

		@Override
		public List<InterimRemain> getRemainBySidPriod(String employeeId, DatePeriod dateData, RemainType remainType) {
			return interimRemainRepository.getRemainBySidPriod(employeeId, dateData, remainType);
		}

		@Override
		public List<InterimDayOffMng> getDayOffBySidPeriod(String sid, DatePeriod period) {
			return interimBreakDayOffMngRepository.getDayOffBySidPeriod(sid, period);
		}

		@Override
		public List<InterimBreakDayOffMng> getBreakDayOffMng(String mngId, boolean breakDay, DataManagementAtr mngAtr) {
			return interimBreakDayOffMngRepository.getBreakDayOffMng(mngId, breakDay, mngAtr);
		}

		@Override
		public List<InterimBreakMng> getBySidPeriod(String sid, DatePeriod period) {
			return interimBreakDayOffMngRepository.getBySidPeriod(sid, period);
		}

		@Override
		public Closure getClosureDataByEmployee(String employeeId, GeneralDate baseDate) {
			return ClosureService.getClosureDataByEmployee(createImp(), new CacheCarrier(), employeeId, baseDate);
		}

		@Override
		public CompanyDto getFirstMonth(String companyId) {
			return companyAdapter.getFirstMonth(companyId);
		}

		@Override
		public List<EmploymentHistShareImport> findByEmployeeIdOrderByStartDate(String employeeId) {
			return shareEmploymentAdapter.findByEmployeeIdOrderByStartDate(employeeId);
		}

		public static class RequireImplBuilder {

			public ComDayOffManaDataRepository comDayOffManaDataRepository;

			public LeaveManaDataRepository leaveManaDataRepository;

			public ShareEmploymentAdapter shareEmploymentAdapter;

			public CompensLeaveEmSetRepository compensLeaveEmSetRepository;

			public CompensLeaveComSetRepository compensLeaveComSetRepository;

			public InterimRemainRepository interimRemainRepository;

			public InterimBreakDayOffMngRepository interimBreakDayOffMngRepository;


			public CompanyAdapter companyAdapter;
			
			private ClosureRepository closureRepo;
			
			private ClosureEmploymentRepository closureEmpRepo;
			
			private ShareEmploymentAdapter shrEmpAdapter;

			public RequireImplBuilder(ComDayOffManaDataRepository comDayOffManaDataRepository,
					LeaveManaDataRepository leaveManaDataRepository, ShareEmploymentAdapter shareEmploymentAdapter,
					CompensLeaveEmSetRepository compensLeaveEmSetRepository,
					CompensLeaveComSetRepository compensLeaveComSetRepository,
					ClosureRepository closureRepo, ClosureEmploymentRepository closureEmpRepo,
					ShareEmploymentAdapter shrEmpAdapter) {
				this.comDayOffManaDataRepository = comDayOffManaDataRepository;
				this.leaveManaDataRepository = leaveManaDataRepository;
				this.shareEmploymentAdapter = shareEmploymentAdapter;
				this.compensLeaveEmSetRepository = compensLeaveEmSetRepository;
				this.compensLeaveComSetRepository = compensLeaveComSetRepository;
				this.closureRepo = closureRepo;
				this.closureEmpRepo = closureEmpRepo;
				this.shrEmpAdapter = shrEmpAdapter;
			}

			public RequireImplBuilder interimRemainRepo(InterimRemainRepository interimRemainRepository) {
				this.interimRemainRepository = interimRemainRepository;
				return this;
			}

			public RequireImplBuilder interimBreakDayOffMngRepo(
					InterimBreakDayOffMngRepository interimBreakDayOffMngRepository) {
				this.interimBreakDayOffMngRepository = interimBreakDayOffMngRepository;
				return this;
			}

			public RequireImplBuilder companyAdapter(CompanyAdapter companyAdapter) {
				this.companyAdapter = companyAdapter;
				return this;
			}

			public RequireImpl build() {
				return new RequireImpl(this);
			}

		}
<<<<<<< HEAD
		
		private ClosureService.RequireM3 createImp() {
			
			return new ClosureService.RequireM3() {
				
				@Override
				public Optional<Closure> closure(String companyId, int closureId) {
					return closureRepo.findById(companyId, closureId);
				}
				
				@Override
				public Optional<ClosureEmployment> employmentClosure(String companyID, String employmentCD) {
					return closureEmpRepo.findByEmploymentCD(companyID, employmentCD);
				}
				
				@Override
				public Optional<BsEmploymentHistoryImport> employmentHistory(CacheCarrier cacheCarrier, String companyId,
						String employeeId, GeneralDate baseDate) {
					return shrEmpAdapter.findEmploymentHistoryRequire(cacheCarrier, companyId, employeeId, baseDate);
				}
			};
=======

		@Override
		public List<InterimBreakDayOffMng> getDayOffByIdAndDataAtr(DataManagementAtr breakAtr,
				DataManagementAtr dayOffAtr, String dayOffId) {
			return interimBreakDayOffMngRepository.getDayOffByIdAndDataAtr(breakAtr, dayOffAtr, dayOffId);
		}

		@Override
		public List<InterimBreakDayOffMng> getBreakByIdAndDataAtr(DataManagementAtr breakAtr,
				DataManagementAtr dayOffAtr, String breakId) {
			return interimBreakDayOffMngRepository.getBreakByIdAndDataAtr(breakAtr, dayOffAtr, breakId);
>>>>>>> 2079ec57059... update requestList 203, 204
		}
	}
}
