package dao;

import db.ConexaoBanco;
import model.TrechoComSensor;
import model.TrechoRodovia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrechoRodoviaDAO {

    public void inserir(TrechoRodovia trecho) {
        String sql = "INSERT INTO trechos (nome, quilometroInicial, quilometroFinal, nivelVegetacaoEmCm, tipoClima, trechoComSenor) VALUES (?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConexaoBanco.getConexao();
            pstmt = conn.prepareStatement(sql, new String[]{"ID"});

            pstmt.setString(1, trecho.getNomeTrecho());
            pstmt.setInt(2, trecho.getQuilometroInicial());
            pstmt.setInt(3, trecho.getQuilometroFinal());
            pstmt.setDouble(4, trecho.getNivelVegetacaoEmCm());
            pstmt.setString(5, trecho.getTipoClima());

            // 1 para verdadeiro (com sensor) e 0 para falso
            if (trecho instanceof TrechoComSensor) {
                pstmt.setInt(6, 1);
            } else {
                pstmt.setInt(6, 0);
            }

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    trecho.setId(rs.getLong(1));
                }
                System.out.println("✅ Trecho adicionado: " + trecho.getNomeTrecho() + " (ID: " + trecho.getId() + ")");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir trecho: " + e.getMessage(), e);
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { }
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { }
            try { if (conn != null) conn.close(); } catch (SQLException e) { }
        }
    }

    public TrechoRodovia buscarPorId(Long id) {
        String sql = "SELECT * FROM trechos WHERE id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConexaoBanco.getConexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                String itemEncontrado = trechoEncontrado(rs);
                System.out.println(itemEncontrado);
            } else {
                System.out.printf("Nenhum trecho encontrado para o ID: %d\n", id);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar trecho: " + e.getMessage(), e);
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { }
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { }
            try { if (conn != null) conn.close(); } catch (SQLException e) { }
        }
    }

    public void listarTodos() {
        String sql = "SELECT * FROM trechos ORDER BY id";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConexaoBanco.getConexao();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String trecho = trechoEncontrado(rs);
                System.out.println(trecho);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar trechos: " + e.getMessage(), e);
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { }
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { }
            try { if (conn != null) conn.close(); } catch (SQLException e) { }
        }
    }

    public void atualizar(TrechoRodovia trechoRodovia) {
        String sql = "UPDATE trechos SET nivelVegetacaoEmCm = ? WHERE id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = ConexaoBanco.getConexao();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);

            pstmt.setDouble(1, trechoRodovia.getNivelVegetacaoEmCm());
            pstmt.setLong(2, trechoRodovia.getId());

            int rowsAffected = pstmt.executeUpdate();

            conn.commit();

            if (rowsAffected > 0) {
                System.out.printf("✅ Nível da vegetação atualizado no trecho %s\n\n", trechoRodovia.getNomeTrecho());
            } else {
                System.out.println("⚠️ Trecho não encontrado.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar nível da vegetação: " + e.getMessage(), e);
        } finally {
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { }
            try { if (conn != null) conn.close(); } catch (SQLException e) { }
        }
    }

    public void deletar(Long id) {
        String sql = "DELETE FROM trechos WHERE id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = ConexaoBanco.getConexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Trecho deletado (ID: " + id + ")");
            } else {
                System.out.println("⚠️ Trecho não encontrado.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar trecho: " + e.getMessage(), e);
        } finally {
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { }
            try { if (conn != null) conn.close(); } catch (SQLException e) { }
        }
    }

    public void deletarTodosEResetarId() {
        String sql = "DROP TABLE trechos";

        Connection conn = null;
        Statement stmt = null;

        try {
            conn = ConexaoBanco.getConexao();
            stmt = conn.createStatement();

            stmt.execute(sql);
            System.out.println("🗑️ Tabela de trechos excluida");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao limpar a tabela trechos: " + e.getMessage(), e);
        } finally {
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { }
            if (conn != null) try { conn.close(); } catch (SQLException e) { }
        }
    }

    public List<TrechoRodovia> buscarTodosEInstanciar() {
        String sql = "SELECT * FROM trechos";
        List<TrechoRodovia> lista = new ArrayList<>();

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConexaoBanco.getConexao();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Long id = rs.getLong("id");
                String nome = rs.getString("nome");
                int kmInicial = rs.getInt("quilometroInicial");
                int kmFinal = rs.getInt("quilometroFinal");
                double nivelVeg = rs.getDouble("nivelVegetacaoEmCm");
                String tipoClima = rs.getString("tipoClima");
                boolean temSensor = rs.getInt("trechoComSenor") == 1;

                TrechoRodovia trecho;
                if (temSensor) {
                    trecho = new TrechoComSensor(nome, kmInicial, kmFinal, nivelVeg, tipoClima);
                } else {
                    trecho = new TrechoRodovia(nome, kmInicial, kmFinal, nivelVeg, tipoClima);
                }
                trecho.setId(id);
                lista.add(trecho);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao carregar trechos do banco: " + e.getMessage(), e);
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { }
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { }
            try { if (conn != null) conn.close(); } catch (SQLException e) { }
        }

        return lista;
    }

    public String trechoEncontrado(ResultSet rs) throws SQLException {
        return String.format("""
                Nome: %s,
                Km inicial: %d,
                Km final: %d,
                Nível vegetação: %.2f,
                Tipo clima: %s,
                Possui Sensor: %s
                """,
                rs.getString("nome"),
                rs.getInt("quilometroInicial"),
                rs.getInt("quilometroFinal"),
                rs.getDouble("nivelVegetacaoEmCm"),
                rs.getString("tipoClima"),
                rs.getInt("trechoComSenor") == 1 ? "Sim" : "Não"
        );
    }
}