package nts.uk.screen.com.app.smm.smm001.screenquery;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.uk.ctx.exio.app.find.exi.condset.StdAcceptCondSetDto;
import nts.uk.ctx.exio.app.find.exi.condset.StdAcceptCondSetFinder;
//import nts.uk.ctx.exio.dom.exi.condset.StdAcceptCondSetRepository;
import nts.uk.ctx.exio.dom.exi.condset.SystemType;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.smile.dom.smilelinked.cooperationacceptance.SmileCooperationAcceptanceSetting;
import nts.uk.smile.dom.smilelinked.cooperationacceptance.SmileCooperationAcceptanceSettingRepository;
//import nts.uk.smile.dom.smilelinked.cooperationoutput.SmileLinkageOutputSettingRepository;

/**
 * Smile初期起動の情報取得する
 *
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class GetInitialStartupInformationScreenQuery {

	// Smile連携受入設定
	@Inject
	private SmileCooperationAcceptanceSettingRepository smileCooperationAcceptanceSettingRepository;
	
	@Inject
	private StdAcceptCondSetFinder acceptCondSetFinder;

	public OutputOfStartupDto get() {
		/**
		 * Function: 会社IDを指定してSM連携受入設定を取得する - Specify the company ID to get the SM linkage acceptance settings
		 * Input: 契約コード、会社ID -Contract code, company ID
		 * Return : List＜SM連携受入設定＞ - List <SM cooperation acceptance setting>
		 */
		String contractCode = AppContexts.user().contractCode();
		String companyId = AppContexts.user().companyId();
		List<SmileCooperationAcceptanceSetting> acceptanceSettings = smileCooperationAcceptanceSettingRepository
				.get(contractCode, companyId);
		List<SmileCooperationAcceptanceSettingDto> smileCooperationAcceptanceSettingDtos = acceptanceSettings.stream()
				.map(item -> new SmileCooperationAcceptanceSettingDto(item.getCooperationAcceptance().value,
						item.getCooperationAcceptanceClassification().value,
						item.getCooperationAcceptanceConditions().isPresent()
								? item.getCooperationAcceptanceConditions().get().v()
								: null))
				.collect(Collectors.toList());
		
		/**
		 * Function: システム区分から受入条件設定（定型）を取得 - Acquire acceptance condition setting (standard) from system classification 
		 * Input: 会社ID、システム種類 - Company ID, system type
		 * Return: List＜受入条件設定（定型）＞ - List <Acceptance condition setting
		 * (standard)>
		 */
		Integer sysType = SystemType.ATTENDANCE_SYSTEM.value;
		List<StdAcceptCondSetDto> stdAcceptCondSetList = this.acceptCondSetFinder
				.getStdAcceptCondSetBySysType(sysType);
		
		/**
		 * return SM linkage acceptance setting list, acceptance condition setting list
		 */
		return new OutputOfStartupDto(smileCooperationAcceptanceSettingDtos, stdAcceptCondSetList);
	}
}
