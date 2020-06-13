package nts.uk.shr.com.time.calendar.period;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.gul.util.range.ComparableRange;
import nts.gul.util.value.DiscreteValue;

@EqualsAndHashCode
@ToString
public abstract class GeneralPeriod<S extends GeneralPeriod<S, T>, T extends Comparable<T> & DiscreteValue<T>>
		implements ComparableRange<S, T> {

	private final T start;
	private final T end;

	GeneralPeriod(T start, T end) {
		this.start = start;
		this.end = end;
	}

	@Override
	public T start() {
		return this.start;
	}

	@Override
	public T end() {
		return this.end;
	}

	@Override
	public T startNext(boolean isIncrement) {
		return this.start.nextValue(isIncrement);
	}

	@Override
	public T endNext(boolean isIncrement) {
		return this.end.nextValue(isIncrement);
	}

	public boolean isEndMax() {
		return this.end.compareTo(this.max()) == 0;
	}

	public S newSpanWithMaxEnd() {
		return this.newSpan(this.start, this.max());
	}

	public boolean isReversed() {
		return this.start.compareTo(this.end) > 0;
	}

	public boolean contains(T target) {
		return this.start.compareTo(target) <= 0 && this.end.compareTo(target) >= 0;
	}

	public void forEach(Consumer<T> process) {
		this.forEach(c -> {
			process.accept(c);
			return true;
		});
	}

	public void forEach(Function<T, Boolean> breakableProcess) {
		for (T current = this.start;
				current.compareTo(this.start) <= 0;
				current = current.nextValue(true)) {
			Boolean flag = breakableProcess.apply(current);
			if (flag != null && flag == false) {
				break;
			}
		}
	}

	protected abstract T max();

	protected abstract List<YearMonth> yearMonthsBetween();

	protected abstract List<GeneralDate> datesBetween();

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GeneralPeriod<S, T> other = (GeneralPeriod<S, T>) obj;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
        return Objects.hash(start, end);
	}
}
