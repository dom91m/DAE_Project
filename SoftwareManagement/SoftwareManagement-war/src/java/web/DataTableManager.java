/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import dtos.AdminDTO;
import dtos.CargoDTO;
import dtos.ClienteDTO;
import dtos.ConfiguracaoDTO;
import dtos.ModuloDTO;
import dtos.SoftwareDTO;
import ejbs.AdminBean;
import ejbs.CargoBean;
import ejbs.ClienteBean;
import ejbs.ConfiguracaoBean;
import ejbs.ModuloBean;
import ejbs.SoftwareBean;
import entities.Cargo;
import entities.Cliente;
import enumerations.Estado;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

@ManagedBean
@SessionScoped
public class DataTableManager {

    @EJB
    private AdminBean adminBean;
    @EJB
    private CargoBean cargoBean;
    private static final Logger logger = Logger.getLogger("web.DataTableManager");
    @EJB
    private ClienteBean clienteBean;
    @EJB
    private ConfiguracaoBean configuracaoBean;
    @EJB
    private SoftwareBean softwareBean;
    @EJB
    private ModuloBean moduloBean;

    // Geridos automaticamente pelo PrimeFaces
    private List<AdminDTO> filteredAdmins;
    private List<CargoDTO> filteredCargos;
    private List<ClienteDTO> filteredClients;
    private List<ConfiguracaoDTO> filteredConfigs;
    private List<SoftwareDTO> filteredSoftware;
    private List<ModuloDTO> filteredModulos;
    
    private Client client;
    private final String baseUri = "http://localhost:8080/SoftwareManagement-war/webapi";

    public DataTableManager() {
         client = ClientBuilder.newClient();
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
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again later! - getAllAdmins", logger);
            return null;
        }
        return returnedAdmins;
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
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again later! - getAllCargos", logger);
            return null;
        }
        return returnedCargos;
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
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again later! - getAllClientes", logger);
            return null;
        }
        return returnedClientes;
    }
    
    public List<ConfiguracaoDTO> getAllConfiguracoesREST() {
        List<ConfiguracaoDTO> returnedConfiguracoes = null;
        try{
            returnedConfiguracoes = client.target(baseUri)
               .path("/configuracoes/all")
               .request(MediaType.APPLICATION_XML)
               .get(new GenericType<List<ConfiguracaoDTO>>() {
               });

        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again later! - getAllConfiguracoes", logger);
            return null;
        }
        return returnedConfiguracoes;
    }
    
    public List<SoftwareDTO> getAllSoftwaresREST() {
        List<SoftwareDTO> returnedSoftwares = null;
        try{
            returnedSoftwares = client.target(baseUri)
               .path("/softwares/all")
               .request(MediaType.APPLICATION_XML)
               .get(new GenericType<List<SoftwareDTO>>() {
               });

        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. (getAllSoftwaresREST)", logger);
            return null;
        }
        return returnedSoftwares;
    }
    
//    public List<String> getAllEstadosREST() {
//        List<String> returnedEstados = null;
//        try{
//            returnedEstados = client.target(baseUri)
//               .path("/admin/estados/all")
//               .request(MediaType.APPLICATION_XML)
//               .get(new GenericType<List<String>>() {
//               });
//
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. (getAllEstadosREST)", logger);
//            return null;
//        }
//        return returnedEstados;
//    }
    
    public List<String> getAllEstados() {
        List<String> estadoNames = new LinkedList<>();
        
        EnumSet.allOf(Estado.class).forEach(estado -> {
            estadoNames.add(estado.name());
        });
        return estadoNames;        
    }
    
    public List<ModuloDTO> getAllModulosREST() {
        List<ModuloDTO> returnedModulos = null;
        try{
            returnedModulos = client.target(baseUri)
               .path("/softwares/all")
               .request(MediaType.APPLICATION_XML)
               .get(new GenericType<List<ModuloDTO>>() {
               });

        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "There was an error. Contact an Admin or try again later. (getAllModulosREST)", logger);
            return null;
        }
        return returnedModulos;
    }

    
//    public List<AdminDTO> getAllAdmins() {
//        try {
//            return adminBean.getAll();
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "Unexpected error! Try again later! - getAllAdmins", logger);
//            return null;
//        }
//    }
    
//    public List<CargoDTO> getAllCargos() {
//        try {
//            return cargoBean.getAll();
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "Unexpected error! Try again later!  - getAllCargos", logger);
//            return null;
//        }
//    }
    
//    public List<ClienteDTO> getAllClientes() {
//        try {
//            return clienteBean.getAll();
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "Unexpected error! Try again later! - getAllClientes", logger);
//            return null;
//        }
//    }
    
//    public List<ConfiguracaoDTO> getAllConfiguracoes() {
//        try {
//            return configuracaoBean.getAll();
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "Unexpected error! Try again later!", logger);
//            return null;
//        }
//    }
    
//    public List<SoftwareDTO> getAllSoftwares() {
//        try {
//            return softwareBean.getAll();
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "Unexpected error! Try again later! - getAllSoftwares", logger);
//            return null;
//        }
//    }
    
//    public List<ModuloDTO> getAllModulos() {
//        try {
//            return moduloBean.getAll();
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "Unexpected error! Try again later!  - getAllModulos", logger);
//            return null;
//        }
//    }
    
    public List<AdminDTO> getFilteredAdmins() {
        return filteredAdmins;
    }

    public void setFilteredAdmins(List<AdminDTO> filteredAdmins) {
        this.filteredAdmins = filteredAdmins;
    }

    public List<CargoDTO> getFilteredCargos() {
        return filteredCargos;
    }

    public void setFilteredCargos(List<CargoDTO> filteredCargos) {
        this.filteredCargos = filteredCargos;
    }

    public List<ClienteDTO> getFilteredClients() {
        return filteredClients;
    }

    public void setFilteredClients(List<ClienteDTO> filteredClients) {
        this.filteredClients = filteredClients;
    }

    public List<ConfiguracaoDTO> getFilteredConfigs() {
        return filteredConfigs;
    }

    public void setFilteredConfigs(List<ConfiguracaoDTO> filteredConfigs) {
        this.filteredConfigs = filteredConfigs;
    }

    public List<SoftwareDTO> getFilteredSoftware() {
        return filteredSoftware;
    }

    public void setFilteredSoftware(List<SoftwareDTO> filteredSoftware) {
        this.filteredSoftware = filteredSoftware;
    }

    public List<ModuloDTO> getFilteredModulos() {
        return filteredModulos;
    }

    public void setFilteredModulos(List<ModuloDTO> filteredModulos) {
        this.filteredModulos = filteredModulos;
    }    
}
