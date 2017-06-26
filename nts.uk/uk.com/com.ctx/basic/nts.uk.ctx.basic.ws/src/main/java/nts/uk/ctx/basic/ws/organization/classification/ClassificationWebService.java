package nts.uk.ctx.basic.ws.organization.classification;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.basic.app.command.organization.classification.AddClassificationCommand;
import nts.uk.ctx.basic.app.command.organization.classification.AddClassificationCommandHandler;
import nts.uk.ctx.basic.app.command.organization.classification.RemoveClassificationCommand;
import nts.uk.ctx.basic.app.command.organization.classification.RemoveClassificationCommandHandler;
import nts.uk.ctx.basic.app.command.organization.classification.UpdateClassificationCommand;
import nts.uk.ctx.basic.app.command.organization.classification.UpdateClassificationCommandHandler;
import nts.uk.ctx.basic.app.find.organization.classification.ClassificationDto;
import nts.uk.ctx.basic.app.find.organization.classification.ClassificationFinder;
import nts.uk.shr.com.context.AppContexts;

@Path("basic/organization/classification/")
@Produces(MediaType.APPLICATION_JSON)
public class ClassificationWebService extends WebService {

	@Inject
	private ClassificationFinder classificationFinder;

	@Inject
	private AddClassificationCommandHandler addClassificationCommandHandler;

	@Inject
	private UpdateClassificationCommandHandler updateClassificationCommandHandler;

	@Inject
	private RemoveClassificationCommandHandler removeClassificationCommandHandler;

	@Path("findAllClassification")
	@POST
	public List<ClassificationDto> init() {
		return classificationFinder.getAllClassification();
	}

	@Path("add")
	@POST
	public void add(AddClassificationCommand command) {
		this.addClassificationCommandHandler.handle(command);
	}

	@Path("update")
	@POST
	public void update(UpdateClassificationCommand command) {
		this.updateClassificationCommandHandler.handle(command);
	}

	@Path("remove")
	@POST
	public void remove(RemoveClassificationCommand command) {
		this.removeClassificationCommandHandler.handle(command);
	}

}
