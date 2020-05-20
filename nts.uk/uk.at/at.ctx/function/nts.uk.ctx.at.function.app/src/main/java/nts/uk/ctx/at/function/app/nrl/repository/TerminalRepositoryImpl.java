package nts.uk.ctx.at.function.app.nrl.repository;

import java.util.Optional;

import javax.ejb.Stateless;

/**
 * Terminal repository.
 * 
 * @author manhnd
 */
@Stateless
public class TerminalRepositoryImpl implements TerminalRepository {

	/* (non-Javadoc)
	 * @see nts.uk.nrl.repository.TerminalRepository#get(java.lang.String, java.lang.String)
	 */
	@Override
	public Optional<Terminal> get(String nrlNo, String macAddress) {
		// TODO: Get from DB
		return Optional.ofNullable(new Terminal("0001", "001047FF94D9"));
	}

}
