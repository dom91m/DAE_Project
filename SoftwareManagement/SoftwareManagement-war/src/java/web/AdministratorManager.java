package web;

import dtos.CargoDTO;
import dtos.AdminDTO;
import dtos.ClienteDTO;
import dtos.ConfiguracaoDTO;
import dtos.DocumentDTO;
import dtos.ModuloDTO;
import dtos.QuestaoDTO;
import dtos.RespostaDTO;
import dtos.SoftwareDTO;
import entities.UserGroup.GROUP;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityDoesNotExistsException;
import exceptions.MyConstraintViolationException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.mail.MessagingException;
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
public class AdministratorManager {

//    @EJB
//    private AdminBean adminBean;
//    @EJB
//    private CargoBean cargoBean;
    
//    @EJB
//    private ClienteBean clienteBean;
//    @EJB
//    private ConfiguracaoBean configuracaoBean;
//    @EJB
//    private ModuloBean moduloBean;
//    @EJB
//    private SoftwareBean softwareBean;
//    @EJB
//    private QuestaoBean questaoBean;
//    @EJB
//    private RespostaBean respostaBean;
    private static final Logger logger = Logger.getLogger("web.AdministratorManager");

    private AdminDTO newAdmin;
    private AdminDTO currentAdmin;

    private CargoDTO newCargo;
    private CargoDTO currentCargo;

    private ClienteDTO newCliente;
    private ClienteDTO currentCliente;

    private ConfiguracaoDTO newConfiguracao;
    private ConfiguracaoDTO currentConfiguracao;

    private ModuloDTO newModulo;
    private ModuloDTO currentModulo;

    private SoftwareDTO newSoftware;
    private SoftwareDTO currentSoftware;

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

    public AdministratorManager() {
        newAdmin = new AdminDTO();
        newCargo = new CargoDTO();
        newCliente = new ClienteDTO();
        newConfiguracao = new ConfiguracaoDTO();
        newModulo = new ModuloDTO();
        newSoftware = new SoftwareDTO();
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

    // <editor-fold desc="///////////////// ADMINS /////////////////">
    public String createAdminREST() throws MessagingException {
        
        try {    
            client.target(URILookup.getBaseAPI())
                    .path("/admins/")
                    .request(MediaType.APPLICATION_XML)
                    .post(Entity.xml(newAdmin)); 
            newAdmin.reset();
//        } catch (EntityAlreadyExistsException | EntityDoesNotExistsException | MyConstraintViolationException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), component, logger);
//            return null;
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. (createAdminREST)", component, logger);
            return null;
        }

        return "/admin/administracao_utilizadores?faces-redirect=true";
    }
    
    public List<AdminDTO> getAllAdminsREST() {
         List<AdminDTO> returnedAdmins = null;
         try{
             returnedAdmins = client.target(baseUri)
                .path("/admins/all")
                .request(MediaType.APPLICATION_XML)
                .get(new GenericType<List<AdminDTO>>() {
                });
         } catch (Exception e) {
             FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. (getAllAdminsREST)", component, logger);
         }
         
         return returnedAdmins;
    }
    
    public String updateAdminREST() {
        try {
            client.target(URILookup.getBaseAPI())
                    .path("/admins/"+currentAdmin.getUsername())
                    .request(MediaType.APPLICATION_XML)
                    .put(Entity.xml(currentAdmin));
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. (updateAdminREST)", logger);
            return null;
        }
        
        return "/admin/administracao_utilizadores?faces-redirect=true";
    }
    
    public String removeAdminREST(ActionEvent event) {
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("adminUsername");
            String username = param.getValue().toString();
            client.target(URILookup.getBaseAPI())
                    .path("/admins/"+username)
                    .request(MediaType.APPLICATION_XML)
                    .delete();
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. (removeAdminREST)", logger);
            return null;
        }
        
        return "/admin/administracao_utilizadores?faces-redirect=true";
    }

//    public String createAdmin() {
//        try {
//            adminBean.create(
//                    newAdmin.getUsername(),
//                    newAdmin.getPassword(),
//                    GROUP.ADMIN,
//                    newAdmin.getName(),
//                    newAdmin.getEmail(),
//                    newAdmin.getCargoCode());
//            newAdmin.reset();
//        } catch (EntityAlreadyExistsException | EntityDoesNotExistsException | MyConstraintViolationException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), component, logger);
//            return null;
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. - createAdmin", component, logger);
//            return null;
//        }
//
//        return "/index?faces-redirect=true";
//    }

//    public List<AdminDTO> getAllAdmins() {
//        try {
//            return adminBean.getAll();
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. - getAllAdmins", logger);
//            return null;
//        }
//    }

//    public String updateAdmin() {
//        try {
//            adminBean.update(
//                    currentAdmin.getUsername(),
//                    currentAdmin.getPassword(),
//                    currentAdmin.getName(),
//                    currentAdmin.getEmail(),
//                    currentAdmin.getCargoCode());
//
//        } catch (EntityDoesNotExistsException | MyConstraintViolationException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
//            return null;
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. - updateAdmin", logger);
//            return null;
//        }
//        return "/index?faces-redirect=true";
//    }

//    public void removeAdmin(ActionEvent event) {
//        try {
//            UIParameter param = (UIParameter) event.getComponent().findComponent("adminUsername");
//            String id = param.getValue().toString();
//            adminBean.remove(id);
//        } catch (EntityDoesNotExistsException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later.  - removeAdmin", logger);
//        }
//    }
    
