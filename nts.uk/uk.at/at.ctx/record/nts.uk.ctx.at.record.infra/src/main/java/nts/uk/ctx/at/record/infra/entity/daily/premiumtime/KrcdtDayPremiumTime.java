package nts.uk.ctx.at.record.infra.entity.daily.premiumtime;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import nts.arc.layer.infra.data.jdbc.JdbcProxy;
import nts.arc.layer.infra.data.jdbc.map.JpaEntityMapper;
import nts.arc.time.GeneralDate;
import nts.gul.reflection.FieldReflection;
import nts.uk.ctx.at.record.dom.premiumtime.PremiumTime;
import nts.uk.ctx.at.record.dom.premiumtime.PremiumTimeOfDailyPerformance;
//import nts.uk.ctx.at.record.infra.entity.daily.actualworktime.KrcdtDayAttendanceTime;
import nts.uk.ctx.at.record.infra.entity.daily.time.KrcdtDayTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Entity
@Table(name = "KRCDT_DAY_PREMIUM_TIME")
public class KrcdtDayPremiumTime extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final JpaEntityMapper<KrcdtDayPremiumTime> MAPPER = new JpaEntityMapper<>(KrcdtDayPremiumTime.class);
	
	public static class Query {
		public static List<KrcdtDayPremiumTime> find(JdbcProxy proxy, KrcdtDayTime.Query.Key key) {
			return KrcdtDayTime.Query.query(proxy, "KRCDT_DAY_PREMIUM_TIME", key, MAPPER);
		}
	}

	public KrcdtDayTime.Query.RecordKey getRecordKey() {
		return new KrcdtDayTime.Query.RecordKey(this.krcdtDayPremiumTimePK.employeeID, this.krcdtDayPremiumTimePK.generalDate);
	}
	
	
	/* 主キー */
	@EmbeddedId
	public KrcdtDayPremiumTimePK krcdtDayPremiumTimePK;
	
	/* 割増時間1 */
	@Column(name = "PREMIUM_TIME1")
	public int premiumTime1;
	/* 割増時間2 */
	@Column(name = "PREMIUM_TIME2")
	public int premiumTime2;
	/* 割増時間3 */
	@Column(name = "PREMIUM_TIME3")
	public int premiumTime3;
	/* 割増時間4 */
	@Column(name = "PREMIUM_TIME4")
	public int premiumTime4;
	/* 割増時間5 */
	@Column(name = "PREMIUM_TIME5")
	public int premiumTime5;
	/* 割増時間6 */
	@Column(name = "PREMIUM_TIME6")
	public int premiumTime6;
	/* 割増時間7 */
	@Column(name = "PREMIUM_TIME7")
	public int premiumTime7;
	/* 割増時間8 */
	@Column(name = "PREMIUM_TIME8")
	public int premiumTime8;
	/* 割増時間9 */
	@Column(name = "PREMIUM_TIME9")
	public int premiumTime9;
	/* 割増時間10 */
	@Column(name = "PREMIUM_TIME10")
	public int premiumTime10;
	
	@OneToOne(mappedBy="KrcdtDayPremiumTime", fetch = FetchType.LAZY)
	//public KrcdtDayAttendanceTime krcdtDayAttendanceTime;
	public KrcdtDayTime krcdtDayTime;
	
	@Override
	protected Object getKey() {
		return this.krcdtDayPremiumTimePK;
	}
	
	public PremiumTimeOfDailyPerformance toDomain() {	
		List<PremiumTime> premiumTimeList = new ArrayList<>();
		premiumTimeList.add(new PremiumTime(1, new AttendanceTime(this.premiumTime1)));
		premiumTimeList.add(new PremiumTime(2, new AttendanceTime(this.premiumTime2)));
		premiumTimeList.add(new PremiumTime(3, new AttendanceTime(this.premiumTime3)));
		premiumTimeList.add(new PremiumTime(4, new AttendanceTime(this.premiumTime4)));
		premiumTimeList.add(new PremiumTime(5, new AttendanceTime(this.premiumTime5)));
		premiumTimeList.add(new PremiumTime(6, new AttendanceTime(this.premiumTime6)));
		premiumTimeList.add(new PremiumTime(7, new AttendanceTime(this.premiumTime7)));
		premiumTimeList.add(new PremiumTime(8, new AttendanceTime(this.premiumTime8)));
		premiumTimeList.add(new PremiumTime(9, new AttendanceTime(this.premiumTime9)));
		premiumTimeList.add(new PremiumTime(10, new AttendanceTime(this.premiumTime10)));
		return new PremiumTimeOfDailyPerformance(premiumTimeList);
	}
	
	public void setData(PremiumTimeOfDailyPerformance domain) {
		if(domain == null || domain.getPremiumTimes() == null || domain.getPremiumTimes().isEmpty() )
			return;
		//値のセットループ
		for(int loopNumber = 1 ; loopNumber <= 10 ; loopNumber++) {
			Optional<PremiumTime> premiumTime = domain.getPremiumTime(loopNumber);
			//自分自身の値セット先(フィールド)取得
			Field field = FieldReflection.getField(this.getClass(), "premiumTime" + loopNumber);		
			if(premiumTime.isPresent()&&premiumTime.get().getPremitumTime()!=null) {
				//値セット
				FieldReflection.setField(field, this, premiumTime.get().getPremitumTime().valueAsMinutes());
			}else {
				//値セット
				FieldReflection.setField(field, this, 0);
			}
		}
	}
	
	public static KrcdtDayPremiumTime totoEntity(String employeeId,GeneralDate targetDate,PremiumTimeOfDailyPerformance domain) {
		KrcdtDayPremiumTime entity = new KrcdtDayPremiumTime();
		/*主キーセット*/
		entity.krcdtDayPremiumTimePK = new KrcdtDayPremiumTimePK(employeeId, targetDate);
		/*データセット*/
		entity.setData(domain);
		return entity;
	}
	
	
}
