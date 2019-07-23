package ejbs;

import dtos.ModuloDTO;
import dtos.RespostaDTO;
import dtos.RespostaDTO;
import entities.Configuracao;
import entities.Modulo;
import entities.Questao;
import entities.Resposta;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityDoesNotExistsException;
import exceptions.MyConstraintViolationException;
import exceptions.ModuloEnrolledException;
import exceptions.RespostaAlreadyExistsException;
import exceptions.Utils;
import java.util.ArrayList;
import java.util.List;
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
@Path("/respostas")
public class RespostaBean {

    @PersistenceContext
    private EntityManager em;

    @POST
    @Consumes({MediaType.APPLICATION_XML})
    public void createREST(RespostaDTO resposta)
            throws EntityDoesNotExistsException, EntityAlreadyExistsException, MyConstraintViolationException {
        try {
            create(resposta.getId(), resposta.getTexto(), resposta.getUsername(), resposta.getQuestaoId());
        } catch (RespostaAlreadyExistsException | MyConstraintViolationException e) {
            throw new EJBException(e.getMessage());
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public void create(int id, String texto, String username, int questaoId)
            throws RespostaAlreadyExistsException, MyConstraintViolationException {
        try {
            if (em.find(Resposta.class, id) != null) {
                throw new RespostaAlreadyExistsException("A resposta with that id already exists.");
            }

            Questao questao = em.find(Questao.class, questaoId);
            if (questao == null) {
                throw new EntityDoesNotExistsException("There is no questao with that questaoId.");
            }

            Resposta resposta = new Resposta(id, texto, username, questao);
            questao.addResposta(resposta);
            em.persist(resposta);

        } catch (RespostaAlreadyExistsException e) {
            throw e;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

// Listar as respostas por questao
    @GET
    @Produces({MediaType.APPLICATION_XML})
    @Path("all-in-questao/{questao}")
    public List<RespostaDTO> getEnrolledRespostas(@PathParam("questao") int questaoId) throws EntityDoesNotExistsException {
        try {

            Questao questao = em.find(Questao.class, questaoId);
            if (questao == null) {
                throw new EntityDoesNotExistsException("There is no Questao with that questaoId.");
            }

            List<Resposta> respostas = (List<Resposta>) questao.getRespostas();
            return respostasToDTOs(respostas);

        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    List<RespostaDTO> respostasToDTOs(List<Resposta> respostas) {
        List<RespostaDTO> dtos = new ArrayList<>();
        for (Resposta c : respostas) {
            dtos.add(new RespostaDTO(
                    c.getId(), c.getTexto(), c.getUsername(), c.getData(), c.getQuestao().getId()
            ));
        }
        return dtos;
    }

}
