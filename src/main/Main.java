package main;

import dao.EquipeManutencaoDAO;
import dao.IntervencaoOperacionalDAO;
import dao.RelatorioPrioridadeDAO;
import dao.TrechoRodoviaDAO;
import model.EquipeManutencao;
import service.Relatorio;
import model.TrechoRodovia;

import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        AtualizandoTrechos atualizacao = new AtualizandoTrechos();
        AvaliacaoTrechos analise = new AvaliacaoTrechos();

        TrechoRodoviaDAO trechoDAO = new TrechoRodoviaDAO();
        EquipeManutencaoDAO equipeManutencaoDAO = new EquipeManutencaoDAO();
        IntervencaoOperacionalDAO intervencaoDAO = new IntervencaoOperacionalDAO();
        Relatorio relatorio = new Relatorio();
        RelatorioPrioridadeDAO relatorioDAO = new RelatorioPrioridadeDAO();

        atualizacao.atualizandoTrechosBanco(trechoDAO);


        List<TrechoRodovia> trechos = trechoDAO.buscarTodosEInstanciar();
        List<EquipeManutencao> equipes = equipeManutencaoDAO.buscarTodosEInstanciar();

        System.out.println("=======================TRECHOS=====================");
        trechoDAO.listarTodos();

        System.out.println("=======================EQUIPES=====================");
        equipeManutencaoDAO.listarTodos();

        trechos.sort(Comparator.comparing(TrechoRodovia::getNivelVegetacaoEmCm).reversed());

        System.out.println("===================GERANDO INTERVENÇÕES=================");
        analise.exibindoTrechos(trechos, equipes, intervencaoDAO, trechoDAO);

        System.out.println("=====================INTERVENCOES==================");
        intervencaoDAO.listarTodos();


//        System.out.println("=====================RELATORIO==================");
        relatorio.exibirRelatorio(relatorioDAO);
//        intervencaoDAO.deletarTodosEResetarId();
//        trechoDAO.deletarTodosEResetarId();
//        equipeManutencaoDAO.deletarTodosEResetarId();

        System.out.println("================GUARDANDO RELATORIO=============");
        relatorio.salvarRelatorio(relatorioDAO);

        System.out.println("=======CODIGO FINALIZADO========");

    }
}