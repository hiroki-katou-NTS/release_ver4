package nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.vacationapplicationsetting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.gul.text.StringUtil;
import nts.uk.ctx.at.request.dom.application.appabsence.HolidayAppType;
/**
 * 休暇申請設定
 * @author yennth
 *
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HdAppSet extends AggregateRoot {
	// 会社ID
	private String companyId;
	// 60H超休を利用する
	private UseAtr use60h;
	// 代休名称
	private ObstacleName obstacleName;
	// 代休残数不足登録できる
	private UseAtr regisShortLostHd;
	// 休日名称
	private ObstacleName hdName;
	// 公休残数不足登録できる
	private UseAtr regisLackPubHd;
	// 勤務時間を変更できる
	private UseAtr changeWrkHour;
	// 半日年休の使用上限チェック
	private AppliedDate ckuperLimit;
	// 実績表示区分
	private UseAtr actualDisp;
	// 就業時間帯利用区分
	private WorkUse wrkHours;
	// 年休より優先消化チェック区分
	private AppliedDate pridigCheck;
	// 年休名称
	private ObstacleName yearHdName;
	// 年休残数不足登録できる
	private UseAtr regisNumYear;
	// 振休名称
	private ObstacleName furikyuName;
	// 振休残数不足登録できる
	private UseAtr regisInsuff;
	// 時間代休を利用する
	private UseAtr useGener;
	// 時間年休を利用する
	private UseAtr useYear;
	// 時間消化名称
	private ObstacleName timeDigest;
	// 欠勤名称
	private ObstacleName absenteeism;
	// 法内法外の矛盾チェック
	private CheckUper concheckOutLegal;
	// 特別休暇名称
	private ObstacleName specialVaca;
	// 申請対象日が振出日の場合の矛盾チェック
	private AppliedDate concheckDateRelease;
	// 申請日矛盾区分
	private AppliedDate appDateContra;
	// 積立年休名称 
	private ObstacleName yearResig;
	// 積立年休残数不足登録できる
	private UseAtr regisShortReser;
	// 休暇種類
	private HolidayAppType hdType;
	// 未選択を表示する
	private UseAtr displayUnselect;
	// 終日・半日選択表示区分
	private UseAtr dayDispSet;
	
	
	public static HdAppSet createFromJavaType(String companyId,
												int use60h, String obstacleName, int regisShortLostHd, 
												String hdName, int regisLackPubHd, int changeWrkHour, 
												int ckuperLimit, int actualDisp, int wrkHours, int pridigCheck, 
												String yearHdName, int regisNumYear, String furikyuName, int regisInsuff, 
												int useGener, int useYear, String timeDigest, String absenteeism, 
												int concheckOutLegal, String specialVaca, int concheckDateRelease, 
												int appDateContra, String yearResig, int regisShortReser, int hdType, int displayUnselect, int dayDispSet){
		return new HdAppSet(companyId, 
				EnumAdaptor.valueOf(use60h, UseAtr.class), 
				StringUtil.isNullOrEmpty(obstacleName, true) ? null : new ObstacleName(obstacleName), 
				EnumAdaptor.valueOf(regisShortLostHd, UseAtr.class), 
				StringUtil.isNullOrEmpty(hdName, true) ? null : new ObstacleName(hdName), 
				EnumAdaptor.valueOf(regisLackPubHd, UseAtr.class), 
				EnumAdaptor.valueOf(changeWrkHour, UseAtr.class), 
				EnumAdaptor.valueOf(ckuperLimit, AppliedDate.class), 
				EnumAdaptor.valueOf(actualDisp, UseAtr.class), 
				EnumAdaptor.valueOf(wrkHours, WorkUse.class), 
				EnumAdaptor.valueOf(pridigCheck, AppliedDate.class), 
				StringUtil.isNullOrEmpty(yearHdName, true) ? null : new ObstacleName(yearHdName), 
				EnumAdaptor.valueOf(regisNumYear, UseAtr.class), 
				StringUtil.isNullOrEmpty(furikyuName, true) ? null : new ObstacleName(furikyuName), 
				EnumAdaptor.valueOf(regisInsuff, UseAtr.class), 
				EnumAdaptor.valueOf(useGener, UseAtr.class), 
				EnumAdaptor.valueOf(useYear, UseAtr.class), 
				StringUtil.isNullOrEmpty(timeDigest, true) ? null : new ObstacleName(timeDigest), 
				StringUtil.isNullOrEmpty(absenteeism, true) ? null : new ObstacleName(absenteeism), 
				EnumAdaptor.valueOf(concheckOutLegal, CheckUper.class), 
				StringUtil.isNullOrEmpty(specialVaca, true) ? null : new ObstacleName(specialVaca), 
				EnumAdaptor.valueOf(concheckDateRelease, AppliedDate.class), 
				EnumAdaptor.valueOf(appDateContra, AppliedDate.class), 
				StringUtil.isNullOrEmpty(yearResig, true) ? null : new ObstacleName(yearResig), 
				EnumAdaptor.valueOf(regisShortReser, UseAtr.class),
				EnumAdaptor.valueOf(hdType, HolidayAppType.class),
				EnumAdaptor.valueOf(displayUnselect, UseAtr.class),
				EnumAdaptor.valueOf(dayDispSet, UseAtr.class));
	}
}
