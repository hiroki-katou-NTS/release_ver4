package nts.uk.ctx.at.request.infra.entity.setting.company.vacationapplicationsetting;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KRQST_HD_APP_SET")
public class KrqstHdAppSet extends UkJpaEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	public KrqstHdAppSetPK krqstHdAppSetPK;
	
	/** 60H超休を利用する名 */
	@Column(name = "USE_60H_HOLIDAY")
	public int use60h;
	
	/** 代休名称  */
	@Column(name = "OBSTACLE_NAME")
	public String obstacleName;
	
	/** 代休残数不足登録できる */
	@Column(name = "REGIS_SHORT_LOST_HD")
	public int regisShortLostHd;
	
	/** 休日名称 */
	@Column(name = "HD_NAME")
	public String hdName;
	
	/** 公休残数不足登録できる  */
	@Column(name = "REGIS_LACK_PUBLIC_HD")
	public int regisLackPubHd;
	
	/** 勤務時間を変更できる */
	@Column(name = "CHANGE_WRK_HOURS")
	public int changeWrkHour;
	
	/** 半日年休の使用上限チェック */
	@Column(name = "CKUPER_LIMIT_HALFDAY_HD")
	public int ckuperLimit;
	
	/** 実績表示区分 */
	@Column(name = "ACTUAL_DISPLAY_ATR")
	public int actualDisp;
	
	/** 就業時間帯利用区分 */
	@Column(name = "WRK_HOURS_USE_ATR")
	public int wrkHours;
	
	/** 年休より優先消化チェック区分 */
	@Column(name = "PRIORITY_DIGESTION_ATR")
	public int pridigCheck;
	
	/** 年休名称  */
	@Column(name = "YEAR_HD_NAME")
	public String yearHdName;
	
	/** 年休残数不足登録できる  */
	@Column(name = "REGIS_NUM_YEAR_OFF")
	public int regisNumYear;
	
	/** 振休名称 */
	@Column(name = "FURIKYU_NAME")
	public String furikyuName;
	
	/** 振休残数不足登録できる */
	@Column(name = "REGIS_INSUFF_NUMOFREST")
	public int regisInsuff;
	
	/** 時間代休を利用する */
	@Column(name = "USE_TIME_GENER_HD")
	public int useGener;
	
	/** 時間年休を利用する */
	@Column(name = "USE_TIME_YEAR_HD")
	public int useYear;
	
	/** 時間消化名称 */
	@Column(name = "TIME_DIGEST_NAME")
	public String timeDigest;
	
	/** 欠勤名称 */
	@Column(name = "ABSENTEEISM_NAME")
	public String absenteeism;
	
	/** 法内法外の矛盾チェック */
	@Column(name = "CONCHECK_OUTLEGAL_LAW")
	public int concheckOutLegal;
	
	/** 特別休暇名称 */
	@Column(name = "SPECIAL_VACATION_NAME")
	public String specialVaca;
	
	/** 申請対象日が振出日の場合の矛盾チェック */
	@Column(name = "CONCHECK_DATE_RELEASEDATE")
	public int concheckDateRelease;
	
	/** 申請日矛盾区分 */
	@Column(name = "APPLI_DATE_CONTRA_ATR")
	public int appDateContra;
	
	/** 積立年休名称 */
	@Column(name = "YEAR_RESIG_NAME")
	public String yearResig;
	
	/** 積立年休残数不足登録できる  */
	@Column(name = "REGIS_SHORT_RESER_YEAR")
	public int regisShortReser;
	
	/** 休暇種類  */
	@Column(name = "HD_APPTYPE")
	public int hdType;
	
	/** 未選択を表示する  */
	@Column(name = "DISPLAY_UNSELECT")
	public int displayUnselect;
	
	/** 終日・半日選択表示区分 */
	@Column(name = "DAY_DISP_SET")
	public int dayDispSet;
	
	@Override
	protected Object getKey() {
		return krqstHdAppSetPK;
	}
}
