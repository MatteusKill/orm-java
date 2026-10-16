package ORM;
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

public class Database {

    private String url = "jdbc:mysql://localhost:3306/loja";
    private String usuario = System.getenv("DB_USUARIO");
    private String senha = System.getenv("DB_SENHA");

    private Connection conexao;

    private String tabela;
    private String query;

    private ArrayList<Object> parametros = new ArrayList<>();

    private boolean temWhere = false;

    public Database(String nomeTabela){
        this.tabela = nomeTabela;

        try {
            this.conexao = DriverManager.getConnection(this.url, this.usuario, this.senha);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void conectar(){

    }

    private void reiniciar(){
        this.query = "";
        this.parametros.clear();
        this.temWhere = false;
    }

    public Database selecionar(ArrayList<String> nomeColunas){
        reiniciar();
        this.query = "SELECT ";

        if(nomeColunas.isEmpty()){
            this.query += "*";
        }

        for(int indexAtual = 0; indexAtual < nomeColunas.size(); indexAtual++){
            query += nomeColunas.get(indexAtual);

            if(indexAtual < nomeColunas.size() - 1){
                query+= ", ";
            }
        }
        this.query+= " FROM " + this.tabela;
        return this;
    }

    public Database criar(Map<String, Object> dados){
        reiniciar();
        String colunas = "";
        String interrogacoes = "";

        int indexAtual = 0;
        for(Map.Entry<String, Object> item : dados.entrySet()){
            colunas += item.getKey();
            interrogacoes += "?";
            this.parametros.add(item.getValue());

            if(indexAtual < dados.size() - 1){
                colunas += ", ";
                interrogacoes += ", ";
            }
            indexAtual++;
        }

        this.query = "INSERT INTO " + this.tabela + " (" + colunas + ") VALUES (" + interrogacoes + ")";
        return this;
    }

    public Database atualizar(Map<String, Object> dados){
        reiniciar();
        this.query = "UPDATE " + this.tabela + " SET ";

        int indexAtual = 0;
        for(Map.Entry<String, Object> item : dados.entrySet()){
            this.query += item.getKey() + " = ?";
            this.parametros.add(item.getValue());

            if(indexAtual < dados.size() - 1){
                this.query += ", ";
            }
            indexAtual++;
        }
        return this;
    }

    public Database deletar(){
        reiniciar();
        this.query = "DELETE FROM " + this.tabela;
        return this;
    }

    public Database onde(String coluna, String operador, Object valor){
        if(this.temWhere){
            this.query += " AND ";
        }else{
            this.query += " WHERE ";
            this.temWhere = true;
        }

        this.query += coluna + " " + operador + " ?";
        this.parametros.add(valor);
        return this;
    }

    private PreparedStatement preparar() throws SQLException {
        PreparedStatement comando = this.conexao.prepareStatement(this.query);

        for(int indexAtual = 0; indexAtual < this.parametros.size(); indexAtual++){
            comando.setObject(indexAtual + 1, this.parametros.get(indexAtual));
        }
        return comando;
    }

    public ResultSet executar(){
        try {
            return preparar().executeQuery();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public int executarAlteracao(){
        try (PreparedStatement comando = preparar()) {
            return comando.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public String getQuery(){
        return this.query;
    }
}
