package entities;

import entities.Cliente;
import entities.Document;
import entities.Modulo;
import entities.Questao;
import entities.Software;
import enumerations.Estado;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-12-23T15:22:27")
@StaticMetamodel(Configuracao.class)
public class Configuracao_ { 

    public static volatile SingularAttribute<Configuracao, Estado> estado;
    public static volatile ListAttribute<Configuracao, Modulo> modulos;
    public static volatile SingularAttribute<Configuracao, Software> software;
    public static volatile ListAttribute<Configuracao, Document> documents;
    public static volatile ListAttribute<Configuracao, Questao> questoes;
    public static volatile ListAttribute<Configuracao, Cliente> clientes;
    public static volatile SingularAttribute<Configuracao, Double> versao;
    public static volatile SingularAttribute<Configuracao, String> descricao;

}