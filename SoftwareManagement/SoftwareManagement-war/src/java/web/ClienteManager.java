package web;

import static com.sun.faces.facelets.tag.jstl.fn.JstlFunction.toUpperCase;
import static com.sun.xml.ws.security.impl.policy.Constants.logger;
import dtos.CargoDTO;
import ejbs.CargoBean;
import dtos.AdminDTO;
import dtos.ClienteDTO;
import dtos.ConfiguracaoDTO;
import dtos.DocumentDTO;
import dtos.ModuloDTO;
import dtos.QuestaoDTO;
import dtos.RespostaDTO;
import dtos.SoftwareDTO;
import ejbs.AdminBean;
import ejbs.ClienteBean;
import ejbs.ConfiguracaoBean;
import ejbs.ModuloBean;
import ejbs.QuestaoBean;
import ejbs.RespostaBean;
import ejbs.SoftwareBean;
import entities.UserGroup.GROUP;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityDoesNotExistsException;
import exceptions.MyConstraintViolationException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import util.URILookup;
import web.FacesExceptionHandler;

@ManagedBean
@SessionScoped
public class ClienteManager {

    private static final Logger logger = Logger.getLogger("web.AdministratorManager");

    @EJB
    private ClienteBean clienteBean;
    @EJB
    private ConfiguracaoBean configuracaoBean;
    @EJB
    private ModuloBean moduloBean;
    @EJB
    private SoftwareBean softwareBean;
    @EJB
    private QuestaoBean questaoBean;
    @EJB
    private RespostaBean respostaBean;

    private ClienteDTO cliente;
    private ConfiguracaoDTO currentConfiguracao;
    private ModuloDTO modulo;
    private SoftwareDTO software;

    private QuestaoDTO newQuestao;
    private QuestaoDTO currentQuestao;

    private RespostaDTO newResposta;
    private RespostaDTO currentResposta;

    private DocumentDTO document;
    private String filePath;
    private Client client;
    private final String baseUri = "http://localhost:8080/SoftwareManagement-war/webapi";

    @ManagedProperty(value = "#{uploadManager}")
    private UploadManager uploadManager;

    private UIComponent component;

    // Geridos automaticamente pelo PrimeFaces
    private List<ConfiguracaoDTO> filteredConfigs;
    private List<SoftwareDTO> filteredSoftware;
    private List<ModuloDTO> filteredModulos;
    
    

    public ClienteManager() {
        client = ClientBuilder.newClient();
        newQuestao = new QuestaoDTO();
        newResposta = new RespostaDTO();
    }

    @ManagedProperty(value = "#{userManager}")
    private UserManager userManager;
    private HttpAuthenticationFeature feature;

    @PostConstruct
    public void initClient() {
        feature = HttpAuthenticationFeature.basic(userManager.getUsername(), userManager.getPassword());
        client.register(feature);
    }

    // LISTAR AS CONFIGURACOES DESTE CLIENTE
    public List<ConfiguracaoDTO> getAllConfiguracoesDesteClienteREST() {
         List<ConfiguracaoDTO> returnedConfiguracoes = null;
         try{
             returnedConfiguracoes = client.target(baseUri)
                .path("/configuracoes/all-in-cliente/" + userManager.getUsername())
                .request(MediaType.APPLICATION_XML)
                .get(new GenericType<List<ConfiguracaoDTO>>() {
                });
         } catch (Exception e) {
             FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. (getEnrolledClientesNaConfiguracaoREST)", component, logger);
         }
         
         return returnedConfiguracoes;
    }
    
//    public List<ConfiguracaoDTO> getAllConfiguracoesDesteCliente() {
//        try {
//            return configuracaoBean.getEnrolledConfiguracoes(userManager.getUsername());
////        } catch (EntityDoesNotExistsException e) {
////            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
////            return null;
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
//            return null;
//        }
//    }

