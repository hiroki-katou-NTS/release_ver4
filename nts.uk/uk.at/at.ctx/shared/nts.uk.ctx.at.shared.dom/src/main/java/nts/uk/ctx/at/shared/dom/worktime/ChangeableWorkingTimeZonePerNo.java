package nts.uk.ctx.at.shared.dom.worktime;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.val;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.worktime.predset.WorkNo;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 勤務NOごとの変更可能な勤務時間帯
 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.就業.shared.就業規則.就業時間帯.勤務NOごとの変更可能な勤務時間帯
 *
 * @author kumiko_otake
 *
 */
@Value
public class ChangeableWorkingTimeZonePerNo {

	/** 勤務NO **/
	private final WorkNo workNo;

	/** 開始時刻の変更可能な時間帯 **/
	private final TimeSpanForCalc forStart;

	/** 終了時刻の変更可能な時間帯 **/
	private final TimeSpanForCalc forEnd;


	/**
	 * 作成する
	 * @param workNo 勤務NO
	 * @param forStart 開始時刻の変更可能な時間帯
	 * @param forEnd 終了時刻の変更可能な時間帯
	 * @return 変更可能な時間帯
	 */
	public static ChangeableWorkingTimeZonePerNo create( WorkNo workNo, TimeSpanForCalc forStart, TimeSpanForCalc forEnd ) {

		// 不変条件： 勤務NO between 1 and 2
		if ( workNo.lessThan( 1 ) || workNo.greaterThan( 2 ) ) {
			throw new RuntimeException( "WorkNo is out of range. value:" + workNo.v() );
		}

		return new ChangeableWorkingTimeZonePerNo( workNo, forStart, forEnd );

	}

	/**
	 * 開始と終了が同じ
	 * @param workNo 勤務NO
	 * @param timeSpan 時間帯
	 * @return 勤務NOごとの変更可能な勤務時間帯(開始時刻と終了時刻の時間帯が同じ)
	 */
	public static ChangeableWorkingTimeZonePerNo createAsStartEqualsEnd( WorkNo workNo, TimeSpanForCalc timeSpan ) {
		return ChangeableWorkingTimeZonePerNo.create( workNo, timeSpan, timeSpan );
	}

	/**
	 * 変更可能な時間帯なし
	 * @param workNo 勤務NO
	 * @param workingTimespan 勤務時間帯
	 * @return 勤務NOごとの変更可能な勤務時間帯(変更可能な時間帯がない)
	 */
	public static ChangeableWorkingTimeZonePerNo createAsUnchangeable( WorkNo workNo, TimeSpanForCalc workingTimespan) {
		return ChangeableWorkingTimeZonePerNo.create( workNo
						, new TimeSpanForCalc( workingTimespan.getStart(), workingTimespan.getStart() )
						, new TimeSpanForCalc( workingTimespan.getEnd(), workingTimespan.getEnd() )
					);
	}


	/**
	 * 変更可能な時間帯に含まれているか
	 * @param time 時刻
	 * @param checkTarget チェック対象の時刻区分
	 * @return
	 */
	public ContainsResult contains(TimeWithDayAttr time, ClockAreaAtr checkTarget) {

		// 対応する時間帯を取得
		val timeSpan = this.getTimeSpan(checkTarget);

		return new ContainsResult(
							timeSpan.contains(time)	// 時間帯が時刻を含むか
						,	Optional.of(timeSpan) 	// 対応する時間帯
					);

	}

	/**
	 * 対象時刻区分に対応する時間帯を取得する
	 * @param atr
	 * @return 変更可能な時間帯
	 */
	private TimeSpanForCalc getTimeSpan(ClockAreaAtr atr) {

		switch (atr) {
			case START:	// 開始時刻
				return this.forStart;
			case END:	// 終了時刻
				return this.forEnd;
		}

		throw new RuntimeException("Out of Enum: " + atr.toString());

	}


	/**
	 * 対象時刻区分
	 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.就業.shared.就業規則.就業時間帯.対象時刻区分
	 *
	 * @author kumiko_otake
	 *
	 */
	@AllArgsConstructor
	public static enum ClockAreaAtr {

		/** 開始時刻 **/
		START(1),
		/** 終了時刻 **/
		END(2),
		;

		private final int value;

	}


	/**
	 * 変更可能な時間帯に含まれているかの判定結果
	 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.就業.shared.就業規則.就業時間帯.時間帯に含まれているか
	 *
	 * @author kumiko_otake
	 *
	 */
	@Value
	public static class ContainsResult {

		/** 含まれているか true:含まれている/false:含まれていない **/
		private final boolean contains;
		/** 時間帯 **/
		private final Optional<TimeSpanForCalc> timeSpan;

		/**
		 * 含まれている
		 * @param timeSpan 時間帯
		 * @return
		 */
		public static ContainsResult contains( TimeSpanForCalc timeSpan ) {
			return new ContainsResult( true, Optional.of( timeSpan ) );
		}

		/**
		 * 含まれていない
		 * @return
		 */
		public static ContainsResult notContains() {
			return new ContainsResult( false, Optional.empty() );
		}

	}

}
