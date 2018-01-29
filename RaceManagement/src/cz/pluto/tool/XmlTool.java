package cz.pluto.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

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
            try {
                OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
                jaxbMarshaller.marshal(form.race, out);
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // jaxbMarshaller.marshal(customer, System.out);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

       
    }
   
   
   
    public static void importFromXML(RMForm form, File file) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Race.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            try {
                InputStreamReader is = new InputStreamReader(new FileInputStream(file), "UTF-8");
                form.race = (Race) jaxbUnmarshaller.unmarshal(is);
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
           
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
