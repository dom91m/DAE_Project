package ejbs;

import dtos.CargoDTO;
import entities.Cargo;
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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Stateless
@Path("/cargos")
public class CargoBean {

    @PersistenceContext
    private EntityManager em;

    @POST
    @Consumes({MediaType.APPLICATION_XML})
    public void createREST(CargoDTO cargo)
            throws EntityDoesNotExistsException, EntityAlreadyExistsException, MyConstraintViolationException {
        try {
            create(cargo.getCode(), cargo.getName());
        } catch (EntityAlreadyExistsException | MyConstraintViolationException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public void create(int code, String name)
            throws EntityAlreadyExistsException, MyConstraintViolationException {
        try {
            if (em.find(Cargo.class, code) != null) {
                throw new EntityAlreadyExistsException("A cargo with that code already exists.");
            }

            em.persist(new Cargo(code, name));

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
    public List<CargoDTO> getAll() {
        try {
            List<Cargo> cargos = (List<Cargo>) em.createNamedQuery("getAllCargos").getResultList();
            return cargosToDTOs(cargos);
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    @DELETE
    @Path("{code}")
    public void removeREST(@PathParam("code") int code) throws EntityDoesNotExistsException {
        try {
            Cargo cargo = em.find(Cargo.class, code);
            if (cargo == null) {
                throw new EntityDoesNotExistsException("There is no cargo with that code");
            }
            em.remove(cargo);
        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }   

//remove sem rest
//    public void remove(int code) throws EntityDoesNotExistsException {
//        try {
//            Cargo cargo = em.find(Cargo.class, code);
//            if (cargo == null) {
//                throw new EntityDoesNotExistsException("There is no cargo with that code");
//            }
//
//            em.remove(cargo);
//
//        } catch (EntityDoesNotExistsException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new EJBException(e.getMessage());
//        }
//    }

    List<CargoDTO> cargosToDTOs(List<Cargo> cargos) {
        List<CargoDTO> dtos = new ArrayList<>();
        for (Cargo c : cargos) {
            dtos.add(new CargoDTO(c.getCode(), c.getName()));            
        }
        return dtos;
    }
}
