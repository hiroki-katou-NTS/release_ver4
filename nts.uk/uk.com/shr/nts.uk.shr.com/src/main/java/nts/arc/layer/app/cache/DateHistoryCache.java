package nts.arc.layer.app.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public class DateHistoryCache<E> {

	private Cache<E> cache;
	
	public DateHistoryCache() {
		cache = Cache.empty();
	}
	
	public DateHistoryCache(List<Entry<E>> entries) {
		cache = new Cache<>(
				entries.stream().map(e -> e.period).collect(Collectors.toList()),
				entries.stream().collect(Collectors.toMap(e -> e.period.start(), e -> e.element)));
	}
	
	public Optional<E> get(GeneralDate date) {
		return cache.find(date);
	}
	
	public synchronized Optional<E> get(GeneralDate date, Function<GeneralDate, Optional<Entry<E>>> loader) {
		val cached = cache.find(date);
		if (cached.isPresent()) {
			return cached;
		}
		
		return loader.apply(date)
				.map(entry -> {
					add(entry.period, entry.element);
					return entry.element;
				});
	}
	
	public synchronized void add(DatePeriod period, E element) {
		cache = cache.add(period, element);
	}
	
	@Value
	public static class Entry<E> {
		private final DatePeriod period;
		private final E element;
		
		public static <E> Entry<E> of(DatePeriod period, E element) {
			return new Entry<>(period, element);
		}
		
		public static <E> List<Entry<E>> listOf(
				List<E> elements,
				Class<E> elementClass,
				Function<E, DatePeriod> periodMap) {
			
			return elements.stream()
					.map(e -> new Entry<>(periodMap.apply(e), e))
					.collect(Collectors.toList());
		}
		
		public static <S, E> List<Entry<E>> listOf(
				List<S> source,
				Class<E> elementClass,
				Function<S, DatePeriod> periodMap,
				Function<S, E> elementMap) {
			
			return source.stream()
					.map(s -> new Entry<>(periodMap.apply(s), elementMap.apply(s)))
					.collect(Collectors.toList());
		}
	}
	
	@RequiredArgsConstructor
	private static class Cache<E> {
		final List<DatePeriod> periods;
		final Map<GeneralDate, E> elements;
		
		public static <E> Cache<E> empty() {
			return new Cache<>(Collections.emptyList(), Collections.emptyMap());
		}
		
		Cache<E> add(DatePeriod period, E element) {
			val newPeriods = new ArrayList<>(periods);
			newPeriods.add(period);
			
			val newElements = new HashMap<>(elements);
			newElements.put(period.start(), element);
			
			return new Cache<>(newPeriods, newElements);
		}
		
		Optional<E> find(GeneralDate date) {
			return findPeriod(date)
					.map(period -> elements.get(period.start()));
		}
		
		private Optional<DatePeriod> findPeriod(GeneralDate date) {
			return periods.stream()
					.filter(p -> p.contains(date))
					.findFirst();
		}
	}
}
