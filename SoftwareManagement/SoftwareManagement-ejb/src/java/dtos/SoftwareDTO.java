package dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Software")
@XmlAccessorType(XmlAccessType.FIELD)
public class SoftwareDTO {

    private int id;
    private String name;
    private String descricao;
    private double versao;

    public SoftwareDTO() {
    }

    public SoftwareDTO(int id, String name, String descricao, double versao) {
        this.id = id;
        this.name = name;
        this.descricao = descricao;
        this.versao = versao;
    }

    public void reset() {
        id = id;
        name = null;
        descricao = null;
        versao = 0;
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

}
