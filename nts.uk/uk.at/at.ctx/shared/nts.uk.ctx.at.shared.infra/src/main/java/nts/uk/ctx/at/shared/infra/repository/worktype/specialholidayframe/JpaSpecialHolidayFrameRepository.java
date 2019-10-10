package nts.uk.ctx.at.shared.infra.repository.worktype.specialholidayframe;

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
import nts.uk.ctx.at.shared.dom.worktype.specialholidayframe.SpecialHolidayFrame;
import nts.uk.ctx.at.shared.dom.worktype.specialholidayframe.SpecialHolidayFrameRepository;
import nts.uk.ctx.at.shared.infra.entity.worktype.specialholidayframe.KshmtSpecialHolidayFrame;
import nts.uk.ctx.at.shared.infra.entity.worktype.specialholidayframe.KshmtSpecialHolidayFramePK;

@Stateless
public class JpaSpecialHolidayFrameRepository extends JpaRepository implements SpecialHolidayFrameRepository  {
	
	private static final String SEL_1 = "SELECT a FROM KshmtSpecialHolidayFrame a  WHERE a.kshmtSpecialHolidayFramePK.companyId = :companyId AND a.abolishAtr = :abolishAtr ";
	private static final String GET_ALL = "SELECT a FROM KshmtSpecialHolidayFrame a  WHERE a.kshmtSpecialHolidayFramePK.companyId = :companyId ";
	private static final String GET_ALL_BY_LIST_FRAME_NO = GET_ALL
			+ " AND a.kshmtSpecialHolidayFramePK.specialHdFrameNo IN :frameNos ";
	private static SpecialHolidayFrame toDomain(KshmtSpecialHolidayFrame entity) {
		SpecialHolidayFrame domain = SpecialHolidayFrame.createSimpleFromJavaType(entity.kshmtSpecialHolidayFramePK.companyId,
				entity.kshmtSpecialHolidayFramePK.specialHdFrameNo,
				entity.name,
				entity.abolishAtr);
		return domain;
	}		

	@Override
	public List<SpecialHolidayFrame> findSpecialHolidayFrame(String companyId) {
		return this.queryProxy().query(SEL_1, KshmtSpecialHolidayFrame.class)
				.setParameter("companyId", companyId)
				.setParameter("abolishAtr", 0)
				.getList(a -> toDomain(a));
	}

	@Override
	public List<SpecialHolidayFrame> findAll(String companyId) {
		return this.queryProxy().query(GET_ALL, KshmtSpecialHolidayFrame.class)
				.setParameter("companyId", companyId)
				.getList(a -> toDomain(a));
	}

	@Override
	public Optional<SpecialHolidayFrame> findHolidayFrameByCode(String companyId, int frameNo) {
		return this.queryProxy().find(new KshmtSpecialHolidayFramePK(companyId, frameNo), KshmtSpecialHolidayFrame.class)
				.map(x -> convertToDoma(x));
	}
	
	@Override
	public void update(SpecialHolidayFrame specialHolidayFrame) {
		this.commandProxy().update(toEntity(specialHolidayFrame));
	}

	/**
	 * Convert to domain
	 * 
	 * @param KshmtSpecialHolidayFrame
	 * @return
	 */
	private SpecialHolidayFrame convertToDoma(KshmtSpecialHolidayFrame x) {
		return SpecialHolidayFrame.createFromJavaType(x.kshmtSpecialHolidayFramePK.companyId, 
				x.kshmtSpecialHolidayFramePK.specialHdFrameNo,
				x.name,
				x.abolishAtr);
	}

	/**
	 * Convert to entity
	 * 
	 * @param SpecialHolidayFrame
	 * @return
	 */
	private KshmtSpecialHolidayFrame toEntity(SpecialHolidayFrame specialHolidayFrame) {
		return new KshmtSpecialHolidayFrame(
				new KshmtSpecialHolidayFramePK(specialHolidayFrame.getCompanyId(), specialHolidayFrame.getSpecialHdFrameNo()),
				specialHolidayFrame.getSpecialHdFrameName().v(),
				specialHolidayFrame.getDeprecateSpecialHd().value);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<SpecialHolidayFrame> findHolidayFrameByListFrame(String companyId, List<Integer> frameNos) {
		if(frameNos.isEmpty())
			return Collections.emptyList();
		List<SpecialHolidayFrame> resultList = new ArrayList<>();
		CollectionUtil.split(frameNos, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			resultList.addAll(this.queryProxy().query(GET_ALL_BY_LIST_FRAME_NO, KshmtSpecialHolidayFrame.class)
								.setParameter("companyId", companyId)
								.setParameter("frameNos", subList)
								.getList(x -> convertToDoma(x)));
		});
		return resultList;
	}
}
