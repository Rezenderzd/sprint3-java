package dao;

import db.ConexaoBanco;
import model.EquipesRanking;
import model.TrechoRanking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RelatorioPrioridadeDAO {

    public int contarEquipes() {
        String sql = "SELECT COUNT(*) FROM equipesManutencao";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConexaoBanco.getConexao();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao contar equipes: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { }
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { }
            try { if (conn != null) conn.close(); } catch (SQLException e) { }
        }
        return 0;
    }

    public int contarTrechos() {
        String sql = "SELECT COUNT(*) FROM trechos";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConexaoBanco.getConexao();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao contar trechos: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { }
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { }
            try { if (conn != null) conn.close(); } catch (SQLException e) { }
        }
        return 0;
    }

    public Map<String, Integer> contarTrechosComESemSensor() {
        String sql = "SELECT " +
                "  COUNT(CASE WHEN trechoComSenor = 1 THEN 1 END) AS com_sensor, " +
                "  COUNT(CASE WHEN trechoComSenor = 0 THEN 1 END) AS sem_sensor " +
                "FROM trechos";

        Map<String, Integer> resultado = new LinkedHashMap<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConexaoBanco.getConexao();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            if (rs.next()) {
                resultado.put("comSensor", rs.getInt("com_sensor"));
                resultado.put("semSensor", rs.getInt("sem_sensor"));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao contar sensores em trechos: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { }
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { }
            try { if (conn != null) conn.close(); } catch (SQLException e) { }
        }
        return resultado;
    }

    public List<EquipesRanking> obterRankingEquipes() {
        String sql = "SELECT nomeEquipe, COUNT(*) AS total " +
                "FROM intervencoesOperacionais " +
                "GROUP BY nomeEquipe " +
                "ORDER BY total DESC";

        List<EquipesRanking> ranking = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConexaoBanco.getConexao();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                ranking.add(new EquipesRanking(
                        rs.getString("nomeEquipe"),
                        rs.getInt("total")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao gerar ranking de equipes: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { }
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { }
            try { if (conn != null) conn.close(); } catch (SQLException e) { }
        }
        return ranking;
    }

    public List<TrechoRanking> obterRankingTrechosComMaisIntervencoes() {
        String sql = "SELECT nome, quilometroInicial, quilometroFinal, COUNT(*) AS total " +
                "FROM intervencoesOperacionais " +
                "GROUP BY nome, quilometroInicial, quilometroFinal " +
                "ORDER BY total DESC";

        List<TrechoRanking> ranking = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConexaoBanco.getConexao();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                ranking.add(new TrechoRanking(
                        rs.getString("nome"),
                        rs.getDouble("quilometroInicial"),
                        rs.getDouble("quilometroFinal"),
                        rs.getInt("total")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao gerar ranking de trechos: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { }
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { }
            try { if (conn != null) conn.close(); } catch (SQLException e) { }
        }
        return ranking;
    }

    public void salvarRelatorio(
            int totalEquipes,
            int totalTrechos,
            int trechosComSensor,
            int trechosSemSensor,
            List<EquipesRanking> rankingEquipes,
            List<TrechoRanking> rankingTrechos) {

        String sqlRelatorio =
                "INSERT INTO relatoriosPrioridade " +
                        "(totalEquipes, totalTrechos, trechosComSensor, trechosSemSensor) " +
                        "VALUES (?, ?, ?, ?)";

        String sqlEquipe =
                "INSERT INTO rankingEquipes " +
                        "(relatorioId, nomeEquipe, totalIntervencoes) " +
                        "VALUES (?, ?, ?)";

        String sqlTrecho =
                "INSERT INTO rankingTrechos " +
                        "(relatorioId, nome, quilometroInicial, quilometroFinal, totalIntervencoes) " +
                        "VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement stmtRelatorio = null;
        PreparedStatement stmtEquipe = null;
        PreparedStatement stmtTrecho = null;
        ResultSet rs = null;

        try {

            conn = ConexaoBanco.getConexao();
            conn.setAutoCommit(false);


            stmtRelatorio = conn.prepareStatement(
                    sqlRelatorio,
                    new String[]{"ID"}
            );

            stmtRelatorio.setInt(1, totalEquipes);
            stmtRelatorio.setInt(2, totalTrechos);
            stmtRelatorio.setInt(3, trechosComSensor);
            stmtRelatorio.setInt(4, trechosSemSensor);

            stmtRelatorio.executeUpdate();


            rs = stmtRelatorio.getGeneratedKeys();

            int relatorioId = 0;

            if (rs.next()) {
                relatorioId = rs.getInt(1);
            }

            stmtEquipe = conn.prepareStatement(sqlEquipe);

            for (EquipesRanking equipe : rankingEquipes) {

                stmtEquipe.setInt(1, relatorioId);
                stmtEquipe.setString(2, equipe.nomeEquipe());
                stmtEquipe.setInt(3, equipe.totalIntervencoes());

                stmtEquipe.executeUpdate();
            }


            stmtTrecho = conn.prepareStatement(sqlTrecho);

            for (TrechoRanking trecho : rankingTrechos) {

                stmtTrecho.setInt(1, relatorioId);
                stmtTrecho.setString(2, trecho.nomeTrecho());
                stmtTrecho.setDouble(3, trecho.kmInicial());
                stmtTrecho.setDouble(4, trecho.kmFinal());
                stmtTrecho.setInt(5, trecho.totalIntervencoes());

                stmtTrecho.executeUpdate();
            }


            conn.commit();

            System.out.println("Relatório salvo com sucesso!");


        } catch (SQLException e) {

            System.err.println(
                    "Erro ao salvar relatório: "
                            + e.getMessage()
            );
            try {

                if (conn != null) {
                    conn.rollback();
                }

            } catch (SQLException ex) {

                System.err.println(
                        "Erro ao desfazer relatório: "
                                + ex.getMessage()
                );
            }


        } finally {

            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar ResultSet: " + e.getMessage());
            }

            try {
                if (stmtRelatorio != null) stmtRelatorio.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar statement do relatório: " + e.getMessage());
            }

            try {
                if (stmtEquipe != null) stmtEquipe.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar statement das equipes: " + e.getMessage());
            }

            try {
                if (stmtTrecho != null) stmtTrecho.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar statement dos trechos: " + e.getMessage());
            }

            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão com o banco: " + e.getMessage());
            }
        }
    }
}