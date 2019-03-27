package nts.uk.ctx.at.function.dom.adapter;

import java.util.List;
import java.util.Optional;

public interface ErAlApplicationAdapter {
	Optional<ErAlApplicationAdapterDto> getAllErAlAppByEralCode(String companyID, String errorAlarmCode);

	List<ErAlApplicationAdapterDto> getAllErAlAppByEralCode(String companyID, List<String> errorAlarmCode);
}
