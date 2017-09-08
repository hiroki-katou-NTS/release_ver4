package nts.uk.ctx.at.shared.infra.repository.specialholiday;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.shared.dom.specialholiday.grantdate.GrantDateCom;
import nts.uk.ctx.at.shared.dom.specialholiday.grantdate.GrantDatePer;
import nts.uk.ctx.at.shared.dom.specialholiday.grantdate.GrantDatePerSet;
import nts.uk.ctx.at.shared.dom.specialholiday.grantdate.GrantDateSet;
import nts.uk.ctx.at.shared.dom.specialholiday.grantday.GrantRegular;
import nts.uk.ctx.at.shared.dom.specialholiday.grantday.GrantRegularRepository;
import nts.uk.ctx.at.shared.infra.entity.specialholiday.KshstGrantDateCom;
import nts.uk.ctx.at.shared.infra.entity.specialholiday.KshstGrantDateComPK;
import nts.uk.ctx.at.shared.infra.entity.specialholiday.KshstGrantDatePer;
import nts.uk.ctx.at.shared.infra.entity.specialholiday.KshstGrantDatePerPK;
import nts.uk.ctx.at.shared.infra.entity.specialholiday.KshstGrantDatePerSet;
import nts.uk.ctx.at.shared.infra.entity.specialholiday.KshstGrantDatePerSetPK;
import nts.uk.ctx.at.shared.infra.entity.specialholiday.KshstGrantDateSet;
import nts.uk.ctx.at.shared.infra.entity.specialholiday.KshstGrantDateSetPK;
import nts.uk.ctx.at.shared.infra.entity.specialholiday.KshstGrantRegular;

@Stateless
public class JpaGrantRegularRepository extends JpaRepository implements GrantRegularRepository {
	private static final String SELECT_ALL;

	private static final String SELECT_ALL_COM;
	
	private static final String SELECT_ALL_SET;
	
	private static final String SELECT_ALL_PER_SET;

	static {

		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT e");
		builderString.append(" FROM KshstGrantRegular e");
		builderString.append(" WHERE e.kshstGrantRegularPK.companyId = :companyId");
		builderString.append(" AND e.kshstGrantRegularPK.specialHolidayCode = :specialHolidayCode");
		SELECT_ALL = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT e");
		builderString.append(" FROM KshstGrantDateCom e");
		builderString.append(" WHERE e.kshstGrantDateComPK.companyId = :companyId");
		builderString.append(" AND e.kshstGrantDateComPK.specialHolidayCode = :specialHolidayCode");
		SELECT_ALL_COM = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT e");
		builderString.append(" FROM KshstGrantDateSet e");
		builderString.append(" WHERE e.kshstGrantDateSetPK.companyId = :companyId");
		builderString.append(" AND e.kshstGrantDateSetPK.specialHolidayCode = :specialHolidayCode");
		SELECT_ALL_SET = builderString.toString();
		
		builderString = new StringBuilder();
		builderString.append("SELECT e");
		builderString.append(" FROM KshstGrantDatePerSet e");
		builderString.append(" WHERE e.kshstGrantDatePerSetPK.companyId = :companyId");
		builderString.append(" AND e.kshstGrantDatePerSetPK.specialHolidayCode = :specialHolidayCode");
		builderString.append(" AND e.kshstGrantDatePerSetPK.personalGrantDateCode = :personalGrantDateCode");
		SELECT_ALL_PER_SET = builderString.toString();
	}

	/**
	 * Convert to Domain Grant Regular
	 * 
	 * @param kshstGrantRegular
	 * @return
	 */
	private GrantRegular convertToDomain(KshstGrantRegular kshstGrantRegular) {
		GrantRegular grantRegular = GrantRegular.createFromJavaType(kshstGrantRegular.kshstGrantRegularPK.companyId,
				kshstGrantRegular.kshstGrantRegularPK.specialHolidayCode, kshstGrantRegular.grantStartDate,
				kshstGrantRegular.months, kshstGrantRegular.years, kshstGrantRegular.grantRegularMethod);
		return grantRegular;
	}

