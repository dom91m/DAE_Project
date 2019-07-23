package entities;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "DOCUMENTS")
@NamedQueries({
    @NamedQuery(name = "getDocumentsOfConfiguracao", query = "SELECT doc FROM Document doc WHERE doc.configuracao.descricao = :descricao")
})
public class Document implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    private String filepath;

    private String desiredName;
    
    private String mimeType;
    
    @ManyToOne(cascade = CascadeType.REMOVE)
    private Configuracao configuracao;
    
    public Document() {
        
    }

    public Document(String filepath, String desiredName, String mimeType, Configuracao configuracao) {
        this.filepath = filepath;
        this.desiredName = desiredName;
        this.mimeType = mimeType;
        this.configuracao = configuracao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getDesiredName() {
        return desiredName;
    }

    public void setDesiredName(String desiredName) {
        this.desiredName = desiredName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
    
    public Configuracao getConfiguracao() {
        return configuracao;
    }

    public void setConfiguracao(Configuracao configuracao) {
        this.configuracao = configuracao;
    }
}
