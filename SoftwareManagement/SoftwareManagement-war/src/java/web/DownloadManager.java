package web;

import dtos.DocumentDTO;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.ws.rs.core.MediaType;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import util.URILookup;

@ManagedBean
public class DownloadManager implements Serializable {

    private static final Logger logger = Logger.getLogger("web.downloadManager");

    private int documentId;

    @ManagedProperty("#{administratorManager}")
    private AdministratorManager administratorManager;

    public DownloadManager() {

    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public StreamedContent getFile() {
        DocumentDTO document = null;
        try {
            document = administratorManager.getClient().target(URILookup.getBaseAPI())
                    .path("/configuracao/document")
                    .path(Integer.toString(documentId))
                    .request(MediaType.APPLICATION_XML)
                    .get(DocumentDTO.class);

            InputStream in = new FileInputStream(document.getFilepath());

            return new DefaultStreamedContent(in, document.getMimeType(), document.getDesiredName());
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Could not download the file", logger);
        }
        return null;
    }

    public AdministratorManager getAdministratorManager() {
        return administratorManager;
    }

    public void setAdministratorManager(AdministratorManager administratorManager) {
        this.administratorManager = administratorManager;
    }
}
