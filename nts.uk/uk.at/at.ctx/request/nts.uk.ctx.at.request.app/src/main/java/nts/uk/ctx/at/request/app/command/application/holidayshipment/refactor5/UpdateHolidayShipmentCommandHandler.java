package nts.uk.ctx.at.request.app.command.application.holidayshipment.refactor5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.request.app.command.application.holidayshipment.refactor5.dto.HolidayShipmentRefactor5Command;
import nts.uk.ctx.at.request.app.find.application.ApplicationDto;
import nts.uk.ctx.at.request.dom.application.Application;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository;
import nts.uk.ctx.at.request.dom.application.appabsence.service.AbsenceServiceProcess;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.after.DetailAfterUpdate;
import nts.uk.ctx.at.request.dom.application.common.service.setting.output.AppDispInfoStartupOutput;
import nts.uk.ctx.at.request.dom.application.holidayshipment.absenceleaveapp.AbsenceLeaveApp;
import nts.uk.ctx.at.request.dom.application.holidayshipment.absenceleaveapp.AbsenceLeaveAppRepository;
import nts.uk.ctx.at.request.dom.application.holidayshipment.recruitmentapp.RecruitmentApp;
import nts.uk.ctx.at.request.dom.application.holidayshipment.recruitmentapp.RecruitmentAppRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.InterimRemainDataMngRegisterDateChange;
import nts.uk.ctx.at.shared.dom.remainingnumber.paymana.PayoutSubofHDManagement;
import nts.uk.ctx.at.shared.dom.remainingnumber.subhdmana.LeaveComDayOffManagement;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeUnit;
import nts.uk.shr.com.context.AppContexts;

/**
 * @author thanhpv
 * UKDesign.UniversalK.就業.KAF_申請.KAF011_振休振出申請.B：振休振出申請（詳細）.ユースケース.登録する.登録する
 */
@Stateless
public class UpdateHolidayShipmentCommandHandler {

	@Inject
	private PreUpdateErrorCheck preUpdateErrorCheck;
	
	@Inject
	private ApplicationRepository applicationRepository;
	
	@Inject
	private RecruitmentAppRepository recruitmentAppRepository;
	
	@Inject
	private AbsenceServiceProcess absenceServiceProcess;
	
	@Inject
	private AbsenceLeaveAppRepository absenceLeaveAppRepository;
	
	@Inject
	private InterimRemainDataMngRegisterDateChange interimRemainDataMngRegisterDateChange;
	
	@Inject
	private DetailAfterUpdate detailAfterUpdate;
	
	@Inject
	private WorkTypeRepository workTypeRepo;
	
	/**
	 * @name 登録する
	 */
	public void update(HolidayShipmentRefactor5Command command){
		String companyId = AppContexts.user().companyId();
		
		Optional<AbsenceLeaveApp> abs = Optional.empty(); 
		if(command.existAbs()) {
			Optional<Application> application = applicationRepository.findByID(command.abs.appID);
			if(application.isPresent()) {
				abs = Optional.of(command.abs.toDomainUpdate(ApplicationDto.fromDomain(application.get())));
			}
		}
		Optional<RecruitmentApp> rec = Optional.empty();
		if(command.existRec()) {
			Optional<Application> application = applicationRepository.findByID(command.rec.appID);
			if(application.isPresent()) {
				rec = Optional.of(command.rec.toDomainUpdate(ApplicationDto.fromDomain(application.get())));
			}
		}
		//振休振出申請（詳細）登録前のチェック(PreUpdateErrorCheck.errorCheck())
		this.preUpdateErrorCheck.errorCheck(companyId, abs, rec, command.displayInforWhenStarting);
		
		//アルゴリズム「振休振出申請の更新登録」を実行する
		this.updateApplicationProcess(
				companyId, 
				rec, 
				abs, 
				rec.isPresent()? command.rec.leaveComDayOffManaOld.stream().map(c->c.toDomain()).collect(Collectors.toList()): new ArrayList<>(), 
				rec.isPresent()? command.rec.leaveComDayOffMana.stream().map(c->c.toDomain()).collect(Collectors.toList()): new ArrayList<>(), 
				abs.isPresent()? command.abs.leaveComDayOffManaOld.stream().map(c->c.toDomain()).collect(Collectors.toList()): new ArrayList<>(), 
				abs.isPresent()? command.abs.payoutSubofHDManagementsOld.stream().map(c->c.toDomain()).collect(Collectors.toList()): new ArrayList<>(),
				abs.isPresent()? command.abs.leaveComDayOffMana.stream().map(c->c.toDomain()).collect(Collectors.toList()): new ArrayList<>(), 
				abs.isPresent()? command.abs.payoutSubofHDManagements.stream().map(c->c.toDomain()).collect(Collectors.toList()): new ArrayList<>(),
				command.displayInforWhenStarting.appDispInfoStartup.toDomain());
	}
	
