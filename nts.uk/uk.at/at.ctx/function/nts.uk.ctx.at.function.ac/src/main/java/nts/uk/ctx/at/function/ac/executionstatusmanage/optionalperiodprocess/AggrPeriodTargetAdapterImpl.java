package nts.uk.ctx.at.function.ac.executionstatusmanage.optionalperiodprocess;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import nts.uk.ctx.at.function.dom.executionstatusmanage.optionalperiodprocess.AggrPeriodTargetAdapter;
import nts.uk.ctx.at.function.dom.executionstatusmanage.optionalperiodprocess.AggrPeriodTargetImport;
import nts.uk.ctx.at.record.dom.executionstatusmanage.optionalperiodprocess.AggrPeriodTarget;
import nts.uk.ctx.at.record.pub.executionstatusmanage.optionalperiodprocess.AggrPeriodTargetPub;

/**
 * The Class AggrPeriodTargetAdapterImpl.
 */
public class AggrPeriodTargetAdapterImpl implements AggrPeriodTargetAdapter {

	/** The pub. */
	@Inject
	private AggrPeriodTargetPub pub;
	
	/**
	 * Find all.
	 *
	 * @param aggrId the aggr id
	 * @return the list
	 */
	@Override
	public List<AggrPeriodTargetImport> findAll(String aggrId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Adds the target.
	 *
	 * @param target the target
	 */
	@Override
	public void addTarget(List<AggrPeriodTargetImport> target) {
		this.pub.addTarget(target.stream()
			.map(itemImport -> AggrPeriodTarget.createFromJavaType(
					itemImport.getEmployeeId(), 
					itemImport.getAggrId(),
					itemImport.getState()))
			.collect(Collectors.toList())
			);
	}

	/**
	 * Find by aggr.
	 *
	 * @param aggrId the aggr id
	 * @return the optional
	 */
	@Override
	public Optional<AggrPeriodTargetImport> findByAggr(String aggrId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Update excution.
	 *
	 * @param target the target
	 */
	@Override
	public void updateExcution(AggrPeriodTargetImport target) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Update target.
	 *
	 * @param target the target
	 */
	@Override
	public void updateTarget(List<AggrPeriodTargetImport> target) {
		// TODO Auto-generated method stub
		
	}

}
