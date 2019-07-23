package entities;

import entities.Configuracao;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-12-23T15:22:27")
@StaticMetamodel(Document.class)
public class Document_ { 

    public static volatile SingularAttribute<Document, String> filepath;
    public static volatile SingularAttribute<Document, Configuracao> configuracao;
    public static volatile SingularAttribute<Document, Integer> id;
    public static volatile SingularAttribute<Document, String> desiredName;
    public static volatile SingularAttribute<Document, String> mimeType;

}