package nts.uk.ctx.at.request.app.command.application.stamp;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.request.app.command.application.stamp.command.AppStampCmd;
import nts.uk.ctx.at.request.app.command.application.stamp.command.AppStampGoOutPermitCmd;
import nts.uk.ctx.at.request.app.command.application.stamp.command.AppStampWorkCmd;
import nts.uk.ctx.at.request.dom.application.AppReason;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.ProcessResult;
import nts.uk.ctx.at.request.dom.application.stamp.AppStamp;
import nts.uk.ctx.at.request.dom.application.stamp.AppStampAtr;
import nts.uk.ctx.at.request.dom.application.stamp.AppStampCancel;
import nts.uk.ctx.at.request.dom.application.stamp.AppStampCombinationAtr;
import nts.uk.ctx.at.request.dom.application.stamp.AppStampGoOutAtr;
import nts.uk.ctx.at.request.dom.application.stamp.AppStampGoOutPermit;
import nts.uk.ctx.at.request.dom.application.stamp.AppStampNewDomainService;
import nts.uk.ctx.at.request.dom.application.stamp.AppStampOnlineRecord;
import nts.uk.ctx.at.request.dom.application.stamp.AppStampWork;
import nts.uk.ctx.at.request.dom.application.stamp.StampRequestMode;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class RegisterAppStampCommandHandler extends CommandHandlerWithResult<AppStampCmd, ProcessResult> {
	
	private static final String DATE_FORMAT = "yyyy/MM/dd";

	@Inject
	private AppStampNewDomainService applicationStampNewDomainService;

	@Override
	protected ProcessResult handle(CommandHandlerContext<AppStampCmd> context) {
		String companyID = AppContexts.user().companyId();
		String employeeID = AppContexts.user().employeeId();
		AppStampCmd appStampCmd = context.getCommand();
		String applicationReason = "";
		if(!appStampCmd.getTitleReason().isEmpty() 
				|| !appStampCmd.getDetailReason().isEmpty()) {
			applicationReason = !appStampCmd.getTitleReason().isEmpty()? appStampCmd.getTitleReason() + System.lineSeparator() + appStampCmd.getDetailReason() : appStampCmd.getDetailReason();
		}
		
		AppStamp appStamp = null;
		StampRequestMode stampRequestMode = EnumAdaptor.valueOf(appStampCmd.getStampRequestMode(), StampRequestMode.class);
		switch(stampRequestMode){
			case STAMP_GO_OUT_PERMIT: 
				List<AppStampGoOutPermitCmd> appStampGoOutPermitCmdsReal = appStampCmd.getAppStampGoOutPermitCmds()
				.stream()
				.filter(x -> x.getStartTime()!=null||x.getEndTime()!=null||Strings.isNotBlank(x.getStartLocation()))
				.collect(Collectors.toList());
				if(CollectionUtil.isEmpty(appStampGoOutPermitCmdsReal)){
					return null;
				}
				appStamp = AppStamp.createGoOutPermitStamp(
						companyID, 
						GeneralDate.fromString(appStampCmd.getApplicationDate(), DATE_FORMAT), 
						employeeID, 
						new AppReason(applicationReason), 
						appStampGoOutPermitCmdsReal.stream().map(
								x -> new AppStampGoOutPermit(
										EnumAdaptor.valueOf(x.getStampAtr(), AppStampAtr.class), 
										x.getStampFrameNo(), 
										EnumAdaptor.valueOf(x.getStampGoOutAtr(), AppStampGoOutAtr.class), 
										Optional.ofNullable(x.getStartTime()).map(p -> new TimeWithDayAttr(p)), 
										Optional.ofNullable(x.getStartLocation()), 
										Optional.ofNullable(x.getEndTime()).map(p -> new TimeWithDayAttr(p)), 
										Optional.ofNullable(x.getEndLocation()))
							).collect(Collectors.toList()));
				return applicationStampNewDomainService.appStampRegister(applicationReason, appStamp, appStampCmd.isCheckOver1Year());
			case STAMP_WORK: 
				List<AppStampWorkCmd> appStampWorkCmdsReal = appStampCmd.getAppStampWorkCmds()
				.stream()
				.filter(x -> x.getStartTime()!=null||x.getEndTime()!=null||Strings.isNotBlank(x.getStartLocation()))
				.collect(Collectors.toList());
				if(CollectionUtil.isEmpty(appStampWorkCmdsReal)){
					return null;
				}
				appStamp = AppStamp.createWorkStamp(
						companyID, 
						GeneralDate.fromString(appStampCmd.getApplicationDate(), DATE_FORMAT), 
						employeeID, 
						new AppReason(applicationReason),
						appStampWorkCmdsReal.stream().map(
								x -> new AppStampWork(
										EnumAdaptor.valueOf(x.getStampAtr(), AppStampAtr.class), 
										x.getStampFrameNo(), 
										EnumAdaptor.valueOf(x.getStampGoOutAtr(), AppStampGoOutAtr.class), 
										Optional.ofNullable(x.getSupportCard()), 
										Optional.ofNullable(x.getSupportLocation()), 
										Optional.ofNullable(x.getStartTime()).map(p -> new TimeWithDayAttr(p)), 
										Optional.ofNullable(x.getStartLocation()), 
										Optional.ofNullable(x.getEndTime()).map(p -> new TimeWithDayAttr(p)), 
										Optional.ofNullable(x.getEndLocation()))
							).collect(Collectors.toList()));
				return applicationStampNewDomainService.appStampRegister(applicationReason, appStamp, appStampCmd.isCheckOver1Year());
			case STAMP_CANCEL: 
				appStamp = AppStamp.createCancelStamp(
						companyID, 
						GeneralDate.fromString(appStampCmd.getApplicationDate(), DATE_FORMAT), 
						employeeID, 
						new AppReason(applicationReason),
						appStampCmd.getAppStampCancelCmds().stream().map(
								x -> new AppStampCancel(
										EnumAdaptor.valueOf(x.getStampAtr(), AppStampAtr.class), 
										x.getStampFrameNo(), 
										x.getCancelAtr())	
							).collect(Collectors.toList()));
				return applicationStampNewDomainService.appStampRegister(applicationReason, appStamp, appStampCmd.isCheckOver1Year());
			case STAMP_ONLINE_RECORD: 
				appStamp = AppStamp.createOnlineRecordStamp(
						companyID, 
						GeneralDate.fromString(appStampCmd.getApplicationDate(), DATE_FORMAT), 
						employeeID, 
						new AppReason(applicationReason),
						Optional.of(new AppStampOnlineRecord(
								EnumAdaptor.valueOf(appStampCmd.getAppStampOnlineRecordCmd().getStampCombinationAtr(), AppStampCombinationAtr.class),
								appStampCmd.getAppStampOnlineRecordCmd().getAppTime())));
				return applicationStampNewDomainService.appStampRegister(applicationReason, appStamp, appStampCmd.isCheckOver1Year());
			case OTHER: 
				List<AppStampWorkCmd> appStampOtherCmdsReal = appStampCmd.getAppStampWorkCmds()
					.stream()
					.filter(x -> x.getStartTime()!=null||x.getEndTime()!=null||Strings.isNotBlank(x.getStartLocation()))
					.collect(Collectors.toList());
				if(CollectionUtil.isEmpty(appStampOtherCmdsReal)){
					return null;
				}
				appStamp = AppStamp.createOtherStamp(
						companyID, 
						GeneralDate.fromString(appStampCmd.getApplicationDate(), DATE_FORMAT), 
						employeeID, 
						new AppReason(applicationReason),
						appStampOtherCmdsReal.stream().map(
								x -> new AppStampWork(
										EnumAdaptor.valueOf(x.getStampAtr(), AppStampAtr.class), 
										x.getStampFrameNo(), 
										EnumAdaptor.valueOf(x.getStampGoOutAtr(), AppStampGoOutAtr.class), 
										Optional.ofNullable(x.getSupportCard()), 
										Optional.ofNullable(x.getSupportLocation()), 
										Optional.ofNullable(x.getStartTime()).map(p -> new TimeWithDayAttr(p)), 
										Optional.ofNullable(x.getStartLocation()), 
										Optional.ofNullable(x.getEndTime()).map(p -> new TimeWithDayAttr(p)), 
										Optional.ofNullable(x.getEndLocation()))
							).collect(Collectors.toList()));
				return applicationStampNewDomainService.appStampRegister(applicationReason, appStamp, appStampCmd.isCheckOver1Year());
			default:
				return null;
			
		}
	}

}
