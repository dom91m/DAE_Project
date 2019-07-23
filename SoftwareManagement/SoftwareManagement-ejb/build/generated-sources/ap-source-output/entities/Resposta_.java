package entities;

import entities.Questao;
import java.text.DateFormat;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-12-23T15:22:27")
@StaticMetamodel(Resposta.class)
public class Resposta_ { 

    public static volatile SingularAttribute<Resposta, String> texto;
    public static volatile SingularAttribute<Resposta, String> data;
    public static volatile SingularAttribute<Resposta, DateFormat> data_aux;
    public static volatile SingularAttribute<Resposta, Integer> id;
    public static volatile SingularAttribute<Resposta, Questao> questao;
    public static volatile SingularAttribute<Resposta, String> username;

}