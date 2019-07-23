package dtos;

import enumerations.Estado;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Configuracao")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfiguracaoDTO implements Serializable {

    private String descricao;
    private Estado estado;
    private int softwareId;
    private double versao;
    private String softwareName;

    public ConfiguracaoDTO() {
    }

    public ConfiguracaoDTO(String descricao, Estado estado, double versao, int softwareId, String softwareName) {
        this.descricao = descricao;
        this.estado = estado;
        this.softwareId = softwareId;
        this.versao = versao;
        this.softwareName = softwareName;
    }

    public void reset() {
        descricao = null;
        estado = null;
        softwareId = -1;
        versao = 0;
        softwareName = null;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public int getSoftwareId() {
        return softwareId;
    }

    public void setSoftwareId(int softwareId) {
        this.softwareId = softwareId;
    }



    public double getVersao() {
        return versao;
    }

    public void setVersao(double versao) {
        this.versao = versao;
    }

    public String getSoftwareName() {
        return softwareName;
    }

    public void setSoftwareName(String softwareName) {
        this.softwareName = softwareName;
    }
    
    

}
