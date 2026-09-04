package model;

public abstract class IntervencaoOperacional {
    protected Long id;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public abstract String executarServico(TrechoRodovia trecho, EquipeManutencao equipe);
}
