package entities;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "SOFTWARES")
@NamedQueries({
    @NamedQuery(name = "getAllSoftwares",
    query = "SELECT s FROM Software s ORDER BY s.id")})
public class Software implements Serializable {

    @Id
    private int id;
    @NotNull(message = "Id must not be empty")
    private String name;
    @NotNull(message = "Name must not be empty")
    private String descricao;
    @NotNull(message = "Descricao must not be empty")
    private double versao;
    @NotNull(message = "versao must not be empty")

    
    @OneToMany(mappedBy = "software", cascade = CascadeType.REMOVE)
    private List<Configuracao> configuracoes;

    public Software() {
    }

    public Software(int id, String name, String descricao, double versao) {
        this.id = id;
        this.name = name;
        this.descricao = descricao;
        this.versao = versao;
        configuracoes = new LinkedList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getVersao() {
        return versao;
    }

    public void setVersao(double versao) {
        this.versao = versao;
    }

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

}