	/**
	 * Convert to Domain Grant Date Company
	 * 
	 * @param kshstGrantDateCom
	 * @return
	 */
	private GrantDateCom convertToDomainCom(KshstGrantDateCom kshstGrantDateCom) {
		List<GrantDateSet> grantDateSets = kshstGrantDateCom.grantDateSets.stream().map(x -> convertToDomainSet(x))
				.collect(Collectors.toList());
		GrantDateCom grantDateCom = GrantDateCom.createFromJavaType(kshstGrantDateCom.kshstGrantDateComPK.companyId,
				kshstGrantDateCom.kshstGrantDateComPK.specialHolidayCode, kshstGrantDateCom.grantDateAtr,
				kshstGrantDateCom.grantDate, grantDateSets);
		return grantDateCom;
	}

	/**
	 * Convert to Domain Grant Date Setting
	 * 
	 * @param kshstGrantDateCom
	 * @return
	 */
	private GrantDateSet convertToDomainSet(KshstGrantDateSet kshstGrantDateSet) {
		GrantDateSet grantDateSet = GrantDateSet.createFromJavaType(kshstGrantDateSet.kshstGrantDateSetPK.companyId,
				kshstGrantDateSet.kshstGrantDateSetPK.specialHolidayCode,
				kshstGrantDateSet.kshstGrantDateSetPK.grantDateNo, 
				kshstGrantDateSet.grantDateM,
				kshstGrantDateSet.grantDateY);
		return grantDateSet;
	}

	/**
	 * Convert to Database Type Grant Date Company
	 * 
	 * @param grantDateCom
	 * @return
	 */
	private KshstGrantDateCom convertToDbTypeCom(GrantDateCom grantDateCom) {
		KshstGrantDateCom kshstGrantDateCom = new KshstGrantDateCom();
		KshstGrantDateComPK kshstGrantDateComPK = new KshstGrantDateComPK(grantDateCom.getCompanyId(),
				grantDateCom.getSpecialHolidayCode().v());
		kshstGrantDateCom.grantDateAtr = grantDateCom.getGrantDateAtr().value;
		kshstGrantDateCom.grantDate = grantDateCom.getGrantDate().v();
		kshstGrantDateCom.kshstGrantDateComPK = kshstGrantDateComPK;

		List<KshstGrantDateSet> dateSetList = grantDateCom.getGrantDateSets().stream().map(x -> convertToDbTypeSet(x))
				.collect(Collectors.toList());
		kshstGrantDateCom.grantDateSets = dateSetList;

		return kshstGrantDateCom;
	}

	/**
	 * Convert to Database Type Grant Date Setting
	 * 
	 * @param grantDateSet
	 * @return
	 */
	private KshstGrantDateSet convertToDbTypeSet(GrantDateSet grantDateSet) {
		KshstGrantDateSet kshstGrantDateSet = new KshstGrantDateSet();
		KshstGrantDateSetPK kshstGrantDateSetPK = new KshstGrantDateSetPK(grantDateSet.getCompanyId(),
				grantDateSet.getSpecialHolidayCode().v(), grantDateSet.getGrantDateNo());
		kshstGrantDateSet.grantDateM = grantDateSet.getGrantDateMonth().v();
		kshstGrantDateSet.grantDateY = grantDateSet.getGrantDateYear().v();
		kshstGrantDateSet.kshstGrantDateSetPK = kshstGrantDateSetPK;
		return kshstGrantDateSet;
	}

	/**
	 * Find all Grant Regular by CompanyId & SpecialHolidayCode
	 */
	@Override
	public List<GrantRegular> findAll(String companyId, String specialHolidayCode) {
		return this.queryProxy().query(SELECT_ALL, KshstGrantRegular.class).setParameter("companyId", companyId)
				.setParameter("specialHolidayCode", specialHolidayCode).getList(c -> convertToDomain(c));
	}

	/**
	 * Find all Grant Date Company
	 */
	@Override
	public List<GrantDateCom> findAllCom(String companyId, String specialHolidayCode) {
		return this.queryProxy().query(SELECT_ALL_COM, KshstGrantDateCom.class).setParameter("companyId", companyId)
				.setParameter("specialHolidayCode", specialHolidayCode).getList(c -> convertToDomainCom(c));
	}

