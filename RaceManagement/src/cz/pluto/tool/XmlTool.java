package cz.pluto.tool;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import cz.pluto.data.Race;
import cz.pluto.gui.RMForm;

public class XmlTool {
    
    public static void exportToXml(RMForm form, File file) {
        
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Race.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(form.race, file);
            // jaxbMarshaller.marshal(customer, System.out);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        
    }
    
    
    
    public static void importFromXML(RMForm form, File file) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Race.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            form.race = (Race) jaxbUnmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
