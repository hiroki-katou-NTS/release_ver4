/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.payment.refundsetting.refundpadding.find;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.report.app.payment.refundsetting.refundpadding.find.dto.RefundPaddingOnceOut;
import nts.uk.ctx.pr.report.app.payment.refundsetting.refundpadding.find.dto.RefundPaddingThreeOut;
import nts.uk.ctx.pr.report.app.payment.refundsetting.refundpadding.find.dto.RefundPaddingTwoOut;
import nts.uk.ctx.pr.report.dom.payment.refundsetting.refundpadding.PrintType;
import nts.uk.ctx.pr.report.dom.payment.refundsetting.refundpadding.RefundPadding;
import nts.uk.ctx.pr.report.dom.payment.refundsetting.refundpadding.RefundPaddingRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * The Class RefundPaddingFinder.
 */
@Stateless
public class RefundPaddingFinder {

	/** The repository. */
	@Inject
	private RefundPaddingRepository repository;

	/**
	 * Find print type three.
	 *
	 * @return the refund padding three out
	 */
	public RefundPaddingThreeOut findPrintTypeThree() {
		// get login user
		LoginUserContext loginUserContext = AppContexts.user();

		// get company code
		String companyCode = loginUserContext.companyCode();

		Optional<RefundPadding> optionalFinder = this.repository.findByPrintType(companyCode,
			PrintType.A4_THREE_PERSON);
		RefundPaddingThreeOut dto = new RefundPaddingThreeOut();
		if (optionalFinder.isPresent()) {
			optionalFinder.get().saveToMemento(dto);
		} else {
			dto.defaultData();
		}
		return dto;
	}

	/**
	 * Find print type two.
	 *
	 * @return the refund padding two out
	 */
	public RefundPaddingTwoOut findPrintTypeTwo() {
		// get login user
		LoginUserContext loginUserContext = AppContexts.user();

		// get company code
		String companyCode = loginUserContext.companyCode();

		Optional<RefundPadding> optionalFinder = this.repository.findByPrintType(companyCode,
			PrintType.A4_TWO_PERSON);
		RefundPaddingTwoOut dto = new RefundPaddingTwoOut();
		if (optionalFinder.isPresent()) {
			optionalFinder.get().saveToMemento(dto);
		} else {
			dto.defaultData();
		}
		return dto;
	}

	/**
	 * Find print type one.
	 *
	 * @return the refund padding once out
	 */
	public RefundPaddingOnceOut findPrintTypeOne() {
		// get login user
		LoginUserContext loginUserContext = AppContexts.user();

		// get company code
		String companyCode = loginUserContext.companyCode();

		Optional<RefundPadding> optionalFinder = this.repository.findByPrintType(companyCode,
			PrintType.A4_ONCE_PERSON);
		RefundPaddingOnceOut dto = new RefundPaddingOnceOut();
		if (optionalFinder.isPresent()) {
			optionalFinder.get().saveToMemento(dto);
		} else {
			dto.defaultData();
		}
		return dto;
	}
}
