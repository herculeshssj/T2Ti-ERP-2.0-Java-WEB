/*
* The MIT License
* 
* Copyright: Copyright (C) 2014 T2Ti.COM
* 
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
* 
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
* 
* The author may be contacted at: t2ti.com@gmail.com
*
* @author Claudio de Barros (T2Ti.com)
* @version 2.0
*/
package com.t2tierp.controller.conciliacaocontabil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.t2tierp.controller.AbstractController;
import com.t2tierp.model.bean.cadastros.Cliente;
import com.t2tierp.model.bean.conciliacaocontabil.ConciliacaoCliente;
import com.t2tierp.model.bean.contabilidade.ContabilLancamentoDetalhe;
import com.t2tierp.model.bean.financeiro.FinParcelaRecebimento;
import com.t2tierp.model.dao.Filtro;
import com.t2tierp.model.dao.InterfaceDAO;
import com.t2tierp.util.FacesContextUtil;

@ManagedBean
@ViewScoped
public class ConciliacaoClienteController extends AbstractController<Cliente> implements Serializable {

    private static final long serialVersionUID = 1L;
    @EJB
    private InterfaceDAO<FinParcelaRecebimento> parcelaRecebimentoDAO;
    @EJB
    private InterfaceDAO<ContabilLancamentoDetalhe> lancamentoDAO;
    
    private List<FinParcelaRecebimento> parcelaRecebimento;
    private List<ContabilLancamentoDetalhe> contabilLancamentoDetalhe;
    private List<ConciliacaoCliente> conciliacaoCliente;
    private Date dataInicial;
    private Date dataFinal;
    
    @Override
    public Class<Cliente> getClazz() {
        return Cliente.class;
    }

    @Override
    public String getFuncaoBase() {
        return "CONCILIACAO_CLIENTE";
    }

    public void carregaDados() {
    	try {
    		List<Filtro> filtros = new ArrayList<>();
    		filtros.add(new Filtro(Filtro.AND, "dataRecebimento", Filtro.MAIOR_OU_IGUAL, dataInicial));
    		filtros.add(new Filtro(Filtro.AND, "dataRecebimento", Filtro.MENOR_OU_IGUAL, dataFinal));
    		filtros.add(new Filtro(Filtro.AND, "finParcelaReceber.finLancamentoReceber.cliente", Filtro.IGUAL, getObjeto()));
    		parcelaRecebimento = parcelaRecebimentoDAO.getBeans(FinParcelaRecebimento.class, filtros);
    		
    		filtros.clear();
    		filtros.add(new Filtro(Filtro.AND, "contabilLancamentoCabecalho.dataLancamento", Filtro.MAIOR_OU_IGUAL, dataInicial));
    		filtros.add(new Filtro(Filtro.AND, "contabilLancamentoCabecalho.dataLancamento", Filtro.MENOR_OU_IGUAL, dataFinal));
    		contabilLancamentoDetalhe = lancamentoDAO.getBeans(ContabilLancamentoDetalhe.class, filtros);
    		
    	} catch (Exception e) {
    		FacesContextUtil.adicionaMensagem(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro ao carregar os dados!", e.getMessage());
    	}
    }

    public void conciliaLancamentos() {
    	try {
            FinParcelaRecebimento parcela;
            ContabilLancamentoDetalhe lancamento;
            ConciliacaoCliente conciliado;
            conciliacaoCliente = new ArrayList<>();
            for (int i = 0; i < parcelaRecebimento.size(); i++) {
                parcela = parcelaRecebimento.get(i);
                for (int j = 0; j < contabilLancamentoDetalhe.size(); j++) {
                    lancamento = contabilLancamentoDetalhe.get(j);

                    if (parcela.getValorRecebido().compareTo(lancamento.getValor()) == 0) {
                        conciliado = new ConciliacaoCliente();

                        conciliado.setParcelaDataRecebimento(parcela.getDataRecebimento());
                        conciliado.setParcelaValorDesconto(parcela.getValorDesconto());
                        conciliado.setParcelaValorJuro(parcela.getValorJuro());
                        conciliado.setParcelaValorMulta(parcela.getValorMulta());
                        conciliado.setParcelaValorPago(parcela.getValorRecebido());

                        conciliado.setLancamentoContabilConta(lancamento.getContabilConta());
                        conciliado.setLancamentoContabilHistorico(lancamento.getContabilHistorico());
                        conciliado.setLancamentoHistorico(lancamento.getHistorico());
                        conciliado.setLancamentoTipo(lancamento.getTipo());
                        conciliado.setLancamentoValor(lancamento.getValor());

                        conciliacaoCliente.add(conciliado);
                    }
                }
            }
    	} catch (Exception e) {
    		FacesContextUtil.adicionaMensagem(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro ao conciliar os lanšamentos!", e.getMessage());
    	}
    }

	public List<FinParcelaRecebimento> getParcelaRecebimento() {
		return parcelaRecebimento;
	}

	public void setParcelaRecebimento(List<FinParcelaRecebimento> parcelaRecebimento) {
		this.parcelaRecebimento = parcelaRecebimento;
	}

	public List<ContabilLancamentoDetalhe> getContabilLancamentoDetalhe() {
		return contabilLancamentoDetalhe;
	}

	public void setContabilLancamentoDetalhe(List<ContabilLancamentoDetalhe> contabilLancamentoDetalhe) {
		this.contabilLancamentoDetalhe = contabilLancamentoDetalhe;
	}

	public List<ConciliacaoCliente> getConciliacaoCliente() {
		return conciliacaoCliente;
	}

	public void setConciliacaoCliente(List<ConciliacaoCliente> conciliacaoCliente) {
		this.conciliacaoCliente = conciliacaoCliente;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

}