    // ******************* FORUM QUESTOES ******************* //
// LISTAR AS QUESTOES POR CONFIGURACAO
    public List<QuestaoDTO> getEnrolledQuestoesREST() {
        List<QuestaoDTO> documents = null;
        try {
            documents = client.target(URILookup.getBaseAPI())
                    .path("/questoes/all-in-configuracao/" + currentConfiguracao.getDescricao())
                    .request(MediaType.APPLICATION_XML)
                    .get(new GenericType<List<QuestaoDTO>>() {
                    });
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter! - getDocuments", logger);
        }
        return documents;
    }
    
//    public List<QuestaoDTO> getEnrolledQuestoes() {
//        try {
//            return questaoBean.getEnrolledQuestoes(currentConfiguracao.getDescricao());
//        } catch (EntityDoesNotExistsException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
//            return null;
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
//            return null;
//        }
//    }

    //LISTAR AS RESPOSTAS POR QUESTAO
    public List<RespostaDTO> getEnrolledRespostasREST() {
        List<RespostaDTO> documents = null;
        try {
            documents = client.target(URILookup.getBaseAPI())
                    .path("/respostas/all-in-questao/" + currentQuestao.getId())
                    .request(MediaType.APPLICATION_XML)
                    .get(new GenericType<List<RespostaDTO>>() {
                    });
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
        }
        return documents;
    }
    
//    public List<RespostaDTO> getEnrolledRespostas() {
//        try {
//            return respostaBean.getEnrolledRespostas(currentQuestao.getId());
//        } catch (EntityDoesNotExistsException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
//            return null;
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
//            return null;
//        }
//    }
    
    public String createQuestaoREST() {
        QuestaoDTO questaoAEnviar = new QuestaoDTO(newQuestao.getId(), newQuestao.getTitulo(), userManager.getUsername(), "dataDummy", currentConfiguracao.getDescricao());
        try {    
            Response response = client.target(URILookup.getBaseAPI())   //LIDAR COM RESPOSTAS (AJUDA DO STOR - USAR RESPONSE)
                    .path("/questoes/")
                    .request(MediaType.APPLICATION_XML)
                    .post(Entity.xml(questaoAEnviar)); 
            newQuestao.reset();
            
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. (createQuestaoREST)", component, logger);
            return null;
        }
        return "/cliente/cliente_configuracoes?faces-redirect=true";
    }
    
//    public String createQuestao() {
//        try {
//            questaoBean.create(
//                    newQuestao.getId(),
//                    newQuestao.getTitulo(),
//                    userManager.getUsername(),
//                    currentConfiguracao.getDescricao()
//            );
//            newQuestao.reset();
//        } catch (EntityAlreadyExistsException | MyConstraintViolationException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), component, logger);
//            return null;
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter! - createQuestao", component, logger);
//            return null;
//        }
//
//        return "forum";
//    }

    public String createRespostaREST() {
        RespostaDTO respostaAEnviar = new RespostaDTO(newResposta.getId(), newResposta.getTexto(), userManager.getUsername(), "", currentQuestao.getId()); //data é inserida mais tarde
        try {    
            Response response = client.target(URILookup.getBaseAPI())   //LIDAR COM RESPOSTAS (AJUDA DO STOR - USAR RESPONSE)
                    .path("/respostas/")
                    .request(MediaType.APPLICATION_XML)
                    .post(Entity.xml(respostaAEnviar)); 
            newResposta.reset();            
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. (createRespostaREST)", component, logger);
            return null;
        }

        return "/cliente/cliente_configuracoes?faces-redirect=true";
    }
    
    
//    public String createResposta() {
//        try {
//            respostaBean.create(
//                    newResposta.getId(),
//                    newResposta.getTexto(),
//                    userManager.getUsername(),
//                    currentQuestao.getId()
//            );
//            newResposta.reset();
//        } catch (MyConstraintViolationException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), component, logger);
//            return null;
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter! - createResposta", component, logger);
//            return null;
//        }
//
//        return "respostas";
//    }

