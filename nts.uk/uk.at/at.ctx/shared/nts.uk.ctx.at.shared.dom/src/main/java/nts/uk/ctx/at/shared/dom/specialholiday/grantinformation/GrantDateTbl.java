package nts.uk.ctx.at.shared.dom.specialholiday.grantinformation;

import java.util.ArrayList;
//import java.util.HashSet;
import java.util.List;
//import java.util.Set;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.shared.dom.specialholiday.SpecialHolidayCode;

/**
 * 特別休暇付与日数テーブル
 * @author masaaki_jinno
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GrantDateTbl extends AggregateRoot {

	/** 会社ID */
	private String companyId;

	/** 特別休暇コード */
	private SpecialHolidayCode specialHolidayCode;

	/** コード */
	private GrantDateCode grantDateCode;

	/** 名称 */
	private GrantDateName grantDateName;

	/** 付与日数  */
	private List<GrantElapseYearMonth> elapseYear;

	/** 規定のテーブルとする */
	private boolean isSpecified;

	/** テーブル以降の付与日数 */
	private Optional<GrantedDays> grantedDays;

	@Override
	public void validate() {
		super.validate();
	}
	
	// 経過年数テーブルより多いテーブルを削除する
	public void deleteMoreTableThanElapsedYearsTable(int numOfElapsedYears) {
		if (this.elapseYear.size() > numOfElapsedYears) {
			this.elapseYear.removeIf(e -> e.getElapseNo() > numOfElapsedYears);
		}
	}
	
	// 経過年数テーブルより少ない分のテーブルを追加する
	public void addLessTableThanElapsedYearsTable(int numOfElapsedYears) {
		for (int numOfGrants = this.elapseYear.size() + 1; numOfGrants < numOfElapsedYears; numOfGrants++) {
			GrantElapseYearMonth grantElapseYearMonth = new GrantElapseYearMonth(numOfGrants, new GrantedDays(0));
		}
	}
	
	
	/**
	 * Validate input data
	 */
	public List<String> validateInput() {
		List<String> errors = new ArrayList<>();
		List<YearMonth> yearMonth = new ArrayList<>();

//		for (int i = 0; i < this.elapseYear.size(); i++) {
//			GrantElapseYearMonth currentElapseYear = this.elapseYear.get(i);
//
//			// 同じ経過年数の場合は登録不可
//			YearMonth currentYearMonth = new YearMonth();
//			currentYearMonth.setMonth(currentElapseYear.getMonths().v());
//			currentYearMonth.setYear(currentElapseYear.getYears().v());
//
//			if (currentYearMonth.getMonth()==0 && currentYearMonth.getYear()==0) {
//				throw new BusinessException("Msg_95");
//			}
//
//			if (yearMonth.stream().anyMatch(x -> x.equals(currentYearMonth))) {
//				throw new BusinessException("Msg_96");
//			}
//
//			yearMonth.add(currentYearMonth);
//
//			// 付与日数が入力されていても、年数、月数ともに未入力の場合登録不可
//			if ((currentElapseYear.getMonths() == null && currentElapseYear.getYears() == null)
//					&& (currentElapseYear.getGrantedDays() != null || currentElapseYear.getGrantedDays().v() != 0)) {
//				errors.add("Msg_100");
//			}
//
//			// 経過年数が入力されており、付与日数が未入力の場合登録不可
//			if ((currentElapseYear.getGrantedDays() == null)
//					&& ((currentElapseYear.getYears() != null && currentElapseYear.getMonths() != null)
//							|| (currentElapseYear.getYears().v() != 0 && currentElapseYear.getMonths().v() != 0))) {
//				errors.add("Msg_101");
//			}
//		}

		return errors;
	}

//	public GrantDateTbl(
//			GrantDateCode grantDateCode,
//			GrantDateName grantDateName,
//			boolean isSpecified,
//			boolean fixedAssign,
//			Integer numberOfDays) {
//		this.grantDateCode = grantDateCode;
//		this.grantDateName = grantDateName;
//		this.isSpecified = isSpecified;
//		this.fixedAssign = fixedAssign;
//		this.numberOfDays = numberOfDays;
//	}

//	public static GrantDateTbl createFromJavaType(
//			String grantDateCode,
//			String grantDateName,
//			boolean isSpecified,
//			Integer numberOfDays) {
//		return new GrantDateTbl(
//				new GrantDateCode(grantDateCode),
//				new GrantDateName(grantDateName),
//				isSpecified,
////				fixedAssign,
//				numberOfDays);
//	}

	/**
	 * Create from Java Type
	 * @param companyId 会社ID
	 * @param specialHolidayCode 特別休暇コード
	 * @param grantDateCode 付与テーブルコード
	 * @param grantDateName 付与テーブル名称
	 * @param isSpecified 規定のテーブルとする
	 * @param numberOfDays テーブル以降の付与日数
	 * @return
	 */
	public static GrantDateTbl createFromJavaType(
			String companyId,
			int specialHolidayCode,
			String grantDateCode,
			String grantDateName,
			boolean isSpecified,
			Integer numberOfDays) {
		return new GrantDateTbl(companyId,
				new SpecialHolidayCode(specialHolidayCode),
				new GrantDateCode(grantDateCode),
				new GrantDateName(grantDateName),
				new ArrayList<GrantElapseYearMonth>(),
				isSpecified,
				Optional.ofNullable(new GrantedDays(numberOfDays))
				);
	}

}