	/**
	 * Add Grant Date Company
	 */
	@Override
	public void add(GrantDateCom grantDateCom) {
		this.commandProxy().insert(convertToDbTypeCom(grantDateCom));
	}

	@Override
	public Optional<GrantDateCom> getComByCode(String companyId, String specialHolidayCode) {
		return this.queryProxy().find(new KshstGrantDateComPK(companyId, specialHolidayCode), KshstGrantDateCom.class)
				.map(x -> convertToDomainCom(x));
	}

	@Override
	public List<GrantDateSet> getSetByCode(String companyId, String specialHolidayCode) {
		return this.queryProxy().query(SELECT_ALL_SET, KshstGrantDateSet.class)
				.setParameter("companyId", companyId)
				.setParameter("specialHolidayCode", specialHolidayCode)
				.getList(c -> convertToDomainSet(c));
	}

	@Override
	public void update(GrantDateCom grantDateCom) {
		KshstGrantDateComPK key = new KshstGrantDateComPK(grantDateCom.getCompanyId(), grantDateCom.getSpecialHolidayCode().v());
		Optional<KshstGrantDateCom> entity = this.queryProxy().find(key, KshstGrantDateCom.class);
		KshstGrantDateCom kshstGrantDateCom = entity.get();
		kshstGrantDateCom.grantDateAtr = grantDateCom.getGrantDateAtr().value;
		kshstGrantDateCom.grantDate = grantDateCom.getGrantDate().v();
				
		List<KshstGrantDateSet> grantDateSets = grantDateCom.getGrantDateSets().stream()
				.map(x -> {
					KshstGrantDateSetPK keyDateSet = new KshstGrantDateSetPK(grantDateCom.getCompanyId(), grantDateCom.getSpecialHolidayCode().v(), x.getGrantDateNo());
					return new KshstGrantDateSet(keyDateSet, x.getGrantDateMonth().v(), x.getGrantDateYear().v());
				}).collect(Collectors.toList());
		
		kshstGrantDateCom.grantDateSets = grantDateSets;
		
		this.commandProxy().update(kshstGrantDateCom);
	}

	@Override
	public Optional<GrantDatePer> getPerByCode(String companyId, String specialHolidayCode, String personalGrantDateCode) {
		return this.queryProxy().find(new KshstGrantDatePerPK(companyId, specialHolidayCode, personalGrantDateCode), KshstGrantDatePer.class)
				.map(x -> convertToDomainPer(x));
	}

	private GrantDatePer convertToDomainPer(KshstGrantDatePer kshstGrantDatePer) {
		List<GrantDatePerSet> grantDatePerSet = kshstGrantDatePer.grantDatePerSet.stream().map(x -> convertToDomainPerSet(x))
				.collect(Collectors.toList());
		
		GrantDatePer grantDatePer = GrantDatePer.createSimpleFromJavaType(kshstGrantDatePer.kshstGrantDatePerPK.companyId,
				kshstGrantDatePer.kshstGrantDatePerPK.specialHolidayCode, kshstGrantDatePer.kshstGrantDatePerPK.personalGrantDateCode,
				kshstGrantDatePer.personalGrantDateName, kshstGrantDatePer.grantDate, kshstGrantDatePer.grantDateAtr, grantDatePerSet);
		return grantDatePer;
	}

	@Override
	public List<GrantDatePerSet> getPerSetByCode(String companyId, String specialHolidayCode, String personalGrantDateCode) {
		return this.queryProxy().query(SELECT_ALL_PER_SET, KshstGrantDatePerSet.class)
				.setParameter("companyId", companyId)
				.setParameter("specialHolidayCode", specialHolidayCode)
				.setParameter("personalGrantDateCode", personalGrantDateCode)
				.getList(c -> convertToDomainPerSet(c));
	}