    // </editor-fold>
    
    // <editor-fold desc="///////////////// CLIENTES /////////////////">
    public String createClienteREST() {
        try {    
            Response response = client.target(URILookup.getBaseAPI())   //LIDAR COM RESPOSTAS (AJUDA DO STOR - USAR RESPONSE)
                    .path("/clientes/")
                    .request(MediaType.APPLICATION_XML)
                    .post(Entity.xml(newCliente)); 
            newCliente.reset();
            if (response.getStatus() != 200 && response.getStatus() != 201) {
                FacesExceptionHandler.handleException(null, response.getEntity().toString() + " ||" + response.toString(), component, logger);
            }
            
//        } catch (EntityAlreadyExistsException | EntityDoesNotExistsException | MyConstraintViolationException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), component, logger);
//            return null;
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. (createClienteREST)", component, logger);
            return null;
        }

        return "/admin/administracao_utilizadores?faces-redirect=true";
    }
    
    public List<ClienteDTO> getAllClientesREST() {
         List<ClienteDTO> returnedClientes = null;
         try{
             returnedClientes = client.target(baseUri)
                .path("/clientes/all")
                .request(MediaType.APPLICATION_XML)
                .get(new GenericType<List<ClienteDTO>>() {
                });
         } catch (Exception e) {
             FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. (getAllClientesREST)", component, logger);
         }
         
         return returnedClientes;
    }

    public String updateClienteREST() {
        try {
            client.target(URILookup.getBaseAPI())
                    .path("/clientes/"+currentCliente.getUsername())
                    .request(MediaType.APPLICATION_XML)
                    .put(Entity.xml(currentCliente));
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "\"There was an error. Contact an Admin or try again later. (updateClienteREST)\"", logger);
            return null;
        }
        
        return "/admin/administracao_utilizadores?faces-redirect=true";
    }
    
    public String removeClienteREST(ActionEvent event) {
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("clienteUsername");
            String username = param.getValue().toString();
            client.target(URILookup.getBaseAPI())
                    .path("/clientes/"+username)
                    .request(MediaType.APPLICATION_XML)
                    .delete();
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. (removeClienteREST)", logger);
            return null;
        }
        
        return "/admin/administracao_utilizadores?faces-redirect=true";
    }
    
    public void removeClienteDaConfiguracaoREST(ActionEvent event) {
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("configuracaoDescricao");
            String descricao = param.getValue().toString();

            UIParameter param2 = (UIParameter) event.getComponent().findComponent("clienteUsername");
            String username = param2.getValue().toString();

            client.target(URILookup.getBaseAPI())
                    .path("/configuracoes/"+descricao+"/cliente/"+username)
                    .request(MediaType.APPLICATION_XML)
                    .delete();

//        } catch (EntityDoesNotExistsException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. (removeClienteDaConfiguracaoREST)", logger);
        }
    }

    // LISTAR AS CONFIGURACOES POR CLIENTE
    public List<ConfiguracaoDTO> getEnrolledConfiguracoesREST() {
         List<ConfiguracaoDTO> returnedConfiguracoes = null;
         try{
             returnedConfiguracoes = client.target(baseUri)
                .path("/configuracoes/all-in-cliente/" + currentCliente.getUsername())
                .request(MediaType.APPLICATION_XML)
                .get(new GenericType<List<ConfiguracaoDTO>>() {
                });
         } catch (Exception e) {
             FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. (getEnrolledConfiguracoesREST)", component, logger);
         }
         
         return returnedConfiguracoes;
    }
    
    
//    public List<ConfiguracaoDTO> getEnrolledConfiguracoes() {
//        try {
//            return configuracaoBean.getEnrolledConfiguracoes(currentCliente.getUsername());
//        } catch (EntityDoesNotExistsException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
//            return null;
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later.", logger);
//            return null;
//        }
//    }

    // LISTAR AS CONFIGURACOES QUE O CLIENTE NAO TEM
    public List<ConfiguracaoDTO> getEnrolledConfiguracoesNotInClienteREST() {
         List<ConfiguracaoDTO> returnedConfiguracoes = null;
         try{
             returnedConfiguracoes = client.target(baseUri)
                .path("/configuracoes/not-in-cliente/" + currentCliente.getUsername())
                .request(MediaType.APPLICATION_XML)
                .get(new GenericType<List<ConfiguracaoDTO>>() {
                });
         } catch (Exception e) {
             FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. (getEnrolledConfiguracoesNotInClienteREST)", component, logger);
         }
         
         return returnedConfiguracoes;
    }

    // LISTAR OS CLIENTES POR CONFIGURACAO
    public List<ClienteDTO> getEnrolledClientesNaConfiguracaoREST() {
         List<ClienteDTO> returnedConfiguracoes = null;
         try{
             returnedConfiguracoes = client.target(baseUri)
                .path("/clientes/all-in-configuracao/" + currentConfiguracao.getDescricao())
                .request(MediaType.APPLICATION_XML)
                .get(new GenericType<List<ClienteDTO>>() {
                });
         } catch (Exception e) {
             FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. (getEnrolledClientesNaConfiguracaoREST)", component, logger);
         }
         
         return returnedConfiguracoes;
    }
   

    // ADICIONAR CONFIGURACAO A UM CLIENTE
     public String addConfiguracaoNoClienteREST(ActionEvent event) {
        try {    
            UIParameter param = (UIParameter) event.getComponent().findComponent("configuracaoDescricao");
            String descricao = param.getValue().toString();

            UIParameter param2 = (UIParameter) event.getComponent().findComponent("usernameCliente");
            String usernameCliente = param2.getValue().toString();
            
            client.target(URILookup.getBaseAPI())
                    .path("/clientes/" + usernameCliente + "/descricoes/" + descricao)
                    .request(MediaType.APPLICATION_XML)
                    .post(null); 
//        } catch (EntityAlreadyExistsException | EntityDoesNotExistsException | MyConstraintViolationException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), component, logger);
//            return null;
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. (addConfiguracaoNoClienteREST)", component, logger);
            return null;
        }

        return "/admin/administracao_utilizadores?faces-redirect=true";
    }
    
