package entities;

import entities.UserGroup.GROUP;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "CLIENTES")
@NamedQueries({
    @NamedQuery(name = "getAllClientes",
            query = "SELECT s FROM Cliente s ORDER BY s.name")})
public class Cliente extends User implements Serializable {

    private String morada;
    private String pessoaDeContacto;

    @ManyToMany(mappedBy = "clientes")
    private List<Configuracao> configuracoes;

    public Cliente() {
        configuracoes = new LinkedList<>();
    }

    public Cliente(String username, String password, GROUP group, String name, String email,
            String morada, String pessoaDeContacto) {
        super(username, password, group, name, email);
        this.morada = morada;
        this.pessoaDeContacto = pessoaDeContacto;
        configuracoes = new LinkedList<>();
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public String getPessoaDeContacto() {
        return pessoaDeContacto;
    }

    public void setPessoaDeContacto(String pessoaDeContacto) {
        this.pessoaDeContacto = pessoaDeContacto;
    }

    // ------------------------ CONFIGURACOES DO CLIENTE ------------------------//
    public List<Configuracao> getConfiguracoes() {
        return configuracoes;
    }

    public void setConfiguracoes(List<Configuracao> configuracoes) {
        this.configuracoes = configuracoes;
    }

    public void addConfiguracao(Configuracao configuracao) {
        configuracoes.add(configuracao);
    }

    public void removeConfiguracao(Configuracao configuracao) {
        configuracoes.remove(configuracao);
    }

    // ------------------------ FIM CONFIGURACOES DO CLIENTE ------------------------ //
    @Override
    public String toString() {
        return "Cliente{" + "username=" + username + ""
                + ", password=" + password + ""
                + ", name=" + name + ""
                + ", morada=" + morada + ""
                + ", pessoa de contacto=" + pessoaDeContacto + ""
                + ", email=" + email + '}';
    }
}
