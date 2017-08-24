package nts.uk.ctx.at.shared.dom.yearholidaygrant;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;

/**
 * 年休付与テーブル
 * 
 * @author TanLV
 *
 */

@Getter
public class GrantHdTbl extends AggregateRoot {
	/* 会社ID */
	private String companyId;
	
	/* 年休付与NO */
	private int grantYearHolidayNo;
	
	/* 条件NO */
	private int conditionNo;
	
	/* 年休付与テーブル設定コード */
	private YearHolidayCode yearHolidayCode;
	
	/* 年休付与日数 */
	private GrantDays grantDays;
	
	/* 時間年休上限日数 */
	private LimitedTimeHdDays limitedTimeHdDays;
	
	/* 半日年休上限回数 */
	private LimitedHalfHdCnt limitedHalfHdCnt;
	
	/* 勤続年数月数 */
	private LengthOfServiceMonths lengthOfServiceMonths;
	
	/* 勤続年数年数 */
	private LengthOfServiceYears lengthOfServiceYears;
	
	/* 付与基準日 */
	private GrantReferenceDate grantReferenceDate;
	
	/* 一斉付与する */
	private GrantSimultaneity grantSimultaneity;
	
	private GeneralDate grantDate;

	@Override
	public void validate() {
		super.validate();
		// 一斉付与する　＝　TRUE　の場合 付与基準日は年休付与基準日でなければならない
		
		
		// 重複した勤続年数の登録不可
		
		
		// 勤続年数、0年0ヶ月は登録不可
		if (this.lengthOfServiceMonths.v() == 0 && this.lengthOfServiceYears.v() == 0) {
			throw new BusinessException("Msg_268");
		}
		
		// 利用区分がTRUEの付与条件は、選択されている計算方法の条件値が入力されていること
	}
	
	public GrantHdTbl(String companyId, int grantYearHolidayNo, int conditionNo, YearHolidayCode yearHolidayCode,
			GrantDays grantDays, LimitedTimeHdDays limitedTimeHdDays, LimitedHalfHdCnt limitedHalfHdCnt, LengthOfServiceMonths lengthOfServiceMonths, 
			LengthOfServiceYears lengthOfServiceYears, GrantReferenceDate grantReferenceDate, GrantSimultaneity grantSimultaneity) {
		
		this.companyId = companyId;
		this.grantYearHolidayNo = grantYearHolidayNo;
		this.conditionNo = conditionNo;
		this.yearHolidayCode = yearHolidayCode;
		this.grantDays = grantDays;
		this.limitedTimeHdDays = limitedTimeHdDays;
		this.limitedHalfHdCnt = limitedHalfHdCnt;
		this.lengthOfServiceMonths = lengthOfServiceMonths;
		this.lengthOfServiceYears = lengthOfServiceYears;
		this.grantReferenceDate = grantReferenceDate;
		this.grantSimultaneity = grantSimultaneity;
	}
	
	/**
	 * 
	 * @param grantHolidayList
	 */
	public static void validateInput(List<GrantHdTbl> grantHolidayList) {
		
		for(int i=0; i< grantHolidayList.size(); i++) {
			if (i == 0) {
				continue;
			}
			
			GrantHdTbl currentCondition = grantHolidayList.get(i);		
			
			// 勤続年数は上から昇順になっていること
			int firstValueYear = grantHolidayList.get(i - 1).getLengthOfServiceYears().v();
			int secondValueYear = currentCondition.getLengthOfServiceMonths().v();
			if (firstValueYear < secondValueYear) {
				throw new BusinessException("Msg_269");
			}
			
			// 勤続年数が入力されている場合、付与日数を入力すること
			if((currentCondition.getLengthOfServiceMonths() != null || currentCondition.getLengthOfServiceYears() != null) && currentCondition.getGrantDays() == null){
				throw new BusinessException("Msg_270");
			}
			// 付与日数が入力されている場合、勤続年数を入力すること			
			if((currentCondition.getLengthOfServiceMonths() == null || currentCondition.getLengthOfServiceYears() == null) && currentCondition.getGrantDays() != null){
				throw new BusinessException("Msg_270");
			}
		}
		
	}

	/**
	 * 
	 * @param companyId
	 * @param grantYearHolidayNo
	 * @param conditionNo
	 * @param yearHolidayCode
	 * @param grantDays
	 * @param limitedTimeHdDays
	 * @param limitedHalfHdCnt
	 * @param lengthOfServiceMonths
	 * @param lengthOfServiceYears
	 * @param grantReferenceDate
	 * @param grantSimultaneity
	 * @return
	 */
	public static GrantHdTbl createFromJavaType(String companyId, int grantYearHolidayNo, int conditionNo, String yearHolidayCode,
			BigDecimal grantDays, int limitedTimeHdDays, int limitedHalfHdCnt, int lengthOfServiceMonths, 
			int lengthOfServiceYears, int grantReferenceDate, int grantSimultaneity) {

		return new GrantHdTbl(companyId, 
				grantYearHolidayNo, 
				conditionNo, 
				new YearHolidayCode(yearHolidayCode),
				new GrantDays(grantDays),
				new LimitedTimeHdDays(limitedTimeHdDays),
				new LimitedHalfHdCnt(limitedHalfHdCnt),
				new LengthOfServiceMonths(lengthOfServiceMonths),
				new LengthOfServiceYears(lengthOfServiceYears),
				EnumAdaptor.valueOf(grantReferenceDate, GrantReferenceDate.class),
				EnumAdaptor.valueOf(grantSimultaneity, GrantSimultaneity.class)
				);
	}
	
	/**
	 * 
	 * @param grantHolidayList
	 */
	public void calculateGrantDate(GeneralDate referenceDate, GeneralDate simultaneousGrantDate, UseSimultaneousGrant useSimultaneousGrant) {
		referenceDate.addMonths(lengthOfServiceMonths.v());
		referenceDate.addYears(lengthOfServiceYears.v());
		
		if (UseSimultaneousGrant.USE.equals(useSimultaneousGrant)) {
			if (GrantSimultaneity.USE.equals(this.grantSimultaneity)) {
				// 処理名4.付与日シュミレーション計算処理について			
				this.grantDate = simultaneousGrantDate.after(referenceDate) 
						? referenceDate
						: simultaneousGrantDate;
			} else {
				this.grantDate = referenceDate;
			}
			
		} else {
			// 処理名3.付与日シュミレーション計算処理について
			this.grantDate = referenceDate;
		}
	}
}

