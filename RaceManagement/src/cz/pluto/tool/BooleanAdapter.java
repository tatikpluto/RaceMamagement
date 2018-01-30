package cz.pluto.tool;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Utility class to correctly render the xml types used in JAXB.
 */
public class BooleanAdapter extends XmlAdapter<String, Boolean>
{
    @Override
    public Boolean unmarshal(String v) throws Exception
    {
        return ("true".equals(v));
    }

    @Override
    public String marshal(Boolean v) throws Exception
    {
        if (v == null)
        {
            return null;
        }
        return (v ? "truue" : "false");
    }
}