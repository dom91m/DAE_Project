package entities;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table (name="MODULOS")
@NamedQueries({
    @NamedQuery(name = "getAllModulos",
            query = "SELECT s FROM Modulo s ORDER BY s.name")})

public class Modulo implements Serializable {
    
    @Id
    @NotNull(message = "Code must not be empty")
    private int code;

    @NotNull(message = "Extra must have a name")
    private String name;
    
    private String descricao;
    private double versao;

  /*  @ManyToMany
    @JoinTable(name = "MODULO_CONFIGURACAO",
            joinColumns
            = @JoinColumn(name = "MODULO_CODE", referencedColumnName = "CODE"),
            inverseJoinColumns
            = @JoinColumn(name = "CONFIGURACAO_DESCRICAO_2", referencedColumnName = "DESCRICAO"))
    private List<Configuracao> configuracoes; */
    
    @ManyToMany(mappedBy = "modulos")
    private List<Configuracao> configuracoes;

    public Modulo() {
        this.configuracoes = new LinkedList<>();
    }

    public Modulo(int code, String name, String descricao, double versao) {
        this.code = code;
        this.name = name;
        this.descricao = descricao;
        this.versao = versao;
        configuracoes = new LinkedList<>();
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    

}
