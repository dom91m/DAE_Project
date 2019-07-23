package dtos;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Cargo")
@XmlAccessorType(XmlAccessType.FIELD)
public class CargoDTO implements Serializable{

    private int code;
    private String name;
    
    public CargoDTO(){
    }
    
    public CargoDTO(int code, String name){
        this.code = code;
        this.name = name;
    }
    
    public void reset(){
        code = 0;
        name = null;
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

}