//    public void addConfiguracaoNoCliente(ActionEvent event) {
//        try {
//            UIParameter param = (UIParameter) event.getComponent().findComponent("configuracaoDescricao");
//            String descricao = param.getValue().toString();
//
//            UIParameter param2 = (UIParameter) event.getComponent().findComponent("usernameCliente");
//            String usernameCliente = param2.getValue().toString();
//
//            clienteBean.addConfiguracaoNoCliente(descricao, usernameCliente);
//
//        } catch (EntityDoesNotExistsException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later.", logger);
//        }
//    }
    
//    public String createCliente() {
//        try {
//            clienteBean.create(
//                    newCliente.getUsername(),
//                    newCliente.getPassword(),
//                    GROUP.CLIENTE,
//                    newCliente.getName(),
//                    newCliente.getEmail(),
//                    //newCliente.getCargoCode()
//                    newCliente.getMorada(),
//                    newCliente.getPessoaDeContacto());
//            newCliente.reset();
//        } catch (EntityAlreadyExistsException | EntityDoesNotExistsException | MyConstraintViolationException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), component, logger);
//            return null;
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. - createCliente", component, logger);
//            return null;
//        }
//
//        return "/admin/administracao_utilizadores?faces-redirect=true";
//    }

//    public List<ClienteDTO> getAllClientes() {
//        try {
//            return clienteBean.getAll();
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. - getAllClientes", logger);
//            return null;
//        }
//    }
    
//    public String updateCliente() {
//        try {
//            clienteBean.update(
//                    currentCliente.getUsername(),
//                    currentCliente.getPassword(),
//                    currentCliente.getName(),
//                    currentCliente.getEmail(),
//                    //currentCliente.getCargoCode()
//                    currentCliente.getMorada(),
//                    currentCliente.getPessoaDeContacto()
//            );
//
//        } catch (EntityDoesNotExistsException | MyConstraintViolationException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
//            return null;
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. - updateCliente", logger);
//            return null;
//        }
//        return "/admin/administracao_utilizadores?faces-redirect=true";
//    }
    
//    public void removeCliente(ActionEvent event) {
//        try {
//            UIParameter param = (UIParameter) event.getComponent().findComponent("clienteUsername");
//            String id = param.getValue().toString();
//            clienteBean.remove(id);
//        } catch (EntityDoesNotExistsException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later.  - removeCliente", logger);
//        }
//    }

//    public void removeClienteDaConfiguracao(ActionEvent event) {
//        try {
//            UIParameter param = (UIParameter) event.getComponent().findComponent("configuracaoDescricao");
//            String descricao = param.getValue().toString();
//
//            UIParameter param2 = (UIParameter) event.getComponent().findComponent("clienteUsername");
//            String username = param2.getValue().toString();
//
//            configuracaoBean.removeClienteConfiguracao(username, descricao);
//            //clienteBean.removeConfiguracaoDoCliente(descricao, username); //codigo repetido
//
//        } catch (EntityDoesNotExistsException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later.", logger);
//        }
//    }
    
//    public List<ConfiguracaoDTO> getEnrolledConfiguracoes() {
//        try {
//            return configuracaoBean.getEnrolledConfiguracoes(currentCliente.getUsername());
//        } catch (EntityDoesNotExistsException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
//            return null;
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later.", logger);
//            return null;
//        }
//    }
    
