package model;

public class RocadaMecanizada extends IntervencaoOperacional {
    @Override
    public String executarServico(TrechoRodovia trecho, EquipeManutencao equipe) {
        String frase = String.format("O km %d ao km %d do trecho %s precisa de serviço de tratores, a equipe %s foi destinada para essa tarefa. A grama está com %.2f cm", trecho.getQuilometroInicial(), trecho.getQuilometroFinal(), trecho.getNomeTrecho(), equipe.getNomeEquipe(), trecho.getNivelVegetacaoEmCm());
        return frase;
    }
}