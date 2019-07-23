package dtos;

import entities.Questao;
import java.text.DateFormat;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Resposta")
@XmlAccessorType(XmlAccessType.FIELD)
public class RespostaDTO {

    private int id;
    private String texto;
    private String username;
    private String data;
    private int questaoId;

    public RespostaDTO() {
    }

    public RespostaDTO(int id, String texto, String username, String data, int questaoId) {
        this.id = id;
        this.texto = texto;
        this.username = username;
        this.data = data;
        this.questaoId = questaoId;
    }

    public void reset() {
        id = -1;
        texto = null;
        username = null;
        data = null;
        questaoId = -1;
    }

    public int getId() {
        return id;
    }

    public int getQuestaoId() {
        return questaoId;
    }

    public void setQuestaoId(int questaoId) {
        this.questaoId = questaoId;
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
