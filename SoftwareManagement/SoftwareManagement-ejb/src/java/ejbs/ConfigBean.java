package ejbs;

import entities.UserGroup;
import entities.UserGroup.GROUP;
import static enumerations.Estado.ATIVO;
import static enumerations.Estado.INATIVO;
import static enumerations.Estado.SUSPENSO;

import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class ConfigBean {

    private static final Logger logger = Logger.getLogger("ejbs.ConfigBean");

    @EJB
    private AdminBean adminBean;
    @EJB
    private CargoBean cargoBean;
    @EJB
    private ClienteBean clienteBean;

    // CONFIGURAÇÕES
    @EJB
    private ConfiguracaoBean configuracaoBean;
    @EJB
    private ModuloBean moduloBean;

    //SOFTWARES
    @EJB
    private SoftwareBean softwareBean;

    // FORUM DE DISCUSSAO
    @EJB
    private QuestaoBean questaoBean;
    @EJB
    private RespostaBean respostaBean;
    
    @EJB
    private EmailBean emailBean;

    @PostConstruct
    public void populateBD() {

        try {
            // enviar email
            String mensagemEmail = "<h2 style=\"color: #2e6c80;\">Segue-se um resumo do estado atual da configuração:</h2>\n" +
                "<p>\n" +
                "  Descrição: <br />\n" +
                "  Estado: <br />\n" +
                "  SoftwareId: (incluir?) <br />\n" +
                "  Versão: <br />\n" +
                "  Nome do software:\n" +
                "</p>\n" +
                "\n" +
                "<br />\n <p><h3>\n Cumprimentos,\n </h3></p>\n <p><h3>\n Software Management 2018/2019\n </h3></p>";
            // emailBean.sendEmail("danffaustino@gmail.com", mensagemEmail, "mensagemconfigbean");
            
            //criar os softwares
            softwareBean.create(1, "Office 2019", "Software para tratamento de texto.", 1.23);
            softwareBean.create(2, "Avast 2018", "Anti-virus", 2.89);
            softwareBean.create(3, "Google Chrome", "Browser", 31.2);

            cargoBean.create(1, "CONTABILISTA");
            cargoBean.create(2, "PRESIDENTE");
            cargoBean.create(3, "ANALISTA");
            cargoBean.create(4, "GESTOR");

            adminBean.create("Joao_admin", "admin1", GROUP.ADMIN, "admin1", "admin1@ipleiria.pt", 1);
            adminBean.create("Marcelo_admin", "admin2", GROUP.ADMIN, "admin2", "admin2@ipleiria.pt", 2);
            adminBean.create("Daniel_admin", "admin3", GROUP.ADMIN, "admin3", "admin3@ipleiria.pt", 3);

            clienteBean.create("Jose_cliente", "cliente1", GROUP.CLIENTE, "clienteEmpresa1", "danffaustino@gmail.com", "Rua A", "Manel");
            clienteBean.create("Maria_cliente", "cliente2", GROUP.CLIENTE, "clienteEmpresa2", "2151311@my.ipleiria.pt", "Rua B", "Luis");

            configuracaoBean.createInicial("Template_1", ATIVO, 1.37, 1);
            configuracaoBean.createInicial("Template_2", INATIVO, 2.67, 2);

            moduloBean.create(1, "Modulo_1", "descricao_1", 1.37);
            moduloBean.create(2, "Modulo_2", "descricao_2", 1.37);
            moduloBean.create(3, "Modulo_3", "descricao_3", 1.37);

            //colocar as configuracoes nos clientes ("descricao da config" no "cliente")
            configuracaoBean.enrollConfiguracao("Template_1", "Maria_cliente");
            configuracaoBean.enrollConfiguracao("Template_1", "Jose_cliente");
            configuracaoBean.enrollConfiguracao("Template_2", "Jose_cliente");

            //colocar os modulos nas configuracoes ("codigo do modulo" na "descricao da configuracao")
            moduloBean.enrollModulo(1, "Template_1");
            moduloBean.enrollModulo(2, "Template_1");
            moduloBean.enrollModulo(2, "Template_2");
            moduloBean.enrollModulo(3, "Template_2");

            //criar questoes (int id, String titulo, String username, String configuracaoDescricao)
            questaoBean.create(1, "Porque está demasiado lento?", "Joao_admin", "Template_1");
            questaoBean.create(4, "Tenho virus, e agora?", "Joao_admin", "Template_1");

            questaoBean.create(2, "Quero renovar a minha licença, o que fazer?", "Joao_admin", "Template_2");
            questaoBean.create(3, "Quero acrescentar novos módulos à minha configuração.", "Joao_admin", "Template_2");

            //criar respostas as perguntas
            respostaBean.create(0, "Tente reiniciar!", "Jose_cliente", 1);
            respostaBean.create(1, "Mas como reinicio um computador?", "Maria_cliente", 1);
            respostaBean.create(2, "Menu iniciar, ligar/desligar, e selecione Reiniciar. ", "Jose_cliente", 1);

            respostaBean.create(3, "Tente reiniciar!", "Jose_cliente", 2);
            respostaBean.create(4, "Mas como reinicio um computador?", "Maria_cliente", 2);
            respostaBean.create(5, "Menu iniciar, ligar/desligar, e selecione Reiniciar. ", "Jose_cliente", 2);

            respostaBean.create(6, "Tente reiniciar!", "Jose_cliente", 3);
            respostaBean.create(7, "Mas como reinicio um computador?", "Maria_cliente", 3);
            respostaBean.create(8, "Menu iniciar, ligar/desligar, e selecione Reiniciar. ", "Jose_cliente", 3);

            respostaBean.create(9, "Reside na necessidade de sobreviver.", "Jose_cliente", 4);
            respostaBean.create(10, "Mas sobreviver como assim?", "Maria_cliente", 4);
            respostaBean.create(11, "Respire Maria, respire. ", "Jose_cliente", 4);

        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
    }
}
