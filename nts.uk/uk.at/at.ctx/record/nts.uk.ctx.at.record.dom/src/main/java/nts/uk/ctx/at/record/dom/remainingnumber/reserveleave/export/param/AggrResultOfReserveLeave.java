package nts.uk.ctx.at.record.dom.remainingnumber.reserveleave.export.param;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDate;

/**
 * 積立年休の集計結果
 * @author shuichu_ishida
 */
@Getter
@Setter
public class AggrResultOfReserveLeave {

	/** 積立年休情報（期間終了日時点） */
	private ReserveLeaveInfo asOfPeriodEnd;
	/** 積立年休情報（末日翌日付与時） */
	private Optional<ReserveLeaveInfo> asOfGrantNextDayOfLastDay;
	/** 積立年休情報（付与時点） */
	private Optional<List<ReserveLeaveInfo>> asOfGrant;
	/** 積立年休情報（消滅） */
	private Optional<List<ReserveLeaveInfo>> lapsed;
	/** 積立年休エラー情報 */
	private List<ReserveLeaveError> reserveLeaveErrors;
	
	/**
	 * コンストラクタ
	 */
	public AggrResultOfReserveLeave(){
		
		this.asOfPeriodEnd = new ReserveLeaveInfo(GeneralDate.today());
		this.asOfGrantNextDayOfLastDay = Optional.empty();
		this.asOfGrant = Optional.empty();
		this.lapsed = Optional.empty();
		this.reserveLeaveErrors = new ArrayList<>();
	}
	
	/**
	 * ファクトリー
	 * @param asOfPeriodEnd 積立年休情報（期間終了日時点）
	 * @param asOfGrantNextDayOfLastDay 積立年休情報（末日翌日付与時）
	 * @param asOfGrant 積立年休情報（付与時点）
	 * @param lapsed 積立年休情報（消滅）
	 * @param reserveLeaveErrors 積立年休エラー情報
	 * @return 積立年休の集計結果
	 */
	public static AggrResultOfReserveLeave of(
			ReserveLeaveInfo asOfPeriodEnd,
			Optional<ReserveLeaveInfo> asOfGrantNextDayOfLastDay,
			Optional<List<ReserveLeaveInfo>> asOfGrant,
			Optional<List<ReserveLeaveInfo>> lapsed,
			List<ReserveLeaveError> reserveLeaveErrors){
		
		AggrResultOfReserveLeave domain = new AggrResultOfReserveLeave();
		domain.asOfPeriodEnd = asOfPeriodEnd;
		domain.asOfGrantNextDayOfLastDay = asOfGrantNextDayOfLastDay;
		domain.asOfGrant = asOfGrant;
		domain.lapsed = lapsed;
		domain.reserveLeaveErrors = reserveLeaveErrors;
		return domain;
	}
}
