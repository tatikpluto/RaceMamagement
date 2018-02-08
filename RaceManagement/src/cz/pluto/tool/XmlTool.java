package cz.pluto.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

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
            //jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            try {
                Writer out = new OutputStreamWriter(new FileOutputStream(file));
                jaxbMarshaller.marshal(form.race, out);
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }

       
    }
   
   
   
    public static void importFromXML(RMForm form, File file) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Race.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            try {
                InputStreamReader is = new InputStreamReader(new FileInputStream(file));
                form.race = (Race) jaxbUnmarshaller.unmarshal(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
