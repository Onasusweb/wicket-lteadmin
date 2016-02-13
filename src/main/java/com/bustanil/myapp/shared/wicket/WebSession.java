package com.bustanil.myapp.shared.wicket;

import com.bustanil.myapp.auth.model.User;
import com.bustanil.myapp.auth.service.UserManagementService;
import com.bustanil.myapp.WicketApplication;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class WebSession extends AuthenticatedWebSession {

    public WebSession(Request request) {
        super(request);
    }

    public static WebSession get()
    {
        return (WebSession) Session.get();
    }

    public User getUser(){
        return (User) getAttribute("user");
    }

    @Override
    protected boolean authenticate(String username, String password) {
        WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(WicketApplication.getWicketApplication().getServletContext());
        UserManagementService userManagementService = applicationContext.getBean(UserManagementService.class);
        User user = userManagementService.getUser(username);
        boolean authenticated = user != null && user.getPassword().equals(password);
        if (authenticated) {
            setAttribute("user", user);
        }
        return authenticated;
    }

    @Override
    public Roles getRoles() {
        return null;
    }

}
