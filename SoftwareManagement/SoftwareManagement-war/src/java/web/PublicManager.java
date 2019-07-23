package web;

import dtos.ClienteDTO;
import dtos.ConfiguracaoDTO;
import dtos.DocumentDTO;
import dtos.ModuloDTO;
import dtos.SoftwareDTO;
import ejbs.ConfiguracaoBean;
import ejbs.ModuloBean;
import ejbs.SoftwareBean;
import enumerations.Estado;
import exceptions.EntityDoesNotExistsException;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import util.URILookup;

import web.FacesExceptionHandler;

@ManagedBean
@SessionScoped
public class PublicManager {

    private static final Logger logger = Logger.getLogger("web.AdministratorManager");

    @EJB
    private ConfiguracaoBean configuracaoBean;
    @EJB
    private ModuloBean moduloBean;
    @EJB
    private SoftwareBean softwareBean;

    private ClienteDTO cliente;
    private ConfiguracaoDTO currentConfiguracao;
    private ModuloDTO modulo;
    private SoftwareDTO software;

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

    public PublicManager() {
        client = ClientBuilder.newClient();
    }

    @ManagedProperty(value = "#{userManager}")
    private UserManager userManager;
    private HttpAuthenticationFeature feature;

    @PostConstruct
    public void initClient() {
        userManager.setUsername("Guest");
        userManager.setPassword("Guest");
        feature = HttpAuthenticationFeature.basic(userManager.getUsername(), userManager.getPassword());
        client.register(feature);
    }

    public List<ConfiguracaoDTO> getAllConfiguracoesTemplatesREST() {
         List<ConfiguracaoDTO> returnedConfiguracoestTemplates = null;
         try{
             returnedConfiguracoestTemplates = client.target(baseUri)
                .path("/configuracoes/alltemplates")
                .request(MediaType.APPLICATION_XML)
                .get(new GenericType<List<ConfiguracaoDTO>>() {
                });
         } catch (Exception e) {
             FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. (getAllConfiguracoesREST)", component, logger);
         }
         
         return returnedConfiguracoestTemplates;
    }
    
//    public List<ConfiguracaoDTO> getAllConfiguracoesTemplates() {
//        try {
//            return configuracaoBean.getAllConfiguracoesTemplates();
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
//            return null;
//        }
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

    public List<String> getAllEstados() {
        List<String> estadoNames = new LinkedList<>();

        EnumSet.allOf(Estado.class).forEach(estado -> {
            estadoNames.add(estado.name());
        });
        return estadoNames;
    }

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
