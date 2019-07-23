
package dtos;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Modulo")
@XmlAccessorType(XmlAccessType.FIELD)
public class ModuloDTO implements Serializable {
    
    private int code;
    private String name;
    private String descricao;
    private double versao;
    
    public ModuloDTO(){}

    public ModuloDTO(int code, String name, String descricao, double versao) {
        this.code = code;
        this.name = name;
        this.descricao = descricao;
        this.versao = versao;
    }

    public void reset() {
        setCode(0);
        setName(null);
        setDescricao(null);
        setVersao(0);
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
    
    
    
    public double getVersao() {
        return versao;
    }

    public void setVersao(double versao) {
        this.versao = versao;
    }
    
    
   
}
