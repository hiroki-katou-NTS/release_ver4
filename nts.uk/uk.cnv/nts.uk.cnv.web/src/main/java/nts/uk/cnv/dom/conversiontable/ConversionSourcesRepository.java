package nts.uk.cnv.dom.conversiontable;

import java.util.List;
import java.util.Optional;

public interface ConversionSourcesRepository {

	Optional<ConversionSource> get(String sourceId);

	List<ConversionSource> getByCategory(String category);

	String insert(ConversionSource source);
	void update(ConversionSource source);
	void delete(String sourceId);

}
