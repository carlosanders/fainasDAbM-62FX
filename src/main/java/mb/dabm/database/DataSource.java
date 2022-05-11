package mb.dabm.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource implements IDataSource
{
    private String url;
    private String driver;
    private String usuario;
    private String senha;

    /**
     * Construtor com parametros para a conexao
     * 
     * @param url
     * @param driver
     * @param usuario
     * @param senha
     */
    public DataSource(String url, String driver, String usuario, String senha)
    {
	super();
	this.url = url;
	this.driver = driver;
	this.usuario = usuario;
	this.senha = senha;
	try {
	    Class.forName(this.driver);
	} catch (ClassNotFoundException ex) {
	    System.out.println("Debug: Classe nao encontrada");
	    //Logger.getLogger(DataSource.class.getName()).log(Level.SEVERE, null, ex);
	}

    }

    /**
     * Abre-se a conexão com o Banco de Dados e retorna uma conexao
     */
    public Connection getConnection() throws SQLException
    {
	return DriverManager.getConnection(url, usuario, senha);
    }

}
