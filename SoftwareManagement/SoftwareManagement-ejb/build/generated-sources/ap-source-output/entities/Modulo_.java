package entities;

import entities.Configuracao;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-12-23T15:22:27")
@StaticMetamodel(Modulo.class)
public class Modulo_ { 

    public static volatile SingularAttribute<Modulo, Integer> code;
    public static volatile ListAttribute<Modulo, Configuracao> configuracoes;
    public static volatile SingularAttribute<Modulo, String> name;
    public static volatile SingularAttribute<Modulo, Double> versao;
    public static volatile SingularAttribute<Modulo, String> descricao;

}