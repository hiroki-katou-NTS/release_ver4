package nts.uk.ctx.at.request.infra.repository.setting.company.vacationapplicationsetting;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.vacationapplicationsetting.HdAppSet;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.vacationapplicationsetting.HdAppSetRepository;
import nts.uk.ctx.at.request.infra.entity.setting.company.vacationapplicationsetting.KrqstHdAppSet;
import nts.uk.ctx.at.request.infra.entity.setting.company.vacationapplicationsetting.KrqstHdAppSetPK;
import nts.uk.shr.com.context.AppContexts;
/**
 * the hd app set repository
 * @author yennth
 *
 */
@Stateless
public class JpaHdAppSetRepository extends JpaRepository implements HdAppSetRepository{
	/**
	 * convert from entity to domain
	 * @param entity
	 * @return
	 * @author yennth
	 */
	private HdAppSet toDomain(KrqstHdAppSet entity){
		HdAppSet domain = HdAppSet.createFromJavaType(entity.krqstHdAppSetPK.companyId, 
				entity.use60h, entity.obstacleName, 
				entity.regisShortLostHd, entity.hdName, entity.regisLackPubHd, entity.changeWrkHour,
				entity.ckuperLimit, entity.actualDisp, entity.wrkHours, entity.pridigCheck, 
				entity.yearHdName, entity.regisNumYear, entity.furikyuName, entity.regisInsuff, 
				entity.useGener, entity.useYear, entity.timeDigest, entity.absenteeism, 
				entity.concheckOutLegal, entity.specialVaca, entity.concheckDateRelease, 
				entity.appDateContra, entity.yearResig, entity.regisShortReser, entity.hdType, entity.displayUnselect, entity.dayDispSet);
		return domain;
	}
	/**
	 * convert from domain to entity
	 * @param domain
	 * @return
	 * @author yennth
	 */
	private static KrqstHdAppSet toEntity(HdAppSet domain){
		val entity = new KrqstHdAppSet();
		entity.krqstHdAppSetPK = new KrqstHdAppSetPK(domain.getCompanyId());
		entity.absenteeism = domain.getAbsenteeism() == null ? null : domain.getAbsenteeism().v();
		entity.actualDisp = domain.getActualDisp().value;
		entity.appDateContra = domain.getAppDateContra().value;
		entity.changeWrkHour = domain.getChangeWrkHour().value;
		entity.ckuperLimit = domain.getCkuperLimit().value;
		entity.concheckDateRelease = domain.getConcheckDateRelease().value;
		entity.concheckOutLegal = domain.getConcheckOutLegal().value;
		entity.furikyuName = domain.getFurikyuName() == null ? null : domain.getFurikyuName().v();
		entity.hdName = domain.getHdName() == null ? null : domain.getHdName().v();
		entity.obstacleName = domain.getObstacleName() == null ? null : domain.getObstacleName().v();
		entity.pridigCheck = domain.getPridigCheck().value;
		entity.regisInsuff = domain.getRegisInsuff().value;
		entity.regisLackPubHd = domain.getRegisLackPubHd().value;
		entity.regisNumYear = domain.getRegisNumYear().value;
		entity.regisShortLostHd = domain.getRegisShortLostHd().value;
		entity.regisShortReser = domain.getRegisShortReser().value;
		entity.specialVaca = domain.getSpecialVaca() == null ? null : domain.getSpecialVaca().v();
		entity.timeDigest = domain.getTimeDigest() == null ? null : domain.getTimeDigest().v();
		entity.use60h = domain.getUse60h().value;
		entity.useGener = domain.getUseGener().value;
		entity.useYear = domain.getUseYear().value;
		entity.wrkHours = domain.getWrkHours().value;
		entity.yearHdName = domain.getYearHdName() == null ? null : domain.getYearHdName().v();
		entity.yearResig = domain.getYearResig() == null ? null : domain.getYearResig().v();
		entity.displayUnselect = domain.getDisplayUnselect().value;
		entity.dayDispSet = domain.getDayDispSet().value;
		return entity;
	}
	
	/**
	 * get hd app set by companyId and hd app type
	 * @author yennth
	 */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public Optional<HdAppSet> getAll() {
		val pk = new KrqstHdAppSetPK(AppContexts.user().companyId());
		return this.queryProxy().find(pk, KrqstHdAppSet.class).map(x -> toDomain(x));
	}
	/**
	 * update hd app set
	 * @author yennth
	 */
	@Override
	public void update(HdAppSet hdAppSet) {
		KrqstHdAppSet entity = toEntity(hdAppSet);
		Optional<KrqstHdAppSet> oldEntity = this.queryProxy().find(entity.krqstHdAppSetPK, KrqstHdAppSet.class);
		if(oldEntity.isPresent()){
			KrqstHdAppSet entityUpdate = oldEntity.get();
			entityUpdate.absenteeism = entity.absenteeism;
			entityUpdate.actualDisp = entity.actualDisp;
			entityUpdate.appDateContra = entity.appDateContra;
			entityUpdate.changeWrkHour = entity.changeWrkHour;
			entityUpdate.ckuperLimit = entity.ckuperLimit;
			entityUpdate.concheckDateRelease = entity.concheckDateRelease;
			entityUpdate.concheckOutLegal = entity.concheckOutLegal;
			entityUpdate.furikyuName = entity.furikyuName;
			entityUpdate.hdName = entity.hdName;
			entityUpdate.obstacleName = entity.obstacleName;
			entityUpdate.pridigCheck = entity.pridigCheck;
			entityUpdate.regisInsuff = entity.regisInsuff;
			entityUpdate.regisLackPubHd = entity.regisLackPubHd;
			entityUpdate.regisNumYear = entity.regisNumYear;
			entityUpdate.regisShortLostHd = entity.regisShortLostHd;
			entityUpdate.regisShortReser = entity.regisShortReser;
			entityUpdate.specialVaca = entity.specialVaca;
			entityUpdate.timeDigest = entity.timeDigest;
			entityUpdate.use60h = entity.use60h;
			entityUpdate.useGener = entity.useGener;
			entityUpdate.useYear = entity.useYear;
			entityUpdate.wrkHours = entity.wrkHours;
			entityUpdate.yearHdName = entity.yearHdName;
			entityUpdate.yearResig = entity.yearResig;
			entityUpdate.displayUnselect = entity.displayUnselect;
			entityUpdate.dayDispSet = entity.dayDispSet;
			this.commandProxy().update(entityUpdate);
		}else{
			this.commandProxy().insert(hdAppSet);
		}
	}
	/**
	 * insert hd app set
	 * @author yennth
	 */
	@Override
	public void insert(HdAppSet hdAppSet) {
		KrqstHdAppSet entity = toEntity(hdAppSet);
		this.commandProxy().insert(entity);
	}
}
