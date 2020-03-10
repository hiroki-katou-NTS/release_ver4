package nts.uk.ctx.at.request.app.find.setting.company.vacationapplicationsetting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.vacationapplicationsetting.HdAppSet;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class HdAppSetDto {
	/** 会社ID **/
	public String companyId;
	
	/** 60H超休を利用する名 */
	public int use60h;
	
	/** 代休名称  */
	public String obstacleName;
	
	/** 代休残数不足登録できる */
	public int regisShortLostHd;
	
	/** 休日名称 */
	public String hdName;
	
	/** 公休残数不足登録できる  */
	public int regisLackPubHd;
	
	/** 勤務時間を変更できる */
	public int changeWrkHour;
	
	/** 半日年休の使用上限チェック */
	public int ckuperLimit;
	
	/** 実績表示区分 */
	public int actualDisp;
	
	/** 就業時間帯利用区分 */
	public int wrkHours;
	
	/** 年休より優先消化チェック区分 */
	public int pridigCheck;
	
	/** 年休名称  */
	public String yearHdName;
	
	/** 年休残数不足登録できる  */
	public int regisNumYear;
	
	/** 振休名称 */
	public String furikyuName;
	
	/** 振休残数不足登録できる */
	public int regisInsuff;
	
	/** 時間代休を利用する */
	public int useGener;
	
	/** 時間年休を利用する */
	public int useYear;
	
	/** 時間消化名称 */
	public String timeDigest;
	
	/** 欠勤名称 */
	public String absenteeism;
	
	/** 法内法外の矛盾チェック */
	public int concheckOutLegal;
	
	/** 特別休暇名称 */
	public String specialVaca;
	
	/** 申請対象日が振出日の場合の矛盾チェック */
	public int concheckDateRelease;
	
	/** 申請日矛盾区分 */
	public int appDateContra;
	
	/** 積立年休名称 */
	public String yearResig;
	
	/** 積立年休残数不足登録できる */
	public int regisShortReser;
	
	// 未選択を表示する
	public int displayUnselect;
	
	// 終日・半日選択表示区分
	public int dayDispSet;
	
	public static HdAppSetDto convertToDto(HdAppSet domain){
		return new HdAppSetDto(domain.getCompanyId(), 
				domain.getUse60h().value, domain.getObstacleName() == null ? null : domain.getObstacleName().v(), 
				domain.getRegisShortLostHd().value, 
				domain.getHdName() == null ? null : domain.getHdName().v(), domain.getRegisLackPubHd().value,
				domain.getChangeWrkHour().value, domain.getCkuperLimit().value, domain.getActualDisp().value, 
				domain.getWrkHours().value, domain.getPridigCheck().value, 
				domain.getYearHdName() == null ? null : domain.getYearHdName().v(), 
				domain.getRegisNumYear().value, 
				domain.getFurikyuName() == null ? null : domain.getFurikyuName().v(), domain.getRegisInsuff().value, 
				domain.getUseGener().value, domain.getUseYear().value, 
				domain.getTimeDigest() == null ? null : domain.getTimeDigest().v(), 
				domain.getAbsenteeism() == null ? null : domain.getAbsenteeism().v(), 
				domain.getConcheckOutLegal().value, 
				domain.getSpecialVaca() == null ? null : domain.getSpecialVaca().v(), 
				domain.getConcheckDateRelease().value, domain.getAppDateContra().value, 
				domain.getYearResig() == null ? null : domain.getYearResig().v(),
				domain.getRegisShortReser().value,
				domain.getDisplayUnselect().value,
				domain.getDayDispSet().value);
	}
}
