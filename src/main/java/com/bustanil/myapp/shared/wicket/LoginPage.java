package com.bustanil.myapp.shared.wicket;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.PackageResourceReference;

public class LoginPage extends WebPage {

    private FeedbackPanel feedbackPanel;
    private final TextField<String> kodeUserField;
    private final PasswordTextField passwordTextField;
    private boolean showLoader = false;

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
    }

    public LoginPage() {
        feedbackPanel = new FeedbackPanel("feedback");
        feedbackPanel.setOutputMarkupPlaceholderTag(true);
        add(feedbackPanel);
        StatelessForm<?> loginForm = new StatelessForm<Void>("loginForm");
        add(loginForm);

        final Image loader = new Image("loader", new PackageResourceReference(getClass(), "img/loader.gif")){

            @Override
            public boolean isVisible() {
                return showLoader;
            }
        };
        loader.setOutputMarkupPlaceholderTag(true);

        loginForm.add(loader);

        kodeUserField = new TextField<>("kodeUser");
        loginForm.add(kodeUserField);
        passwordTextField = new PasswordTextField("password");
        loginForm.add(passwordTextField);

        AjaxButton ajaxButton = new AjaxButton("loginButton", Model.of("Login")) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                boolean authenticated = AuthenticatedWebSession.get().signIn(kodeUserField.getRawInput(), passwordTextField.getRawInput());
                setShowLoader(false);
                if (authenticated) {
                    continueToOriginalDestination();
                    setResponsePage(MainPage.class);
                } else {
                    error("Invalid username or password");
                    target.add(feedbackPanel);
                    target.add(loader);
                }
            }
        };
        ajaxButton.setDefaultFormProcessing(false);
        ajaxButton.add(new AjaxEventBehavior("click") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                setShowLoader(true);
                target.add(loader);
            }
        });
        loginForm.add(ajaxButton);
        loginForm.setDefaultButton(ajaxButton);

    }

    public boolean isShowLoader() {
        return showLoader;
    }

    public void setShowLoader(boolean showLoader) {
        this.showLoader = showLoader;
    }
}
