package dao;
import db.ConexaoBanco;
import model.*;

import java.sql.*;
import model.IntervencaoOperacional;
import java.sql.*;

public class IntervencaoOperacionalDAO {

    public boolean criarTabela() {
        String sqlTabela = "CREATE TABLE intervencoesOperacionais (" +
                "id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
                "nome VARCHAR2(100) NOT NULL, " +
                "quilometroInicial NUMBER NOT NULL, " +
                "quilometroFinal NUMBER NOT NULL, " +
                "tipoClima VARCHAR2(100) NOT NULL, " +
                "nomeEquipe VARCHAR2(100) NOT NULL" +
                ")";

        Connection conn = null;
        Statement stmt = null;

        try {
            conn = ConexaoBanco.getConexao();
            stmt = conn.createStatement();
            stmt.execute(sqlTabela);
            System.out.println("📋 Tabela 'intervencoesOperacionais' criada com sucesso.");
            return false;
        } catch (SQLException e) {
            if (e.getErrorCode() == 955) {
                System.out.println("📋 Tabela 'intervencoesOperacionais' já existe.");
                return true;
            } else {
                System.out.println("⚠️ Falha ao tentar criar tabela: " + e.getMessage());
                return false;
            }
        } finally {
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { }
            if (conn != null) try { conn.close(); } catch (SQLException e) { }
        }
    }


    public void inserir(IntervencaoOperacional intervencao, TrechoRodovia trecho, EquipeManutencao equipe) {
        String sql = "INSERT INTO intervencoesOperacionais (nome, quilometroInicial, quilometroFinal, tipoClima, nomeEquipe) VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConexaoBanco.getConexao();
            pstmt = conn.prepareStatement(sql, new String[]{"ID"});

            pstmt.setString(1, trecho.getNomeTrecho());
            pstmt.setInt(2, trecho.getQuilometroInicial());
            pstmt.setInt(3, trecho.getQuilometroFinal());
            pstmt.setString(4, trecho.getTipoClima());
            pstmt.setString(5, equipe.getNomeEquipe());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {

                    intervencao.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir intervenção: " + e.getMessage(), e);
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { }
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { }
            try { if (conn != null) conn.close(); } catch (SQLException e) { }
        }
    }

    public IntervencaoOperacional buscarPorId(Long id) {
        String sql = "SELECT * FROM intervencoesOperacionais WHERE id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConexaoBanco.getConexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                String itemEncontrado = intervencaoEncontrada(rs);
                System.out.println(itemEncontrado);
            } else {
                System.out.printf("Nenhuma intervenção encontrada para o ID: %d\n", id);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar intervenção: " + e.getMessage(), e);
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { }
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { }
            try { if (conn != null) conn.close(); } catch (SQLException e) { }
        }
    }

    public void listarTodos() {
        String sql = "SELECT * FROM intervencoesOperacionais ORDER BY id";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConexaoBanco.getConexao();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String intervencao = intervencaoEncontrada(rs);
                System.out.println(intervencao);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar intervenções: " + e.getMessage(), e);
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { }
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { }
            try { if (conn != null) conn.close(); } catch (SQLException e) { }
        }
    }

//    public void atualizar(IntervencaoOperacional intervencao) {
//        String sql = "UPDATE intervencoesOperacionais SET nome = ?, quilometroInicial = ?, quilometroFinal = ?, tipoClima = ?, nomeEquipe = ? WHERE id = ?";
//
//        Connection conn = null;
//        PreparedStatement pstmt = null;
//
//        try {
//            conn = ConexaoBanco.getConexao();
//            conn.setAutoCommit(false);
//            pstmt = conn.prepareStatement(sql);
//
//            pstmt.setString(1, intervencao.getNome());
//            pstmt.setInt(2, intervencao.getQuilometroInicial());
//            pstmt.setInt(3, intervencao.getQuilometroFinal());
//            pstmt.setString(4, intervencao.getTipoClima());
//            pstmt.setString(5, intervencao.getNomeEquipe());
//            pstmt.setLong(6, intervencao.getId());
//
//            int rowsAffected = pstmt.executeUpdate();
//
//            conn.commit();
//
//            if (rowsAffected > 0) {
//                System.out.printf("✅ Dados atualizados para a intervenção %s\n", intervencao.getNome());
//            } else {
//                System.out.println("⚠️ Intervenção não encontrada.");
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException("Erro ao atualizar intervenção: " + e.getMessage(), e);
//        } finally {
//            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { }
//            try { if (conn != null) conn.close(); } catch (SQLException e) { }
//        }
//    }

    public void deletar(Long id) {
        String sql = "DELETE FROM intervencoesOperacionais WHERE id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = ConexaoBanco.getConexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Intervenção deletada (ID: " + id + ")");
            } else {
                System.out.println("⚠️ Intervenção não encontrada.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar intervenção: " + e.getMessage(), e);
        } finally {
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { }
            try { if (conn != null) conn.close(); } catch (SQLException e) { }
        }
    }

    public void deletarTodosEResetarId() {
        String sql = "DROP TABLE intervencoesOperacionais";

        Connection conn = null;
        Statement stmt = null;

        try {
            conn = ConexaoBanco.getConexao();
            stmt = conn.createStatement();

            stmt.execute(sql);
            System.out.println("🗑️ Tabela 'intervencoesOperacionais' limpa e IDs reiniciados.");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao limpar a tabela intervencoesOperacionais: " + e.getMessage(), e);
        } finally {
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { }
            if (conn != null) try { conn.close(); } catch (SQLException e) { }
        }
    }

//    public List<IntervencaoOperacional> buscarTodosEInstanciar() {
//        String sql = "SELECT * FROM intervencoesOperacionais";
//        List<IntervencaoOperacional> lista = new ArrayList<>();
//
//        try (Connection conn = ConexaoBanco.getConexao();
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(sql)) {
//
//            while (rs.next()) {
//                Long id = rs.getLong("id");
//                String nome = rs.getString("nome");
//                int kmInicial = rs.getInt("quilometroInicial");
//                int kmFinal = rs.getInt("quilometroFinal");
//                String tipoClima = rs.getString("tipoClima");
//                String nomeEquipe = rs.getString("nomeEquipe");
//
//                IntervencaoOperacional intervencao;
//
//                if ("umido".equalsIgnoreCase(tipoClima)) {
//                    intervencao = new RocadaMecanizada();
//                } else {
//                    intervencao = new Pulverizacao();
//                }
//
//                // Seta o ID na instância criada
//                intervencao.setId(id);
//                lista.add(intervencao);
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException("Erro ao carregar intervenções do banco: " + e.getMessage(), e);
//        }
//
//        return lista;
//    }

    public String intervencaoEncontrada(ResultSet rs) throws SQLException {
        return String.format("""
                ID: %d,
                Nome: %s,
                Km inicial: %d,
                Km final: %d,
                Tipo clima: %s,
                Nome da Equipe: %s
                """,
                rs.getLong("id"),
                rs.getString("nome"),
                rs.getInt("quilometroInicial"),
                rs.getInt("quilometroFinal"),
                rs.getString("tipoClima"),
                rs.getString("nomeEquipe")
        );
    }
}