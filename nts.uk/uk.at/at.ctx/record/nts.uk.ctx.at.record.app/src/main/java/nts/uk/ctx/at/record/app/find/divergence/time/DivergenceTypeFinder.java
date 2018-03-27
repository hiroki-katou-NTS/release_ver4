package nts.uk.ctx.at.record.app.find.divergence.time;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

/**
 * The Class DicergenceTypeFinder.
 */
@Stateless
public class DivergenceTypeFinder {

	/**
	 * Gets the divergence type list.
	 *
	 * @return the divergence type list
	 */
	public List<DivergenceTypeDto> getDivergenceTypeList() {

		List<DivergenceTypeDto> typeList = new ArrayList<DivergenceTypeDto>();
		for (int i = 0; i <= 9; i++) {
			DivergenceTypeDto type = DivergenceTypeDto.valueOf(i);
			typeList.add(type);
		}

		return typeList;
	}
}
