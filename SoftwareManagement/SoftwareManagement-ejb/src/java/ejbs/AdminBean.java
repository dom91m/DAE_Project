package ejbs;

import dtos.AdminDTO;
import entities.Cargo;
import entities.Admin;
import entities.UserGroup.GROUP;
import enumerations.Estado;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityDoesNotExistsException;
import exceptions.MyConstraintViolationException;
import exceptions.Utils;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
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
@Path("/admins")
public class AdminBean {

    @PersistenceContext
    private EntityManager em;

    @POST
    @Consumes({MediaType.APPLICATION_XML})
    public void createREST(AdminDTO admin)
            throws EntityDoesNotExistsException, EntityAlreadyExistsException, MyConstraintViolationException {
        try {
            create(admin.getUsername(), admin.getPassword(), GROUP.ADMIN, admin.getName(), admin.getEmail(), admin.getCargoCode());
        } catch (EntityDoesNotExistsException | EntityAlreadyExistsException | MyConstraintViolationException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public void create(String username, String password, GROUP group, String name, String email, int cargoCode)
         throws EntityAlreadyExistsException, EntityDoesNotExistsException, MyConstraintViolationException {
        try {
            if (em.find(Admin.class, username) != null) {
                throw new EntityAlreadyExistsException("A user with that username already exists.");
            }
            Cargo cargo = em.find(Cargo.class, cargoCode);
            if (cargo == null) {
                throw new EntityDoesNotExistsException("There is no cargo with that code.");
            }
            Admin admin = new Admin(username, password, group, name, email, cargo);
            cargo.addAdmin(admin);
            em.persist(admin);
        } catch (EntityAlreadyExistsException | EntityDoesNotExistsException e) {
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
    public List<AdminDTO> getAll() {
        try {
            List<Admin> admins = (List<Admin>) em.createNamedQuery("getAllAdmins").getResultList();
            return adminsToDTOs(admins);
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
//    @GET
//    @Produces({MediaType.APPLICATION_XML})
//    @Path("/estados/all")
//    public List<String> getAllEstados() {
//        List<String> estadoNames = new LinkedList<>();
//        
//        EnumSet.allOf(Estado.class).forEach(estado -> {
//            estadoNames.add(estado.name());
//        });
//        return estadoNames;  
//    }
    
    @GET
    @Produces({MediaType.APPLICATION_XML})
    @Path("{username}")
    public Admin getAdmin(@PathParam("username") String username) {
        try {
            Admin admin = em.find(Admin.class, username);
            return admin;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    @PUT
    @Path("{username}")
    @Consumes({MediaType.APPLICATION_XML})
    public void updateREST(@PathParam("username") String username, AdminDTO admin)
            throws EntityDoesNotExistsException, MyConstraintViolationException {
        try {
            update(admin.getUsername(), admin.getPassword(), admin.getName(), admin.getEmail(), admin.getCargoCode());
        } catch (EntityDoesNotExistsException | ConstraintViolationException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    public void update(String username, String password, String name, String email, int cargoCode) 
        throws EntityDoesNotExistsException, MyConstraintViolationException{
        try {
            Admin admin = em.find(Admin.class, username);
            if (admin == null) {
                throw new EntityDoesNotExistsException("There is no admin with that username.");
            }

            Cargo cargo = em.find(Cargo.class, cargoCode);
            if (cargo == null) {
                throw new EntityDoesNotExistsException("There is no cargo with that code.");
            }

            admin.setPassword(password);
            admin.setName(name);
            admin.setEmail(email);
            admin.getCargo().removeAdmin(admin);
            admin.setCargo(cargo);
            cargo.addAdmin(admin);
            em.merge(admin);
            
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
            Admin admin = em.find(Admin.class, username);
            if (admin == null) {
                throw new EntityDoesNotExistsException("There is no admin with that username.");
            }
            admin.getCargo().removeAdmin(admin);
            
            em.remove(admin);
        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }   

//remove sem rest
//    public void remove(String username) throws EntityDoesNotExistsException {
//        try {
//            Admin admin = em.find(Admin.class, username);
//            if (admin == null) {
//                throw new EntityDoesNotExistsException("There is no admin with that username.");
//            }
//            admin.getCargo().removeAdmin(admin);
//            
//            em.remove(admin);
//        
//        } catch (EntityDoesNotExistsException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new EJBException(e.getMessage());
//        }
//    }

    /*
    public void enrollAdmin(String username, int subjectCode) 
            throws EntityDoesNotExistsException, SubjectNotInCargoException, AdminEnrolledException{
        try {

            Admin admin = em.find(Admin.class, username);
            if (admin == null) {
                throw new EntityDoesNotExistsException("There is no admin with that username.");
            }

            Subject subject = em.find(Subject.class, subjectCode);
            if (subject == null) {
                throw new EntityDoesNotExistsException("There is no subject with that code.");
            }

            if (!admin.getCargo().getSubjects().contains(subject)) {
                throw new SubjectNotInCargoException("Admin's cargo has no such subject.");
            }

            if (subject.getAdmins().contains(admin)) {
                throw new AdminEnrolledException("Admin is already enrolled in that subject.");
            }

            subject.addAdmin(admin);
            admin.addSubject(subject);

        } catch (EntityDoesNotExistsException | SubjectNotInCargoException | AdminEnrolledException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public void unrollAdmin(String username, int subjectCode) 
            throws EntityDoesNotExistsException, AdminNotEnrolledException {
        try {
            Subject subject = em.find(Subject.class, subjectCode);
            if(subject == null){
                throw new EntityDoesNotExistsException("There is no subject with that code.");
            }            
            
            Admin admin = em.find(Admin.class, username);
            if(admin == null){
                throw new EntityDoesNotExistsException("There is no admin with that username.");
            }
            
            if(!subject.getAdmins().contains(admin)){
                throw new AdminNotEnrolledException();
            }            
            
            subject.removeAdmin(admin);
            admin.removeSubject(subject);

        } catch (EntityDoesNotExistsException | AdminNotEnrolledException e) {
            throw e;             
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    

    public List<AdminDTO> getEnrolledAdmins(int subjectCode) throws EntityDoesNotExistsException{
        try {
            Subject subject = em.find(Subject.class, subjectCode);
            if( subject == null){
                throw new EntityDoesNotExistsException("There is no subject with that code.");
            }            
            List<Admin> admins = (List<Admin>) subject.getAdmins();
            return adminsToDTOs(admins);
        } catch (EntityDoesNotExistsException e) {
            throw e;             
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    public List<AdminDTO> getUnrolledAdmins(int subjectCode) throws EntityDoesNotExistsException{
        try {
            Subject subject = em.find(Subject.class, subjectCode);
            if( subject == null){
                throw new EntityDoesNotExistsException("There is no subject with that code.");
            }            
            List<Admin> admins = (List<Admin>) em.createNamedQuery("getAllCargoAdmins")
                    .setParameter("cargoCode", subject.getCargo().getCode())
                    .getResultList();
            List<Admin> enrolled = em.find(Subject.class, subjectCode).getAdmins();
            admins.removeAll(enrolled);
            return adminsToDTOs(admins);
        } catch (EntityDoesNotExistsException e) {
            throw e;             
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

*/
    AdminDTO adminToDTO(Admin admin) {
        return new AdminDTO(
                admin.getUsername(),
                null,
                admin.getName(),
                admin.getEmail(),
                admin.getCargo().getCode(),
                admin.getCargo().getName());
    }


    List<AdminDTO> adminsToDTOs(List<Admin> admins) {
        List<AdminDTO> dtos = new ArrayList<>();
        for (Admin s : admins) {
            dtos.add(adminToDTO(s));
        }
        return dtos;
    }
}
