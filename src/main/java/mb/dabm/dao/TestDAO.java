package mb.dabm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import mb.dabm.database.IPool;

public class TestDAO
{
    private IPool pool;
    private Connection conn;


    public TestDAO(IPool pool)
    {
	this.pool = pool;
	this.conn = pool.getConnection();
    }
    
    public String getDateBD() throws SQLException
    {
	PreparedStatement ps = null;
	ResultSet rs = null;

	String sqlSelect = "SELECT NOW()";

	try {
	    ps = this.conn.prepareStatement(sqlSelect);
	    rs = ps.executeQuery();

	    if (rs.next()) {

		// System.out.println("Agora: "+ rs);
		
		java.sql.Timestamp mysqlTime = rs.getTimestamp(1);
		
		return mysqlTime.toString();
	    }

	    rs.close();
	    ps.close();

	} finally {
	    pool.liberarConnection(this.conn);
	}

	return null;
    }
}
