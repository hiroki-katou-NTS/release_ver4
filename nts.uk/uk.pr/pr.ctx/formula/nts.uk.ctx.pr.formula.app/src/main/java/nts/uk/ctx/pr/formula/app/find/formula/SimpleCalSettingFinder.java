package nts.uk.ctx.pr.formula.app.find.formula;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.formula.dom.repository.SimpleCalSettingRepository;

@Stateless
public class SimpleCalSettingFinder {
	@Inject
	private SimpleCalSettingRepository simpleCalSettingRepository;
	
	public List<SimpleCalSettingDto> getAll() {
		return this.simpleCalSettingRepository.findAll().stream().map(simpleCalSetting -> SimpleCalSettingDto.fromDomain(simpleCalSetting)).collect(Collectors.toList());
	}
}
