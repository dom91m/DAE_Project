package web;

import entities.UserGroup;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Named(value = "userManager")
@ManagedBean
@SessionScoped
public class UserManager {
    private String username;
    private String password;
    boolean loginFlag;
    private static final Logger logger = Logger.getLogger("web.UserManager");
    
    public UserManager() {
        loginFlag = true;
    }
    
    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        // remove data from beans:
        for (String bean : context.getExternalContext().getSessionMap().keySet()) {
            context.getExternalContext().getSessionMap().remove(bean);
        }
        // destroy session:
        HttpSession session
                = (HttpSession) context.getExternalContext().getSession(false);
        session.invalidate();
        // using faces-redirect to initiate a new request:
        loginFlag = false;
        return "/index_login.xhtml?faces-redirect=true";
    }
    
    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request
                = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            request.login(this.username, this.password);
        } catch (ServletException e) {
            logger.log(Level.WARNING, e.getMessage());
            return "login_error?faces-redirect=true";
        }
        if (isUserInRole("ADMIN")) {
            return "/admin/index_menu?faces-redirect=true";
        }
//         if (isUserInRole("CLIENTE")) {
        if (isUserInRole("CLIENTE")) {
            return "/cliente/cliente_configuracoes?faces-redirect=true";
        }
        loginFlag = false;
        return "login_error?faces-redirect=true";
    }
    
    public boolean isUserInRole(UserGroup.GROUP group) {
        return isUserInRole(group.toString());
    }

    public boolean isUserInRole(String role) {
        return (isSomeUserAuthenticated()
                && FacesContext.getCurrentInstance().getExternalContext().isUserInRole(role));
    }

    public boolean isSomeUserAuthenticated() {
        return FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal() != null;
    }    

    public String clearLogin() {
        if (isSomeUserAuthenticated()) {
            logout();
        }
        return "index_login.xhtml?faces-redirect=true";
    }

    public boolean isLoginFlag() {
        return loginFlag;
    }

    public void setLoginFlag(boolean loginFlag) {
        this.loginFlag = loginFlag;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
    
}
