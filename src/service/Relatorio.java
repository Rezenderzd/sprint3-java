package service;

import dao.RelatorioPrioridadeDAO;
import model.EquipesRanking;
import model.TrechoRanking;

import java.util.List;
import java.util.Map;

public class Relatorio {

    public void exibirRelatorio(RelatorioPrioridadeDAO dao){

        // Armazenando os retornos de cada função em variáveis separadas
        int totalEquipes = dao.contarEquipes();
        int totalTrechos = dao.contarTrechos();

        Map<String, Integer> contagemSensores = dao.contarTrechosComESemSensor();
        int trechosComSensor = contagemSensores.getOrDefault("comSensor", 0);
        int trechosSemSensor = contagemSensores.getOrDefault("semSensor", 0);

        // Exibição no console
        System.out.println("=== RELATÓRIO DE DADOS ===");
        System.out.println("Quantidade equipes de manutenção: " + totalEquipes);
        System.out.println("Quantidade de trechos: " + totalTrechos);
        System.out.println("Trechos com sensor: " + trechosComSensor);
        System.out.println("Trechos sem sensor: " + trechosSemSensor);

        System.out.println("\n=== RANKING DE EQUIPES POR INTERVENÇÃO ===");
        List<EquipesRanking> rankingEquipes = dao.obterRankingEquipes();

        int posicao = 1;
        for (EquipesRanking equipe : rankingEquipes) {
            System.out.println(posicao + "º " + equipe.nomeEquipe() + " - " + equipe.totalIntervencoes() + " intervenções");
            posicao++;
            }
        System.out.println("\n=== RANKING DE TRECHOS POR INTERVENÇÃO ===");
        List<TrechoRanking> rankingTrechos = dao.obterRankingTrechosComMaisIntervencoes();
        posicao = 1;

        for (TrechoRanking trecho : rankingTrechos) {
            System.out.println(posicao + "º " + trecho.nomeTrecho() +
                    " (KM " + trecho.kmInicial() + " até KM " + trecho.kmFinal() + ")" +
                    " - " + trecho.totalIntervencoes() + " aparições");
            posicao++;
        }
    }
}
