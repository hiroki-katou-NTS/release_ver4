package nts.uk.ctx.sys.assist.app.find.datarestoration;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.sys.assist.dom.datarestoration.PerformDataRecoveryRepository;
import nts.uk.ctx.sys.assist.dom.tablelist.TableList;

@Stateless
public class SurfaceItemFinder {
	@Inject
	private PerformDataRecoveryRepository finder;

	public List<SurfaceItemDto> getAllTableList() {
		return finder.getAllTableList().stream().map(item -> SurfaceItemDto.fromDomain(item))
				.collect(Collectors.toList());
	}

	public List<SurfaceItemDto> getSurfaceItemById(String dataRecoveryProcessId) {
		List<TableList> optTableList = finder.getByRecoveryProcessingId(dataRecoveryProcessId);
		if (!optTableList.isEmpty()) {
			return optTableList.stream().map(c -> SurfaceItemDto.fromDomain(c)).collect(Collectors.toList());
		}
		return Collections.EMPTY_LIST;
	}
}
