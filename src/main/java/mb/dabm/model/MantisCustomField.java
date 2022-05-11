package mb.dabm.model;

public class MantisCustomField
{
    private int id;
    private String name;
    private String values;
    private int type;

    public MantisCustomField()
    {

    }

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

    public String getValues()
    {
	return values;
    }

    public void setValues(String values)
    {
	this.values = values;
    }

    public int getType()
    {
	return type;
    }

    public void setType(int type)
    {
	this.type = type;
    }
    
    @Override
    public String toString()
    {
	return id + " : " + name;
    }

}
