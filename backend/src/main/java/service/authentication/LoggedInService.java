package service.authentication;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@SessionScoped
@Named
public class LoggedInService implements Serializable {

    @Inject PasswordHandler passwordHandler;

    private User user;


    public void login(User  loggingInUser) {
        user=loggingInUser;
    }

    public void logout() {
        user = null;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public String getCurrentUserName(){
        return user.getUsername();
    }


    @Produces
    @LoggedIn
    User getCurrentUser() {
        return user;
    }

    public void checkToken(String token) throws Exception{
        if(!token.equals(user.getToken())){
            throw new Exception("user token not valid for the current user");
        }
    }
}