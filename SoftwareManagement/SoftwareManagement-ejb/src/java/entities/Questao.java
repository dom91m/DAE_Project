package entities;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
@Table(name = "QUESTOES")
@NamedQueries({
    @NamedQuery(name = "getAllQuestoes",
            query = "SELECT s FROM Questao s ORDER BY s.id")})
public class Questao implements Serializable {

    @Id
    @NotNull(message = "'id' nao pode ser vazia")
    private int id;

    @NotNull(message = "'titulo' nao pode ser vazia")
    private String titulo;

    @NotNull(message = "'username' nao pode ser vazia")
    private String username;

    private String data;
    private DateFormat data_aux;

    @OneToMany(mappedBy = "questao", cascade = CascadeType.REMOVE)
    private List<Resposta> respostas;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private Configuracao configuracao;
    
    public Questao(){
    }

    public Questao(int id, String titulo, String username, Configuracao configuracao) {
        this.id = id;
        this.titulo = titulo;
        this.username = username;
        this.configuracao = configuracao;

        this.data_aux = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        this.data = data_aux.format(date);

        respostas = new LinkedList<>();
    }

    public Configuracao getConfiguracao() {
        return configuracao;
    }

    public void setConfiguracao(Configuracao configuracao) {
        this.configuracao = configuracao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<Resposta> getRespostas() {
        return respostas;
    }

    public void setRespostas(List<Resposta> respostas) {
        this.respostas = respostas;
    }

    public void addResposta(Resposta resposta) {
        this.respostas.add(resposta);
    }

    public void removeResposta(Resposta resposta) {
        this.respostas.remove(resposta);
    }
    
}
