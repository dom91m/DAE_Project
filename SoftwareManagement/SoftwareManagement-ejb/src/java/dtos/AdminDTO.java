package dtos;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Admin")
@XmlAccessorType(XmlAccessType.FIELD)
public class AdminDTO extends UserDTO implements Serializable{

    private int cargoCode;
    private String cargoName;
   
    public AdminDTO(){
    }

    public AdminDTO(String username, String password, String name, String email, int cargoCode, String cargoName) {
        super(username, password, name, email);
        this.cargoCode = cargoCode;
        this.cargoName = cargoName;
    }
    
    @Override
    public void reset() {
        super.reset();
        setCargoCode(0);
        setCargoName(null);
    }

    public int getCargoCode() {
        return cargoCode;
    }

    public void setCargoCode(int cargoCode) {
        this.cargoCode = cargoCode;
    }

    public String getCargoName() {
        return cargoName;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName;
    }
    

}