	private GrantDatePerSet convertToDomainPerSet(KshstGrantDatePerSet kshstGrantDatePerSet) {
		GrantDatePerSet grantDatePerSet = GrantDatePerSet.createSimpleFromJavaType(kshstGrantDatePerSet.kshstGrantDatePerSetPK.companyId,
				kshstGrantDatePerSet.kshstGrantDatePerSetPK.specialHolidayCode,
				kshstGrantDatePerSet.kshstGrantDatePerSetPK.personalGrantDateCode,
				kshstGrantDatePerSet.kshstGrantDatePerSetPK.grantDateNo,
				kshstGrantDatePerSet.grantDateMonth,
				kshstGrantDatePerSet.grantDateYear);
		return grantDatePerSet;
	}

	@Override
	public void addPer(GrantDatePer grantDatePer) {
		this.commandProxy().insert(convertToDbPer(grantDatePer));
	}

	private KshstGrantDatePer convertToDbPer(GrantDatePer grantDatePer) {
		KshstGrantDatePer kshstGrantDatePer = new KshstGrantDatePer();
		KshstGrantDatePerPK kshstGrantDatePerPK = new KshstGrantDatePerPK(grantDatePer.getCompanyId(),
				grantDatePer.getSpecialHolidayCode(), grantDatePer.getPersonalGrantDateCode().v());
		kshstGrantDatePer.personalGrantDateName = grantDatePer.getPersonalGrantDateName().v();
		kshstGrantDatePer.grantDate = grantDatePer.getGrantDate().v();
		kshstGrantDatePer.grantDateAtr = grantDatePer.getGrantDateAtr().value;

		List<KshstGrantDatePerSet> grantDatePerSet = grantDatePer.getGrantDatePerSet().stream().map(x -> convertToDbPerSet(x))
				.collect(Collectors.toList());
		
		kshstGrantDatePer.grantDatePerSet = grantDatePerSet;
		kshstGrantDatePer.kshstGrantDatePerPK = kshstGrantDatePerPK;

		return kshstGrantDatePer;
	}

	private KshstGrantDatePerSet convertToDbPerSet(GrantDatePerSet grantDatePerSet) {
		KshstGrantDatePerSet kshstGrantDatePerSet = new KshstGrantDatePerSet();
		KshstGrantDatePerSetPK kshstGrantDatePerSetPK = new KshstGrantDatePerSetPK(grantDatePerSet.getCompanyId(),
				grantDatePerSet.getSpecialHolidayCode(), grantDatePerSet.getPersonalGrantDateCode().v(), grantDatePerSet.getGrantDateNo());
		kshstGrantDatePerSet.grantDateMonth = grantDatePerSet.getGrantDateMonth().v();
		kshstGrantDatePerSet.grantDateYear = grantDatePerSet.getGrantDateYear().v();
		kshstGrantDatePerSet.kshstGrantDatePerSetPK = kshstGrantDatePerSetPK;
		
		return kshstGrantDatePerSet;
	}

	@Override
	public void updatePer(GrantDatePer grantDatePer) {
		KshstGrantDatePerPK key = new KshstGrantDatePerPK(grantDatePer.getCompanyId(), grantDatePer.getSpecialHolidayCode(), grantDatePer.getPersonalGrantDateCode().v());
		Optional<KshstGrantDatePer> entity = this.queryProxy().find(key, KshstGrantDatePer.class);
		KshstGrantDatePer kshstGrantDatePer = entity.get();
		kshstGrantDatePer.personalGrantDateName = grantDatePer.getPersonalGrantDateName().v();
		kshstGrantDatePer.grantDate = grantDatePer.getGrantDate().v();
		kshstGrantDatePer.grantDateAtr = grantDatePer.getGrantDateAtr().value;
		
		List<KshstGrantDatePerSet> grantDatePerSet = grantDatePer.getGrantDatePerSet().stream()
				.map(x -> {
					KshstGrantDatePerSetPK keyDateSet = new KshstGrantDatePerSetPK(grantDatePer.getCompanyId(), grantDatePer.getSpecialHolidayCode(), 
							grantDatePer.getPersonalGrantDateCode().v(), x.getGrantDateNo());
					return new KshstGrantDatePerSet(keyDateSet, x.getGrantDateMonth().v(), x.getGrantDateYear().v());
				}).collect(Collectors.toList());
		
		kshstGrantDatePer.grantDatePerSet = grantDatePerSet;
		
		this.commandProxy().update(kshstGrantDatePer);
	}
}
