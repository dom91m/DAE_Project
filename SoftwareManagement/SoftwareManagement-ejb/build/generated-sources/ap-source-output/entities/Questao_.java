package entities;

import entities.Configuracao;
import entities.Resposta;
import java.text.DateFormat;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-12-23T15:22:27")
@StaticMetamodel(Questao.class)
public class Questao_ { 

    public static volatile SingularAttribute<Questao, String> data;
    public static volatile SingularAttribute<Questao, DateFormat> data_aux;
    public static volatile SingularAttribute<Questao, Configuracao> configuracao;
    public static volatile SingularAttribute<Questao, String> titulo;
    public static volatile SingularAttribute<Questao, Integer> id;
    public static volatile SingularAttribute<Questao, String> username;
    public static volatile ListAttribute<Questao, Resposta> respostas;

}