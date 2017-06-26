package nts.uk.ctx.pr.core.dom.paymentdata.repository;

import java.util.Optional;

import nts.uk.ctx.pr.core.dom.paymentdata.CommuNotaxLimit;

public interface CommuNotaxLimitRepository {
	Optional<CommuNotaxLimit> find(String ccd, String commuNotaxLimitCode);
}
