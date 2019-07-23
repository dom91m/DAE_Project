package entities;

import enumerations.Estado;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "CONFIGURACOES")/*,
        uniqueConstraints
        = @UniqueConstraint(columnNames = {"DESCRICAO"}))*/
@NamedQueries({
    @NamedQuery(name = "getAllConfiguracoes",
            query = "SELECT c FROM Configuracao c ORDER BY c.descricao")
    ,
        @NamedQuery(name = "getAllConfiguracoesTemplates",
            query = "SELECT c FROM Configuracao c WHERE c.descricao LIKE '%Template%' ORDER BY c.descricao")
    ,
        
        @NamedQuery(name = "getAllConfiguracoesDescricoes",
            query = "SELECT c.descricao FROM Configuracao c ORDER BY c.descricao")})

public class Configuracao implements Serializable {

    @Id
    @NotNull(message = "Descricao must not be empty")
    private String descricao;

    @NotNull(message = "A configuracao must have a estado")
    private Estado estado;

    @NotNull(message = "Versao do software must not be empty")
    private double versao;

    // PARECE-ME BEM (1)
    @ManyToMany
    @JoinTable(name = "CONFIGURACAO_MODULO",
            joinColumns
            = @JoinColumn(name = "CONFIGURACAO_DESCRICAO", referencedColumnName = "DESCRICAO"),
            inverseJoinColumns
            = @JoinColumn(name = "MODULO_CODE", referencedColumnName = "CODE"))
    private List<Modulo> modulos;

    // PARECE-ME BEM (2)
    @ManyToMany
    @JoinTable(name = "CONFIGURACAO_CLIENTE",
            joinColumns
            = @JoinColumn(name = "CONFIGURACAO_DESCRICAO", referencedColumnName = "DESCRICAO"),
            inverseJoinColumns
            = @JoinColumn(name = "CLIENTE_USERNAME", referencedColumnName = "USERNAME"))
    private List<Cliente> clientes;

    // PARECE-ME BEM (3)
    @ManyToOne
    @JoinColumn(name = "SOFTWARE_ID")
    //@NotNull (message="A configuracao must have a software")
    private Software software;

    // PARECE-ME BEM (4)
    @OneToMany(mappedBy = "configuracao")
    public List<Document> documents;

    @OneToMany(mappedBy = "configuracao")
    public List<Questao> questoes;

    /*
    @OneToMany(mappedBy = "configuracao", cascade = CascadeType.REMOVE)
    private List<Hardware> hardwares;

    @OneToMany(mappedBy = "configuracao", cascade = CascadeType.REMOVE)
    private List<ServicoCloud> servicosCloud;

    @OneToMany(mappedBy = "configuracao", cascade = CascadeType.REMOVE)
    private List<Licenca> licencas;

    @OneToMany(mappedBy = "configuracao", cascade = CascadeType.REMOVE)
    private List<Extensao> extensoes;

    @OneToMany(mappedBy = "configuracao", cascade = CascadeType.REMOVE)
    private List<Repositorio> repositorios;

    @OneToMany(mappedBy = "configuracao", cascade = CascadeType.REMOVE)
    private List<MaterialDeApoio> materiaisDeApoio;
     */
    public Configuracao() {
        clientes = new LinkedList<>();
        modulos = new LinkedList<>();
        documents = new LinkedList<>();
        questoes = new LinkedList<>();
        // softwares = new LinkedList<>();
        /*   hardwares = new LinkedList<>();
        servicosCloud = new LinkedList<>();
        licencas = new LinkedList<>();
        extensoes = new LinkedList<>();
        repositorios = new LinkedList<>();
        materiaisDeApoio = new LinkedList<>();
         */
    }

