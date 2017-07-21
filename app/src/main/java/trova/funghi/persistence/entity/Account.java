package trova.funghi.persistence.entity;

import com.google.firebase.database.Exclude;

import java.util.Map;

/**
 * Created by xid73 on 30/06/2017.
 */

public class Account implements IEntity{
    public static final String CHILD_ACCOUNT = "users";
    public enum ROLE{
        CERCATORE("cercatore"),
        CORSISTA("corsista"),
        SEGNALATORE("segnalatore");
        private String roleName;

        ROLE(String _role){
            this.roleName=_role;
        }

        public String getRoleName(){
            return this.roleName;
        }
    }
    private Long contractEndDate;
    private Boolean policyAccepted;
    private Long policyAcceptedDate;
    private Map<String,Boolean> roles;

    public Map<String, Boolean> getRoles() {
        return roles;
    }

    public void setRoles(Map<String, Boolean> roles) {
        this.roles = roles;
    }

    public Long getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(Long contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public Boolean getPolicyAccepted() {
        return policyAccepted;
    }

    public void setPolicyAccepted(Boolean policyAccepted) {
        this.policyAccepted = policyAccepted;
    }

    public Long getPolicyAcceptedDate() {
        return policyAcceptedDate;
    }

    public void setPolicyAcceptedDate(Long policyAcceptedDate) {
        this.policyAcceptedDate = policyAcceptedDate;
    }

    @Override
    @Exclude
    public String getEntityName() {
        return UserProfile.K_ACCOUNT;
    }
}
