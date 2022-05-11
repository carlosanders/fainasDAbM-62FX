package mb.dabm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import mb.dabm.database.IPool;
import mb.dabm.model.MantisCustomField;
import mb.dabm.model.MantisCustomFieldString;
import mb.dabm.model.MantisUser;

public class MantisCustomFieldDAO
{
    private Connection connection;
    private static final Logger LOGGER = LogManager.getLogger(MantisCustomFieldDAO.class.getName());

    public MantisCustomFieldDAO(IPool pool)
    {
	this.connection = pool.getConnection();
    }

    public List<MantisCustomField> listar()
    {
	String sql = "SELECT id, name, type, possible_values FROM mantis_custom_field_table";
	List<MantisCustomField> retorno = new ArrayList<>();
	try {
	    PreparedStatement stmt = connection.prepareStatement(sql);
	    ResultSet resultado = stmt.executeQuery();
	    while (resultado.next()) {
		MantisCustomField mc = new MantisCustomField();
		mc.setId(resultado.getInt("id"));
		mc.setName(resultado.getString("name"));
		mc.setType(resultado.getInt("type"));
		mc.setValues(resultado.getString("possible_values"));
		retorno.add(mc);
	    }
	} catch (SQLException ex) {
	    if (LOGGER.isErrorEnabled()) {
		LOGGER.error("[SQLException] " + ex.getMessage());
	    }
	}
	return retorno;
    }

    public List<MantisUser> listarUser()
    {
	String sql = "SELECT id, username, realname FROM mantis_user_table order by username ASC";
	List<MantisUser> retorno = new ArrayList<>();
	try {
	    PreparedStatement stmt = connection.prepareStatement(sql);
	    ResultSet resultado = stmt.executeQuery();
	    while (resultado.next()) {
		MantisUser mc = new MantisUser();
		mc.setId(resultado.getInt("id"));
		mc.setName(resultado.getString("username"));
		mc.setRealName(resultado.getString("realname"));

		retorno.add(mc);
	    }
	} catch (SQLException ex) {
	    if (LOGGER.isErrorEnabled()) {
		LOGGER.error("[SQLException] " + ex.getMessage());
	    }
	}
	return retorno;
    }

    public MantisCustomFieldString buscar(MantisCustomFieldString mantis)
    {
	String sql = "SELECT * FROM mantis_custom_field_string_table " + " WHERE 1 = 1 " + " AND field_id = ? "
		+ " AND bug_id = ?";

	MantisCustomFieldString retorno = new MantisCustomFieldString();
	try {
	    PreparedStatement stmt = connection.prepareStatement(sql);
	    stmt.setInt(1, mantis.getId());
	    stmt.setInt(2, mantis.getBugId());

	    ResultSet resultado = stmt.executeQuery();

	    if (resultado.next()) {

		/*
		 * String v = (resultado.getString("value").isEmpty()) ? "|" + mantis.getValor()
		 * + "|" : resultado.getString("value") + mantis.getValor() + "|";
		 */

		StringBuilder valor = new StringBuilder();
		if (resultado.getString("value").isEmpty()) {
		    valor.append("|" + mantis.getValor() + "|");
		} else {

		    String toCount = resultado.getString("value");
		    // System.out.println("Todos: " + toCount);
		    // System.out.println("valor obj: " + mantis.getValor());

		    // List<String> items = Arrays.asList(toCount.split("|"));
		    List<String> items = Lists.newArrayList(Splitter.on("|").split(toCount));

		    // System.out.println("valores da lista: "+ items.toString());

		    if (items.toString().matches("\\[.*\\b" + mantis.getValor() + "\\b.*]")) {
			valor.append(resultado.getString("value"));
			// System.out.println("Sim: " + resultado.getString("value"));
		    } else {
			valor.append(resultado.getString("value") + mantis.getValor() + "|");
			// System.out.println("Não: " +resultado.getString("value"));
		    }

		}

		mantis.setValor(valor.toString());
		// vrf para concatenar aqui c/valor recebido do form.
		mantis.setId(resultado.getInt("field_id"));
		mantis.setBugId(resultado.getInt("bug_id"));

		retorno = mantis;
	    } else {

		String v = "|" + mantis.getValor() + "|";
		mantis.setValor(v);

		retorno = mantis;

		// System.out.println("bug_id: " + mantis.getBugId());
	    }

	} catch (SQLException ex) {
	    if (LOGGER.isErrorEnabled()) {
		LOGGER.error("[SQLException] " + ex.getMessage());
	    }
	}
	return retorno;
    }

    public boolean remover(MantisCustomFieldString mantis, MantisCustomField m)
    {
	String sql = "DELETE FROM mantis_custom_field_string_table WHERE bug_id = ? AND field_id = ?";
	try {
	    PreparedStatement stmt = connection.prepareStatement(sql);
	    stmt.setInt(1, mantis.getBugId());
	    stmt.setInt(2, m.getId());
	    stmt.execute();
	    return true;
	} catch (SQLException ex) {
	    if (LOGGER.isErrorEnabled()) {
		LOGGER.error("[SQLException] " + ex.getMessage());
	    }
	    return false;
	}
    }

    public boolean inserir(MantisCustomFieldString mantis)
    {
	String sql = "INSERT INTO mantis_custom_field_string_table(field_id, bug_id, value) VALUES(?,?,?)";
	try {
	    PreparedStatement stmt = connection.prepareStatement(sql);
	    stmt.setInt(1, mantis.getId());
	    stmt.setInt(2, mantis.getBugId());
	    stmt.setString(3, mantis.getValor());
	    stmt.execute();
	    return true;
	} catch (SQLException ex) {
	    if (LOGGER.isErrorEnabled()) {
		LOGGER.error("[SQLException] " + ex.getMessage());
	    }
	    return false;
	}
    }

    public boolean inserirHistorico(MantisCustomFieldString mantis, 
	    MantisUser user, MantisCustomField custom, String valor)
    {
	String sql = "INSERT INTO mantis_bug_history_table(user_id, bug_id, date_modified, field_name, new_value, old_value) VALUES(?,?,?,?,?,?)";
	try {

	    Date now = new Date();
	    long ut3 = now.getTime() / 1000L;
	    System.out.println(ut3);

	    PreparedStatement stmt = connection.prepareStatement(sql);
	    stmt.setInt(1, user.getId());
	    stmt.setInt(2, mantis.getBugId());
	    stmt.setLong(3, ut3);
	    stmt.setString(4, custom.getName());
	    stmt.setString(5, valor);
	    stmt.setString(6, "");
	    stmt.execute();
	    return true;
	} catch (SQLException ex) {
	    if (LOGGER.isErrorEnabled()) {
		LOGGER.error("[SQLException] " + ex.getMessage());
	    }
	    return false;
	}
    }
}
