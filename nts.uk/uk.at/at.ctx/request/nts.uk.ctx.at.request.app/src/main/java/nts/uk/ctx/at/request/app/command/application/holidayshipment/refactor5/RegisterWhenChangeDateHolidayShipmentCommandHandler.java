package nts.uk.ctx.at.request.app.command.application.holidayshipment.refactor5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.app.find.application.holidayshipment.refactor5.dto.DisplayInforWhenStarting;
import nts.uk.ctx.at.request.app.find.application.holidayshipment.refactor5.dto.LinkingManagementInforDto;
import nts.uk.ctx.at.request.dom.application.ReflectedState;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ErrorFlagImport;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.ActualContentDisplay;
import nts.uk.ctx.at.request.dom.application.holidayshipment.absenceleaveapp.AbsenceLeaveApp;
import nts.uk.ctx.at.request.dom.application.holidayshipment.absenceleaveapp.AbsenceLeaveAppRepository;
import nts.uk.ctx.at.request.dom.application.holidayshipment.recruitmentapp.RecruitmentApp;
import nts.uk.ctx.at.request.dom.application.holidayshipment.recruitmentapp.RecruitmentAppRepository;
import nts.uk.ctx.at.shared.app.find.remainingnumber.paymana.PayoutSubofHDManagementDto;
import nts.uk.ctx.at.shared.app.find.remainingnumber.subhdmana.dto.LeaveComDayOffManaDto;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.InterimRemainDataMngRegisterDateChange;
import nts.uk.ctx.at.shared.dom.remainingnumber.paymana.PayoutSubofHDManaRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.subhdmana.LeaveComDayOffManaRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.ManageDistinct;
import nts.uk.shr.com.context.AppContexts;

/**
 * @author thanhpv
 *	UKDesign.UniversalK.就業.KAF_申請.KAF011_振休振出申請.C：振休日変更.ユースケース.登録する(Đăng ký)
 */
@Stateless
public class RegisterWhenChangeDateHolidayShipmentCommandHandler {

	@Inject
	private ErrorCheckProcessingBeforeRegistrationKAF011 errorCheckProcessingBeforeRegistrationKAF011;
	
	@Inject
	private RecruitmentAppRepository recruitmentAppRepository;
	
	@Inject
	private AbsenceLeaveAppRepository absenceLeaveAppRepository;
	
	@Inject
	private SaveHolidayShipmentCommandHandlerRef5 saveHolidayShipmentCommandHandlerRef5;
	
	@Inject
	private InterimRemainDataMngRegisterDateChange interimRemainDataMngRegisterDateChange;
	
	@Inject
	private PreRegistrationErrorCheck preRegistrationErrorCheck;
	
	@Inject
	private LeaveComDayOffManaRepository leaveComDayOffManaRepository;
	
	@Inject
	private PayoutSubofHDManaRepository payoutSubofHDManaRepository;
	
	/**
	 * @name 登録する
	 */
	public void register(DisplayInforWhenStarting command, GeneralDate appDateNew, String appReason, Integer appStandardReasonCD){
		String companyId = AppContexts.user().companyId();
		
		AbsenceLeaveApp absNew = this.errorCheckWhenChangingHolidays(companyId, command, appDateNew, appReason, appStandardReasonCD);
		
		this.registerProcess(companyId, command, absNew);
	}
	
	/**
	 * @name 振休日変更時のエラーチェック
	 * @param companyId 会社ID
	 * @param displayInforWhenStarting 振休振出申請起動時の表示情報
	 * @param appDate 振休日_NEW
	 * @param appReason 申請理由_NEW
	 * @param appStandardReasonCD 定型理由コード_NEW
	 */
	public AbsenceLeaveApp errorCheckWhenChangingHolidays(String companyId, DisplayInforWhenStarting displayInforWhenStarting, GeneralDate appDate, String appReason, Integer appStandardReasonCD) {
		//「INPUT．振休日_NEW = INPUT．振休振出申請起動時の表示情報．申請表示情報．申請詳細画面情報．申請(振休)．申請日」がtrue
		if(appDate.equals(displayInforWhenStarting.abs.application.toDomain().getAppDate().getApplicationDate())) {
			throw new BusinessException("Msg_1683");
		}
		displayInforWhenStarting.abs.application.setAppDate(appDate.toString());
		displayInforWhenStarting.abs.application.setOpAppStandardReasonCD(appStandardReasonCD);
		displayInforWhenStarting.abs.application.setOpAppReason(appReason);
	
		AbsenceLeaveApp abs = displayInforWhenStarting.abs.toDomainInsertAbs();
		Optional<RecruitmentApp> rec = Optional.empty();
		if(displayInforWhenStarting.existRec()) {
			rec = recruitmentAppRepository.findByAppId(displayInforWhenStarting.rec.application.getAppID());
		}
	
		preRegistrationErrorCheck.applicationDateRelatedCheck(companyId, Optional.of(abs), rec, displayInforWhenStarting.appDispInfoStartup.getAppDispInfoWithDateOutput().getEmpHistImport().getEmploymentCode());
		
		errorCheckProcessingBeforeRegistrationKAF011.processing(companyId, Optional.of(abs), rec, displayInforWhenStarting.represent, 
				displayInforWhenStarting.appDispInfoStartup.toDomain().getAppDispInfoWithDateOutput().getOpErrorFlag().orElse(ErrorFlagImport.NO_ERROR), 
				displayInforWhenStarting.appDispInfoStartup.toDomain().getAppDispInfoWithDateOutput().getOpActualContentDisplayLst().orElse(new ArrayList<ActualContentDisplay>()), 
				displayInforWhenStarting.appDispInfoStartup.toDomain());
		return abs;
	}
	
