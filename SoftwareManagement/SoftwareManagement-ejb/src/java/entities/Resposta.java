package entities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "RESPOSTAS")
@NamedQueries({
    @NamedQuery(name = "getAllRespostas",
            query = "SELECT s FROM Resposta s ORDER BY s.id")})
public class Resposta {

    @Id
    @NotNull(message = "id must not be empty")
    private int id;

    private String texto;
    private String username;

    private String data;
    private DateFormat data_aux;

    @ManyToOne
    @JoinColumn(name="QUESTAO_ID")
    private Questao questao;
    
    public Resposta(){}

    public Resposta(int id, String texto, String username, Questao questao) {
        this.id = id;
        this.texto = texto;
        this.username = username;
        this.questao = questao;

        this.data_aux = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        this.data = data_aux.format(date);
    }

    public Questao getQuestao() {
        return questao;
    }

    public void setQuestao(Questao questao) {
        this.questao = questao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
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

}
