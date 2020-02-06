package nts.uk.ctx.at.request.app.command.setting.company.vacationapplicationsetting;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HdAppSetCommand {
	
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
	
	// 休暇種類
	public int hdType;
	// 未選択を表示する
	public int displayUnselect;
	
	// 終日・半日選択表示区分
	public int dayDispSet;
}
