/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.wagetable.reference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.query.TypedQueryWrapper;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.gul.text.StringUtil;
import nts.uk.ctx.pr.core.dom.enums.PayBonusAtr;
import nts.uk.ctx.pr.core.dom.enums.SparePayAtr;
import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.PaydayRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.payday.Payday;
import nts.uk.ctx.pr.core.dom.wagetable.reference.WtCodeRef;
import nts.uk.ctx.pr.core.dom.wagetable.reference.WtCodeRefItem;
import nts.uk.ctx.pr.core.dom.wagetable.reference.WtMasterRef;
import nts.uk.ctx.pr.core.dom.wagetable.reference.WtReferenceRepository;

/**
 * The Class JpaWtReferenceRepository.
 */
@Stateless
public class JpaWtReferenceRepository extends JpaRepository implements WtReferenceRepository {

	/** The comma. */
	private final String COMMA = " , ";

	/** The jpa argument prefix. */
	private final String JPA_ARGUMENT_PREFIX = ":";

	/** The payday repository. */
	@Inject
	private PaydayRepository paydayRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.wagetable.reference.WtReferenceRepository#
	 * getMasterRefItem(nts.uk.ctx.pr.core.dom.wagetable.reference.WtMasterRef)
	 */
	@Override
	// TODO: Delayed.
	public List<WtCodeRefItem> getMasterRefItem(WtMasterRef masterRef, YearMonth startMonth) {
		// Create query string.
		StringBuilder strBuilder = new StringBuilder();

		// Add query fields
		strBuilder.append("SELECT ");
		strBuilder.append(masterRef.getWageRefField());
		strBuilder.append(COMMA);
		strBuilder.append(masterRef.getWageRefDispField());

		// from table
		strBuilder.append(" FROM ");
		strBuilder.append(masterRef.getWageRefTable());

		// Add conditions
		strBuilder.append(" WHERE ");

		// Add ref query
		String refQuery = masterRef.getWageRefQuery();
		Map<String, Object> mapValues = new HashMap<>();

		// Check exist condition string
		if (!StringUtil.isNullOrEmpty(refQuery, true)) {
			// Detect argument
			List<String> params = this.detectArguments(refQuery);
			params.stream().forEach(param -> {
				// Check is company code
				if (param.toLowerCase().contains(ParamType.COMPANY_CODE.prefix.toLowerCase())) {
					mapValues.put(param, masterRef.getCompanyCode());
				}

				// Check is base date
				if (param.toLowerCase().contains(ParamType.BASEDATE.prefix.toLowerCase())) {
					mapValues.put(param,
							this.getBaseDate(masterRef.getCompanyCode(), startMonth.v(), param));
				}
			});

			strBuilder.append(refQuery);
		}

		// Create query
		TypedQueryWrapper<Object[]> query = this.queryProxy().query(strBuilder.toString(),
				Object[].class);

		// Set parameter
		if (!StringUtil.isNullOrEmpty(refQuery, true)) {
			// Set parameter
			mapValues.keySet().stream().forEach(item -> {
				query.setParameter(item.replace(JPA_ARGUMENT_PREFIX, ""), mapValues.get(item));
			});
		}

		// Get results
		return query.getList(result -> new WtCodeRefItem((String) result[0], (String) result[1]));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.wagetable.reference.WtReferenceRepository#
	 * getCodeRefItem(nts.uk.ctx.pr.core.dom.wagetable.reference.WtCodeRef)
	 */
	@Override
	// TODO: Delayed.
	public List<WtCodeRefItem> getCodeRefItem(WtCodeRef codeRef) {
		// Create query string.
		StringBuilder strBuilder = new StringBuilder();

		// Add query fields
		strBuilder.append("SELECT ");
		strBuilder.append(codeRef.getWagePersonField());
		strBuilder.append(COMMA);
		strBuilder.append(codeRef.getWagePersonField());

		// from table
		strBuilder.append(" FROM ");
		strBuilder.append(codeRef.getWagePersonTable());

		// Add conditions
		// strBuilder.append(" WHERE ");

		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Create query
		TypedQuery<Object[]> query = em.createQuery(strBuilder.toString(), Object[].class);

		// Get results
		List<Object[]> results = query.getResultList();

		// Convert data
		List<WtCodeRefItem> codeRefItems = results.stream()
				.map(result -> new WtCodeRefItem((String) result[0], (String) result[1]))
				.collect(Collectors.toList());

		// Return
		return codeRefItems;
	}

	/**
	 * Gets the base date.
	 *
	 * @param companyCode
	 *            the company code
	 * @param processingYm
	 *            the processing ym
	 * @param baseDateParam
	 *            the base date param
	 * @return the base date
	 */
	private GeneralDate getBaseDate(String companyCode, Integer processingYm,
			String baseDateParam) {
		// Get processingNo
		Integer processingNo = Integer
				.parseInt(baseDateParam.replace(ParamType.BASEDATE.prefix, ""));

		// Get the base date from db
		Payday payday = paydayRepository.select3(companyCode, processingNo,
				PayBonusAtr.SALARY.value, processingYm, SparePayAtr.NORMAL.value);

		// Return
		return payday.getStdDate();
	}

	/**
	 * Detect arguments.
	 *
	 * @param conditionStr
	 *            the condition str
	 * @return the list
	 */
	private List<String> detectArguments(String conditionStr) {
		List<String> arguments = new ArrayList<>();

		// Detect Jpa prefix :
		String pattern = "(?:^|\\s)(:[^ ]+)";

		// Create a Pattern object
		Pattern r = Pattern.compile(pattern);

		// Now create matcher object.
		Matcher m = r.matcher(conditionStr);

		// Find arguments
		while (m.find()) {
			arguments.add(m.group(1));
		}

		// Return
		return arguments;
	}

}