	/**
	 * @name 振休振出申請（新規）登録処理 (Xử lý đăng ký application nghỉ bù làm bù (New))
	 * @param companyId 申請者会社ID
	 * @param rec 振出申請
	 * @param abs 振休申請
	 * @param leaveComDayOffMana_Rec_Old 振出_古いの休出代休紐付け管理
	 * @param leaveComDayOffMana_Rec 振出_休出代休紐付け管理
	 * @param leaveComDayOffMana_Abs_Old 振休_古いの休出代休紐付け管理
	 * @param payoutSubofHDManagement_Abs_Old 振休_古いの振出振休紐付け管理
	 * @param leaveComDayOffMana_Abs 振休_休出代休紐付け管理
	 * @param payoutSubofHDManagement_Abs 振休_振出振休紐付け管理
	 * @param appDispInfoStartup 申請表示情報  -> http://192.168.50.4:3000/issues/113502 -> done
	 */
	public void updateApplicationProcess(String companyId, Optional<RecruitmentApp> rec, Optional<AbsenceLeaveApp> abs, 
			List<LeaveComDayOffManagement> leaveComDayOffMana_Rec_Old, List<LeaveComDayOffManagement> leaveComDayOffMana_Rec,
			List<LeaveComDayOffManagement> leaveComDayOffMana_Abs_Old, List<PayoutSubofHDManagement> payoutSubofHDManagement_Abs_Old,
			List<LeaveComDayOffManagement> leaveComDayOffMana_Abs, List<PayoutSubofHDManagement> payoutSubofHDManagement_Abs,
			AppDispInfoStartupOutput appDispInfoStartup) {
		if(rec.isPresent()){
			//ドメイン「振出申請」を1件更新する
			recruitmentAppRepository.update(rec.get());
			//休暇紐付け管理を更新する 
			absenceServiceProcess.updateVacationLinkManage(leaveComDayOffMana_Rec_Old, new ArrayList<>(), leaveComDayOffMana_Rec, new ArrayList<>());
			//暫定データの登録(đăng ký data tạm thời)
			interimRemainDataMngRegisterDateChange.registerDateChange(companyId, rec.get().getEmployeeID(), Arrays.asList(rec.get().getAppDate().getApplicationDate()));
			//アルゴリズム「詳細画面登録後の処理」を実行する
			detailAfterUpdate.processAfterDetailScreenRegistration(companyId, rec.get().getAppID(), appDispInfoStartup);
			
		}
		if(abs.isPresent()){
			//ドメイン「振休申請」を1件更新する
			absenceLeaveAppRepository.update(abs.get());
			if(!rec.isPresent()) {
				//休暇紐付け管理を更新する
				absenceServiceProcess.updateVacationLinkManage(leaveComDayOffMana_Abs_Old, payoutSubofHDManagement_Abs_Old, leaveComDayOffMana_Abs, payoutSubofHDManagement_Abs);
			}else {
				//振休振出同時登録時紐付け管理を更新する
				List<PayoutSubofHDManagement> payoutSubofHDManagement_Abs_New = this.registerTheLinkManagement(companyId, abs.get(), payoutSubofHDManagement_Abs);
				//休暇紐付け管理を更新する
				absenceServiceProcess.updateVacationLinkManage(leaveComDayOffMana_Abs_Old, payoutSubofHDManagement_Abs_Old, leaveComDayOffMana_Abs, payoutSubofHDManagement_Abs_New);
			}
			//暫定データの登録(đăng ký data tạm thời)
			interimRemainDataMngRegisterDateChange.registerDateChange(companyId, abs.get().getEmployeeID(), Arrays.asList(abs.get().getAppDate().getApplicationDate()));
			//アルゴリズム「詳細画面登録後の処理」を実行する
			detailAfterUpdate.processAfterDetailScreenRegistration(companyId, abs.get().getAppID(), appDispInfoStartup);
			
		}
	}
	
	/**
	 * @name 振休振出同時登録時紐付け管理を更新する
	 * @param companyId 会社ID
	 * @param abs 振休申請
	 * @param payoutSubofHDManagement_Abs 振休_振出振休紐付け管理
	 */
	public List<PayoutSubofHDManagement> registerTheLinkManagement(String companyId, AbsenceLeaveApp abs, List<PayoutSubofHDManagement> payoutSubofHDManagement_Abs) {
		if(payoutSubofHDManagement_Abs.isEmpty()) {
			return new ArrayList<>();
		}else {
			//<<Public>> 指定した勤務種類をすべて取得する
			Optional<WorkType> workTypes = workTypeRepo.findByDeprecated(companyId, abs.getWorkInformation().getWorkTypeCode().v());;
			if(workTypes.isPresent()) {
				return payoutSubofHDManagement_Abs.stream().map(c-> {
					return new PayoutSubofHDManagement(
							c.getSid(), 
							c.getAssocialInfo().getOutbreakDay(), 
							c.getAssocialInfo().getDateOfUse(),
							workTypes.get().getDailyWork().getWorkTypeUnit() == WorkTypeUnit.OneDay ? 1.0 : 0.5,
							c.getAssocialInfo().getTargetSelectionAtr().value);
				}).collect(Collectors.toList());
			}
			return new ArrayList<>();
		}
	}
}
