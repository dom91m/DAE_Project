package entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "USERS_GROUPS")
public class UserGroup implements Serializable {

    public static enum GROUP {
        ADMIN,
        CLIENTE
    }
    
    @Id
    @Column(name="GROUP_NAME")
    @Enumerated(EnumType.STRING)
    private GROUP groupName;
    @Id
    @OneToOne
    @JoinColumn(name = "USERNAME")
    private User user;

    public UserGroup() {
    }

    public UserGroup(GROUP groupName, User user) {
        this.groupName = groupName;
        this.user = user;
    }
    
    public GROUP getGroupName() {
        return groupName;
    }

    public void setGroupName(GROUP groupName) {
        this.groupName = groupName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.user);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserGroup other = (UserGroup) obj;
        if (!Objects.equals(this.user, other.user)) {
            return false;
        }
        return true;
    }

    
    
    
//    @Override
//    public int hashCode() {
//        int hash = 0;
//        hash += (user != null ? user.hashCode() : 0); //id alterado para user
//        return hash;
//    }

//    @Override
//    public boolean equals(Object object) {
////        // TODO: Warning - this method won't work in the case the id fields are not set
////        if (!(object instanceof UserGroup)) {
////            return false;
////        }
////        UserGroup other = (UserGroup) object;
////        if ((this.user == null && other.user != null) || (this.user != null && !this.user.equals(other.user))) {
////            return false;
////        }
//        return true;
//    }

    @Override
    public String toString() {
        return "entities.UserGroup[ user=" + user + " ]";
    }

}
