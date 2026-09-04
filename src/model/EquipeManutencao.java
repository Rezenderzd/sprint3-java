package model;

public class EquipeManutencao {

    private String nomeEquipe;
    private int numeroFuncionarios;
    private String tipoDeRocadaDeAtuacao;
    private Long id;

    public EquipeManutencao(String nomeEquipe, int numeroFuncionarios, String tipoDeRocadaDeAtuacao) {
        this.nomeEquipe = nomeEquipe;
        this.numeroFuncionarios = numeroFuncionarios;
        this.tipoDeRocadaDeAtuacao = tipoDeRocadaDeAtuacao;
    }

    public String getNomeEquipe() {
        return nomeEquipe;
    }

    public int getNumeroFuncionarios() {
        return numeroFuncionarios;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoDeRocadaDeAtuacao() {
        return tipoDeRocadaDeAtuacao;
    }

}
