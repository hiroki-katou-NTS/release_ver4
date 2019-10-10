package nts.uk.ctx.at.function.ac.monthly.agreement;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.uk.ctx.at.function.dom.adapter.monthly.agreement.GetExcessTimesYearAdapter;
import nts.uk.ctx.at.record.pub.monthly.agreement.GetExcessTimesYearPub;
import nts.uk.ctx.at.shared.dom.common.Year;

/**
 * @author dat.lh
 *
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class GetExcessTimesYearAcFinder implements GetExcessTimesYearAdapter {
	@Inject
	GetExcessTimesYearPub getExcessTimesYearPub;

	@Override
	public int algorithm(String employeeId, Year year) {
		return getExcessTimesYearPub.algorithm(employeeId, year).getExcessTimes();
	}
	
	@Override
	public Map<String,Integer> algorithm(List<String> employeeIds, Year year) {
		return getExcessTimesYearPub.algorithm(employeeIds, year).entrySet().stream().collect(Collectors.toMap(
	            e -> e.getKey(),
	            e -> e.getValue().getExcessTimes()
	        ));
	}

}
