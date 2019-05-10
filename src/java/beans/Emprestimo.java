/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ieieo
 */
@Entity
@Table(name = "emprestimo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Emprestimo.findAll", query = "SELECT e FROM Emprestimo e")
    , @NamedQuery(name = "Emprestimo.findById", query = "SELECT e FROM Emprestimo e WHERE e.id = :id")
    , @NamedQuery(name = "Emprestimo.findByDataInicio", query = "SELECT e FROM Emprestimo e WHERE e.dataInicio = :dataInicio")
    , @NamedQuery(name = "Emprestimo.findByDataFim", query = "SELECT e FROM Emprestimo e WHERE e.dataFim = :dataFim")
    , @NamedQuery(name = "Emprestimo.findByDataEntrega", query = "SELECT e FROM Emprestimo e WHERE e.dataEntrega = :dataEntrega")
    , @NamedQuery(name = "Emprestimo.findByRenovacoes", query = "SELECT e FROM Emprestimo e WHERE e.renovacoes = :renovacoes")})
public class Emprestimo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "data_inicio")
    @Temporal(TemporalType.DATE)
    private Date dataInicio;
    @Basic(optional = false)
    @Column(name = "data_fim")
    @Temporal(TemporalType.DATE)
    private Date dataFim;
    @Column(name = "data_entrega")
    @Temporal(TemporalType.DATE)
    private Date dataEntrega;
    @Basic(optional = false)
    @Column(name = "renovacoes")
    private int renovacoes;
    @JoinColumn(name = "livro", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Livro livro;
    @JoinColumn(name = "usuario", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuario usuario;


    public Emprestimo() {
    }

    public Emprestimo(Integer id) {
        this.id = id;
    }

    public Emprestimo(Integer id, Date dataInicio, Date dataFim, int renovacoes) {
        this.id = id;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.renovacoes = renovacoes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public Date getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(Date dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public int getRenovacoes() {
        return renovacoes;
    }

    public void setRenovacoes(int renovacoes) {
        this.renovacoes = renovacoes;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Emprestimo)) {
            return false;
        }
        Emprestimo other = (Emprestimo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    public  String getDataDoInicio() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyy");
        return format.format(dataInicio);
    }

    public String getDataDaEntrega() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyy");

        return format.format(dataFim);
    }
    public String getDataQueEntregue() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyy");
        if(dataEntrega != null){
        return format.format(dataEntrega);
        }else{
            return "----------";
        }
    }
    public String getDataEntregue(){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyy");
        if (getDataEntrega() == null) {
            return format.format("-----------");
        }
        return format.format(dataEntrega);
    }

    public int daysBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }

    public String getDiasAtrasados() {
        if (dataEntrega != null && dataEntrega.after(dataFim)) {
            return -1*daysBetween(dataEntrega, dataFim) + " dias";
        } else {
            return "------";
        }
    }

    @Override
    public String toString() {
        return "beans.Emprestimo[ id=" + id + " ]";
    }

}
