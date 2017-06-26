package nts.uk.ctx.pr.core.dom.personalinfo.commute;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.pr.core.dom.enums.CommuteAtr;
import nts.uk.ctx.pr.core.dom.paymentdata.UseOrNot;
import nts.uk.shr.com.primitive.PersonId;

/**
 * Aggregate Root: Person commute fee
 *
 */
public class PersonalCommuteFee extends AggregateRoot {
	/**
	 * Commute no tax limit number;
	 */
	@Getter
	private String commuteNoTaxLimitPrivateNo;

	/**
	 * Commute no tax limit publish number;
	 */
	@Getter
	private String commuteNoTaxLimitPublishNo;

	@Getter
	private List<PersonalCommuteValue> commutes;

	/**
	 * Personal Id
	 */
	@Getter
	private PersonId personId;

	/**
	 * Constructor with parameter.
	 * @param commuteNoTaxLimitPrivateNo commute No Tax Limit Private No
	 * @param commuteNoTaxLimitPublishNo commute No Tax Limit Publish No
	 * @param commute1 other information
	 * @param commute2 other information
	 * @param commute3 other information
	 * @param commute4 other information
	 * @param commute5 other information
	 * @param personId person id
	 */
	public PersonalCommuteFee(String commuteNoTaxLimitPrivateNo, String commuteNoTaxLimitPublishNo,
			PersonalCommuteValue commute1, PersonalCommuteValue commute2, PersonalCommuteValue commute3,
			PersonalCommuteValue commute4, PersonalCommuteValue commute5, PersonId personId) {
		super();
		this.commuteNoTaxLimitPrivateNo = commuteNoTaxLimitPrivateNo;
		this.commuteNoTaxLimitPublishNo = commuteNoTaxLimitPublishNo;
		this.commutes = new ArrayList<>();
		addCommute(commute1);
		addCommute(commute2);
		addCommute(commute3);
		addCommute(commute4);
		addCommute(commute5);
		this.personId = personId;
	}

	/**
	 * Create instance using Java type parameters.
	 * 
	 * @return PersonalCommuteFee
	 */
	public static PersonalCommuteFee createFromJavaType(String commuteNoTaxLimitPrivateNo,
			String commuteNoTaxLimitPublishNo, PersonalCommuteValue commute1, PersonalCommuteValue commute2,
			PersonalCommuteValue commute3, PersonalCommuteValue commute4, PersonalCommuteValue commute5,
			String personId) {
		return new PersonalCommuteFee(commuteNoTaxLimitPrivateNo, commuteNoTaxLimitPublishNo, commute1, commute2,
				commute3, commute4, commute5, new PersonId(personId));
	}

	/**
	 * Calculate sum commute allowance
	 * @param currentProcessingYm currentProcessing Year month
	 * @return
	 */
	public double sumCommuteAllowance(CommuteAtr commuteAttribute, int currentProcessingYm) {
		double sum = 0;
		for (int i = 0; i < 5; i++) {
			PersonalCommuteValue commute = commutes.get(i);
			boolean match = (commuteAttribute == commute.getCommuteMeansAttribute())
				&& (UseOrNot.USE == commute.getUseOrNot())
				&& ( (currentProcessingYm - commute.getPayStartYm() ) % commute.getCommuteCycle() ) == 0;
				
			if (match) {
				sum += commute.getCommuteAllowance();
			}
		}
		
		return sum;
	}
	
	/**
	 * Calculate sum commute allowance not where Commute Means Attribute
	 * @param currentProcessingYm currentProcessing Year month
	 * @return
	 */
	public double sumCommuteAllowance(int currentProcessingYm) {
		double sum = 0;
		for (int i = 0; i < 5; i++) {
			PersonalCommuteValue commute = commutes.get(i);
				
			sum += commute.getCommuteAllowance();
		}
		
		return sum;
	}
	
	/**
	 * Add commute to list
	 * @param commute
	 */
	private void addCommute(PersonalCommuteValue commute) {
		if (commute != null) {
			this.commutes.add(commute);
		}
	}
}
