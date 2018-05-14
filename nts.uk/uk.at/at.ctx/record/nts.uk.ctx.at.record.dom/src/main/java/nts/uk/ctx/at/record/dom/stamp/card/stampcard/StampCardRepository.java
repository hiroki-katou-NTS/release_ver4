package nts.uk.ctx.at.record.dom.stamp.card.stampcard;

import java.util.List;
import java.util.Optional;

public interface StampCardRepository {

	List<StampCard> getListStampCard(String sid);

	Optional<StampCard> getByStampCardId(String stampCardId);

	void add(StampCard domain);

	void update(StampCard domain);

	void delete(String stampCardId);

}
