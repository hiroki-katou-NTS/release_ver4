package nts.uk.ctx.at.shared.dom.common.time;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
//import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import lombok.EqualsAndHashCode;
import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//import lombok.Setter;
import lombok.val;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.at.shared.dom.common.ComparableRange;
import nts.uk.shr.com.time.TimeWithDayAttr;
/**
 * Calculate time from start to end. 計算時間帯
 * 
 * 開始～終了の範囲の最大値は48時間
 * 
 * @author keisuke_hoshina
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class TimeSpanForCalc extends DomainObject implements ComparableRange<Integer> {
	private final TimeWithDayAttr start;
	private final TimeWithDayAttr end;

	/**
	 * Constructor 
	 */
	public TimeSpanForCalc(TimeWithDayAttr start, TimeWithDayAttr end) {
		super();

		if(start == null && end == null) {
			this.start = new TimeWithDayAttr(0);
			this.end = new TimeWithDayAttr(0);
		}
		else if(start == null){
			this.start = end;
			this.end = end;
		}
		else if(end == null) {
			this.start = start;
			this.end = start;
		}
		else {
			this.start = start;
			this.end = end;
		}
	}
	
	
	public TimeSpanForCalc getSpan() {
		return this;
	}
	
	/**
	 * 開始から終了までの時間を分の整数で返す Returns time as minutes from start to end.
	 * 
	 * @return 分の整数
	 */
	public int lengthAsMinutes() {
		if(this.end.lessThan(this.start))  return 0;
		return this.end.v() - this.start.v();
		
	}

	/**
	 * 指定時刻が時間帯に含まれているか判断する Returns true if a given time is contained by this time
	 * span.
	 * 
	 * @param time
	 *            指定時刻
	 * @return 含まれていればtrue
	 */
	public boolean contains(TimeWithDayAttr time) {
		return this.start.lessThanOrEqualTo(time) && this.end.greaterThanOrEqualTo(time);
	}

	/**
	 * 時間帯を基準時間の前後で分割する
	 * 
	 * @param basePoint
	 *            時間帯を分割する時の基準時間
	 * @return 分割後の時間帯List
	 */
	public List<TimeSpanForCalc> timeDivide(TimeWithDayAttr basePoint) {
		return Arrays.asList(
				new TimeSpanForCalc(this.start, basePoint),
				new TimeSpanForCalc(basePoint, this.end));
	}

	/**
	 * 開始時刻と終了時刻を戻す（前にズラす）
	 * 
	 * @param minutesToShiftBack 移動させる時間（分）
	 * @return ズラした後の開始時刻と終了時刻
	 */
	public TimeSpanForCalc shiftBack(int minutesToShiftBack) {
		return this.shiftAhead(-minutesToShiftBack);
	}

	/**
	 * 開始時刻と終了時刻を進める（後ろにズラす）
	 * 
	 * @param minutesToMoveBack　移動させる時間（分）
	 * @return ズラした後の開始時刻と終了時刻
	 */
	public TimeSpanForCalc shiftAhead(int minutesToShiftAhead) {
		return new TimeSpanForCalc(
				this.start.shiftWithLimit(minutesToShiftAhead),
				this.end.shiftWithLimit(minutesToShiftAhead));
	}
	
	/**
	 * 開始時刻だけを戻す（前にずらす）
	 * @param minutesToShiftBack
	 * @return
	 */
	public TimeSpanForCalc shiftStartBack(int minutesToShiftBack) {
		return this.shiftStartAhead(-minutesToShiftBack);
	}
	
	/**
	 * 終了時刻だけを進める（後ろにずらす）
	 * @param minutesToShiftAhead
	 * @return
	 */
	public TimeSpanForCalc shiftEndAhead(int minutesToShiftAhead) {
		return new TimeSpanForCalc(
				this.start,
				this.end.shiftWithLimit(minutesToShiftAhead));
	}
	
	/**
	 * 終了時刻だけを戻す（前にずらす）
	 * @param minutesToShiftBack
	 * @return
	 */
	public TimeSpanForCalc shiftEndBack(int minutesToShiftBack) {
		return this.shiftEndAhead(-minutesToShiftBack);
	}
	
	/**
	 * 開始時刻だけを進める（後ろにずらす）
	 * @param minutesToShiftAhead
	 * @return
	 */
	public TimeSpanForCalc shiftStartAhead(int minutesToShiftAhead) {
		return new TimeSpanForCalc(
				this.start.shiftWithLimit(minutesToShiftAhead),
				this.end);
	}
	
	/**
	 * 指定した開始時刻に合わせてずらす
	 * @param start 新しい開始時刻
	 * @return ズラした後の開始時刻と終了時刻
	 */
	public TimeSpanForCalc shiftWithStartAt(TimeWithDayAttr newStart) {
		return this.shiftAhead(newStart.valueAsMinutes() - this.start.valueAsMinutes());
	}

	/**
	 * 比較元と比較したい時間帯の位置関係を判定する
	 * @param other 比較したい時間帯
	 * @return　重複状態区分
	 */
	public TimeSpanDuplication checkDuplication(TimeSpanForCalc other) {
		return TimeSpanDuplication.createFrom(this.compare(other));
	}
	
	/**
	 * 比較元と比較対象が接している状態か判定する
	 * @param other　比較したい時間帯
	 * @return　接している
	 */
	public boolean isContinus(TimeSpanForCalc other) {
		val result = this.compare(other);
		return result.isContinuousAfterBase() || result.isContinuousBeforeBase();
	}
	/**
	 * 開始時刻のみ指定した時刻に移動する
	 * @param newStart 新しい開始時刻
	 * @return
	 */
	public TimeSpanForCalc shiftOnlyStart(TimeWithDayAttr newStart) {
		return new TimeSpanForCalc(newStart,this.end);
	}
	
	/**
	 * 終了時刻のみ指定した時刻に移動する
	 * @param newStart
	 * @return
	 */
	public TimeSpanForCalc shiftOnlyEnd(TimeWithDayAttr newEnd) {
		return new TimeSpanForCalc(this.start,newEnd);
	}
	
	/**
	 * 重複している時間帯を返す
	 * @param other 比較対象
	 * @return 重複部分
	 */
	public Optional<TimeSpanForCalc> getDuplicatedWith(TimeSpanForCalc other) {
		TimeSpanForCalc result;
		
		if (this.contains(other)) {
			result = other;
		} else if (other.contains(this)) {
			result = this;
		} else if (this.contains(other.start)) {
			result = new TimeSpanForCalc(other.start, this.end);
		} else if (this.contains(other.end)) {
			result = new TimeSpanForCalc(this.start, other.end);
		} else {
			return Optional.empty();
		}
		
		if (result.lengthAsMinutes() == 0) {
			return Optional.empty();
		}
		
		return Optional.of(result);
	}
	
	/**
	 * 基準の時間帯で比較対象の時間帯に重複していない部分を取得する
	 * @param other 比較対象
	 * @return 重複してない部分
	 */
	public List<TimeSpanForCalc> getNotDuplicationWith(TimeSpanForCalc other){
		List<TimeSpanForCalc> result = new ArrayList<>();
		
		switch(this.checkDuplication(other))
		{
		case SAME_SPAN:
		case CONTAINED:
			/*時間帯をクリア*/
			break;
		//相手の開始を跨ぐ
		case CONNOTATE_BEGINTIME:
			/*自分の終了を相手の開始にズラす*/
			result.add(new TimeSpanForCalc(this.start,other.start));
			break;
		case CONTAINS:
			result.add(new TimeSpanForCalc(this.start,other.start));
			result.add(new TimeSpanForCalc(other.end,this.end));
			break;
		//相手の終了を跨ぐ
		case CONNOTATE_ENDTIME:
			/*自分の開始を相手の終了にずらす*/
			result.add(new TimeSpanForCalc(other.end,this.end));
			break;
		case NOT_DUPLICATE:
			/*何もしない*/
			result.add(this);
			break;
		default:
			throw new RuntimeException("unknown duplicatoin");
		}
		return result;
	}
	
	
	/**
	 *　重複している時間分時間帯をずらす
	 * @param other 比較したい時間帯
	 * @return 時間帯の重複状況
	 */
	public TimeSpanForCalc reviseToAvoidDuplicatingWith(TimeSpanForCalc other) {
		if (this.checkDuplication(other).isDuplicated()) {
			return other.shiftWithStartAt(this.end);
		}
		return other;
	}

	@Override
	public Integer startValue() {
		return this.start.v();
	}

	@Override
	public Integer endValue() {
		return this.end.v();
	}

	@Override
	public Integer startNext(boolean isIncrement) {
		return isIncrement ? this.startValue() + 1 : this.startValue() - 1;
	}

	@Override
	public Integer endNext(boolean isIncrement) {
		return isIncrement ? this.endValue() + 1 : this.endValue() - 1;
	}
	
	/**
	 * 計算用時間帯クラスを1つの計算用時間帯クラスとして結合する
	 * @author ken_takasu
	 * @param sources
	 * @return
	 */
	public static Optional<TimeSpanForCalc> join(Collection<TimeSpanForCalc> sources) {
		if (sources.isEmpty()) {
			return Optional.empty();
		}
		val start = sources.stream().map(s -> s.getStart()).min(Comparator.naturalOrder()).get();
		val end = sources.stream().map(s -> s.getEnd()).max(Comparator.naturalOrder()).get();
		return Optional.of(new TimeSpanForCalc(start, end));
	}

	
}
