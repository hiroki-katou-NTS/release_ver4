package nts.uk.ctx.at.request.dom.applicationreflect.service.getapp;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.AllArgsConstructor;
import nts.uk.ctx.at.request.dom.application.Application;
import nts.uk.ctx.at.request.dom.application.appabsence.ApplyForLeave;
import nts.uk.ctx.at.request.dom.application.appabsence.ApplyForLeaveRepository;
import nts.uk.ctx.at.request.dom.application.businesstrip.BusinessTrip;
import nts.uk.ctx.at.request.dom.application.businesstrip.BusinessTripRepository;
import nts.uk.ctx.at.request.dom.application.gobackdirectly.GoBackDirectly;
import nts.uk.ctx.at.request.dom.application.gobackdirectly.GoBackDirectlyRepository;
import nts.uk.ctx.at.request.dom.application.holidayshipment.absenceleaveapp.AbsenceLeaveApp;
import nts.uk.ctx.at.request.dom.application.holidayshipment.absenceleaveapp.AbsenceLeaveAppRepository;
import nts.uk.ctx.at.request.dom.application.holidayshipment.recruitmentapp.RecruitmentApp;
import nts.uk.ctx.at.request.dom.application.holidayshipment.recruitmentapp.RecruitmentAppRepository;
import nts.uk.ctx.at.request.dom.application.holidayworktime.AppHolidayWork;
import nts.uk.ctx.at.request.dom.application.holidayworktime.AppHolidayWorkRepository;
import nts.uk.ctx.at.request.dom.application.lateleaveearly.ArrivedLateLeaveEarly;
import nts.uk.ctx.at.request.dom.application.lateleaveearly.ArrivedLateLeaveEarlyRepository;
import nts.uk.ctx.at.request.dom.application.optional.OptionalItemApplication;
import nts.uk.ctx.at.request.dom.application.optional.OptionalItemApplicationRepository;
import nts.uk.ctx.at.request.dom.application.overtime.AppOverTime;
import nts.uk.ctx.at.request.dom.application.overtime.AppOverTimeRepository;
import nts.uk.ctx.at.request.dom.application.stamp.AppRecordImage;
import nts.uk.ctx.at.request.dom.application.stamp.AppRecordImageRepository;
import nts.uk.ctx.at.request.dom.application.stamp.AppStamp;
import nts.uk.ctx.at.request.dom.application.stamp.AppStampRepository;
import nts.uk.ctx.at.request.dom.application.timeleaveapplication.TimeLeaveApplication;
import nts.uk.ctx.at.request.dom.application.timeleaveapplication.TimeLeaveApplicationRepository;
import nts.uk.ctx.at.request.dom.application.workchange.AppWorkChange;
import nts.uk.ctx.at.request.dom.application.workchange.AppWorkChangeRepository;

@Stateless
public class GetApplicationRequireImpl {

	@Inject
	private GoBackDirectlyRepository repoGoBack;
	@Inject
	private BusinessTripRepository repoBusTrip;
	@Inject
	private ArrivedLateLeaveEarlyRepository repoLateLeave;
	@Inject
	private AppStampRepository repoStamp;
	@Inject
	private AppWorkChangeRepository repoWorkChange;
	@Inject
	private AppRecordImageRepository repoRecordImg;
	@Inject
	private TimeLeaveApplicationRepository timeLeaveApplicationRepository;
	@Inject
	private AppOverTimeRepository appOverTimeRepository;
	@Inject
	private ApplyForLeaveRepository applyForLeaveRepository;
	@Inject
	private AppHolidayWorkRepository appHolidayWorkRepository;
	@Inject
	private AbsenceLeaveAppRepository absenceLeaveAppRepository;
	@Inject
	private RecruitmentAppRepository recruitmentAppRepository;
	@Inject
	private OptionalItemApplicationRepository optionalItemApplicationRepository;
	
	public RequireImpl createImpl() {

		return new RequireImpl(repoGoBack, repoBusTrip, repoLateLeave, repoStamp, repoWorkChange, repoRecordImg,
				timeLeaveApplicationRepository, appOverTimeRepository, applyForLeaveRepository,
				absenceLeaveAppRepository, recruitmentAppRepository, optionalItemApplicationRepository);
	}

	@AllArgsConstructor
	public class RequireImpl implements GetApplicationForReflect.Require {

		private final GoBackDirectlyRepository repoGoBack;

		private final BusinessTripRepository repoBusTrip;

		private final ArrivedLateLeaveEarlyRepository repoLateLeave;

		private final AppStampRepository repoStamp;

		private final AppWorkChangeRepository repoWorkChange;

		private final AppRecordImageRepository repoRecordImg;

		private final TimeLeaveApplicationRepository timeLeaveApplicationRepository;
		
		private final AppOverTimeRepository appOverTimeRepository;
		
		private final ApplyForLeaveRepository applyForLeaveRepository;
		
		private final AbsenceLeaveAppRepository absenceLeaveAppRepository;
		
		private final RecruitmentAppRepository recruitmentAppRepository;
		
		private final OptionalItemApplicationRepository optionalItemApplicationRepository;
		

		@Override
		public Optional<AppWorkChange> findAppWorkCg(String companyId, String appID, Application app) {
			return repoWorkChange.findbyID(companyId, appID, app);
		}

		@Override
		public Optional<GoBackDirectly> findGoBack(String companyId, String appID, Application app) {
			return repoGoBack.find(companyId, appID, app);
		}

		@Override
		public Optional<AppStamp> findAppStamp(String companyId, String appID, Application app) {
			return repoStamp.findByAppID(companyId, appID, app);
		}

		@Override
		public Optional<ArrivedLateLeaveEarly> findArrivedLateLeaveEarly(String companyId, String appID,
				Application application) {
			ArrivedLateLeaveEarly app = repoLateLeave.getLateEarlyApp(companyId, appID, application);
			return app == null ? Optional.empty() : Optional.of(app);

		}

		@Override
		public Optional<BusinessTrip> findBusinessTripApp(String companyId, String appID, Application app) {
			return repoBusTrip.findByAppId(companyId, appID, app);
		}

		@Override
		public Optional<AppRecordImage> findAppRecordImage(String companyId, String appID, Application app) {
			return repoRecordImg.findByAppID(companyId, appID, app);
		}

		@Override
		public Optional<TimeLeaveApplication> findTimeLeavById(String companyId, String appId) {
			return timeLeaveApplicationRepository.findById(companyId, appId);
		}

		@Override
		public Optional<AppOverTime> findOvertime(String companyId, String appId) {
			return appOverTimeRepository.find(companyId, appId);
		}

		@Override
		public Optional<ApplyForLeave> findApplyForLeave(String CID, String appId) {
			return applyForLeaveRepository.findApplyForLeave(CID, appId);
		}

		@Override
		public Optional<AppHolidayWork> findAppHolidayWork(String companyId, String appId) {
			return appHolidayWorkRepository.find(companyId, appId);
		}

		@Override
		public Optional<AbsenceLeaveApp> findAbsenceByID(String applicationID) {
			return absenceLeaveAppRepository.findByAppId(applicationID);
		}

		@Override
		public Optional<RecruitmentApp> findRecruitmentByID(String applicationID) {
			return recruitmentAppRepository.findByID(applicationID);
		}

		@Override
		public Optional<OptionalItemApplication> getOptionalByAppId(String companyId, String appId) {
			return optionalItemApplicationRepository.getByAppId(companyId, appId);
		}
	}
}
