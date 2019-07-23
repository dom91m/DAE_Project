package ejbs;

import dtos.ClienteDTO;
import entities.Cliente;
import entities.Configuracao;
import entities.UserGroup.GROUP;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityDoesNotExistsException;
import exceptions.MyConstraintViolationException;
import exceptions.Utils;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.security.RolesAllowed;
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
@Path("/clientes")
public class ClienteBean {

    @PersistenceContext
    private EntityManager em;
    
    @POST
    @Consumes({MediaType.APPLICATION_XML})
    public void createREST(ClienteDTO cliente)
            throws EntityDoesNotExistsException, EntityAlreadyExistsException, MyConstraintViolationException {
        try {
            create(cliente.getUsername(), cliente.getPassword(), GROUP.CLIENTE, cliente.getName(), cliente.getEmail(), cliente.getMorada(), cliente.getPessoaDeContacto());
        } catch (EntityDoesNotExistsException | EntityAlreadyExistsException | MyConstraintViolationException e) {
            throw new EJBException(e.getMessage());
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    public void create(String username, String password, GROUP group, String name, String email, String morada, String pessoaDeContacto)
            throws EntityAlreadyExistsException, EntityDoesNotExistsException, MyConstraintViolationException {
        try {
            if (em.find(Cliente.class, username) != null) {
                throw new EntityAlreadyExistsException("A user with that username already exists.");
            }

            Cliente cliente = new Cliente(username, password, group, name, email, morada, pessoaDeContacto);
            em.persist(cliente);

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
    public List<ClienteDTO> getAll() {
        try {
            List<Cliente> clientes = (List<Cliente>) em.createNamedQuery("getAllClientes").getResultList();
            return clientesToDTOs(clientes);
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_XML})
    @Path("{username}")
    public Cliente getCliente(@PathParam("username") String username) {
        try {
            Cliente cliente = em.find(Cliente.class, username);
            return cliente;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    @PUT
    @Path("{username}")
    @Consumes({MediaType.APPLICATION_XML})
    public void updateREST(@PathParam("username") String username, ClienteDTO cliente)
            throws EntityDoesNotExistsException, MyConstraintViolationException {
        try {
            update(cliente.getUsername(), cliente.getPassword(), cliente.getName(), cliente.getEmail(), cliente.getMorada(), cliente.getPessoaDeContacto());
        } catch (EntityDoesNotExistsException | ConstraintViolationException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public void update(String username, String password, String name, String email, String morada, String pessoaDeContacto)
            throws EntityDoesNotExistsException, MyConstraintViolationException {
        try {
            Cliente cliente = em.find(Cliente.class, username);
            if (cliente == null) {
                throw new EntityDoesNotExistsException("There is no cliente with that username.");
            }

            cliente.setPassword(password);
            cliente.setName(name);
            cliente.setEmail(email);
            cliente.setMorada(morada);
            cliente.setPessoaDeContacto(pessoaDeContacto);

        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    @DELETE
    @Path("{username}")
    public void removeREST(@PathParam("username") String username) throws EntityDoesNotExistsException {
        try {
            Cliente cliente = em.find(Cliente.class, username);
            if (cliente == null) {
                throw new EntityDoesNotExistsException("There is no cliente with that username.");
            }
            //cliente.getConfiguracao().removeCliente(cliente);

            em.remove(cliente);
        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }   
    
//remove sem rest
//    public void remove(String username) throws EntityDoesNotExistsException {
//        try {
//            Cliente cliente = em.find(Cliente.class, username);
//            if (cliente == null) {
//                throw new EntityDoesNotExistsException("There is no cliente with that username.");
//            }
//            //cliente.getConfiguracao().removeCliente(cliente);
//
//            /*
//            for (Subject subject : cliente.getSubjects()) {
//                subject.removeCliente(cliente);
//            }
//             */
//            em.remove(cliente);
//
//        } catch (EntityDoesNotExistsException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new EJBException(e.getMessage());
//        }
//    }

    
    
    
    /*
    public void enrollCliente(String username, int subjectCode) 
            throws EntityDoesNotExistsException, SubjectNotInConfiguracaoException, ClienteEnrolledException{
        try {

            Cliente cliente = em.find(Cliente.class, username);
            if (cliente == null) {
                throw new EntityDoesNotExistsException("There is no cliente with that username.");
            }

            Subject subject = em.find(Subject.class, subjectCode);
            if (subject == null) {
                throw new EntityDoesNotExistsException("There is no subject with that code.");
            }

            if (!cliente.getConfiguracao().getSubjects().contains(subject)) {
                throw new SubjectNotInConfiguracaoException("Cliente's configuracao has no such subject.");
            }

            if (subject.getClientes().contains(cliente)) {
                throw new ClienteEnrolledException("Cliente is already enrolled in that subject.");
            }

            subject.addCliente(cliente);
            cliente.addSubject(subject);

        } catch (EntityDoesNotExistsException | SubjectNotInConfiguracaoException | ClienteEnrolledException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public void unrollCliente(String username, int subjectCode) 
            throws EntityDoesNotExistsException, ClienteNotEnrolledException {
        try {
            Subject subject = em.find(Subject.class, subjectCode);
            if(subject == null){
                throw new EntityDoesNotExistsException("There is no subject with that code.");
            }            
            
            Cliente cliente = em.find(Cliente.class, username);
            if(cliente == null){
                throw new EntityDoesNotExistsException("There is no cliente with that username.");
            }
            
            if(!subject.getClientes().contains(cliente)){
                throw new ClienteNotEnrolledException();
            }            
            
            subject.removeCliente(cliente);
            cliente.removeSubject(subject);

        } catch (EntityDoesNotExistsException | ClienteNotEnrolledException e) {
            throw e;             
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    

    public List<ClienteDTO> getEnrolledClientes(int subjectCode) throws EntityDoesNotExistsException{
        try {
            Subject subject = em.find(Subject.class, subjectCode);
            if( subject == null){
                throw new EntityDoesNotExistsException("There is no subject with that code.");
            }            
            List<Cliente> clientes = (List<Cliente>) subject.getClientes();
            return clientesToDTOs(clientes);
        } catch (EntityDoesNotExistsException e) {
            throw e;             
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    public List<ClienteDTO> getUnrolledClientes(int subjectCode) throws EntityDoesNotExistsException{
        try {
            Subject subject = em.find(Subject.class, subjectCode);
            if( subject == null){
                throw new EntityDoesNotExistsException("There is no subject with that code.");
            }            
            List<Cliente> clientes = (List<Cliente>) em.createNamedQuery("getAllConfiguracaoClientes")
                    .setParameter("configuracaoCode", subject.getConfiguracao().getCode())
                    .getResultList();
            List<Cliente> enrolled = em.find(Subject.class, subjectCode).getClientes();
            clientes.removeAll(enrolled);
            return clientesToDTOs(clientes);
        } catch (EntityDoesNotExistsException e) {
            throw e;             
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

     */
    ClienteDTO clienteToDTO(Cliente cliente) {
        return new ClienteDTO(
                cliente.getUsername(),
                null,
                cliente.getName(),
                cliente.getEmail(),
                cliente.getMorada(),
                cliente.getPessoaDeContacto());
        //cliente.getConfiguracao().getCode(),
        //cliente.getConfiguracao().getName());
    }

    List<ClienteDTO> clientesToDTOs(List<Cliente> clientes) {
        List<ClienteDTO> dtos = new ArrayList<>();
        for (Cliente s : clientes) {
            dtos.add(clienteToDTO(s));
        }
        return dtos;
    }
    
    @DELETE 
    @Path("{usernameCliente}/configuracoes/{descricao}")           
    public void removeConfiguracaoDoClienteREST(@PathParam("descricao") String descricao, @PathParam("usernameCliente") String usernameCliente)
            throws EntityDoesNotExistsException {
        try {
            Configuracao configuracao = em.find(Configuracao.class, descricao);
            if (configuracao == null) {
                throw new EntityDoesNotExistsException("There is no configuracao with that descricao.");
            }
            Cliente cliente = em.find(Cliente.class, usernameCliente);
            if (cliente == null) {
                throw new EntityDoesNotExistsException("There is no cliente with that username.");
            }

            configuracao.removeCliente(cliente);
            cliente.removeConfiguracao(configuracao);

        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
//    
//    public void removeConfiguracaoDoCliente(String descricao, String usernameCliente)
//            throws EntityDoesNotExistsException {
//        try {
//            Configuracao configuracao = em.find(Configuracao.class, descricao);
//            if (configuracao == null) {
//                throw new EntityDoesNotExistsException("There is no configuracao with that descricao.");
//            }
//            Cliente cliente = em.find(Cliente.class, usernameCliente);
//            if (cliente == null) {
//                throw new EntityDoesNotExistsException("There is no cliente with that username.");
//            }
//
//            configuracao.removeCliente(cliente);
//            cliente.removeConfiguracao(configuracao);
//
//            
//            
//        } catch (EntityDoesNotExistsException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new EJBException(e.getMessage());
//        }
//    }

    @POST
    @Produces({MediaType.APPLICATION_XML})
    @Path("{username}/descricoes/{descricao}")
    public void addConfiguracaoNoClienteREST(@PathParam("descricao") String descricao, @PathParam("username") String usernameCliente)
            throws EntityDoesNotExistsException {
        try {
            Configuracao configuracao = em.find(Configuracao.class, descricao);
            if (configuracao == null) {
                throw new EntityDoesNotExistsException("There is no configuracao with that descricao.");
            }
            Cliente cliente = em.find(Cliente.class, usernameCliente);
            if (cliente == null) {
                throw new EntityDoesNotExistsException("There is no cliente with that username.");
            }

            configuracao.addCliente(cliente);
            cliente.addConfiguracao(configuracao);

        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_XML})
    @Path("/all-in-configuracao/{descricao}")
    public List<ClienteDTO> getEnrolledConfiguracoesREST(@PathParam("descricao") String descricao) throws EntityDoesNotExistsException {
        try {
            Configuracao configuracao = em.find(Configuracao.class, descricao);
            if (configuracao == null) {
                throw new EntityDoesNotExistsException("There is no configuracao with that descricao.");
            }
            List<Cliente> clientes = (List<Cliente>) configuracao.getClientes();
            return clientesToDTOs(clientes);
        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    

//    public void addConfiguracaoNoCliente(String descricao, String usernameCliente)
//            throws EntityDoesNotExistsException {
//        try {
//            Configuracao configuracao = em.find(Configuracao.class, descricao);
//            if (configuracao == null) {
//                throw new EntityDoesNotExistsException("There is no configuracao with that descricao.");
//            }
//            Cliente cliente = em.find(Cliente.class, usernameCliente);
//            if (cliente == null) {
//                throw new EntityDoesNotExistsException("There is no cliente with that username.");
//            }
//
//            configuracao.addCliente(cliente);
//            cliente.addConfiguracao(configuracao);
//
//        } catch (EntityDoesNotExistsException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new EJBException(e.getMessage());
//        }
//    }    
    
//    public List<ClienteDTO> getEnrolledConfiguracoes(String descricao) throws EntityDoesNotExistsException {
//        try {
//            Configuracao configuracao = em.find(Configuracao.class, descricao);
//            if (configuracao == null) {
//                throw new EntityDoesNotExistsException("There is no configuracao with that descricao.");
//            }
//            List<Cliente> clientes = (List<Cliente>) configuracao.getClientes();
//            return clientesToDTOs(clientes);
//        } catch (EntityDoesNotExistsException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new EJBException(e.getMessage());
//        }
//    }
    

    
}