	/**
	 * @name 振休日変更時の登録処理
	 * @param companyId 会社ID
	 * @param displayInforWhenStarting 振休振出申請起動時の表示情報
	 * @param absNew 振休申請_NEW：申請
	 */
	public void registerProcess(String companyId, DisplayInforWhenStarting displayInforWhenStarting, AbsenceLeaveApp absNew) {
		//ドメイン「振休申請」を1件更新する
		Optional<AbsenceLeaveApp> absOld = absenceLeaveAppRepository.findByAppId(displayInforWhenStarting.abs.application.getAppID());
		if(absOld.isPresent()) {
			absOld.get().getApplication().getReflectionStatus().getListReflectionStatusOfDay().get(0).setActualReflectStatus(ReflectedState.CANCELED);
			absOld.get().getApplication().getReflectionStatus().getListReflectionStatusOfDay().get(0).setScheReflectStatus(ReflectedState.CANCELED);
			absenceLeaveAppRepository.update(absOld.get());
		}
		Optional<RecruitmentApp> rec = Optional.empty();
		//ドメイン「振出申請」を1件更新する
		if (displayInforWhenStarting.existRec()) {
			rec = recruitmentAppRepository.findByAppId(displayInforWhenStarting.rec.application.getAppID());
			if(rec.isPresent()) {
				rec.get().getApplication().getReflectionStatus().getListReflectionStatusOfDay().get(0).setActualReflectStatus(ReflectedState.CANCELED);
				rec.get().getApplication().getReflectionStatus().getListReflectionStatusOfDay().get(0).setScheReflectStatus(ReflectedState.CANCELED);
				recruitmentAppRepository.update(rec.get());
			}
		}
		//暫定データの登録
		interimRemainDataMngRegisterDateChange.registerDateChange(companyId, displayInforWhenStarting.abs.application.getEmployeeID(), Arrays.asList(displayInforWhenStarting.abs.application.toDomain().getAppDate().getApplicationDate()));
		//振出振休紐付け管理を作り直す
		LinkingManagementInforDto linkingManagementInfor = this.recreateTheTieUpManagement(absNew.getAppDate().getApplicationDate(), displayInforWhenStarting.substituteManagement, displayInforWhenStarting.holidayManage, displayInforWhenStarting.abs.leaveComDayOffMana, displayInforWhenStarting.abs.payoutSubofHDManagements);
		//振休振出申請（新規）登録処理
		saveHolidayShipmentCommandHandlerRef5.registrationApplicationProcess(companyId, Optional.of(absNew), rec, 
				displayInforWhenStarting.appDispInfoStartup.getAppDispInfoWithDateOutput().toDomain().getBaseDate(), 
				displayInforWhenStarting.appDispInfoStartup.getAppDispInfoNoDateOutput().isMailServerSet(), 
				displayInforWhenStarting.appDispInfoStartup.toDomain().getAppDispInfoWithDateOutput().getOpListApprovalPhaseState().orElse(new ArrayList<>()), 
				displayInforWhenStarting.existRec() ? displayInforWhenStarting.rec.leaveComDayOffMana.stream().map(c->c.toDomain()).collect(Collectors.toList()) : new ArrayList<>(), 
				linkingManagementInfor.absLeaveComDayOffMana.stream().map(c->c.toDomain()).collect(Collectors.toList()), 
				linkingManagementInfor.absPayoutSubofHDManagements.stream().map(c->c.toDomain()).collect(Collectors.toList()), 
				EnumAdaptor.valueOf(displayInforWhenStarting.holidayManage, ManageDistinct.class), 
				displayInforWhenStarting.appDispInfoStartup.toDomain().getAppDispInfoNoDateOutput().getApplicationSetting());
	}
	
	/**
	 * @name 振出振休紐付け管理を作り直す
	 * @param abs
	 * @param substituteManagement 代休紐付け管理区分
	 * @param holidayManage 振休紐付け管理区分
	 * @param leaveComDayOffMana 休出代休紐付け管理<List>
	 * @param payoutSubofHDManagements 振出振休紐付け管理<List>
	 */
	public LinkingManagementInforDto recreateTheTieUpManagement(GeneralDate absDate, Integer substituteManagement, Integer holidayManage, List<LeaveComDayOffManaDto> leaveComDayOffMana, List<PayoutSubofHDManagementDto> payoutSubofHDManagements) {
		if(substituteManagement == 1) {
			if(leaveComDayOffMana != null) {
				for (LeaveComDayOffManaDto item : leaveComDayOffMana) {
					//ドメインモデル「休出代休紐付け管理」を削除する
					leaveComDayOffManaRepository.delete(item.getSid(), item.getOutbreakDay(), item.getDateOfUse());
					//INPUT．休出代休紐付け管理．使用日＝INPUT．申請日_NEW
					item.setDateOfUse(absDate);
				}
			}
		}
		if(holidayManage == 1) {
			if(payoutSubofHDManagements != null) {
				for (PayoutSubofHDManagementDto item : payoutSubofHDManagements) {
					//ドメインモデル「休出代休紐付け管理」を削除する
					payoutSubofHDManaRepository.delete(item.getSid(), item.getOutbreakDay(), item.getDateOfUse());
					//INPUT．休出代休紐付け管理．使用日＝INPUT．申請日_NEW
					item.setDateOfUse(absDate);
				}
			}
		}
		return new LinkingManagementInforDto(new ArrayList<>(), leaveComDayOffMana, payoutSubofHDManagements);
	}
	
	
}
