package entities;

import entities.Configuracao;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-12-23T15:22:27")
@StaticMetamodel(Software.class)
public class Software_ { 

    public static volatile ListAttribute<Software, Configuracao> configuracoes;
    public static volatile SingularAttribute<Software, String> name;
    public static volatile SingularAttribute<Software, Integer> id;
    public static volatile SingularAttribute<Software, Double> versao;
    public static volatile SingularAttribute<Software, String> descricao;

}