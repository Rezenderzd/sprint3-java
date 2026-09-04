package dao;

import db.ConexaoBanco;
import model.EquipeManutencao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipeManutencaoDAO {

    public void inserir(EquipeManutencao equipe) {
        String sql = "INSERT INTO equipesManutencao (nomeEquipe, quantidadeFuncionarios, rocadaDeAtuacao) VALUES (?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConexaoBanco.getConexao();
            pstmt = conn.prepareStatement(sql, new String[]{"ID"});

            pstmt.setString(1, equipe.getNomeEquipe());
            pstmt.setInt(2, equipe.getNumeroFuncionarios());
            pstmt.setString(3, equipe.getTipoDeRocadaDeAtuacao());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    equipe.setId(rs.getLong(1));
                }
                System.out.println("✅ Equipe adicionada: " + equipe.getNomeEquipe() + " (ID: " + equipe.getId() + ")");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir equipe: " + e.getMessage(), e);
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { }
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { }
            try { if (conn != null) conn.close(); } catch (SQLException e) { }
        }
    }

    public EquipeManutencao buscarPorId(Long id) {
        String sql = "SELECT * FROM equipesManutencao WHERE id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConexaoBanco.getConexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                String itemEncontrado = equipeEncontrada(rs);
                System.out.println(itemEncontrado);
            } else {
                System.out.printf("Nenhuma equipe encontrada para o ID: %d\n", id);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar equipe: " + e.getMessage(), e);
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { }
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { }
            try { if (conn != null) conn.close(); } catch (SQLException e) { }
        }
    }

    public void listarTodos() {
        String sql = "SELECT * FROM equipesManutencao ORDER BY id";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConexaoBanco.getConexao();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String equipe = equipeEncontrada(rs);
                System.out.println(equipe);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar equipes: " + e.getMessage(), e);
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { }
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { }
            try { if (conn != null) conn.close(); } catch (SQLException e) { }
        }
    }

    public void atualizar(EquipeManutencao equipe) {
        String sql = "UPDATE equipesManutencao SET nomeEquipe = ?, quantidadeFuncionarios = ?, rocadaDeAtuacao = ? WHERE id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = ConexaoBanco.getConexao();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, equipe.getNomeEquipe());
            pstmt.setInt(2, equipe.getNumeroFuncionarios());
            pstmt.setString(3, equipe.getTipoDeRocadaDeAtuacao());
            pstmt.setLong(4, equipe.getId());

            int rowsAffected = pstmt.executeUpdate();

            conn.commit();

            if (rowsAffected > 0) {
                System.out.printf("✅ Dados atualizados para a equipe %s\n", equipe.getNomeEquipe());
            } else {
                System.out.println("⚠️ Equipe não encontrada.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar equipe: " + e.getMessage(), e);
        } finally {
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { }
            try { if (conn != null) conn.close(); } catch (SQLException e) { }
        }
    }

    public void deletar(Long id) {
        String sql = "DELETE FROM equipesManutencao WHERE id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = ConexaoBanco.getConexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Equipe deletada (ID: " + id + ")");
            } else {
                System.out.println("⚠️ Equipe não encontrada.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar equipe: " + e.getMessage(), e);
        } finally {
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { }
            try { if (conn != null) conn.close(); } catch (SQLException e) { }
        }
    }

    public void deletarTodosEResetarId() {
        String sql = "DROP TABLE equipesManutencao";

        Connection conn = null;
        Statement stmt = null;

        try {
            conn = ConexaoBanco.getConexao();
            stmt = conn.createStatement();

            stmt.execute(sql);
            System.out.println("🗑️ Tabela 'equipesManutencao' limpa e IDs reiniciados.");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao limpar a tabela equipesManutencao: " + e.getMessage(), e);
        } finally {
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { }
            if (conn != null) try { conn.close(); } catch (SQLException e) { }
        }
    }

    public List<EquipeManutencao> buscarTodosEInstanciar() {
        String sql = "SELECT * FROM equipesManutencao";
        List<EquipeManutencao> lista = new ArrayList<>();

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConexaoBanco.getConexao();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Long id = rs.getLong("id");
                String nome = rs.getString("nomeEquipe");
                int qtdFuncionarios = rs.getInt("quantidadeFuncionarios");
                String rocada = rs.getString("rocadaDeAtuacao");

                EquipeManutencao equipe = new EquipeManutencao(nome, qtdFuncionarios, rocada);
                equipe.setId(id);
                lista.add(equipe);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao carregar equipes do banco: " + e.getMessage(), e);
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { }
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { }
            try { if (conn != null) conn.close(); } catch (SQLException e) { }
        }

        return lista;
    }

    public String equipeEncontrada(ResultSet rs) throws SQLException {
        return String.format("""
                Nome da Equipe: %s,
                Quantidade de Funcionários: %d,
                Roçada de Atuação: %s
                """,
                rs.getString("nomeEquipe"),
                rs.getInt("quantidadeFuncionarios"),
                rs.getString("rocadaDeAtuacao")
        );
    }
}