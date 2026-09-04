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
}