//    public List<ConfiguracaoDTO> getEnrolledConfiguracoesNotInCliente() {
//        try {
//            return configuracaoBean.getEnrolledConfiguracoesNotInCliente(currentCliente.getUsername());
//        } catch (EntityDoesNotExistsException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
//            return null;
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later.", logger);
//            return null;
//        }
//    }
    
    
    // </editor-fold>
    
    // <editor-fold desc="///////////////// CONFIGURACOES /////////////////">
    public String createConfiguracaoREST() {
        try {    
            client.target(URILookup.getBaseAPI())
                    .path("/configuracoes/")
                    .request(MediaType.APPLICATION_XML)
                    .post(Entity.xml(newConfiguracao)); 
//        } catch (EntityAlreadyExistsException | EntityDoesNotExistsException | MyConstraintViolationException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), component, logger);
//            return null;
            newConfiguracao.reset();
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. (createConfiguracaoREST)", component, logger);
            return null;
        }

        return "/admin/administracao_configuracoes?faces-redirect=true";
    }
    
    // TODO - port para REST
    public String clonarConfiguracaoREST() {
        try {    
            client.target(URILookup.getBaseAPI())
                    .path("/configuracoes/")
                    .request(MediaType.APPLICATION_XML)
                    .post(Entity.xml(currentConfiguracao)); 
            currentConfiguracao.reset();
//        } catch (EntityAlreadyExistsException | EntityDoesNotExistsException | MyConstraintViolationException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), component, logger);
//            return null;
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. (clonarConfiguracaoREST)", component, logger);
            return null;
        }

        return "/admin/administracao_configuracoes?faces-redirect=true";
    }
     
//    public String createConfiguracao() {
//        try {
//            configuracaoBean.create(
//                    newConfiguracao.getDescricao(),
//                    newConfiguracao.getEstado(),
//                    newConfiguracao.getVersao(),
//                    newConfiguracao.getSoftwareId());
//            newConfiguracao.reset();
//        } catch (EntityAlreadyExistsException | MyConstraintViolationException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), component, logger);
//            return null;
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later.", component, logger);
//            return null;
//        }
//        return "/admin/administracao_configuracoes?faces-redirect=true";
//    }
    
//    public String clonarConfiguracao() {
//        try {
//            configuracaoBean.create(
//                    currentConfiguracao.getDescricao(),
//                    currentConfiguracao.getEstado(),
//                    currentConfiguracao.getVersao(),
//                    currentConfiguracao.getSoftwareId());
//            currentConfiguracao.reset();
//        } catch (EntityAlreadyExistsException | MyConstraintViolationException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), component, logger);
//            return null;
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later.", component, logger);
//            return null;
//        }
//        return "/admin/administracao_configuracoes?faces-redirect=true";
//    }
    
    public List<ConfiguracaoDTO> getAllConfiguracoesREST() {
         List<ConfiguracaoDTO> returnedConfiguracoes = null;
         try{
             returnedConfiguracoes = client.target(baseUri)
                .path("/configuracoes/all")
                .request(MediaType.APPLICATION_XML)
                .get(new GenericType<List<ConfiguracaoDTO>>() {
                });
         } catch (Exception e) {
             FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. (getAllConfiguracoesREST)", component, logger);
         }
         
         return returnedConfiguracoes;
    }
    
    public String updateConfiguracaoREST() {
        try {
            client.target(URILookup.getBaseAPI())
                    .path("/configuracoes/"+currentConfiguracao.getDescricao())
                    .request(MediaType.APPLICATION_XML)
                    .put(Entity.xml(currentConfiguracao));
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "\"There was an error. Contact an Admin or try again later. (updateConfiguracaoREST)\"", logger);
            return null;
        }
        
        return "/admin/administracao_configuracoes?faces-redirect=true";
    }
    
    public String removeConfiguracaoREST(ActionEvent event) {
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("configuracaoDescricao");
            String descricao = param.getValue().toString();
            client.target(URILookup.getBaseAPI())
                    .path("/configuracoes/"+descricao)
                    .request(MediaType.APPLICATION_XML)
                    .delete();
//        } catch (EntityDoesNotExistsException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
        } 
        catch (Exception e) {
            FacesExceptionHandler.handleException(e, "\"There was an error. Contact an Admin or try again later. (removeConfiguracaoREST)\"", logger);
            return null;
        }
        
        return "/admin/administracao_configuracoes?faces-redirect=true";
    }
    
    public String removeModuloDaConfiguracaoREST(ActionEvent event) {
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("configuracaoDescricao");
            String descricao = param.getValue().toString();
            UIParameter param2 = (UIParameter) event.getComponent().findComponent("moduloCode");
            int moduloCode = Integer.parseInt(param2.getValue().toString());
            
            client.target(URILookup.getBaseAPI())
                    .path("/configuracoes/"+descricao+"/modulos/"+moduloCode)
                    .request(MediaType.APPLICATION_XML)
                    .delete();
//        } catch (EntityDoesNotExistsException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
        } 
        catch (Exception e) {
            FacesExceptionHandler.handleException(e, "\"There was an error. Contact an Admin or try again later. (removeModuloDaConfiguracaoREST)\"", logger);
            return null;
        }
        
        return "/admin/administracao_configuracoes?faces-redirect=true";
    }
    
    // ADICIONAR MODULO A UMA CONFIGURACAO
    public String addModuloNaConfiguracaoREST(ActionEvent event) {
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("configuracaoDescricao");
            String descricao = param.getValue().toString();
            UIParameter param2 = (UIParameter) event.getComponent().findComponent("moduloCode");
            int moduloCode = Integer.parseInt(param2.getValue().toString());
            
            client.target(URILookup.getBaseAPI())
                    .path("/configuracoes/"+descricao+"/modulos/"+moduloCode)
                    .request(MediaType.APPLICATION_XML)
                    .post(null);
            
//        } catch (EntityDoesNotExistsException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
        } 
        catch (Exception e) {
            FacesExceptionHandler.handleException(e, "\"There was an error. Contact an Admin or try again later. (removeModuloDaConfiguracaoREST)\"", logger);
            return null;
        }
        
        return "/admin/administracao_configuracoes?faces-redirect=true";
    }
    
    // LISTAR MODULOS QUE NAO EXISTEM NA CONFIGURACAO
    public List<ModuloDTO> getEnrolledModulosNotInConfiguracaoREST() {
         List<ModuloDTO> returnedModulos = null;
         try{
             returnedModulos = client.target(baseUri)
                .path("/modulos/not-in-configuracao/"+currentConfiguracao.getDescricao())
                .request(MediaType.APPLICATION_XML)
                .get(new GenericType<List<ModuloDTO>>() {
                });
         } catch (Exception e) {
             FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. (getEnrolledModulosNotInConfiguracaoREST)", component, logger);
         }
         
         return returnedModulos;
    }
    
    public String removeConfiguracaoDoClienteREST(ActionEvent event) {
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("configuracaoDescricao");
            String descricao = param.getValue().toString();
            UIParameter param2 = (UIParameter) event.getComponent().findComponent("usernameCliente");
            String usernameCliente = param2.getValue().toString();
            
            client.target(URILookup.getBaseAPI())
                    .path("/clientes/"+usernameCliente+"/configuracoes/"+descricao)
                    .request(MediaType.APPLICATION_XML)
                    .delete();
//        } catch (EntityDoesNotExistsException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
        } 
        catch (Exception e) {
            FacesExceptionHandler.handleException(e, "\"There was an error. Contact an Admin or try again later. (removeConfiguracaoDoClienteREST)\"", logger);
            return null;
        }
        
        return "/admin/administracao_configuracoes?faces-redirect=true";
    }

    public List<ModuloDTO> getCurrentConfiguracaoModulosREST() {
         List<ModuloDTO> returnedModulos = null;
         try{
             returnedModulos = client.target(baseUri)
                .path("/modulos/all-in-configuracao/"+currentConfiguracao.getDescricao())
                .request(MediaType.APPLICATION_XML)
                .get(new GenericType<List<ModuloDTO>>() {
                });
         } catch (Exception e) {
             FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. (getCurrentConfiguracaoModulosREST)", component, logger);
         }
         
         return returnedModulos;
    }

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
//            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later.", logger);
//            return null;
//        }
//    }
    
    
//    public List<ConfiguracaoDTO> getAllConfiguracoes() {
//        try {
//            return configuracaoBean.getAll();
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later.", logger);
//            return null;
//        }
//    }
   
    
//    public String updateConfiguracao() {
//        try {
//            configuracaoBean.update(
//                    currentConfiguracao.getDescricao(),
//                    currentConfiguracao.getEstado(),
//                    currentConfiguracao.getVersao(),
//                    currentConfiguracao.getSoftwareId());
//
//        } catch (EntityDoesNotExistsException | MyConstraintViolationException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
//            return null;
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. - updateConfiguracao", logger);
//            return null;
//        }
//        return "/admin/administracao_configuracoes?faces-redirect=true";
//    }

