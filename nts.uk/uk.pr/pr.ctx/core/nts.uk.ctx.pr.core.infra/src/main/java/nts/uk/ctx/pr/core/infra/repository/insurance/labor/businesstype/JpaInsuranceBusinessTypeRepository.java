/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.insurance.labor.businesstype;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.insurance.labor.businesstype.BusinessTypeEnum;
import nts.uk.ctx.pr.core.dom.insurance.labor.businesstype.InsuranceBusinessType;
import nts.uk.ctx.pr.core.dom.insurance.labor.businesstype.InsuranceBusinessTypeRepository;
import nts.uk.ctx.pr.core.infra.entity.insurance.labor.businesstype.QismtBusinessType;

/**
 * The Class JpaInsuranceBusinessTypeRepository.
 */
@Stateless
public class JpaInsuranceBusinessTypeRepository extends JpaRepository
	implements InsuranceBusinessTypeRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.businesstype.
	 * InsuranceBusinessTypeRepository#findAll(java.lang.String)
	 */
	@Override
	public List<InsuranceBusinessType> findAll(String companyCode) {

		Optional<QismtBusinessType> optionalQismtBusinessType = this.queryProxy().find(companyCode,
			QismtBusinessType.class);

		// check exist data
		if (optionalQismtBusinessType.isPresent()) {
			return toDomain(optionalQismtBusinessType.get());
		}
		return Collections.emptyList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.businesstype.
	 * InsuranceBusinessTypeRepository#update(java.util.List)
	 */
	@Override
	public void update(List<InsuranceBusinessType> lstInsuranceBusinessType) {
		this.commandProxy().update(toEntity(lstInsuranceBusinessType));
	}

	/**
	 * To entity.
	 *
	 * @param lstInsuranceBusinessType
	 *            the lst insurance business type
	 * @return the qismt business type
	 */
	public QismtBusinessType toEntity(List<InsuranceBusinessType> lstInsuranceBusinessType) {
		QismtBusinessType entity = new QismtBusinessType();

		// to entity data
		lstInsuranceBusinessType.forEach(businessType -> {
			businessType.saveToMemento(
				new JpaInsuranceBusinessTypeSetMemento(entity, businessType.getBizOrder()));
		});

		return entity;
	}

	/**
	 * To domain.
	 *
	 * @param entity
	 *            the entity
	 * @return the list
	 */
	public List<InsuranceBusinessType> toDomain(QismtBusinessType entity) {

		List<InsuranceBusinessType> lstInsuranceBusinessType = new ArrayList<>();
		lstInsuranceBusinessType.add(new InsuranceBusinessType(
			new JpaInsuranceBusinessTypeGetMemento(entity, BusinessTypeEnum.Biz1St)));

		lstInsuranceBusinessType.add(new InsuranceBusinessType(
			new JpaInsuranceBusinessTypeGetMemento(entity, BusinessTypeEnum.Biz2Nd)));

		lstInsuranceBusinessType.add(new InsuranceBusinessType(
			new JpaInsuranceBusinessTypeGetMemento(entity, BusinessTypeEnum.Biz3Rd)));

		lstInsuranceBusinessType.add(new InsuranceBusinessType(
			new JpaInsuranceBusinessTypeGetMemento(entity, BusinessTypeEnum.Biz4Th)));

		lstInsuranceBusinessType.add(new InsuranceBusinessType(
			new JpaInsuranceBusinessTypeGetMemento(entity, BusinessTypeEnum.Biz5Th)));

		lstInsuranceBusinessType.add(new InsuranceBusinessType(
			new JpaInsuranceBusinessTypeGetMemento(entity, BusinessTypeEnum.Biz6Th)));

		lstInsuranceBusinessType.add(new InsuranceBusinessType(
			new JpaInsuranceBusinessTypeGetMemento(entity, BusinessTypeEnum.Biz7Th)));

		lstInsuranceBusinessType.add(new InsuranceBusinessType(
			new JpaInsuranceBusinessTypeGetMemento(entity, BusinessTypeEnum.Biz8Th)));

		lstInsuranceBusinessType.add(new InsuranceBusinessType(
			new JpaInsuranceBusinessTypeGetMemento(entity, BusinessTypeEnum.Biz9Th)));

		lstInsuranceBusinessType.add(new InsuranceBusinessType(
			new JpaInsuranceBusinessTypeGetMemento(entity, BusinessTypeEnum.Biz10Th)));

		return lstInsuranceBusinessType;
	}

}
