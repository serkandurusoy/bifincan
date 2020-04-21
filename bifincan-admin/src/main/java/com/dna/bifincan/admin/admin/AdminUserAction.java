package com.dna.bifincan.admin.admin;

import com.dna.bifincan.admin.util.FacesUtils;
import com.dna.bifincan.admin.util.spring.ScopeType;
import com.dna.bifincan.model.user.AdminUser;
import com.dna.bifincan.service.AdminUserService;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.encoding.PasswordEncoder;

@Named("adminUserAction")
@Scope(ScopeType.VIEW)
public class AdminUserAction implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(AdminUserAction.class);

    private static final long serialVersionUID = 2213143053101309957L;

    private LazyDataModel<AdminUser> dataModel;

    private DataTable dataTable;

    private AdminUser user;

    private String username;

    private String password;

    @Inject
    AdminUserService userService;

    @Inject
    private PasswordEncoder passwordEncoder;

    public AdminUserAction() {
    }

    @PostConstruct
    public void initialize() {
        loadDataModel();
    }

    private void loadDataModel() {
        if (this.dataModel == null) {
            this.dataModel = new AdminUserDataModel(userService);
        }
    }

    // --- Action methods and listeners --- //
    public void initAdd() {
        this.user = new AdminUser();
        this.password = null;
        this.username = null;
    }

    public void initEdit(AdminUser adminUser) {
        this.user = adminUser;
        this.password = null;
        this.username = this.user.getUsername();
    }
    boolean dataValid = true;

    private void validateInputData() {
        if (log.isDebugEnabled()) {
            log.debug("validateInputData");
        }
        dataValid = true;

        AdminUser existing = userService.findUserByUsername(this.username);
        if (existing != null && (this.user.getId() == null || this.user.getId().longValue() != existing.getId().longValue())) {
            FacesUtils.addErrorMessage("User existed");
            dataValid = false;
        }

        existing = userService.findUserByEmail(this.user.getEmail());

        if (existing != null && (this.user.getId() == null || this.user.getId().longValue() != existing.getId().longValue())) {
            FacesUtils.addErrorMessage("Email existed");
            dataValid = false;
        }

        if (this.user.getId() != null
                && !this.username.equals(this.user.getUsername())
                && (this.getPassword() == null || this.getPassword().trim().length() == 0)) {

            if (log.isDebugEnabled()) {
                log.debug("dataValid@" + dataValid);
                log.debug("username was changed, the password must be regenerated ");
            }
            FacesUtils.addErrorMessage("Password is required");
            dataValid = false;
        }

    }

    public void save() {
        if (log.isDebugEnabled()) {
            log.debug("input username and password@" + username + "," + password);
            log.debug("user username and password@" + this.user.getUsername() + "," + this.user.getPassword());
        }
        RequestContext context = RequestContext.getCurrentInstance();

        validateInputData();
        context.addCallbackParam("dataValid", dataValid);
        if (!dataValid) {
            return;
        }

        //if it is new user or the username is changed, regenerate the password hash.
        if (this.user.getId() == null || !this.username.equals(this.user.getUsername())) {

            if (log.isDebugEnabled()) {
                log.debug("username was changed, update username and  password@");
            }
            user.setUsername(this.username);
            user.setPassword(this.passwordEncoder.encodePassword(this.getPassword(), user.getSalt()));

        }

        userService.saveUser(this.user);
        this.dataModel = null;
        FacesUtils.addSuccessMessage("User wasd saved successfully.");
    }

    public void delete() {
        userService.deleteUser(this.user);
        this.dataModel = null;
    }

    // --- Getters and Setters --- //
    public AdminUser getUser() {
        return user;
    }

    public void setUser(AdminUser user) {
        this.user = user;
    }

    public LazyDataModel<AdminUser> getDataModel() {
        loadDataModel();
        return dataModel;
    }

    public void setDataModel(LazyDataModel<AdminUser> dataModel) {
        this.dataModel = dataModel;
    }

    public DataTable getDataTable() {
        return dataTable;
    }

    public void setDataTable(DataTable dataTable) {
        this.dataTable = dataTable;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