//    public void removeConfiguracao(ActionEvent event) {
//        try {
//            UIParameter param = (UIParameter) event.getComponent().findComponent("configuracaoDescricao");
//            String descricao = param.getValue().toString();
//            configuracaoBean.remove(descricao);
//        } catch (EntityDoesNotExistsException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "Não é possivel remover a configuração. Encontra-se associada a um cliente!", logger);
//        }
//    }
    
//    public void removeConfiguracaoDoCliente(ActionEvent event) {
//        try {
//            UIParameter param = (UIParameter) event.getComponent().findComponent("configuracaoDescricao");
//            String descricao = param.getValue().toString();
//
//            UIParameter param2 = (UIParameter) event.getComponent().findComponent("usernameCliente");
//            String usernameCliente = param2.getValue().toString();
//
//            clienteBean.removeConfiguracaoDoCliente(descricao, usernameCliente);
//
//        } catch (EntityDoesNotExistsException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later.", logger);
//        }
//    }
    
//    public void removeModuloDaConfiguracao(ActionEvent event) {
//        try {
//            UIParameter param = (UIParameter) event.getComponent().findComponent("configuracaoDescricao");
//            String descricao = param.getValue().toString();
//
//            UIParameter param2 = (UIParameter) event.getComponent().findComponent("moduloCode");
//            int moduloCode = Integer.parseInt(param2.getValue().toString());
//
//            configuracaoBean.removeModuloConfiguracao(moduloCode, descricao);
//        } catch (EntityDoesNotExistsException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later.", logger);
//        }
//    }

//    public void addModuloNaConfiguracao(ActionEvent event) {
//        try {
//            UIParameter param = (UIParameter) event.getComponent().findComponent("configuracaoDescricao");
//            String descricao = param.getValue().toString();
//
//            UIParameter param2 = (UIParameter) event.getComponent().findComponent("moduloCode");
//            int moduloCode = Integer.parseInt(param2.getValue().toString());
//
//            configuracaoBean.addModuloNaConfiguracao(descricao, moduloCode);
//
//        } catch (EntityDoesNotExistsException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later.", logger);
//        }
//    }
    
