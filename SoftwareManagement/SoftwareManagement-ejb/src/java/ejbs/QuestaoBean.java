package ejbs;

import dtos.QuestaoDTO;
import entities.Cliente;
import entities.Configuracao;
import entities.Questao;
import entities.Software;
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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Stateless
@Path("/questoes")
public class QuestaoBean {

    @PersistenceContext
    private EntityManager em;

    
    
    @POST
    @Consumes({MediaType.APPLICATION_XML})
    public void createREST(QuestaoDTO questao)
            throws EntityDoesNotExistsException, EntityAlreadyExistsException, MyConstraintViolationException {
        try {
            create(questao.getId(), questao.getTitulo(), questao.getUsername(), questao.getDescricaoConfig());
        } catch (EntityAlreadyExistsException | MyConstraintViolationException e) {
            throw new EJBException(e.getMessage());
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
//     public void createREST(Questao questao)
//            throws EntityDoesNotExistsException, EntityAlreadyExistsException, MyConstraintViolationException {
//        try {
//            create(questao.getId(), questao.getTitulo(), questao.getUsername(), questao.getConfiguracao().getDescricao());
    
    
    public void create(int id, String titulo, String username, String configuracaoDescricao)
            throws EntityAlreadyExistsException, MyConstraintViolationException {
        try {
            if (em.find(Questao.class, id) != null) {
                throw new EntityAlreadyExistsException("A Questao with that id already exists.");
            }

            Configuracao configuracao = em.find(Configuracao.class, configuracaoDescricao);
            if (configuracao == null) {
                throw new EntityDoesNotExistsException("There is no configuracao with that descricao.");
            }

            Questao questao = new Questao(id, titulo, username, configuracao);
            configuracao.addQuestao(questao);
            em.persist(questao);

        } catch (EntityAlreadyExistsException e) {
            throw e;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    // Listar as questoes por configuracao
    @GET
    @Produces({MediaType.APPLICATION_XML})
    @Path("all-in-configuracao/{nome}")
    public List<QuestaoDTO> getEnrolledQuestoes(@PathParam("nome") String configuracaoDescricao) throws EntityDoesNotExistsException {
        try {

            Configuracao configuracao = em.find(Configuracao.class, configuracaoDescricao);
            if (configuracao == null) {
                throw new EntityDoesNotExistsException("There is no configuracao with that descricao.");
            }

            List<Questao> questoes = (List<Questao>) configuracao.getQuestoes();
            return questoesToDTOs(questoes);

        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    List<QuestaoDTO> questoesToDTOs(List<Questao> questoes) {
        List<QuestaoDTO> dtos = new ArrayList<>();
        for (Questao c : questoes) {
            dtos.add(new QuestaoDTO(          
                   c.getId(), c.getTitulo(), c.getUsername(), c.getData()
            ));
        }
        return dtos;
    }
}
