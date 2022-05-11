package mb.dabm.model;

public class MantisUser
{
    private int id;
    private String name;
    private String realName;
    
    
    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public String getRealName()
    {
        return realName;
    }
    public void setRealName(String realName)
    {
        this.realName = realName;
    }
    
    @Override
    public String toString()
    {
	return id + " : " + name;
    }
}
