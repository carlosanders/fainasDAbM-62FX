package mb.dabm.database;

import java.sql.Connection;

/**
 *
 * @author Carlos
 */
public interface IPool {

    public abstract Connection getConnection();

    public void liberarConnection(Connection con);
}
