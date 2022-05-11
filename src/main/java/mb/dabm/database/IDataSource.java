package mb.dabm.database;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Essa interface vai definir como vamos obter as conexoes
 * utilizando o metodo getConnection()
 */
public interface IDataSource {

    public abstract Connection getConnection() throws SQLException;
}