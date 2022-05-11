package mb.dabm.service;

import java.util.ArrayList;
import java.util.List;

import mb.dabm.dao.MantisCustomFieldDAO;
import mb.dabm.database.IPool;
import mb.dabm.database.Pool;
import mb.dabm.model.MantisCustomField;
import mb.dabm.model.MantisCustomFieldString;
import mb.dabm.model.MantisUser;

public class MantisCustomFieldService
{
    private IPool pool = Pool.getInstacia();
    private final MantisCustomFieldDAO mantisCustomFieldDAO = new MantisCustomFieldDAO(pool);

    public List<MantisCustomFieldString> atulizarMantisCustomString(List<String> items, String txtValor,
	    MantisCustomField m, MantisUser user)
    {
	List<MantisCustomFieldString> retorno = new ArrayList<MantisCustomFieldString>();

	for (String item : items) {
	    MantisCustomFieldString mantisString = new MantisCustomFieldString();
	    mantisString.setValor(txtValor);
	    mantisString.setBugId(Integer.parseInt(item));
	    mantisString.setId(m.getId());
	    
	    // vrf se existe e arruma a insercao
	    MantisCustomFieldString mantis = getMantisFieldString(mantisString);
	    
	    //remove e retorna true
	    boolean r = mantisCustomFieldDAO.remover(mantis, m);
	    // se true insere novamente com os dados atualizados	    
	    if(r) {
		mantisCustomFieldDAO.inserir(mantis);
		mantisCustomFieldDAO.inserirHistorico(mantis, user, m, txtValor);
	    }

	    //vrf se existe na tabela o bug_id
	    retorno.add(mantis);
	    //retorno.add(getMantisFieldString(mantisString));
	}

	return retorno;
    }

    private MantisCustomFieldString getMantisFieldString(MantisCustomFieldString m)
    {
	return mantisCustomFieldDAO.buscar(m);
    }

}
