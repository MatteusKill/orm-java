import ORM.Database;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main{
    public static void main(String[] args){
        Database db = new Database("produto");
        ArrayList<String> nomeColunas = new ArrayList<>();

        nomeColunas.add("id");
        nomeColunas.add("nome");
        nomeColunas.add("valor");

        try (ResultSet resultado = db.selecionar(nomeColunas).executar()) {
            while (resultado.next()){
                System.out.println("ID:" + resultado.getInt("id") + " - Nome: " + resultado.getString("nome"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }






//        try{
//            Connection conexao = DriverManager.getConnection(url, usuario, senha);
//            Statement statement = conexao.createStatement();
//            ResultSet resultado = statement.executeQuery("SELECT id, nome FROM produto");
//
//
//            conexao.close();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
    }
}