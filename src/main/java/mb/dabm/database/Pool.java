package mb.dabm.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Padrao de projeto - Singleton
 * 
 * @author Carlos
 */
public class Pool implements IPool
{

    private IDataSource ds;
    private ArrayBlockingQueue<Connection> conexoesLivres;
    private HashMap<String, Connection> conexoesUtilizadas;
    private Integer numeroMaximoConexoes;
    // private ResourceBundle config;
    private static Pool instanciaSingleton = null;
    // public static String database = null;

    private static final Logger LOGGER = LogManager.getLogger(Pool.class.getName());

    // Construtor privativo - assim nenhum objeto instancia
    private Pool()
    {

	Properties defaultProps = new Properties();
	FileInputStream in;
	try {
	    in = new FileInputStream("./bd.properties");

	    defaultProps.load(in);

	    in.close();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    // e.printStackTrace();
	    LOGGER.error("[IOException] " + e.getMessage());
	}

	// localizo qual o pacote e o nome do arquivo de configuracao de conexao
	// config = ResourceBundle.getBundle("mb.dabm.database.config.conexaobd");
	// config = ResourceBundle.getBundle("mb.dabm.database.bd_catalog");

	ds = new DataSource(defaultProps.getProperty("prod.bd.url"), defaultProps.getProperty("prod.bd.driver"),
		defaultProps.getProperty("prod.bd.usuario"), defaultProps.getProperty("prod.bd.senha"));
	numeroMaximoConexoes = Integer.parseInt(defaultProps.getProperty("prod.bd.numeroMaximoConexoes"));

	conexoesLivres = new ArrayBlockingQueue<Connection>(numeroMaximoConexoes, true);
	conexoesUtilizadas = new HashMap<String, Connection>();

    }

    /**
     * Ponto de acesso simples estatico e global para os objetos que usaro a conexao
     * com o banco
     * 
     * @return uma unica instancia de Pool;
     */
    public static synchronized Pool getInstacia()
    {
	if (instanciaSingleton == null) {
	    instanciaSingleton = new Pool();
	    // System.out.println("nova instancia!");
	}

//	} else {
//	    System.out.println("mesma instancia!");
//	}
	return instanciaSingleton;

    }

    public Connection getConnection()
    {
	Connection con = null;

	try {
	    // vrf se o conexoesUtilizadas sao menores que numeroMaximoConexao
	    if (conexoesUtilizadas.size() < numeroMaximoConexoes) {
		con = conexoesLivres.poll();
		if (con == null) {
		    con = ds.getConnection();
		} else if (con.isClosed()) {
		    this.getConnection();
		}
		/*
		 * uma vez entregue a conexao adcionamos ela ao nosso hashMap de
		 * conexoesUtilizadas identificando pela con.toString() que irá imprimir a
		 * conexao e armazenar em con
		 */
		conexoesUtilizadas.put(con.toString(), con);
	    }
	} catch (SQLException e) {
	    // System.out.println("Problemas com o pool!");
	    // e.printStackTrace();
	    LOGGER.error("[SQLException - Pool] " + e.getMessage());
	}
	return con;
    }

    public void liberarConnection(Connection con)
    {
	conexoesLivres.add(con);
	conexoesUtilizadas.remove(con.toString());
    }
}
