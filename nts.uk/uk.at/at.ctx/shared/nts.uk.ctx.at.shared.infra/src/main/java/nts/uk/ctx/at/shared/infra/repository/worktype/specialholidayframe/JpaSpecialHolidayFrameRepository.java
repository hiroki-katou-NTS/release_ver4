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
import nts.arc.layer.infra.data.jdbc.NtsStatement;
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

	// fix Response_UK_Thang_5 104
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<SpecialHolidayFrame> findHolidayFrameByListFrame(String companyId, List<Integer> frameNos) {
		if (frameNos.isEmpty())
			return Collections.emptyList();

		String sql = "SELECT * FROM KSHMT_SPHD_FRAME" + " WHERE CID = @companyId" + " AND SPHD_FRAME_NO IN @frameNos";

		NtsStatement statement = new NtsStatement(sql, this.jdbcProxy()).paramString("companyId", companyId)
				.paramInt("frameNos", frameNos);
		List<SpecialHolidayFrame> resultList = statement.getList(x -> {
			KshmtSpecialHolidayFrame frame = new KshmtSpecialHolidayFrame(
					new KshmtSpecialHolidayFramePK(x.getString("CID"), x.getInt("SPHD_FRAME_NO")), x.getString("NAME"),
					x.getInt("ABOLISH_ATR"));
			return convertToDoma(frame);
		});
		return resultList;
	}
}
