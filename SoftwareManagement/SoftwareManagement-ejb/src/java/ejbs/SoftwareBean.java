package ejbs;

import dtos.SoftwareDTO;
import entities.Configuracao;
import entities.Software;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityDoesNotExistsException;
import exceptions.MyConstraintViolationException;
import exceptions.Utils;
import java.util.ArrayList;
import java.util.List;
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
@Path("/softwares")
public class SoftwareBean {

    @PersistenceContext
    private EntityManager em;

    @POST
    @Consumes({MediaType.APPLICATION_XML})
    public void createREST(SoftwareDTO software)
            throws EntityDoesNotExistsException, EntityAlreadyExistsException, MyConstraintViolationException {
        try {
            if (em.find(Software.class, software.getId()) != null) {
                throw new EntityAlreadyExistsException("A Software with that id already exists.");
            }

            em.persist(new Software(software.getId(), software.getName(), software.getDescricao(), software.getVersao()));

        } catch (EntityAlreadyExistsException e) {
            throw e;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public void create(int id, String name, String descricao, double versao)
            throws EntityAlreadyExistsException, MyConstraintViolationException {
        try {
            if (em.find(Software.class, id) != null) {
                throw new EntityAlreadyExistsException("A Software with that id already exists.");
            }

            em.persist(new Software(id, name, descricao, versao));

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
    public List<SoftwareDTO> getAll() {
        try {
            List<Software> softwares = (List<Software>) em.createNamedQuery("getAllSoftwares").getResultList();
            return softwaresToDTOs(softwares);
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    @DELETE 
    @Path("{id}")           
    public void removeREST(@PathParam("id") int id) throws EntityDoesNotExistsException{
          try {
            Software software = em.find(Software.class, id);
            if (software == null) {
                throw new EntityDoesNotExistsException("There is no software with that id");
            }
            em.remove(software);
        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
//    public void remove(int id) throws EntityDoesNotExistsException {
//        try {
//            Software software = em.find(Software.class, id);
//            if (software == null) {
//                throw new EntityDoesNotExistsException("There is no software with that id");
//            }
//            em.remove(software);
//        } catch (EntityDoesNotExistsException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new EJBException(e.getMessage());
//        }
//    }
/*
    public void removeConfiguracaoSoftware(String descricao, int id)
            throws EntityDoesNotExistsException {
        try {
            Configuracao configuracao = em.find(Configuracao.class, descricao);
            if (configuracao == null) {
                throw new EntityDoesNotExistsException("There is no configuracao with that descricao.");
            }
            Software software = em.find(Software.class, id);
            if (software == null) {
                throw new EntityDoesNotExistsException("There is no software with that id.");
            }

            configuracao.removeSoftware(software);
            software.removeConfiguracao(configuracao);

        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    // ADICIONAR CONFIGURACAO NO SOFTWARE
    public void addConfiguracaoNoSoftware(String descricao, int id)
            throws EntityDoesNotExistsException {
        try {
            Software software = em.find(Software.class, id);
            if (software == null) {
                throw new EntityDoesNotExistsException("There is no software with that id.");
            }
            Configuracao configuracao = em.find(Configuracao.class, descricao);
            if (configuracao == null) {
                throw new EntityDoesNotExistsException("There is no configuracao with that descricao.");
            }

            software.addConfiguracao(configuracao);
            configuracao.addSoftware(software);

        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
*/
    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML})
    public void updateREST(@PathParam("id") int id, SoftwareDTO software) throws EntityDoesNotExistsException, MyConstraintViolationException{
        try {
            update(software.getId(), software.getName(), software.getDescricao(), software.getVersao());
        } catch (EntityDoesNotExistsException | ConstraintViolationException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public void update(int id, String name, String descricao, double versao)
            throws EntityDoesNotExistsException, MyConstraintViolationException {
        try {
            Software software = em.find(Software.class, id);
            if (software == null) {
                throw new EntityDoesNotExistsException("There is no software with that id.");
            }

            software.setName(name);
            software.setDescricao(descricao);
            software.setVersao(versao);

            em.merge(software);

        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    List<SoftwareDTO> softwaresToDTOs(List<Software> softwares) {
        List<SoftwareDTO> dtos = new ArrayList<>();
        for (Software c : softwares) {
            dtos.add(new SoftwareDTO(
                    c.getId(), c.getName(), c.getDescricao(), c.getVersao()));
        }
        return dtos;
    }

    /*
    
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

        } catch (EntityDoesNotExistsException | ModuloEnrolledException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }




    // Listar as configuracoes por cliente
    public List<ConfiguracaoDTO> getEnrolledConfiguracoes(String username) throws EntityDoesNotExistsException {
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

    // Listar as configuracoes que o cliente nao tem
    public List<ConfiguracaoDTO> getEnrolledConfiguracoesNotInCliente(String username) throws EntityDoesNotExistsException {
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
    */
}
