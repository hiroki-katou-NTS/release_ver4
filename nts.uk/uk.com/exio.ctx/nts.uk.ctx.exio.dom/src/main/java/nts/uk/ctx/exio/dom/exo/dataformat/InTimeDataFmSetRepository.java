package nts.uk.ctx.exio.dom.exo.dataformat;

import java.util.Optional;
import java.util.List;

/**
 * 時刻型データ形式設定
 */
public interface InTimeDataFmSetRepository {

	List<InTimeDataFmSet> getAllInTimeDataFmSet();
    Optional<InTimeDataFmSet> getInTimeDataFmSetById(String cid);
	Optional<InTimeDataFmSet> getInTimeDataFmSetByCid(String cid);

	void add(InTimeDataFmSet domain);

	void update(InTimeDataFmSet domain);

	void remove();

}
