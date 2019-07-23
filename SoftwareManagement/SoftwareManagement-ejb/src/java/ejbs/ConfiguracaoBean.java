package ejbs;

import dtos.ConfiguracaoDTO;
import dtos.DocumentDTO;
import entities.Cliente;
import entities.Configuracao;
import entities.Document;
import entities.Modulo;
import entities.Questao;
import entities.Software;
import enumerations.Estado;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityDoesNotExistsException;
import exceptions.ModuloEnrolledException;
import exceptions.MyConstraintViolationException;
import exceptions.Utils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Stateless
@Path("/configuracoes")
public class ConfiguracaoBean extends Bean<Configuracao> {

    @PersistenceContext
    private EntityManager em;
    
    @EJB
    EmailBean emailBean;    

    @POST
    @Consumes({MediaType.APPLICATION_XML})
    public void createREST(ConfiguracaoDTO configuracao)
            throws EntityDoesNotExistsException, EntityAlreadyExistsException, MyConstraintViolationException {
        try {
            create(configuracao.getDescricao(), configuracao.getEstado(), configuracao.getVersao(), configuracao.getSoftwareId());
        } catch (EntityAlreadyExistsException | MyConstraintViolationException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public void create(String descricao, Estado estado, double versao, int softwareId)
            throws EntityAlreadyExistsException, MyConstraintViolationException {
        try {
            Configuracao configuracao = em.find(Configuracao.class, descricao);
            if (configuracao != null) {
                throw new EntityAlreadyExistsException("A Configuracao with that descricao already exists.");
            }
            Software software = em.find(Software.class, softwareId);
            if (software == null) {
                throw new EntityDoesNotExistsException("There is no software with that code.");
            }
            em.persist(new Configuracao(descricao, estado, versao, software));
            software.addConfiguracao(configuracao);
            
            String mensagemEmail = "<h2 style=\"color: #2e6c80;\">Foi adicionada a seguinte configuração:</h2>\n" +
                "<p>\n" +
                "  Descrição: " + descricao + "<br />\n" +
                "  Estado: " + estado + "<br />\n" +
                "  Versão: " + versao + "<br />\n" +
                "  SoftwareId: " + softwareId + "<br />\n" +
                "  Nome do software: " + software.getName() + "<br />\n" +
                "</p>\n" +
                "\n" +
                "<br />\n <p><h3>\n Cumprimentos,\n </h3></p>\n <p><h3>\n Software Management 2018/2019\n </h3></p>";
            emailBean.sendEmail("danffaustino@gmail.com", mensagemEmail, "mensagemconfigbean");

        } catch (EntityAlreadyExistsException e) {
            throw e;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public void createInicial(String descricao, Estado estado, double versao, int softwareId)
            throws EntityAlreadyExistsException, MyConstraintViolationException {
        try {
            Configuracao configuracao = em.find(Configuracao.class, descricao);
            if (configuracao != null) {
                throw new EntityAlreadyExistsException("A Configuracao with that descricao already exists.");
            }
            Software software = em.find(Software.class, softwareId);
            if (software == null) {
                throw new EntityDoesNotExistsException("There is no software with that code.");
            }
            em.persist(new Configuracao(descricao, estado, versao, software));
            software.addConfiguracao(configuracao);
            
            

        } catch (EntityAlreadyExistsException e) {
            throw e;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_XML})
    @Path("all")
    public List<ConfiguracaoDTO> getAll() {
        try {
            List<Configuracao> configuracoes = (List<Configuracao>) em.createNamedQuery("getAllConfiguracoes").getResultList();
            return configuracoesToDTOs(configuracoes);
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    @GET
    @Produces({MediaType.APPLICATION_XML})
    @Path("alltemplates")
    public List<ConfiguracaoDTO> getAllConfiguracoesTemplatesREST() {
        try {
            List<Configuracao> configuracoes = (List<Configuracao>) em.createNamedQuery("getAllConfiguracoesTemplates").getResultList();
            return configuracoesToDTOs(configuracoes);
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    @DELETE
    @Path("{descricao}")
    public void removeREST(@PathParam("descricao") String descricao) throws EntityDoesNotExistsException {
        try {
            Configuracao configuracao = em.find(Configuracao.class, descricao);
            if (configuracao == null) {
                throw new EntityDoesNotExistsException("There is no configuracao with that descricao");
            }

            // ir aos Modulos e remover esta configuracao
            for (Modulo modulo : configuracao.getModulos()) {
                modulo.removeConfiguracao(configuracao);
            }

            // ir aos Clientes e remover esta configuracao
            for (Cliente cliente : configuracao.getClientes()) {
                cliente.removeConfiguracao(configuracao);
            }
            
            // ir a cada questao e colocar a configuracao a null
            for (Questao questao : configuracao.getQuestoes()) {
                questao.setConfiguracao(null);
                //configuracao.getQuestoes().remove(questao);
            }

            configuracao.getSoftware().removeConfiguracao(configuracao);
            em.remove(configuracao);
        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }   
 
    // REMOVER MODULOS DA CONFIGURACAO
    @DELETE
    @Path("{descricao}/modulos/{moduloCode}")
    public void removeModuloConfiguracaoREST(@PathParam("moduloCode") int moduloCode, @PathParam("descricao") String descricao) 
            throws EntityDoesNotExistsException {
        try {
            Modulo modulo = em.find(Modulo.class, moduloCode);
            if (modulo == null) {
                throw new EntityDoesNotExistsException("There is no modulo with that code.");
            }
            Configuracao configuracao = em.find(Configuracao.class, descricao);
            if (configuracao == null) {
                throw new EntityDoesNotExistsException("There is no configuracao with that descricao.");
            }

            modulo.removeConfiguracao(configuracao);
            configuracao.removeModulo(modulo);

        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    // ADICIONAR MODULOS A CONFIGURACAO
    @POST
    @Path("{descricao}/modulos/{moduloCode}")
    @Consumes({MediaType.APPLICATION_XML})
    public void addModuloNaConfiguracaoREST(@PathParam("moduloCode") int moduloCode, @PathParam("descricao") String descricao)
            throws EntityDoesNotExistsException {
        try {
            Configuracao configuracao = em.find(Configuracao.class, descricao);
            if (configuracao == null) {
                throw new EntityDoesNotExistsException("There is no configuracao with that descricao.");
            }
            Modulo modulo = em.find(Modulo.class, moduloCode);
            if (modulo == null) {
                throw new EntityDoesNotExistsException("There is no modulo with that code.");
            }

            configuracao.addModulo(modulo);
            modulo.addConfiguracao(configuracao);

        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    @PUT
    @Path("{descricao}")
    @Consumes({MediaType.APPLICATION_XML})
    public void updateREST(@PathParam("descricao") String descricao, ConfiguracaoDTO configuracao)
            throws EntityDoesNotExistsException, MyConstraintViolationException {
        try {
               update(configuracao.getDescricao(), configuracao.getEstado(), configuracao.getVersao(), configuracao.getSoftwareId());
        } catch (EntityDoesNotExistsException | MyConstraintViolationException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public void update(String descricao, Estado estado, double versao, int softwareId)
            throws EntityDoesNotExistsException, MyConstraintViolationException {
        try {
            Configuracao configuracao = em.find(Configuracao.class, descricao);
            if (configuracao == null) {
                throw new EntityDoesNotExistsException("There is no configuracao with that descricao.");
            }

            Software software = em.find(Software.class, softwareId);
            if (software == null) {
                throw new EntityDoesNotExistsException("There is no software with that code.");
            }
            
            Configuracao configAntiga = configuracao;
            configuracao.setDescricao(descricao);
            configuracao.setEstado(estado);
            configuracao.setVersao(versao);

            configuracao.getSoftware().removeConfiguracao(configuracao);
            configuracao.setSoftware(software);
            software.addConfiguracao(configuracao);

            em.merge(configuracao);
            
            String mensagemEmail = "<h2 style=\"color: #2e6c80;\">Foi alterada a seguinte configuração (" + configAntiga.getDescricao() + "):</h2>\n" +
                "<p>\n" +
                "  Descrição: " + descricao + "<br />\n" +
                "  Estado: " + estado + "<br />\n" +
                "  Versão: " + versao + "<br />\n" +
                "  SoftwareId: " + softwareId + "<br />\n" +
                "  Nome do software: " + software.getName() + "<br />\n" +
                "</p>\n" +
                "\n" +
                "<br />\n <p><h3>\n Cumprimentos,\n </h3></p>\n <p><h3>\n Software Management 2018/2019\n </h3></p>";
            emailBean.sendEmail("danffaustino@gmail.com", mensagemEmail, "mensagemconfigbean");

        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    List<ConfiguracaoDTO> configuracoesToDTOs(List<Configuracao> configuracoes) {
        List<ConfiguracaoDTO> dtos = new ArrayList<>();
        for (Configuracao c : configuracoes) {
            dtos.add(new ConfiguracaoDTO(
                    c.getDescricao(), c.getEstado(), c.getVersao(), c.getSoftware().getId(), c.getSoftware().getName()));
        }
        return dtos;
    }

    //colocar as configuracoes nos clientes ("descricao da config" no "cliente")
    public void enrollConfiguracao(String configuracaoDescricao, String username)
            throws EntityDoesNotExistsException, ModuloEnrolledException {
        try {

            // ver se existe a configuracao
            Configuracao configuracao = em.find(Configuracao.class, configuracaoDescricao);
            if (configuracao == null) {
                throw new EntityDoesNotExistsException("There is no configuracao with that descricao.");
            }

            // ver se existe o cliente
            Cliente cliente = em.find(Cliente.class, username);
            if (cliente == null) {
                throw new EntityDoesNotExistsException("There is no cliente with that username.");
            }

            // ver se o cliente ja tem essa configuracao
            if (cliente.getConfiguracoes().contains(configuracao)) {
                throw new ModuloEnrolledException("configuracao is already enrolled in that cliente.");
            }
            // adicionar a configuracao no cliente
            cliente.addConfiguracao(configuracao);
            configuracao.addCliente(cliente);

        } catch (EntityDoesNotExistsException | ModuloEnrolledException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    // Listar as configuracoes por cliente
    @GET
    @Produces({MediaType.APPLICATION_XML})
    @Path("/all-in-cliente/{username}")
    public List<ConfiguracaoDTO> getEnrolledConfiguracoesREST(@PathParam("username") String username) throws EntityDoesNotExistsException {
        try {
            Cliente cliente = em.find(Cliente.class, username);
            if (cliente == null) {
                throw new EntityDoesNotExistsException("There is no cliente with that username.");
            }

            List<Configuracao> configuracoes = (List<Configuracao>) cliente.getConfiguracoes();
            return configuracoesToDTOs(configuracoes);
        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    

//    public List<ConfiguracaoDTO> getEnrolledConfiguracoes(String username) throws EntityDoesNotExistsException {
//        try {
//
//            Cliente cliente = em.find(Cliente.class, username);
//            if (cliente == null) {
//                throw new EntityDoesNotExistsException("There is no cliente with that username.");
//            }
//
//            List<Configuracao> configuracoes = (List<Configuracao>) cliente.getConfiguracoes();
//            return configuracoesToDTOs(configuracoes);
//        } catch (EntityDoesNotExistsException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new EJBException(e.getMessage());
//        }
//    }

    // Listar as configuracoes que o cliente nao tem
    @GET
    @Produces({MediaType.APPLICATION_XML})
    @Path("/not-in-cliente/{username}")
    public List<ConfiguracaoDTO> getEnrolledConfiguracoesNotInClienteREST(@PathParam("username") String username) throws EntityDoesNotExistsException {
        try {
            Cliente cliente = em.find(Cliente.class, username);
            if (cliente == null) {
                throw new EntityDoesNotExistsException("There is no cliente with that username.");
            }

            boolean encontrou = false;
            // fazer a diferença entre a lista configuracoesDoCliente e todasConfiguracoes
            List<Configuracao> configuracoesNotInCliente;
            List<Configuracao> configuracoesDoCliente = (List<Configuracao>) cliente.getConfiguracoes();
            List<Configuracao> allConfiguracoes = (List<Configuracao>) em.createNamedQuery("getAllConfiguracoes").getResultList();

            configuracoesNotInCliente = allConfiguracoes;

            for (Configuracao c1 : configuracoesDoCliente) {
                encontrou = false;

                while (!encontrou) {
                    for (Configuracao c2 : allConfiguracoes) {
                        if (c1.getDescricao().equals(c2.getDescricao())) {
                            configuracoesNotInCliente.remove(c2);
                            encontrou = true;
                            break;
                        }
                    }
                }
            }

            return configuracoesToDTOs(configuracoesNotInCliente);

        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
//    public List<ConfiguracaoDTO> getEnrolledConfiguracoesNotInCliente(String username) throws EntityDoesNotExistsException {
//        try {
//
//            Cliente cliente = em.find(Cliente.class, username);
//            if (cliente == null) {
//                throw new EntityDoesNotExistsException("There is no cliente with that username.");
//            }
//
//            boolean encontrou = false;
//            // fazer a diferença entre a lista configuracoesDoCliente e todasConfiguracoes
//            List<Configuracao> configuracoesNotInCliente;
//            List<Configuracao> configuracoesDoCliente = (List<Configuracao>) cliente.getConfiguracoes();
//            List<Configuracao> allConfiguracoes = (List<Configuracao>) em.createNamedQuery("getAllConfiguracoes").getResultList();
//
//            configuracoesNotInCliente = allConfiguracoes;
//
//            for (Configuracao c1 : configuracoesDoCliente) {
//                encontrou = false;
//
//                while (!encontrou) {
//                    for (Configuracao c2 : allConfiguracoes) {
//                        if (c1.getDescricao().equals(c2.getDescricao())) {
//                            configuracoesNotInCliente.remove(c2);
//                            encontrou = true;
//                            break;
//                        }
//                    }
//                }
//            }
//
//            return configuracoesToDTOs(configuracoesNotInCliente);
//
//        } catch (EntityDoesNotExistsException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new EJBException(e.getMessage());
//        }
//    }
    
    @DELETE
    @Path("{descricao}/cliente/{username}")           
    public void removeClienteConfiguracaoREST(@PathParam("descricao") String descricao, @PathParam("username") String username)
            throws EntityDoesNotExistsException {
        try {
            Cliente cliente = em.find(Cliente.class, username);
            if (cliente == null) {
                throw new EntityDoesNotExistsException("There is no cliente with that username.");
            }
            Configuracao configuracao = em.find(Configuracao.class, descricao);
            if (configuracao == null) {
                throw new EntityDoesNotExistsException("There is no configuracao with that descricao.");
            }

            cliente.removeConfiguracao(configuracao);
            configuracao.removeCliente(cliente);
            
            String mensagemEmail = "<h2 style=\"color: #2e6c80;\">Foi removida da sua lista de configurações a seguinte configuração:</h2>\n" +
                "<p>\n" +
                "  Descrição: " + descricao + "<br />\n" +
                "</p>\n" +
                "\n" +
                "<br />\n <p><h3>\n Cumprimentos,\n </h3></p>\n <p><h3>\n Software Management 2018/2019\n </h3></p>";
            emailBean.sendEmail("danffaustino@gmail.com", mensagemEmail, "mensagemconfigbean");

        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
//    public void removeClienteConfiguracao(String username, String descricao)
//            throws EntityDoesNotExistsException {
//        try {
//            Cliente cliente = em.find(Cliente.class, username);
//            if (cliente == null) {
//                throw new EntityDoesNotExistsException("There is no cliente with that username.");
//            }
//            Configuracao configuracao = em.find(Configuracao.class, descricao);
//            if (configuracao == null) {
//                throw new EntityDoesNotExistsException("There is no configuracao with that descricao.");
//            }
//
//            cliente.removeConfiguracao(configuracao);
//            configuracao.removeCliente(cliente);
//
//        } catch (EntityDoesNotExistsException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new EJBException(e.getMessage());
//        }
//    }

    // **************** DOCUMENTOS **************** //
    @PUT
    @Path("/addDocument/{descricao}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void addDocument(
            @PathParam("descricao") String descricao,
            DocumentDTO doc)
            throws EntityDoesNotExistsException {
        try {
            Configuracao configuracao = em.find(Configuracao.class, descricao);
            if (configuracao == null) {
                throw new EntityDoesNotExistsException("There is no configuracao with such descricao.");
            }

            Document document = new Document(doc.getFilepath(), doc.getDesiredName(), doc.getMimeType(), configuracao);
            em.persist(document);
            configuracao.addDocument(document);

        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/document/{id}")
    public DocumentDTO getDocument(@PathParam("id") int id) throws EntityDoesNotExistsException {
        Document doc = em.find(Document.class, id);

        if (doc == null) {
            throw new EntityDoesNotExistsException();
        }

        return toDTO(doc, DocumentDTO.class);
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/documents/{descricao}")
    public Collection<DocumentDTO> getDocuments(@PathParam("descricao") String descricao) throws EntityDoesNotExistsException {
        try {
            List<Document> docs = em.createNamedQuery("getDocumentsOfConfiguracao", Document.class).setParameter("descricao", descricao).getResultList();
            return toDTOs(docs, DocumentDTO.class);
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    public boolean hasDocuments(String descricao)
            throws EntityDoesNotExistsException {
        try {
            Configuracao configuracao = em.find(Configuracao.class, descricao);
            if (configuracao == null) {
                throw new EntityDoesNotExistsException("There is no configuracao with such descricao.");
            }
            return !configuracao.getDocuments().isEmpty();
        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    
    
    
    
    
    
    
    
       
//    public void remove(String descricao) throws EntityDoesNotExistsException {
//        try {
//            Configuracao configuracao = em.find(Configuracao.class, descricao);
//            if (configuracao == null) {
//                throw new EntityDoesNotExistsException("There is no configuracao with that descricao");
//            }
//
//            // ir aos Modulos e remover esta configuracao
//            for (Modulo modulo : configuracao.getModulos()) {
//                modulo.removeConfiguracao(configuracao);
//            }
//
//            // ir aos Clientes e remover esta configuracao
//            for (Cliente cliente : configuracao.getClientes()) {
//                cliente.removeConfiguracao(configuracao);
//            }
//            
//            // ir a cada questao e colocar a configuracao a null
//            for (Questao questao : configuracao.getQuestoes()) {
//                questao.setConfiguracao(null);
//                //configuracao.getQuestoes().remove(questao);
//            }
//
//            configuracao.getSoftware().removeConfiguracao(configuracao);
//            em.remove(configuracao);
//
//        } catch (EntityDoesNotExistsException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new EJBException(e.getMessage());
//        }
//    }
    
//    public void addModuloNaConfiguracao(String descricao, int moduloCode)
//            throws EntityDoesNotExistsException {
//        try {
//            Configuracao configuracao = em.find(Configuracao.class, descricao);
//            if (configuracao == null) {
//                throw new EntityDoesNotExistsException("There is no configuracao with that descricao.");
//            }
//            Modulo modulo = em.find(Modulo.class, moduloCode);
//            if (modulo == null) {
//                throw new EntityDoesNotExistsException("There is no modulo with that code.");
//            }
//
//            configuracao.addModulo(modulo);
//            modulo.addConfiguracao(configuracao);
//
//        } catch (EntityDoesNotExistsException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new EJBException(e.getMessage());
//        }
//    }

//    public void removeModuloConfiguracao(int moduloCode, String descricao)
//            throws EntityDoesNotExistsException {
//        try {
//            Modulo modulo = em.find(Modulo.class, moduloCode);
//            if (modulo == null) {
//                throw new EntityDoesNotExistsException("There is no modulo with that code.");
//            }
//            Configuracao configuracao = em.find(Configuracao.class, descricao);
//            if (configuracao == null) {
//                throw new EntityDoesNotExistsException("There is no configuracao with that descricao.");
//            }
//
//            modulo.removeConfiguracao(configuracao);
//            configuracao.removeModulo(modulo);
//
//        } catch (EntityDoesNotExistsException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new EJBException(e.getMessage());
//        }
//    }
}
