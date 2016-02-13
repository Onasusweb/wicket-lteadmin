package com.bustanil.myapp.shared.wicket;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;
import com.bustanil.myapp.auth.dto.PrivilegeDTO;
import com.bustanil.myapp.auth.dto.PrivilegeGroupDTO;
import com.bustanil.myapp.auth.dto.UserPrivilegeDTO;
import com.bustanil.myapp.auth.model.User;
import com.bustanil.myapp.auth.service.UserManagementService;
import com.bustanil.myapp.shared.wicket.panel.WelcomePanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;

public class MainPage extends SecureWebPage {

    @SpringBean
    UserManagementService userManagementService;
    private WebMarkupContainer contentParent;

    private String screenTitle;
    private String screenSubTitle;
    private Label screenTitleLabel;
    private Label screenSubTitleLabel;
    private FeedbackPanel feedbackError;
    private FeedbackPanel feedbackSuccess;

    private static final Logger LOGGER = LoggerFactory.getLogger(MainPage.class);

    public MainPage() {
        AuthenticatedWebSession session = AuthenticatedWebSession.get();
        if (session.isSignedIn()) {
            add(new Image("userImageRight", new PackageResourceReference(MainPage.class, "img/user.jpg")));
            add(new Image("userImageLeft", new PackageResourceReference(MainPage.class, "img/user.jpg")));
            add(new Image("userImageHeader", new PackageResourceReference(MainPage.class, "img/user.jpg")));

            screenTitleLabel = new Label("screenTitle", PropertyModel.of(this, "screenTitle"));
            screenTitleLabel.setOutputMarkupPlaceholderTag(true);
            add(screenTitleLabel);
            screenSubTitleLabel = new Label("screenSubTitle", PropertyModel.of(this, "screenSubTitle"));
            screenSubTitleLabel.setOutputMarkupPlaceholderTag(true);
            add(screenSubTitleLabel);

            User user = (User) session.getAttribute("user");
            add(new Label("userRoleNameRight", user.getName() + " - " + user.getRole().getName()));
            add(new Label("userRoleNamePopup", user.getName() + " - " + user.getRole().getName()));
            add(new Label("userNameLeft", user.getName()));
            renderMenu(user.getKode());

            add(new Link("logOut") {

                @Override
                public void onClick() {
                    AuthenticatedWebSession.get().invalidate();
                    setResponsePage(getApplication().getHomePage());
                }
            });
            contentParent = new WebMarkupContainer("contentParent");
            contentParent.setOutputMarkupPlaceholderTag(true);
            contentParent.add(new WelcomePanel("content"));
            add(contentParent);

            feedbackError = new FeedbackPanel("feedbackError", (IFeedbackMessageFilter) (IFeedbackMessageFilter) (feedbackMessage) -> feedbackMessage.getLevel() == FeedbackMessage.ERROR){
                @Override
                public boolean isVisible() {
                    return !getCurrentMessages().isEmpty();
                }
            };
            feedbackError.add(new CssClassNameAppender("callout callout-danger"));
            feedbackError.setOutputMarkupPlaceholderTag(true);
            add(feedbackError);

            feedbackSuccess = new FeedbackPanel("feedbackSuccess", (IFeedbackMessageFilter) (IFeedbackMessageFilter) (feedbackMessage) -> feedbackMessage.getLevel() == FeedbackMessage.SUCCESS){
                @Override
                public boolean isVisible() {
                    return !getCurrentMessages().isEmpty();
                }
            };
            feedbackSuccess.add(new CssClassNameAppender("callout callout-success"));
            feedbackSuccess.setOutputMarkupPlaceholderTag(true);
            add(feedbackSuccess);

            add(new Link("topLeftLogo") {
                @Override
                public void onClick() {
                    setResponsePage(MainPage.class);
                }
            });
        }

    }

    private void renderMenu(String kodeUser) {
        UserPrivilegeDTO privileges = userManagementService.findPrivilegeGroupByKode(kodeUser);
        add(new ListView<PrivilegeGroupDTO>("topMenu", privileges.getPrivilegeGroups()) {
            @Override
            protected void populateItem(final ListItem<PrivilegeGroupDTO> level1) {
                level1.add(new Label("topMenuLabel", new PropertyModel(level1.getModel(), "name")));
                level1.add(new ListView<PrivilegeDTO>("menu", level1.getModel().getObject().getPrivileges()) {
                    @Override
                    protected void populateItem(final ListItem<PrivilegeDTO> level2) {
                        AjaxLink menuLink = new AjaxLink<Void>("menuLink") {
                            @Override
                            public void onClick(AjaxRequestTarget target) {
                                try {
                                    String className = level2.getModelObject().getPath();

                                    setScreenTitle(level1.getModelObject().getName());
                                    setScreenSubTitle(level2.getModelObject().getName());

                                    Class<?> panelClass = Class.forName(className);
                                    Constructor<?> panelClassConstructor = panelClass.getConstructor(String.class);

                                    Panel content = (Panel) panelClassConstructor.newInstance("content");
                                    content.setOutputMarkupPlaceholderTag(true);
                                    MainPage.this.replaceContent(target, content);
                                    refreshFeedback(target);
                                } catch (Exception e) {
                                    LOGGER.error("Failed to render panel", e);
                                }

                            }
                        };
                        menuLink.add(new Label("menuLabel", new PropertyModel(level2.getModel(), "name")));
                        level2.add(menuLink);
                    }
                });
            }
        });
    }

    private void replaceContent(AjaxRequestTarget target, Panel panel) {
        contentParent.addOrReplace(panel);
        target.add(contentParent);
        target.add(screenTitleLabel);
        target.add(screenSubTitleLabel);
    }

    public String getScreenTitle() {
        return screenTitle;
    }

    public void setScreenTitle(String screenTitle) {
        this.screenTitle = screenTitle;
    }

    public String getScreenSubTitle() {
        return screenSubTitle;
    }

    public void setScreenSubTitle(String screenSubTitle) {
        this.screenSubTitle = screenSubTitle;
    }

    public void refreshFeedback(AjaxRequestTarget target) {
        target.add(feedbackSuccess);
        target.add(feedbackError);
    }

    public void openPanel(AjaxRequestTarget target, Panel content) {
        replaceContent(target, content);
        refreshFeedback(target);
    }
}
