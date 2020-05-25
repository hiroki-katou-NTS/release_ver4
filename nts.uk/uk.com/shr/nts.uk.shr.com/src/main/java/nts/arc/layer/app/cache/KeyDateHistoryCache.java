package nts.arc.layer.app.cache;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import lombok.val;
import nts.arc.layer.app.cache.DateHistoryCache.Entry;
import nts.arc.time.GeneralDate;

public class KeyDateHistoryCache<K, E> {

	private final Map<K, DateHistoryCache<E>> cache;
	
	private final BiFunction<K, GeneralDate, Optional<Entry<E>>> loader;
	
	private KeyDateHistoryCache(Map<K, DateHistoryCache<E>> cache, BiFunction<K, GeneralDate, Optional<Entry<E>>> loader) {
		this.cache = cache;
		this.loader = loader;
	}
	
	public static <K, E> KeyDateHistoryCache<K, E> incremental(BiFunction<K, GeneralDate, Optional<Entry<E>>> loader) {
		return new KeyDateHistoryCache<>(new ConcurrentHashMap<>(), loader);
	}
	
	public static <K, E> KeyDateHistoryCache<K, E> loaded(Map<K, List<Entry<E>>> data) {
		val map = data.entrySet().stream().collect(Collectors.toMap(
				es -> es.getKey(),
				es -> new DateHistoryCache<>(es.getValue())));
		
		return new KeyDateHistoryCache<>(map, (key, date) -> Optional.empty());
	}
	
	public Optional<E> get(K key, GeneralDate date) {
		return cache
				.computeIfAbsent(key, k -> new DateHistoryCache<>())
				.get(date, d -> loader.apply(key, date));
	}
	
}