//    public List<ModuloDTO> getEnrolledModulosNotInConfiguracao() {
//        try {
//            return moduloBean.getEnrolledModulosNotInConfiguracao(currentConfiguracao.getDescricao());
//        } catch (EntityDoesNotExistsException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
//            return null;
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later.", logger);
//            return null;
//        }
//    }

//    public List<ModuloDTO> getCurrentConfiguracaoModulos() {
//        try {
//            return moduloBean.getConfiguracaoModulos(currentConfiguracao.getDescricao());
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later.", logger);
//            return null;
//        }
//    }
    
    // </editor-fold>

    // <editor-fold desc="///////////////// CARGOS /////////////////">
    public String createCargoREST() {
        try {    
            client.target(URILookup.getBaseAPI())
                    .path("/cargos/")
                    .request(MediaType.APPLICATION_XML)
                    .post(Entity.xml(newCargo)); 
            newCargo.reset();
//        } catch (EntityAlreadyExistsException | EntityDoesNotExistsException | MyConstraintViolationException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), component, logger);
//            return null;
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. (createCargoREST)", component, logger);
            return null;
        }

        return "/admin/administracao_utilizadores?faces-redirect=true";
    }
    
    public List<CargoDTO> getAllCargosREST() {
         List<CargoDTO> returnedCargos = null;
         try{
             returnedCargos = client.target(baseUri)
                .path("/cargos/all")
                .request(MediaType.APPLICATION_XML)
                .get(new GenericType<List<CargoDTO>>() {
                });
         } catch (Exception e) {
             FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. (getAllCargosREST)", component, logger);
         }
         
         return returnedCargos;
    }

    public String removeCargoREST(ActionEvent event) {
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("cargoCode");
            int code = Integer.parseInt(param.getValue().toString());
            client.target(URILookup.getBaseAPI())
                    .path("/cargos/"+code)
                    .request(MediaType.APPLICATION_XML)
                    .delete();
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "\"There was an error. Contact an Admin or try again later. (removeCargoREST)\"", logger);
            return null;
        }
        
        return "/admin/administracao_utilizadores?faces-redirect=true";
    }

//    public String createCargo() {
//        try {
//            cargoBean.create(
//                    newCargo.getCode(),
//                    toUpperCase(newCargo.getName()));
//            newCargo.reset();
//        } catch (EntityAlreadyExistsException | MyConstraintViolationException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), component, logger);
//            return null;
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. - createCargo", component, logger);
//            return null;
//        }
//        return "/index?faces-redirect=true";
//    }
    
//    public List<CargoDTO> getAllCargos() {
//        try {
//            return cargoBean.getAll();
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later.  - getAllCargos", logger);
//            return null;
//        }
//    }
    
//    public void removeCargo(ActionEvent event) {
//        try {
//            UIParameter param = (UIParameter) event.getComponent().findComponent("cargoCode");
//            int code = Integer.parseInt(param.getValue().toString());
//            cargoBean.remove(code);
//        } catch (EntityDoesNotExistsException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later.  - Remove Cargo", logger);
//        }
//    }

    // </editor-fold>
    
    // <editor-fold desc="///////////////// MODULOS /////////////////">
    public String createModuloREST() {
        try {    
            
            client.target(URILookup.getBaseAPI())
                    .path("/modulos")
                    .request(MediaType.APPLICATION_XML)
                    .post(Entity.xml(newModulo)); 
            //newModulo.reset();
//        } catch (EntityAlreadyExistsException | EntityDoesNotExistsException | MyConstraintViolationException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), component, logger);
//            return null;
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. (createModuloREST)", component, logger);
            return null;
        }

        return "/admin/administracao_modulos?faces-redirect=true";
    }
    
    public List<ModuloDTO> getAllModulosREST() {
         List<ModuloDTO> returnedModulos = null;
         try{
             returnedModulos = client.target(baseUri)
                .path("/modulos/all")
                .request(MediaType.APPLICATION_XML)
                .get(new GenericType<List<ModuloDTO>>() {
                });
         } catch (Exception e) {
             FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. (getAllModulosREST)", component, logger);
         }
         
         return returnedModulos;
    }
    
    public String updateModuloREST() {
        try {
            client.target(URILookup.getBaseAPI())
                    .path("/modulos/"+currentModulo.getCode())
                    .request(MediaType.APPLICATION_XML)
                    .put(Entity.xml(currentModulo));
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "\"There was an error. Contact an Admin or try again later. (updateModuloREST)\"", logger);
            return null;
        }
        
        return "/admin/administracao_modulos?faces-redirect=true";
    }

    public String removeModuloREST(ActionEvent event) {
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("moduloCode");
            int moduloCode = Integer.parseInt(param.getValue().toString());      
                    
            client.target(URILookup.getBaseAPI())
                    .path("/modulos/"+moduloCode)
                    .request(MediaType.APPLICATION_XML)
                    .delete();
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "\"There was an error. Contact an Admin or try again later. (removeModuloREST)\"", logger);
            return null;
        }
        
        return "/admin/administracao_modulos?faces-redirect=true";
    }
    
