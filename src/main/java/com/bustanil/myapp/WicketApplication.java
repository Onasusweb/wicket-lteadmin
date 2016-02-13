package com.bustanil.myapp;

import com.bustanil.myapp.shared.wicket.LoginPage;
import com.bustanil.myapp.shared.wicket.MainPage;
import com.bustanil.myapp.shared.wicket.WebSession;
import com.bustanil.myapp.shared.wicket.converter.MetricDateConverter;
import com.bustanil.myapp.shared.wicket.themes.AdminLTE;
import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.settings.BootstrapSettings;
import de.agilecoders.wicket.core.settings.SingleThemeProvider;
import org.apache.wicket.ConverterLocator;
import org.apache.wicket.IConverterLocator;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;

import java.util.Date;

public class WicketApplication extends AuthenticatedWebApplication {

	@Override
	public Class<MainPage> getHomePage() {
		return MainPage.class;
	}

	@Override
	public void init() {
		super.init();
		getComponentInstantiationListeners().add(new SpringComponentInjector(this));

		BootstrapSettings settings = new BootstrapSettings();
        settings.setThemeProvider(new SingleThemeProvider(new AdminLTE()));
        Bootstrap.install(this, settings);
	}

	@Override
	protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
		return WebSession.class;
	}

	@Override
	protected Class<? extends WebPage> getSignInPageClass() {
		return LoginPage.class;
	}

	public static WicketApplication getWicketApplication() {
		return ((WicketApplication) get());
	}

	@Override
	protected IConverterLocator newConverterLocator() {
        ConverterLocator newConverterLocator = (ConverterLocator) super.newConverterLocator();
        newConverterLocator.set(Date.class, new MetricDateConverter());
        return newConverterLocator;
    }
}