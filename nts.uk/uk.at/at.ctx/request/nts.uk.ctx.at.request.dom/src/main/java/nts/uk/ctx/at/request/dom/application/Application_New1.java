package nts.uk.ctx.at.request.dom.application;

import java.util.Objects;
import java.util.Optional;

import javax.inject.Inject;

import lombok.val;
import nts.arc.i18n.I18NText;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.at.request.dom.application.appabsence.AppAbsence;
import nts.uk.ctx.at.request.dom.application.appabsence.AppAbsenceRepository;
import nts.uk.ctx.at.request.dom.application.gobackdirectly.GoBackDirectly;
import nts.uk.ctx.at.request.dom.application.gobackdirectly.GoBackDirectlyRepository;
import nts.uk.ctx.at.request.dom.application.holidayinstruction.HolidayInstructRepository;
import nts.uk.ctx.at.request.dom.application.lateorleaveearly.LateOrLeaveEarlyRepository;
import nts.uk.ctx.at.request.dom.application.overtime.AppOverTime;
import nts.uk.ctx.at.request.dom.application.overtime.OvertimeRepository;
import nts.uk.ctx.at.request.dom.application.stamp.AppStamp;
import nts.uk.ctx.at.request.dom.application.stamp.AppStampAtr;
import nts.uk.ctx.at.request.dom.application.stamp.AppStampOnlineRecord;
import nts.uk.ctx.at.request.dom.application.stamp.AppStampRepository;
import nts.uk.ctx.at.request.dom.application.workchange.AppWorkChange;
import nts.uk.ctx.at.request.dom.application.workchange.IAppWorkChangeRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * @author hiep.ld
 * Pending
 */
public class Application_New1 extends Application_New{
	@Inject
	private AppAbsenceRepository absenRepo;
	
	@Inject
	private GoBackDirectlyRepository goBackRepo;
	
	@Inject
	private HolidayInstructRepository holidayRepo;
	
	@Inject
	private LateOrLeaveEarlyRepository lateLeaveEarlyRepo;
	
	@Inject
	private OvertimeRepository overtimeRepo;
	
	@Inject
	private AppStampRepository appStampRepo;
	
	@Inject
	private IAppWorkChangeRepository workChangeRepo;
	
