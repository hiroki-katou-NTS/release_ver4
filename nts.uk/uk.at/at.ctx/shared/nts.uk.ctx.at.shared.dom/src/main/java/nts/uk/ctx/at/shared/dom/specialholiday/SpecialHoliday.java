package nts.uk.ctx.at.shared.dom.specialholiday;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.layer.dom.AggregateRoot;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.shared.dom.specialholiday.event.SpecialHolidayEvent;
import nts.uk.ctx.at.shared.dom.specialholiday.grantday.GrantPeriodic;
import nts.uk.ctx.at.shared.dom.specialholiday.grantday.GrantRegular;
import nts.uk.ctx.at.shared.dom.specialholiday.grantday.GrantSingle;
import nts.uk.shr.com.primitive.Memo;

@AllArgsConstructor
@Getter
public class SpecialHoliday extends AggregateRoot {

	/** 会社ID */
	private String companyId;

	/** 特別休暇コード */
	private SpecialHolidayCode specialHolidayCode;

	/** 特別休暇名称 */
	private SpecialHolidayName specialHolidayName;

	/** 定期付与 */
	private GrantMethod grantMethod;

	/** メモ */
	private Memo memo;

	/** 作業タイプリスト */
	private List<String> workTypeList;

	/** 定期的な付与日 */
	private GrantRegular grantRegular;

	/** 付与日数定期 */
	private GrantPeriodic grantPeriodic;

	/** 特別休暇の期限 */
	private SphdLimit sphdLimit;

	/**/
	private SubCondition subCondition;

	/** シングル付与 */
	private GrantSingle grantSingle;

	@Override
	public void validate() {
		super.validate();
	}

	/**
	 * Check Work Type
	 */
	public void validateInput() {
		if (CollectionUtil.isEmpty(workTypeList)) {
			throw new BusinessException("Msg_93");
		}

		if (this.isMethodManageRemainNumber()) {
			this.grantSingle.validate();
		} else {
			this.grantRegular.validate();
			this.grantPeriodic.validate();
			this.sphdLimit.validate();
			this.subCondition.validate();
		}
	}

	/**
	 * Create from Java Type
	 * 
	 * @param companyId
	 * @param specialHolidayCode
	 * @param specialHolidayName
	 * @param grantPeriodicCls
	 * @param memo
	 * @param workTypeList
	 * @param grantRegular
	 * @param grantPeriodic
	 * @param sphdLimit
	 * @param subCondition
	 * @param grantSingle
	 * @return
	 */
	public static SpecialHoliday createFromJavaType(String companyId, int specialHolidayCode,
			String specialHolidayName, int grantPeriodicCls, String memo, List<String> workTypeList,
			GrantRegular grantRegular, GrantPeriodic grantPeriodic, SphdLimit sphdLimit, SubCondition subCondition,
			GrantSingle grantSingle) {
		return new SpecialHoliday(companyId, new SpecialHolidayCode(specialHolidayCode),
				new SpecialHolidayName(specialHolidayName), EnumAdaptor.valueOf(grantPeriodicCls, GrantMethod.class),
				new Memo(memo), workTypeList, grantRegular, grantPeriodic, sphdLimit, subCondition, grantSingle);
	}

	/**
	 * Check Grant Method
	 * 
	 * @return
	 */
	public boolean isMethodManageRemainNumber() {
		return this.grantMethod == GrantMethod.DoNot_ManageRemainNumber;
	}
	
	public void publishEvent(boolean flag) {
		SpecialHolidayEvent event = new SpecialHolidayEvent(flag,
				this.specialHolidayCode,
				this.specialHolidayName);
		event.toBePublished();
	}
}
