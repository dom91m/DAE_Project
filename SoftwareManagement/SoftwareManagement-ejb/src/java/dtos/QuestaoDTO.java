package dtos;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Questao")
@XmlAccessorType(XmlAccessType.FIELD)
public class QuestaoDTO implements Serializable {

    private int id;
    private String titulo;
    private String username;
    private String data;
    private String descricaoConfig;

    public QuestaoDTO() {
    }

    public QuestaoDTO(int id, String titulo, String username, String data) {
        this.id = id;
        this.titulo = titulo;
        this.username = username;
        this.data = data;
        this.descricaoConfig = "";
    }
    
    public QuestaoDTO(int id, String titulo, String username, String data, String descricaoConfig) {
        this.id = id;
        this.titulo = titulo;
        this.username = username;
        this.data = "";
        this.descricaoConfig = descricaoConfig;
    }

    public void reset() {
        setId(0);
        setTitulo(null);
        setUsername(null);
        setData(null);
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

    public String getDescricaoConfig() {
        return descricaoConfig;
    }

    public void setDescricaoConfig(String descricaoConfig) {
        this.descricaoConfig = descricaoConfig;
    }
    
    

}