	public Application_New1(Long version, String companyID, String appID, PrePostAtr prePostAtr,
			GeneralDateTime inputDate, String enteredPersonID, AppReason reversionReason, GeneralDate appDate,
			AppReason appReason, ApplicationType appType, String employeeID, Optional<GeneralDate> startDate,
			Optional<GeneralDate> endDate, ReflectionInformation_New reflectionInformation) {
		super(version, companyID, appID, prePostAtr, inputDate, enteredPersonID, reversionReason, appDate, appReason, appType,
				employeeID, startDate, endDate, reflectionInformation);
		// TODO Auto-generated constructor stub
	}
	public String getAppContent() {
		// TO-DO
		String appReson = this.getAppReason().toString();
		ApplicationType appType = this.getAppType();
		String appID = this.getAppID();
		String companyID = AppContexts.user().companyId();
		switch (this.getAppType()) {
		case OVER_TIME_APPLICATION: {
			String content = "";
			Optional<AppOverTime> op_overTime = overtimeRepo.getAppOvertimeFrame(companyID, appID);
			if (op_overTime.isPresent()){
				AppOverTime overTime = op_overTime.get();
				content = I18NText.getText("CMM045_268") + " ";
				content = I18NText.getText("CMM045_1") + " ";
				switch (overTime.getOverTimeAtr()){
				
				}
				for (val x : overTime.getOverTimeInput()){
					content += x.getStartTime().toString() + I18NText.getText("CMM045_100") + x.getEndTime().toString()
							  + I18NText.getText("CMM045_270", x.getApplicationTime().toString())
							  + new String (x.getTimeItemTypeAtr().value == 0 ? I18NText.getText("CMM045_270") : I18NText.getText("CMM045_271"));
				}
				
				//app.prePostAtr == 0 ? '事前' : '事後';
			}
			return content + "\n" + appReson ;
		}
		case ABSENCE_APPLICATION: {
			String content = I18NText.getText("CMM045_279");
			Optional<AppAbsence> op_appAbsen = absenRepo.getAbsenceByAppId(companyID, appID);
			if (op_appAbsen.isPresent()){
				AppAbsence appAbsen = op_appAbsen.get();
				// TO-DO
				//content += I18NText.getText("CMM045_248") + I18NText.getText("CMM045_230", appAbsen.get);
			}
			return content + "\n" + appReson;
		}
		case WORK_CHANGE_APPLICATION: {
			String content = I18NText.getText("CMM045_250");
			Optional<AppWorkChange> op_appWork = workChangeRepo.getAppworkChangeById(companyID, appID);
			if (op_appWork.isPresent()){
				AppWorkChange appWork = op_appWork.get();
				if (appWork.getWorkTimeStart1() !=0 || appWork.getWorkTimeEnd1() !=0){
					content += I18NText.getText("CMM045_252") ;
				}
			}
			
			return content  + "\n" + appReson;
		}
		case BUSINESS_TRIP_APPLICATION: {
			// TO- DO
			String content = I18NText.getText("CMM045_254") + "\n" + appReson;
			return content;
		}
		case GO_RETURN_DIRECTLY_APPLICATION: {
			Optional<GoBackDirectly> op_appGoBack = goBackRepo.findByApplicationID(companyID, appID);
			String content = I18NText.getText("CMM045_258") + "\n" + appReson;
			if (op_appGoBack.isPresent()){
				GoBackDirectly appGoBack = op_appGoBack.get();
				content += I18NText.getText("CMM045_259") + I18NText.getText("CMM045_258") ;
			}
			return content;
		}
		case BREAK_TIME_APPLICATION: {
			// TO DO
			return appReson;
		}
		case STAMP_APPLICATION: {
			String content = "";
			AppStamp appStamp = appStampRepo.findByAppID(companyID, appID);
			if (!Objects.isNull(appStamp)){
				switch (appStamp.getStampRequestMode()){
				case STAMP_GO_OUT_PERMIT: {
					int k = 0;
					boolean checkAppend = false;
					for (val x : appStamp.getAppStampGoOutPermits()){
						if (x.getStampAtr() == AppStampAtr.GO_OUT){
							content += I18NText.getText("CMM045_232") + " "
									+  I18NText.getText("CMM045_230", x.getStampGoOutAtr().name) + " "
									+  new String (x.getStartTime().isPresent() ? x.getStartTime().get().toString() : "") + " "
									+  I18NText.getText("CMM045_100") + " "
									+  new String (x.getEndTime().isPresent() ? x.getEndTime().get().toString() : "") + " ";
							if (k==2) break;
							k++;
						} else if (x.getStampAtr() == AppStampAtr.CHILDCARE){
							if (!checkAppend) {
								content += I18NText.getText("CMM045_233") + " ";
								checkAppend = true;
							}
							content += new String(x.getStartTime().isPresent() ? x.getStartTime().get().toString() : "") + " "
									+  I18NText.getText("CMM045_100") + " "
									+  new String(x.getEndTime().isPresent() ? x.getEndTime().get().toString() : "") + " ";
							if (k==1) break;
							k++;
						} else if (x.getStampAtr() == AppStampAtr.CARE){
							if (!checkAppend) {
								content += I18NText.getText("CMM045_234") + " ";
								checkAppend = true;
							}
							content += new String (x.getStartTime().isPresent() ? x.getStartTime().get().toString() : "") + " "
									+  I18NText.getText("CMM045_100") + " "
									+  new String (x.getEndTime().isPresent() ? x.getEndTime().get().toString() : "") + " ";
							if (k==1) break;
							k++;
						}
					}
					content += new String (appStamp.getAppStampGoOutPermits().size() - k > 0 ? I18NText.getText("CMM045_231", (appStamp.getAppStampGoOutPermits().size() - k) + "" ) : "");
					break;
				}
				case STAMP_WORK: {
					int k = 0;
					content += I18NText.getText("CMM045_235") + " ";
					for (val x : appStamp.getAppStampWorks()){
						if (k==2) break;
						k++;
						content +=  x.getStampAtr().name + " "
								+  new String (x.getStartTime().isPresent() ? x.getStartTime().get().toString() : "") + " "
								+  I18NText.getText("CMM045_100") + " "
								+  new String( x.getStartTime().isPresent() ? x.getEndTime().get().toString() : "") + " ";
					}
					content += new String (appStamp.getAppStampGoOutPermits().size() - k > 0 ? I18NText.getText("CMM045_231", (appStamp.getAppStampGoOutPermits().size() - k) + "" ) : "");
					break;
				}
				case STAMP_ONLINE_RECORD: {
					// TO-DO
					int k = 0;
					content += I18NText.getText("CMM045_240");
					Optional<AppStampOnlineRecord> appStampRecord = appStamp.getAppStampOnlineRecord();
					if (appStampRecord.isPresent()){
						content += appStampRecord.get().getStampCombinationAtr().name + appStampRecord.get().getAppTime().toString();
					}
					break;
				}
				case STAMP_CANCEL: {
					content += I18NText.getText("CMM045_235");
					for (val x : appStamp.getAppStampCancels()){
						switch (x.getStampAtr()) {
						// TO-DO
						case ATTENDANCE: {
							content += " ×出勤　9:00　×退勤　17:00 ";
						}
						case CARE: {
							content += " ×出勤　9:00　×退勤　17:00 ";
						}
						case CHILDCARE: {
							content += " ×出勤　9:00　×退勤　17:00 ";
						}
						case GO_OUT: {
							content += " ×出勤　9:00　×退勤　17:00 ";
						}
						case SUPPORT: {
							content += " ×出勤　9:00　×退勤　17:00 ";
						}
						}
					}
					break;
				}
				case OTHER: {
					int k = 0;
					for (val x : appStamp.getAppStampWorks()){
						switch(x.getStampAtr()){
						case ATTENDANCE: {
							if (k==2) break;
							k++;
							content += x.getStampAtr().name +" ";
							content += new String(x.getStartTime().isPresent() ? x.getStartTime().get().toString() : "") +" "
									+  I18NText.getText("CMM045_100") + " "
									+  new String(x.getEndTime().isPresent() ? x.getEndTime().get().toString() : "") + " ";
						}
						case CARE: {
							if (k==2) break;
							k++;
							content += I18NText.getText("CMM045_234") + " "
									+  new String (x.getStartTime().isPresent() ? x.getStartTime().get().toString() : "") + " "
									+  I18NText.getText("CMM045_100") + " "
									+  new String (x.getEndTime().isPresent() ? x.getEndTime().get().toString() : "") + " ";
						}
						case CHILDCARE: {
							if (k==2) break;
							k++;
							content += I18NText.getText("CMM045_233") + " "
									+  new String(x.getStartTime().isPresent() ? x.getStartTime().get().toString() : "") + " "
									+  I18NText.getText("CMM045_100") + " "
									+  new String(x.getEndTime().isPresent() ? x.getEndTime().get().toString() : "") + " ";
						}
						case GO_OUT: {
							if (k==2) break;
							k++;
							content += I18NText.getText("CMM045_232") + " "
									+  I18NText.getText("CMM045_230", x.getStampGoOutAtr().name) + " "
									+  new String (x.getStartTime().isPresent() ? x.getStartTime().get().toString() : "") + " "
									+  I18NText.getText("CMM045_100") + " "
									+  new String (x.getEndTime().isPresent() ? x.getEndTime().get().toString() : "") + " ";
						}
						case SUPPORT: {
							if (k==2) break;
							k++;
							content += I18NText.getText("CMM045_242") + " "
									+  new String (x.getStartTime().isPresent() ? x.getStartTime().get().toString() : "") + " "
									+  I18NText.getText("CMM045_100") + " "
									+  new String (x.getEndTime().isPresent() ? x.getEndTime().get().toString() : "") + " ";
						}
						}
					}
					content += new String (appStamp.getAppStampGoOutPermits().size() - k > 0 ? I18NText.getText("CMM045_231", (appStamp.getAppStampGoOutPermits().size() - k) + "" ) : "");
					break;
				}
				}
			}
			return content + "\n" + appReson;
		}
		case ANNUAL_HOLIDAY_APPLICATION: {
			String content = I18NText.getText("CMM045_264") + "\n" + appReson;
			return content;
		}
		case EARLY_LEAVE_CANCEL_APPLICATION: {
			
			String content = I18NText.getText("CMM045_243") + "\n" + appReson;
			return content;
		}
		case COMPLEMENT_LEAVE_APPLICATION: {
			// TO DO
			return appReson;
		}
		case STAMP_NR_APPLICATION: {
			// TO DO
			return appReson;
		}
		case LONG_BUSINESS_TRIP_APPLICATION: {
			// TO DO
			return appReson;
		}
		case BUSINESS_TRIP_APPLICATION_OFFICE_HELPER: {
			// TO DO
			// NO SPEC
			return appReson;
		}
		case APPLICATION_36: {
			// TO DO
			// NO SPEC
			return appReson;
		}
		}
		return null;
	}
}
