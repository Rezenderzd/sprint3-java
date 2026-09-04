package main;

import dao.IntervencaoOperacionalDAO;
import dao.TrechoRodoviaDAO;
import model.*;

import java.util.List;
import java.util.Random;

public class AvaliacaoTrechos {
    static final double LIMITE_VEGETACAO_CM = 30.0;
    public void exibindoTrechos(List<TrechoRodovia> trechos, List<EquipeManutencao> equipes, IntervencaoOperacionalDAO intervencaoDao, TrechoRodoviaDAO trechoDAO){
        boolean acionou = false;
        for(TrechoRodovia trecho: trechos){
            //System.out.printf("\nTrecho: %s, Km inicial: %d, Km final: %d\n", trecho.getNomeTrecho(), trecho.getQuilometroInicial(), trecho.getQuilometroFinal(), trecho.getNivelVegetacaoEmCm());
            if(trecho.getNivelVegetacaoEmCm()>=LIMITE_VEGETACAO_CM){
                avaliandoEquipeIdeal(trecho, equipes, intervencaoDao, trechoDAO);
                acionou = true;
            }
        }
        if(!acionou){
            System.out.println("Não houve a necessidade de nenhuma intervenção.");
        }
    }

    public IntervencaoOperacional avaliandoEquipeIdeal(TrechoRodovia trecho, List<EquipeManutencao> equipes, IntervencaoOperacionalDAO dao, TrechoRodoviaDAO trechoDAO) {
        Random random = new Random();
        String tipoDeRocada;
        EquipeManutencao equipeSelecionada;
        IntervencaoOperacional intervencao = null;

        if (trecho.getTipoClima().equalsIgnoreCase("umido")) {
            do {
                int equipeSorteada = random.nextInt(equipes.size());
                tipoDeRocada = equipes.get(equipeSorteada).getTipoDeRocadaDeAtuacao();
                equipeSelecionada = equipes.get(equipeSorteada);
            } while (!tipoDeRocada.equalsIgnoreCase("mecanizada"));

            // Instancia a classe concreta específica
            intervencao = new RocadaMecanizada();

            System.out.printf("%s\n", intervencao.executarServico(trecho, equipeSelecionada));

            // Salva no banco e preenche o ID no objeto 'intervencao'
            dao.inserir(intervencao, trecho, equipeSelecionada);
            trecho.realizarCorteVegetacao();
            trechoDAO.atualizar(trecho);
            return intervencao;
        }

        if (trecho.getTipoClima().equalsIgnoreCase("seco")) {
            do {
                int equipeSorteada = random.nextInt(equipes.size());
                tipoDeRocada = equipes.get(equipeSorteada).getTipoDeRocadaDeAtuacao();
                equipeSelecionada = equipes.get(equipeSorteada);
            } while (!tipoDeRocada.equalsIgnoreCase("manual"));

            // Instancia a classe concreta específica
            intervencao = new Pulverizacao();

            System.out.printf("%s\n", intervencao.executarServico(trecho, equipeSelecionada));

            // Salva no banco e preenche o ID no objeto 'intervencao'
            dao.inserir(intervencao, trecho, equipeSelecionada);
            trecho.realizarCorteVegetacao();
            trechoDAO.atualizar(trecho);
            return intervencao;
        }

        System.out.println("Tipo de clima inválido, não tem como saber qual serviço utilizar.");
        return null;
    }
}