    // LISTAR OS MODULOS POR CONFIGURACAO
    public List<ModuloDTO> getEnrolledModulosREST() {
        List<ModuloDTO> returnedModulos = null;
        try{
            returnedModulos = client.target(baseUri)
               .path("/modulos/all-in-configuracao/"+currentConfiguracao.getDescricao())
               .request(MediaType.APPLICATION_XML)
               .get(new GenericType<List<ModuloDTO>>() {
               });
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. (getEnrolledModulosREST)", component, logger);
        }

        return returnedModulos;
    }
    
//    public List<ModuloDTO> getEnrolledModulos() {
//        try {
//            return moduloBean.getEnrolledModulos(currentConfiguracao.getDescricao());
//        } catch (EntityDoesNotExistsException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
//            return null;
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
//            return null;
//        }
//    }

    public Collection<DocumentDTO> getDocuments() {
        Collection<DocumentDTO> documents = null;
        try {
            documents = client.target(URILookup.getBaseAPI())
                    .path("/configuracoes/documents")
                    .path(currentConfiguracao.getDescricao())
                    .request(MediaType.APPLICATION_XML)
                    .get(new GenericType<Collection<DocumentDTO>>() {
                    });
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter! - getDocuments", logger);
        }
        return documents;
    }
    
//    public Collection<DocumentDTO> getDocuments() {
//        try {
//            return configuracaoBean.getDocuments(currentConfiguracao.getDescricao());
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
//            return null;
//        }
//    }

    public ModuloBean getModuloBean() {
        return moduloBean;
    }

    public void setModuloBean(ModuloBean moduloBean) {
        this.moduloBean = moduloBean;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }

    public ModuloDTO getModulo() {
        return modulo;
    }

    public void setModulo(ModuloDTO modulo) {
        this.modulo = modulo;
    }

    public SoftwareDTO getSoftware() {
        return software;
    }

    public void setSoftware(SoftwareDTO software) {
        this.software = software;
    }

    public QuestaoDTO getNewQuestao() {
        return newQuestao;
    }

    public void setNewQuestao(QuestaoDTO newQuestao) {
        this.newQuestao = newQuestao;
    }

    public QuestaoDTO getCurrentQuestao() {
        return currentQuestao;
    }

    public void setCurrentQuestao(QuestaoDTO currentQuestao) {
        this.currentQuestao = currentQuestao;
    }

    public RespostaDTO getNewResposta() {
        return newResposta;
    }

    public void setNewResposta(RespostaDTO newResposta) {
        this.newResposta = newResposta;
    }

    public RespostaDTO getCurrentResposta() {
        return currentResposta;
    }

    public void setCurrentResposta(RespostaDTO currentResposta) {
        this.currentResposta = currentResposta;
    }

    public DocumentDTO getDocument() {
        return document;
    }

    public void setDocument(DocumentDTO document) {
        this.document = document;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public UploadManager getUploadManager() {
        return uploadManager;
    }

    public void setUploadManager(UploadManager uploadManager) {
        this.uploadManager = uploadManager;
    }

    public UIComponent getComponent() {
        return component;
    }

    public void setComponent(UIComponent component) {
        this.component = component;
    }

    public List<ConfiguracaoDTO> getFilteredConfigs() {
        return filteredConfigs;
    }

    public void setFilteredConfigs(List<ConfiguracaoDTO> filteredConfigs) {
        this.filteredConfigs = filteredConfigs;
    }

    public List<SoftwareDTO> getFilteredSoftware() {
        return filteredSoftware;
    }

    public void setFilteredSoftware(List<SoftwareDTO> filteredSoftware) {
        this.filteredSoftware = filteredSoftware;
    }

    public List<ModuloDTO> getFilteredModulos() {
        return filteredModulos;
    }

    public void setFilteredModulos(List<ModuloDTO> filteredModulos) {
        this.filteredModulos = filteredModulos;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public ConfiguracaoDTO getCurrentConfiguracao() {
        return currentConfiguracao;
    }

    public void setCurrentConfiguracao(ConfiguracaoDTO currentConfiguracao) {
        this.currentConfiguracao = currentConfiguracao;
    }

}