//    public String createModulo() {
//        try {
//            moduloBean.create(
//                    newModulo.getCode(),
//                    newModulo.getName(),
//                    newModulo.getDescricao(),
//                    newModulo.getVersao());
//            newModulo.reset();
//        } catch (EntityAlreadyExistsException | MyConstraintViolationException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), component, logger);
//            return null;
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. - createModulo", component, logger);
//            return null;
//        }
//        return "/admin/administracao_modulos?faces-redirect=true";
//    }
    
//    public List<ModuloDTO> getAllModulos() {
//        try {
//            return moduloBean.getAll();
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later.  - getAllModulos", logger);
//            return null;
//        }
//    }
     
//    public String updateModulo() {
//        try {
//            moduloBean.update(
//                    currentModulo.getCode(),
//                    currentModulo.getName(),
//                    currentModulo.getDescricao(),
//                    currentModulo.getVersao());
//        } catch (EntityDoesNotExistsException | MyConstraintViolationException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
//            return null;
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. - updateCliente", logger);
//            return null;
//        }
//        return "/admin/administracao_modulos?faces-redirect=true";
//    }
    
//    public void removeModulo(ActionEvent event) {
//        try {
//            UIParameter param2 = (UIParameter) event.getComponent().findComponent("moduloCode");
//            int moduloCode = Integer.parseInt(param2.getValue().toString());
//
//            moduloBean.remove(moduloCode);
//        } catch (EntityDoesNotExistsException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later.", logger);
//        }
//    }

    // </editor-fold>
    
    // <editor-fold desc="///////////////// SOFTWARES /////////////////">
    public String createSoftwareREST() {
        try {    
            client.target(URILookup.getBaseAPI())
                    .path("/softwares/")
                    .request(MediaType.APPLICATION_XML)
                    .post(Entity.xml(newSoftware)); 
            newSoftware.reset();
//        } catch (EntityAlreadyExistsException | EntityDoesNotExistsException | MyConstraintViolationException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), component, logger);
//            return null;
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. (createSoftwareREST)", component, logger);
            return null;
        }

        return "/admin/administracao_softwares?faces-redirect=true";
    }
    
    public List<SoftwareDTO> getAllSoftwaresREST() {
         List<SoftwareDTO> returnedModulos = null;
         try{
             returnedModulos = client.target(baseUri)
                .path("/softwares/all")
                .request(MediaType.APPLICATION_XML)
                .get(new GenericType<List<SoftwareDTO>>() {
                });
         } catch (Exception e) {
             FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. (getAllSoftwaresREST)", component, logger);
         }
         
         return returnedModulos;
    }
    
    public String updateSoftwareREST() {
        try {
            client.target(URILookup.getBaseAPI())
                    .path("/softwares/"+currentSoftware.getId())
                    .request(MediaType.APPLICATION_XML)
                    .put(Entity.xml(currentSoftware));
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. (updateSoftwareREST)", component, logger);
            return null;
        }
        
        return "/admin/administracao_softwares?faces-redirect=true";
    }
    
    public String removeSoftwareREST(ActionEvent event) {
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("softwareId");
            int softwareId = Integer.parseInt(param.getValue().toString());
            
            client.target(URILookup.getBaseAPI())
                    .path("/softwares/"+softwareId)
                    .request(MediaType.APPLICATION_XML)
                    .delete();
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. (removeSoftwareREST)", logger);
            return null;
        }
        
        return "/admin/administracao_softwares?faces-redirect=true";
    }

//    public String createSoftware() {
//        try {
//            softwareBean.create(
//                    newSoftware.getId(),
//                    newSoftware.getName(),
//                    newSoftware.getDescricao(),
//                    newSoftware.getVersao());
//            newSoftware.reset();
//        } catch (EntityAlreadyExistsException | MyConstraintViolationException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), component, logger);
//            return null;
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. - createSoftware", component, logger);
//            return null;
//        }
//
//        return "/admin/administracao_softwares?faces-redirect=true";
//    }
    
//    public List<SoftwareDTO> getAllSoftwares() {
//        try {
//            return softwareBean.getAll();
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. - getAllSoftwares", logger);
//            return null;
//        }
//    }
    
//    public String updateSoftware() {
//        try {
//            softwareBean.update(
//                    currentSoftware.getId(),
//                    currentSoftware.getName(),
//                    currentSoftware.getDescricao(),
//                    currentSoftware.getVersao());
//        } catch (EntityDoesNotExistsException | MyConstraintViolationException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
//            return null;
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. (updateSoftware)", component, logger);
//            return null;
//        }
//        return "/admin/administracao_softwares?faces-redirect=true";
//    }
    
