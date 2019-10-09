package nts.uk.ctx.at.shared.infra.repository.worktype.absenceframe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.shared.dom.worktype.absenceframe.AbsenceFrame;
import nts.uk.ctx.at.shared.dom.worktype.absenceframe.AbsenceFrameRepository;
import nts.uk.ctx.at.shared.infra.entity.worktype.absenceframe.KshmtAbsenceFrame;
import nts.uk.ctx.at.shared.infra.entity.worktype.absenceframe.KshmtAbsenceFramePK;

@Stateless
public class JpaAbsenceFrameRepository extends JpaRepository implements AbsenceFrameRepository {
    
	private static final String SEL_1 = "SELECT a FROM KshmtAbsenceFrame a  WHERE a.kshmtAbsenceFramePK.companyId = :companyId AND a.abolishAtr = :abolishAtr ";
	private static final String GET_ALL = "SELECT a FROM KshmtAbsenceFrame a  WHERE a.kshmtAbsenceFramePK.companyId = :companyId ";
	private static final String GET_ALL_BY_LIST_FRAME_NO = GET_ALL 
			+"AND a.kshmtAbsenceFramePK.absenceFrameNo IN :frameNos ";
	private static AbsenceFrame toDomain(KshmtAbsenceFrame entity) {
		AbsenceFrame domain = AbsenceFrame.createSimpleFromJavaType(entity.kshmtAbsenceFramePK.companyId,
				entity.kshmtAbsenceFramePK.absenceFrameNo,
				entity.name, 
				entity.abolishAtr);
		return domain;
	}
	
	@Override
	public List<AbsenceFrame> findAbsenceFrame(String companyId) {			
		return this.queryProxy().query(SEL_1, KshmtAbsenceFrame.class)
				.setParameter("companyId", companyId)
				.setParameter("abolishAtr", 0)
				.getList(a -> toDomain(a));
	}

	@Override
	public List<AbsenceFrame> findAll(String companyId) {
		return this.queryProxy().query(GET_ALL, KshmtAbsenceFrame.class)
				.setParameter("companyId", companyId)
				.getList(a -> toDomain(a));
	}

	@Override
	public Optional<AbsenceFrame> findAbsenceFrameByCode(String companyId, int frameNo) {
		return this.queryProxy().find(new KshmtAbsenceFramePK(companyId, frameNo), KshmtAbsenceFrame.class)
				.map(x -> convertToDoma(x));
	}

	@Override
	public void update(AbsenceFrame absenceFrame) {
		this.commandProxy().update(toEntity(absenceFrame));
	}
	
	/**
	 * Convert to domain
	 * 
	 * @param KshmtAbsenceFrame
	 * @return
	 */
	private AbsenceFrame convertToDoma(KshmtAbsenceFrame x) {
		return AbsenceFrame.createFromJavaType(x.kshmtAbsenceFramePK.companyId, 
				x.kshmtAbsenceFramePK.absenceFrameNo,
				x.name,
				x.abolishAtr);
	}
	
	/**
	 * Convert to entity
	 * 
	 * @param AbsenceFrame
	 * @return
	 */
	private KshmtAbsenceFrame toEntity(AbsenceFrame absenceFrame) {
		return new KshmtAbsenceFrame(
				new KshmtAbsenceFramePK(absenceFrame.getCompanyId(), absenceFrame.getAbsenceFrameNo()),
				absenceFrame.getAbsenceFrameName().v(),
				absenceFrame.getDeprecateAbsence().value);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<AbsenceFrame> findAbsenceFrameByListFrame(String companyId, List<Integer> frameNos) {
		if(frameNos.isEmpty())
			return Collections.emptyList();
		List<AbsenceFrame> resultList = new ArrayList<>();
		CollectionUtil.split(frameNos, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			resultList.addAll(this.queryProxy().query(GET_ALL_BY_LIST_FRAME_NO, KshmtAbsenceFrame.class)
								.setParameter("companyId", companyId)
								.setParameter("frameNos", subList)
								.getList(a -> toDomain(a)));
		});
		return resultList;
	}
}
