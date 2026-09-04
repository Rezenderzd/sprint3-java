package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConexaoBanco {

    private static final String HOST = "oracle.fiap.com.br";
    private static final String PORT = "1521";
    private static final String SID = "ORCL";
    private static final String USER = "usuario_fiap";
    private static final String PASSWORD = "senha_fiap";

    public static Connection getConexao() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");

            String url = "jdbc:oracle:thin:@" + HOST + ":" + PORT + ":" + SID;

            Connection conexao = DriverManager.getConnection(url, USER, PASSWORD);

            //System.out.println("✅ Conexão criada com sucesso!");
            return conexao;

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver Oracle não encontrado: " + e.getMessage(), e);
        } catch (SQLException e) {
            if (e.getErrorCode() == 1017) {
                String usuario = USER.substring(0, Math.min(3, USER.length())) + "***";
            }
            throw new RuntimeException("Erro ao conectar ao Oracle: " + e.getMessage(), e);
        }
    }

//    public static void fechar(Connection conexao) {
//        try {
//            if (conexao != null && !conexao.isClosed()) {
//                conexao.close();
//                System.out.println("🔌 Conexão fechada.");
//            }
//        } catch (SQLException e) {
//            System.err.println("Erro ao fechar conexão: " + e.getMessage());
//        }
//    }
}