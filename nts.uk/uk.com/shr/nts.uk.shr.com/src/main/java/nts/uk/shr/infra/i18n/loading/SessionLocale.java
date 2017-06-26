package nts.uk.shr.infra.i18n.loading;

import java.util.Locale;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;

import nts.arc.i18n.custom.ISessionLocale;
import nts.arc.i18n.custom.LanguageChangedEvent;
import nts.arc.i18n.custom.ResourceChangedEvent;
import nts.gul.text.IdentifierUtil;

@Stateful
public class SessionLocale implements ISessionLocale {
	private Locale currentLocale;
	private long version;

	public SessionLocale() {
		this.currentLocale = Locale.getDefault();
		version = IdentifierUtil.randomUniqueId().hashCode();
	}

	private Event<LanguageChangedEvent> languageChanged;

	@Override
	public Locale getSessionLocale() {
		return this.currentLocale;
	}

	@Override
	public void setSessionLocale(Locale locale) {
		this.currentLocale = locale;

		LanguageChangedEvent event = new LanguageChangedEvent();
		languageChanged.fire(event);

	}

	@Override
	public void resourceChangedEventHandler(ResourceChangedEvent event) {
		version++;
	}

	@Override
	public long getVersion() {
		// TODO Auto-generated method stub
		return this.version;
	}
}
