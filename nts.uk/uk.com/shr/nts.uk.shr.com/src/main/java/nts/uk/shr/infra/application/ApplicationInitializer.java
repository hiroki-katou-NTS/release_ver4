package nts.uk.shr.infra.application;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.CDI;

import lombok.extern.slf4j.Slf4j;
import nts.arc.layer.ws.exception.ServerError;
import nts.uk.shr.com.system.config.InitializeWhenDeploy;

@ApplicationScoped
@Slf4j
public class ApplicationInitializer {

	public void initialized(@Observes @Initialized(ApplicationScoped.class) Object event) {
		
		log.info("ApplicationInitializer START");
		
		ServerError.EXPOSES_DEFAILS_OF_ERROR = true;
		
		CDI.current().select(InitializeWhenDeploy.class).forEach(obj -> obj.initialize());

		log.info("ApplicationInitializer END");
	}
}
