package model;

public class TrechoComSensor extends TrechoRodovia implements MonitoravelViaIoT{

    private ValidacoesTrechoRodovia validacao = new ValidacoesTrechoRodovia();
    static final double CRESCIMENTO_UMIDO_CM = 8.0;
    static final double CRESCIMENTO_SECO_CM = 4.0;

    public TrechoComSensor(String nomeTrecho, int quilometroInicial, int quilometroFinal, double nivelVegetacaoEmCm, String tipoClima){
        super(nomeTrecho, quilometroInicial, quilometroFinal, nivelVegetacaoEmCm, tipoClima);
    }

    @Override
    public void simularCrescimento(){
        if(getTipoClima() == null){
            System.out.println("Não é possível simular o crescimento de um trecho inexistente.");
            return;
        }
        if (getTipoClima().equalsIgnoreCase("umido")){
            setNivelVegetacaoEmCm(getNivelVegetacaoEmCm()+CRESCIMENTO_UMIDO_CM);
        }else if(getTipoClima().equalsIgnoreCase("seco")){
            setNivelVegetacaoEmCm(getNivelVegetacaoEmCm()+CRESCIMENTO_SECO_CM);
        }else{
            System.out.println("Não foi possível simular pois o tipo de clima não está na base de dados, ele só pode ser úmido ou seco!");
        }
    }

    @Override
    public void transmitirDadosSensor() {
        System.out.printf("Puxando dados do dispositivo iot do trecho %s", getNomeTrecho());
        simularCrescimento();
    }
}
