package main;

import dao.EquipeManutencaoDAO;
import dao.IntervencaoOperacionalDAO;
import dao.RelatorioPrioridadeDAO;
import dao.TrechoRodoviaDAO;
import model.EquipeManutencao;
import model.TrechoComSensor;
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

        boolean tabelaCriadaTrecho = trechoDAO.criarTabela();
        boolean tabelaCriadaEquipe = equipeManutencaoDAO.criarTabela();
        intervencaoDAO.criarTabela();

        if (!tabelaCriadaTrecho) {
            trechoDAO.inserir(new TrechoRodovia("Br", 10, 15, 20.0, "umido"));
            trechoDAO.inserir(new TrechoRodovia("Rodo Anel", 20, 30, 19.0, "seco"));
            trechoDAO.inserir(new TrechoComSensor("Motiva Sorocabana", 15, 20, 18.0, "umido"));
            trechoDAO.inserir(new TrechoComSensor("Rodovia Presidente Dutra", 30, 40, 22.0, "seco"));
        }

        if (!tabelaCriadaEquipe) {
            equipeManutencaoDAO.inserir(new EquipeManutencao("Equipe Alpha", 5, "manual"));
            equipeManutencaoDAO.inserir(new EquipeManutencao("Equipe Teta", 7, "mecanizada"));
            equipeManutencaoDAO.inserir(new EquipeManutencao("Equipe Gama", 8, "manual"));
            equipeManutencaoDAO.inserir(new EquipeManutencao("Equipe Beta", 7, "mecanizada"));
        }

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

        System.out.println("=======CODIGO FINALIZADO========");

    }
}