package nts.uk.ctx.at.shared.dom.specialholiday.grantinformation;

import java.util.Optional;

import lombok.AllArgsConstructor;
//import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.at.shared.dom.specialholiday.SpecialHolidayCode;

/**
 * 付与・期限情報
 * @author masaaki_jinno
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GrantRegular extends DomainObject {
	
	/** 付与するタイミングの種類 */
	private TypeTime typeTime;
	
	/** 付与基準日 */
	private Optional<GrantDate> grantDate;
	
	/** 指定日付与 */
	private FixGrantDate fixGrantDate;
	
	/** 付与日テーブル参照付与 */
	
	
	/** 期間付与 */
	private PeriodGrantDate periodGrantDate;
	
	
	
	
	
	
	
	/** 会社ID */
	private String companyId;
	
	/** 特別休暇コード */
	private SpecialHolidayCode specialHolidayCode;
	
	/** 取得できなかった端数は消滅する */
	private boolean allowDisappear;
	
	/** 取得できなかった端数は消滅する */
	private GrantTime grantTime;
	
	@Override
	public void validate() {
		super.validate();
	}
	
//	/**
//	 * Create from Java Type
//	 * 
//	 * @param companyId
//	 * @param specialHolidayCode
//	 * @param typeTime
//	 * @param grantDate
//	 * @param allowDisappear
//	 * @param grantTime
//	 * @return
//	 */
//	public static GrantRegular createFromJavaType(String companyId, int specialHolidayCode, int typeTime, int grantDate, boolean allowDisappear, GrantTime grantTime) {
//		return new GrantRegular(companyId, 
//				new SpecialHolidayCode(specialHolidayCode),
//				EnumAdaptor.valueOf(typeTime, TypeTime.class),
//				EnumAdaptor.valueOf(grantDate, GrantDate.class),
//				allowDisappear,
//				grantTime);
//	}
}