    public Configuracao(String descricao, Estado estado, double versao, Software software) {
        this.descricao = descricao;
        this.estado = estado;
        this.versao = versao;
        clientes = new LinkedList<>();
        modulos = new LinkedList<>();
        this.software = software;
        documents = new LinkedList<>();
        questoes = new LinkedList<>();
        //  softwares = new LinkedList<>();
        /*       hardwares = new LinkedList<>();
        servicosCloud = new LinkedList<>();
        licencas = new LinkedList<>();
        extensoes = new LinkedList<>();
        repositorios = new LinkedList<>();
        materiaisDeApoio = new LinkedList<>(); */
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public double getVersao() {
        return versao;
    }

    public void setVersao(double versao) {
        this.versao = versao;
    }

    // ******************** LISTAS DA CONFIGURACAO ******************** //
    // ******************** CLIENTES  ******************** //
    public List<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    public void addCliente(Cliente cliente) {
        clientes.add(cliente);
    }

    public void removeCliente(Cliente cliente) {
        clientes.remove(cliente);
    }

    // ******************** MODULOS  ******************** //
    public List<Modulo> getModulos() {
        return modulos;
    }

    public void setModulos(List<Modulo> modulos) {
        this.modulos = modulos;
    }

    public void addModulo(Modulo modulo) {
        modulos.add(modulo);
    }

    public void removeModulo(Modulo modulo) {
        modulos.remove(modulo);
    }

    // ******************** SOFTWARES  ******************** //
    public Software getSoftware() {
        return software;
    }

    public void setSoftware(Software software) {
        this.software = software;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public void addDocument(Document document) {
        this.documents.add(document);
    }

    public void removeDocument(Document document) {
        this.documents.remove(document);
    }

    // ******************** FORUM DE DISCUSSAO  ******************** //
    public List<Questao> getQuestoes() {
        return questoes;
    }

    public void setQuestoes(List<Questao> questoes) {
        this.questoes = questoes;
    }

    public void addQuestao(Questao questao) {
        this.questoes.add(questao);
    }

    public void removeQuestao(Questao questao) {
        this.questoes.remove(questao);
    }

    public List<Questao> removeAllQuestoes() {
        return questoes = null;
    }


    /*
    // ******************** HARDWARE  ******************** //
    public List<Hardware> getHardwares() {
        return hardwares;
    }

    public void setHardwares(List<Hardware> hardwares) {
        this.hardwares = hardwares;
    }

    public void addHardware(Hardware hardware) {
        hardwares.add(hardware);
    }

    public void removeHardware(Hardware hardware) {
        hardwares.remove(hardware);
    }

    // ******************** CLOUD  ******************** //
    public List<ServicoCloud> getServicosCloud() {
        return servicosCloud;
    }

    public void setServicosCloud(List<ServicoCloud> servicosCloud) {
        this.servicosCloud = servicosCloud;
    }

    public void addServicoCloud(ServicoCloud servicoCloud) {
        servicosCloud.add(servicoCloud);
    }

    public void removeServicoCloud(ServicoCloud servicoCloud) {
        servicosCloud.remove(servicoCloud);
    }

    // ******************** LICENCAS  ******************** //
    public List<Licenca> getLicencas() {
        return licencas;
    }

    public void setLicencas(List<Licenca> licencas) {
        this.licencas = licencas;
    }

    public void addLicenca(Licenca licenca) {
        licencas.add(licenca);
    }

    public void removeLicenca(Licenca licenca) {
        licencas.remove(licenca);
    }

    // ******************** EXTENSOES  ******************** //
    public List<Extensao> getExtensoes() {
        return extensoes;
    }

    public void setExtensoes(List<Extensao> extensoes) {
        this.extensoes = extensoes;
    }

    public void addExtensao(Extensao extensao) {
        extensoes.add(extensao);
    }

    public void removeExtensao(Extensao extensao) {
        extensoes.remove(extensao);
    }

    // ******************** REPOSITORIOS  ******************** //
    public List<Repositorio> getRepositorios() {
        return repositorios;
    }

    public void setRepositorios(List<Repositorio> repositorios) {
        this.repositorios = repositorios;
    }

    public void addRepositorio(Repositorio repositorio) {
        repositorios.add(repositorio);
    }

    public void removeRepositorio(Repositorio repositorio) {
        repositorios.remove(repositorio);
    }

    // ******************** MATERIAIS DE APOIO  ******************** //
    public List<MaterialDeApoio> getMateriaisDeApoio() {
        return materiaisDeApoio;
    }

    public void setMateriaisDeApoio(List<MaterialDeApoio> materiaisDeApoio) {
        this.materiaisDeApoio = materiaisDeApoio;
    }

    public void addMaterialDeApoio(MaterialDeApoio materialDeApoio) {
        materiaisDeApoio.add(materialDeApoio);
    }

    public void removeMaterialDeApoio(MaterialDeApoio materialDeApoio) {
        materiaisDeApoio.remove(materialDeApoio);
    }
     */
}
