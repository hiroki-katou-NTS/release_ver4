package nts.uk.ctx.at.shared.infra.repository.yearholidaygrant;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.LengthServiceRepository;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.LengthServiceTbl;
import nts.uk.ctx.at.shared.infra.entity.yearholidaygrant.KshstLengthServiceTbl;
import nts.uk.ctx.at.shared.infra.entity.yearholidaygrant.KshstLengthServiceTblPK;

@Stateless
public class JpaLengthServiceRepository extends JpaRepository implements LengthServiceRepository{
	private static final String SELECT_BY_CODE = "SELECT c FROM KshstLengthServiceTbl c "
					+ "WHERE c.kshstLengthServiceTblPK.companyId = :companyId "
					+ "AND c.kshstLengthServiceTblPK.yearHolidayCode = :yearHolidayCode ORDER BY c.kshstLengthServiceTblPK.grantNum ";
	
	private static final String DELETE_ALL = "DELETE FROM KshstLengthServiceTbl c "
									+ "WHERE c.kshstLengthServiceTblPK.companyId = :companyId ";
	
	private static final String DELETE_BY_CD = DELETE_ALL + "AND c.kshstLengthServiceTblPK.yearHolidayCode = :yearHolidayCode ";
	
	private static final String DELETE_BY_KEY = DELETE_BY_CD + "AND c.kshstLengthServiceTblPK.grantNum IN :grantNums ";
	/**
	 * convert from KshstLengthServiceTbl entity to LengthServiceTbl domain
	 * @param entity
	 * @return
	 */
	private LengthServiceTbl convertToDomain(KshstLengthServiceTbl entity){
		return LengthServiceTbl.createFromJavaType(entity.kshstLengthServiceTblPK.companyId, 
													entity.kshstLengthServiceTblPK.yearHolidayCode, entity.kshstLengthServiceTblPK.grantNum, 
													entity.allowStatus, entity.standGrantDay, entity.year, entity.month);
	}
	
	private KshstLengthServiceTbl toEntity(LengthServiceTbl domain){
		val entity = new KshstLengthServiceTbl();
		entity.kshstLengthServiceTblPK = new KshstLengthServiceTblPK(domain.getCompanyId(), domain.getYearHolidayCode().v(), domain.getGrantNum().v());
		entity.allowStatus = domain.getAllowStatus().value;
		entity.month = domain.getMonth() != null ? domain.getMonth().v() : 0;
		entity.year = domain.getYear()!= null ? domain.getYear().v() : 0;
		entity.standGrantDay = domain.getStandGrantDay().value;
		return entity;
	}
	/**
	 * find by key
	 * @author yennth
	 */
	@Override
	public Optional<LengthServiceTbl> find(String companyId, String yearHolidayCode, int grantNum) {
		return this.queryProxy().find(new KshstLengthServiceTblPK(companyId, yearHolidayCode, grantNum), KshstLengthServiceTbl.class)
								.map(c -> convertToDomain(c));
	}
	/**
	 * find by code 
	 * @author yennth
	 */
	@Override
	public List<LengthServiceTbl> findByCode(String companyId, String yearHolidayCode) {
		return this.queryProxy().query(SELECT_BY_CODE, KshstLengthServiceTbl.class)
									.setParameter("companyId", companyId)
									.setParameter("yearHolidayCode", yearHolidayCode).getList(c -> convertToDomain(c));
	}
	/**
	 * insert a length service
	 * @author yennth
	 */
	@Override
	public void add(LengthServiceTbl holidayGrant) {
		this.commandProxy().insert(toEntity(holidayGrant));
	}
	/**
	 * update a length service
	 */
	@Override
	public void update(LengthServiceTbl holidayGrant) {
		this.commandProxy().update(toEntity(holidayGrant));
	}
	/**
	 * remove LengthServiceTbl by code 
	 * @author yennth
	 */
	@Override
	public void remove(String companyId, String yearHolidayCode) {
		this.getEntityManager().createQuery(DELETE_BY_CD)
								.setParameter("companyId", companyId)
								.setParameter("yearHolidayCode", yearHolidayCode)
								.executeUpdate();
	}
	/**
	 * delete by companyId
	 * @author yennth
	 */
	@Override
	public void remove(String companyId) {
		this.getEntityManager().createQuery(DELETE_ALL)
								.setParameter("companyId", companyId)
								.executeUpdate();
	}
	/**
	 * remove LengthServiceTbl by list grantNum
	 * @author yennth
	 */
	@Override
	public void remove(String companyId, String yearHolidayCode, List<Integer> grantNums) {
		CollectionUtil.split(grantNums, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			this.getEntityManager().createQuery(DELETE_BY_KEY)
				.setParameter("companyId", companyId)
				.setParameter("yearHolidayCode", yearHolidayCode)
				.setParameter("grantNums", subList)
				.executeUpdate();			
		});
		this.getEntityManager().flush();
	}

	@Override
	public Map<String, List<LengthServiceTbl>> findByCode(String companyId, List<String> yearHolidayCode) {
		if (yearHolidayCode.isEmpty())
			return Collections.emptyMap();
		String query = "SELECT c FROM KshstLengthServiceTbl c "
				+ "WHERE c.kshstLengthServiceTblPK.companyId = :companyId "
				+ "AND c.kshstLengthServiceTblPK.yearHolidayCode IN :yearHolidayCode ORDER BY c.kshstLengthServiceTblPK.grantNum ";
		return this.queryProxy().query(query, KshstLengthServiceTbl.class).setParameter("companyId", companyId)
				.setParameter("yearHolidayCode", yearHolidayCode).getList(c -> convertToDomain(c)).stream()
				.collect(Collectors.groupingBy(i -> i.getYearHolidayCode().v()));
	}

}
