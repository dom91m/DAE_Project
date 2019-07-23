package entities;

import entities.UserGroup.GROUP;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table (name="ADMINS")
@NamedQueries({
    @NamedQuery(name = "getAllAdmins",
    query = "SELECT s FROM Admin s ORDER BY s.name"),
    @NamedQuery(name = "getAllCargoAdmins",
    query = "SELECT s FROM Admin s WHERE s.cargo.code = :cargoCode ORDER BY s.name")})
public class Admin extends User implements Serializable {

    @ManyToOne
    @JoinColumn(name="CARGO_CODE")
    @NotNull (message="A admin must have a cargo")
    private Cargo cargo;

    protected Admin() {
    }

    public Admin(String username, String password, GROUP group, String name, String email, Cargo cargo) {
        super(username, password, group, name, email);
        this.cargo = cargo;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }


    @Override
    public String toString() {
        return "Admin{" + "username=" + username + ""
                + ", password=" + password + ""
                + ", name=" + name + ""
                + ", cargo=" + cargo.getName() + ""
                + ", email=" + email + '}';
    }
}
