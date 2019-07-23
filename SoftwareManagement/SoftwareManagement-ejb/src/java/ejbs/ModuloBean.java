package ejbs;

import dtos.ClienteDTO;
import dtos.ModuloDTO;
import entities.Configuracao;
import entities.Modulo;
import entities.UserGroup;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityDoesNotExistsException;
import exceptions.MyConstraintViolationException;
import exceptions.ModuloEnrolledException;
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
@Path("/modulos")
public class ModuloBean {

    @PersistenceContext
    private EntityManager em;

    @POST
    @Consumes({MediaType.APPLICATION_XML})
    public void createREST(ModuloDTO modulo)
            throws EntityAlreadyExistsException, MyConstraintViolationException {
        try {
            create(modulo.getCode(), modulo.getName(), modulo.getDescricao(), modulo.getVersao());
        } catch (EntityAlreadyExistsException | MyConstraintViolationException e) {
            throw new EJBException(e.getMessage());
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public void create(int code, String name, String descricao, double versao)
            throws EntityAlreadyExistsException, MyConstraintViolationException {
        try {
            if (em.find(Modulo.class, code) != null) {
                throw new EntityAlreadyExistsException("A Modulo with that code already exists.");
            }

            em.persist(new Modulo(code, name, descricao, versao));

        } catch (EntityAlreadyExistsException e) {
            throw e;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    @PUT
    @Path("{code}")
    @Consumes({MediaType.APPLICATION_XML})
    public void updateREST(@PathParam("code") int code, ModuloDTO modulo)
            throws EntityDoesNotExistsException, MyConstraintViolationException {
        try {
            update(modulo.getCode(), modulo.getName(), modulo.getDescricao(), modulo.getVersao());
        } catch (EntityDoesNotExistsException | ConstraintViolationException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public void update(int code, String name, String descricao, double versao)
            throws EntityDoesNotExistsException, MyConstraintViolationException {
        try {
            Modulo modulo = em.find(Modulo.class, code);
            if (modulo == null) {
                throw new EntityDoesNotExistsException("There is no modulo with that code.");
            }

            modulo.setDescricao(descricao);
            modulo.setName(name);
            modulo.setVersao(versao);

        } catch (EntityDoesNotExistsException e) {
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
    public List<ModuloDTO> getAll() {
        try {
            List<Modulo> modulos = (List<Modulo>) em.createNamedQuery("getAllModulos").getResultList();
            return modulosToDTOs(modulos);
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    @DELETE
    @Path("{code}")
    public void removeREST(@PathParam("code") int code) throws EntityDoesNotExistsException {
        try {
            Modulo modulo = em.find(Modulo.class, code);
            if (modulo == null) {
                throw new EntityDoesNotExistsException("There is no modulo with that code");
            }
            // ir as Configuracoes e remover este modulo
            for (Configuracao configuracao : modulo.getConfiguracoes()) {
                configuracao.removeModulo(modulo);
            }

            em.remove(modulo);

        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public void remove(int code) throws EntityDoesNotExistsException {
        try {
            Modulo modulo = em.find(Modulo.class, code);
            if (modulo == null) {
                throw new EntityDoesNotExistsException("There is no modulo with that code");
            }
            // ir as Configuracoes e remover este modulo
            for (Configuracao configuracao : modulo.getConfiguracoes()) {
                configuracao.removeModulo(modulo);
            }

            em.remove(modulo);

        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    List<ModuloDTO> modulosToDTOs(List<Modulo> modulos) {
        List<ModuloDTO> dtos = new ArrayList<>();
        for (Modulo c : modulos) {
            dtos.add(new ModuloDTO(c.getCode(), c.getName(), c.getDescricao(), c.getVersao()));
        }
        return dtos;
    }
    
    // Listar os modulos que a configuracao tem
    @GET
    @Produces({MediaType.APPLICATION_XML})
    @Path("/all-in-configuracao/{descricao}")
    public List<ModuloDTO> getConfiguracaoModulosREST(@PathParam("descricao") String descricao) throws EntityDoesNotExistsException {
         try {
            Configuracao configuracao = em.find(Configuracao.class, descricao);
            if (configuracao == null) {
                throw new EntityDoesNotExistsException("Configuracao does not exists.");
            }

            return modulosToDTOs(configuracao.getModulos());

        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public List<ModuloDTO> getEnrolledModulos(String descricao) throws EntityDoesNotExistsException {
        try {
            Configuracao configuracao = em.find(Configuracao.class, descricao);
            if (configuracao == null) {
                throw new EntityDoesNotExistsException("There is no configuracao with that descricao.");
            }
            List<Modulo> modulos = (List<Modulo>) configuracao.getModulos();
            return modulosToDTOs(modulos);
        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    // colocar o modulo numa configuracao
    public void enrollModulo(int code, String configuracaoDescricao)
            throws EntityDoesNotExistsException, ModuloEnrolledException {
        try {

            Modulo modulo = em.find(Modulo.class, code);
            if (modulo == null) {
                throw new EntityDoesNotExistsException("There is no modulo with that code.");
            }

            Configuracao configuracao = em.find(Configuracao.class, configuracaoDescricao);
            if (configuracao == null) {
                throw new EntityDoesNotExistsException("There is no configuracao with that descricao.");
            }

            if (configuracao.getModulos().contains(modulo)) {
                throw new ModuloEnrolledException("Modulo is already enrolled in that configuracao.");
            }

            configuracao.addModulo(modulo);
            modulo.addConfiguracao(configuracao);

        } catch (EntityDoesNotExistsException | ModuloEnrolledException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    // Listar os modulos que a configuracao nao tem   
    @GET
    @Produces({MediaType.APPLICATION_XML})
    @Path("/not-in-configuracao/{descricao}")
    public List<ModuloDTO> getEnrolledModulosNotInConfiguracaoREST(@PathParam("descricao") String descricao) throws EntityDoesNotExistsException {
        try {
            Configuracao configuracao = em.find(Configuracao.class, descricao);
            if (configuracao == null) {
                throw new EntityDoesNotExistsException("There is no configuracao with that descricao.");
            }

            boolean encontrou = false;
            // fazer a diferença entre a lista configuracoesDoCliente e todasConfiguracoes
            List<Modulo> modulosNotInConfiguracao;
            List<Modulo> modulosDaConfiguracao = (List<Modulo>) configuracao.getModulos();
            List<Modulo> allModulos = (List<Modulo>) em.createNamedQuery("getAllModulos").getResultList();

            modulosNotInConfiguracao = allModulos;

            for (Modulo c1 : modulosDaConfiguracao) {
                encontrou = false;

                while (!encontrou) {
                    for (Modulo c2 : allModulos) {
                        if (c1.getName().equals(c2.getName())) {
                            if (c1.getVersao() == c2.getVersao()) {
                                modulosNotInConfiguracao.remove(c2);
                                encontrou = true;
                                break;
                            }
                        }
                    }
                }
            }

            return modulosToDTOs(modulosNotInConfiguracao);

        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    
//    public List<ModuloDTO> getEnrolledModulosNotInConfiguracao(String descricao) throws EntityDoesNotExistsException {
//        try {
//
//            Configuracao configuracao = em.find(Configuracao.class, descricao);
//            if (configuracao == null) {
//                throw new EntityDoesNotExistsException("There is no configuracao with that descricao.");
//            }
//
//            boolean encontrou = false;
//            // fazer a diferença entre a lista configuracoesDoCliente e todasConfiguracoes
//            List<Modulo> modulosNotInConfiguracao;
//            List<Modulo> modulosDaConfiguracao = (List<Modulo>) configuracao.getModulos();
//            List<Modulo> allModulos = (List<Modulo>) em.createNamedQuery("getAllModulos").getResultList();
//
//            modulosNotInConfiguracao = allModulos;
//
//            for (Modulo c1 : modulosDaConfiguracao) {
//                encontrou = false;
//
//                while (!encontrou) {
//                    for (Modulo c2 : allModulos) {
//                        if (c1.getName().equals(c2.getName())) {
//                            if (c1.getVersao() == c2.getVersao()) {
//                                modulosNotInConfiguracao.remove(c2);
//                                encontrou = true;
//                                break;
//                            }
//                        }
//                    }
//                }
//            }
//
//            return modulosToDTOs(modulosNotInConfiguracao);
//
//        } catch (EntityDoesNotExistsException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new EJBException(e.getMessage());
//        }
//    }
    
    
//    public List<ModuloDTO> getConfiguracaoModulos(String descricao)
//            throws EntityDoesNotExistsException {
//        try {
//            Configuracao configuracao = em.find(Configuracao.class, descricao);
//            if (configuracao == null) {
//                throw new EntityDoesNotExistsException("Configuracao does not exists.");
//            }
//
//            return modulosToDTOs(configuracao.getModulos());
//
//        } catch (EntityDoesNotExistsException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new EJBException(e.getMessage());
//        }
//    }


}