//    public void removeSoftware(ActionEvent event) {
//        try {
//            UIParameter param2 = (UIParameter) event.getComponent().findComponent("softwareId");
//            int softwareId = Integer.parseInt(param2.getValue().toString());
//
//            softwareBean.remove(softwareId);
//        } catch (EntityDoesNotExistsException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "Não é possivel remover. Este Software está associado a uma configuração!", logger);
//        }
//    }
    
    // </editor-fold>
    
    // <editor-fold desc="///////////////// DOCUMENTOS /////////////////">
    public String uploadDocument() {
        try {
            document = new DocumentDTO(uploadManager.getCompletePathFile(), uploadManager.getFilename(), uploadManager.getFile().getContentType());

            client.target(URILookup.getBaseAPI())
                    .path("/configuracoes/addDocument")
                    .path(currentConfiguracao.getDescricao())
                    .request(MediaType.APPLICATION_XML)
                    .put(Entity.xml(document));

        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. (uploadDocument)", logger);
            return null;
        }

        return "update";
    }

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
            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. (getDocuments)", logger);
        }
        return documents;
    }
    
    public Collection<DocumentDTO> getDocumentsConfiguracao() {
        Collection<DocumentDTO> documents = null;
        try {
            documents = client.target(URILookup.getBaseAPI())
                    .path("./admin/configuracoes/documents")
                    .path(currentConfiguracao.getDescricao())
                    .request(MediaType.APPLICATION_XML)
                    .get(new GenericType<Collection<DocumentDTO>>() {
                    });
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. (getDocumentsConfiguracao)", logger);
        }
        return documents;
    }

    // </editor-fold>
        
    // <editor-fold desc="///////////////// FORUM QUESTOES /////////////////">
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
            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. (getEnrolledQuestoesREST)", logger);
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
//            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later.", logger);
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
            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. (getEnrolledRespostasREST)", logger);
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
//            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later.", logger);
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
        return "/admin/administracao_configuracoes?faces-redirect=true";
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
//            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. - createQuestao", component, logger);
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

        return "/admin/administracao_configuracoes?faces-redirect=true";
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
//            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. - createResposta", component, logger);
//            return null;
//        }
//
//        return "respostas";
//    }

    // </editor-fold>
    
    // <editor-fold desc="///////////////// GETTERS & SETTERS /////////////////">
    public AdminDTO getNewAdmin() {
        return newAdmin;
    }

    public void setNewAdmin(AdminDTO newAdmin) {
        this.newAdmin = newAdmin;
    }

    public AdminDTO getCurrentAdmin() {
        return currentAdmin;
    }

    public void setCurrentAdmin(AdminDTO currentAdmin) {
        this.currentAdmin = currentAdmin;
    }

    public CargoDTO getNewCargo() {
        return newCargo;
    }

    public void setNewCargo(CargoDTO newCargo) {
        this.newCargo = newCargo;
    }

    public CargoDTO getCurrentCargo() {
        return currentCargo;
    }

    public void setCurrentCargo(CargoDTO currentCargo) {
        this.currentCargo = currentCargo;
    }

    public ClienteDTO getNewCliente() {
        return newCliente;
    }

    public void setNewCliente(ClienteDTO newCliente) {
        this.newCliente = newCliente;
    }

    public ClienteDTO getCurrentCliente() {
        return currentCliente;
    }

    public void setCurrentCliente(ClienteDTO currentCliente) {
        this.currentCliente = currentCliente;
    }

    public ConfiguracaoDTO getNewConfiguracao() {
        return newConfiguracao;
    }

    public void setNewConfiguracao(ConfiguracaoDTO newConfiguracao) {
        this.newConfiguracao = newConfiguracao;
    }

    public ConfiguracaoDTO getCurrentConfiguracao() {
        return currentConfiguracao;
    }

    public void setCurrentConfiguracao(ConfiguracaoDTO currentConfiguracao) {
        this.currentConfiguracao = currentConfiguracao;
    }

    public UIComponent getComponent() {
        return component;
    }

    public void setComponent(UIComponent component) {
        this.component = component;
    }

    public ModuloDTO getNewModulo() {
        return newModulo;
    }

    public void setNewModulo(ModuloDTO newModulo) {
        this.newModulo = newModulo;
    }

    public ModuloDTO getCurrentModulo() {
        return currentModulo;
    }

    public void setCurrentModulo(ModuloDTO currentModulo) {
        this.currentModulo = currentModulo;
    }
//
//    public SoftwareBean getSoftwareBean() {
//        return softwareBean;
//    }
//
//    public void setSoftwareBean(SoftwareBean softwareBean) {
//        this.softwareBean = softwareBean;
//    }

    public SoftwareDTO getNewSoftware() {
        return newSoftware;
    }

    public void setNewSoftware(SoftwareDTO newSoftware) {
        this.newSoftware = newSoftware;
    }

    public SoftwareDTO getCurrentSoftware() {
        return currentSoftware;
    }

    public void setCurrentSoftware(SoftwareDTO currentSoftware) {
        this.currentSoftware = currentSoftware;
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
    
    // </editor-fold>

    // <editor-fold desc="///////////////// GETTERS & SETTERS DOCUMENTOS /////////////////">
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

    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
    
    // </editor-fold>

    // <editor-fold desc="///////////////// VALIDATORS /////////////////">
    public void validateUsername(FacesContext context, UIComponent toValidate, Object value) {
        try {
            //Your validation code goes here
            String username = (String) value;
            //If the validation fails
            if (username.startsWith("xpto")) {
                FacesMessage message = new FacesMessage("Error: invalid username.");
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                context.addMessage(toValidate.getClientId(context), message);
                ((UIInput) toValidate).setValid(false);
            }
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unkown error.", logger);
        }
    }

    // </editor-fold>
}
