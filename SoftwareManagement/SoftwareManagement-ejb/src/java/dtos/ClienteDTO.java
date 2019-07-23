package dtos;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Cliente")
@XmlAccessorType(XmlAccessType.FIELD)
public class ClienteDTO extends UserDTO implements Serializable{
    
    private String morada;
    private String pessoaDeContacto;

    public ClienteDTO(){
    }

        public ClienteDTO(String username, String password, String name, String email, String morada, String pessoaDeContacto) {
        super(username, password, name, email);
        this.morada = morada;
        this.pessoaDeContacto = pessoaDeContacto;
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
             
    @Override
    public void reset() {
        super.reset();
        setMorada(null);
        setPessoaDeContacto(null);
    }
}
