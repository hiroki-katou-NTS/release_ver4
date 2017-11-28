package nts.uk.ctx.at.auth.infra.repository.wplmanagementauthority;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.auth.dom.wplmanagementauthority.WorkPlaceFunction;
import nts.uk.ctx.at.auth.dom.wplmanagementauthority.WorkPlaceFunctionRepository;
import nts.uk.ctx.at.auth.infra.entity.wplmanagementauthority.KacmtWorkPlaceFunction;

@Stateless
public class JpaWorkPlaceFunctionRepository  extends JpaRepository implements WorkPlaceFunctionRepository  {

	private static final String GET_ALL_WRK_FUNCTION = "SELECT c FROM KacmtWorkPlaceFunction c ";
	private static final String GET_WRK_FUNCTION_BY_NO =GET_ALL_WRK_FUNCTION
			+ " WHERE c.functionNo = :functionNo ";
	
	@Override
	public List<WorkPlaceFunction> getAllWorkPlaceFunction() {
		List<WorkPlaceFunction> data = this.queryProxy().query(GET_ALL_WRK_FUNCTION,KacmtWorkPlaceFunction.class)
				.getList(c -> c.toDomain());
		return data;
	}

	@Override
	public Optional<WorkPlaceFunction> getWorkPlaceFunctionById(int functionNo) {
		Optional<WorkPlaceFunction> data = this.queryProxy().query(GET_WRK_FUNCTION_BY_NO,KacmtWorkPlaceFunction.class)
				.setParameter("functionNo", functionNo)
				.getSingle(c-> c.toDomain());
		return data;
	}

}
