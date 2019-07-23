package entities;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "CARGOS")
@NamedQueries({
    @NamedQuery(name = "getAllCargos",
    query = "SELECT c FROM Cargo c ORDER BY c.name"),
    
    @NamedQuery(name = "getAllCargosNames",
    query = "SELECT c.name FROM Cargo c ORDER BY c.name")})

public class Cargo implements Serializable {
    @Id
    private int code;
    
    @NotNull (message= "Nome do cargo nao pode estar vazio")
    private String name;
    
    @OneToMany(mappedBy = "cargo", cascade = CascadeType.REMOVE)
    private List<Admin> admins;
    
    public Cargo(){
        admins = new LinkedList<>();
    }
    
    public Cargo(int code, String name){
        this.code = code;
        this.name = name;
        admins = new LinkedList<>();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Admin> getAdmins() {
        return admins;
    }

    public void setAdmins(List<Admin> admins) {
        this.admins = admins;
    }
    
    public void addAdmin(Admin admin) {
        admins.add(admin);
    }

    public void removeAdmin(Admin admin) {
        admins.remove(admin);
    }

  
}
