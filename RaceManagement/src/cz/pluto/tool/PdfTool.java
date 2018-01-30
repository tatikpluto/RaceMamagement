package cz.pluto.tool;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceGray;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;


import cz.pluto.data.Category;
import cz.pluto.data.Person;
import cz.pluto.data.Race;

public class PdfTool {
    
    public static String toUTF8(String in) {
        byte[] byteText = in.getBytes(Charset.forName("UTF-8"));
        return new String(byteText, Charset.forName("UTF-8"));
    }

   
    public static void createRaceList(Race race, String destination) throws IOException {
        if (destination.endsWith(".xml")) {
            int lastInd = destination.lastIndexOf('\\');
            destination = destination.substring(0, lastInd);
        }
        destination = destination+"\\startovka.pdf";
        PdfDocument pdf = new PdfDocument(new PdfWriter(destination));
        PageSize pagesize = PageSize.A4;
        Document document = new Document(pdf, pagesize);
        PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN,PdfEncodings.CP1250);
        PdfFont bold = PdfFontFactory.createFont(FontConstants.TIMES_BOLD, PdfEncodings.CP1250);
        PdfFont italic = PdfFontFactory.createFont(FontConstants.TIMES_ITALIC, PdfEncodings.CP1250);
        Text title = new Text(race.getName()).setFont(bold).setFontSize(20);
        Text place = new Text(race.getPlace()).setFont(italic).setFontSize(16);
        document.setTopMargin(15f); //odsazeni z hora
        document.add(new Paragraph().add(title).setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph().add(place).setTextAlignment(TextAlignment.CENTER).setMarginTop(-8f));
        
        for (Category cat : race.getCategories()) {
            List<Person> persons = new ArrayList<>();
            for (Person per : race.getPersons()) {
                if (per.getCategoryName().equals(cat.getName()))
                    persons.add(per);
            }
            Collections.sort(persons, new Comparator<Person>() {
                public int compare(Person p1, Person p2) {
                    return p1.getStartNumber().compareTo(p1.getStartNumber());
                }
            });
            if (!persons.isEmpty()) {

                Paragraph catName = new Paragraph(cat.getName()+" :  "+cat.getDelka()    + "    Start: "+Tool.durationToStringStart(Duration.ofMillis(cat.getStartTime())) );
                document.add(catName.setFont(bold).setFontSize(11));
                Table table = new Table(4);
                //header
                Cell[] headerFooter = new Cell[] {
                        new Cell().setFont(bold).setBackgroundColor(new DeviceGray(0.90f)).setTextAlignment(TextAlignment.CENTER).setWidthPercent(7).add("St. èíslo"),
                        new Cell().setFont(bold).setBackgroundColor(new DeviceGray(0.90f)).setTextAlignment(TextAlignment.CENTER).add("Jméno"),
                        new Cell().setFont(bold).setBackgroundColor(new DeviceGray(0.90f)).setTextAlignment(TextAlignment.CENTER).add("Klub"),
                        new Cell().setFont(bold).setBackgroundColor(new DeviceGray(0.90f)).setTextAlignment(TextAlignment.CENTER).setWidthPercent(5).add("Roèník")
                };
                for (Cell hfCell : headerFooter) {
                    table.addHeaderCell(hfCell.setFontSize(9));
                }
                float fontSize = 9;
                for (Person person : persons) {
                    // Startovní èíslo
                    Cell sn = new Cell().add(Integer.toString(person.getStartNumber()));
                    sn.setFont(font).setFontSize(fontSize);
                    sn.setTextAlignment(TextAlignment.RIGHT);
                    sn.setWidthPercent(7);
                    table.addCell(sn);
                    
                    table.addCell(new Cell().setTextAlignment(TextAlignment.LEFT).setFont(font).setFontSize(fontSize).add(person.getLabel()));
                    table.addCell(new Cell().setTextAlignment(TextAlignment.LEFT).setFont(font).setFontSize(fontSize).add(person.getClub()));
                    table.addCell(new Cell().setTextAlignment(TextAlignment.RIGHT).setFont(font).setFontSize(fontSize).setWidthPercent(5).add(Integer.toString(person.getYear())));
                }
                
                document.add(table);
                document.add(new Paragraph().setHeight(6f));
            }
        }
        
        document.close();
    }
    
   
    public static void createRaceResult(Race race, String destination) throws IOException {
        if (destination.endsWith(".xml")) {
            int lastInd = destination.lastIndexOf('\\');
            destination = destination.substring(0, lastInd);
        }
        destination = destination+"\\vysledky.pdf";
        PdfDocument pdf = new PdfDocument(new PdfWriter(destination));
        PageSize pagesize = PageSize.A4;
        Document document = new Document(pdf, pagesize);
        PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN,PdfEncodings.CP1250);
        PdfFont bold = PdfFontFactory.createFont(FontConstants.TIMES_BOLD, PdfEncodings.CP1250);
        PdfFont italic = PdfFontFactory.createFont(FontConstants.TIMES_ITALIC, PdfEncodings.CP1250);
        Text title = new Text(race.getName()).setFont(italic).setFontSize(16);
        Text place = new Text(race.getPlace()).setFont(italic).setFontSize(16);
        Text vysl = new Text("Výsledková listina").setFont(bold).setFontSize(20);
        
        SolidLine line = new SolidLine(1f);
        line.setColor(Color.BLACK);
        LineSeparator ls = new LineSeparator(line);
        ls.setWidthPercent(100);
        
        document.setTopMargin(15f); //odsazeni z hora
        document.add(ls);
        document.add(new Paragraph().add(place).setTextAlignment(TextAlignment.LEFT));
        document.add(new Paragraph().add(title).setTextAlignment(TextAlignment.LEFT).setMarginTop(-8f));
        document.add(new Paragraph().add(vysl).setTextAlignment(TextAlignment.LEFT).setMarginTop(-8f));
        document.add(ls);
        
        for (Category cat : race.getCategories()) {
            List<Person> persons = new ArrayList<>();
            for (Person per : race.getPersons()) {
                if (per.getCategoryName().equals(cat.getName())) {
                    persons.add(per);
                }
            }
            if (persons.isEmpty())
                continue;
            
            persons.sort(Comparator.comparing(Person::getTime,Comparator.nullsLast(Comparator.naturalOrder())));
            if (!persons.isEmpty()) {
                Paragraph catName = new Paragraph(cat.getName()+" :  "+cat.getDelka());
                document.add(catName.setFont(bold).setFontSize(11));
                Table table = new Table(7);
                //header
                Cell[] headerFooter = new Cell[] {
                        new Cell().setFont(bold).setBackgroundColor(new DeviceGray(0.90f)).setTextAlignment(TextAlignment.CENTER).setWidthPercent(5).add("Poøadí"),
                        new Cell().setFont(bold).setBackgroundColor(new DeviceGray(0.90f)).setTextAlignment(TextAlignment.CENTER).setWidthPercent(7).add("St. èíslo"),
                        new Cell().setFont(bold).setBackgroundColor(new DeviceGray(0.90f)).setTextAlignment(TextAlignment.CENTER).add("Jméno"),
                        new Cell().setFont(bold).setBackgroundColor(new DeviceGray(0.90f)).setTextAlignment(TextAlignment.CENTER).add("Klub"),
                        new Cell().setFont(bold).setBackgroundColor(new DeviceGray(0.90f)).setTextAlignment(TextAlignment.CENTER).setWidthPercent(5).add("Roèník"),
                        new Cell().setFont(bold).setBackgroundColor(new DeviceGray(0.90f)).setTextAlignment(TextAlignment.CENTER).setWidthPercent(7).add("Výsl. èas"),
                        new Cell().setFont(bold).setBackgroundColor(new DeviceGray(0.90f)).setTextAlignment(TextAlignment.CENTER).setWidthPercent(7).add("Ztráta")
                };
                for (Cell hfCell : headerFooter) {
                    table.addHeaderCell(hfCell.setFontSize(9));
                }
                float fontSize = 8;
                int poradi = 1;
                Long firstTime = persons.get(0).getTime();
                for (Person person : persons) {
                    // Poradi
                    Cell pc = new Cell().add(person.getTime()==null ? "DNF" : Integer.toString(poradi));
                    pc.setFont(font).setFontSize(fontSize).setTextAlignment(TextAlignment.LEFT).setWidthPercent(5);
                    table.addCell(pc);
                    // Startovní èíslo
                    Cell sn = new Cell().add(Integer.toString(person.getStartNumber()));
                    sn.setFont(font).setFontSize(fontSize).setTextAlignment(TextAlignment.RIGHT).setWidthPercent(7);
                    table.addCell(sn);
                    
                    table.addCell(new Cell().setTextAlignment(TextAlignment.LEFT).setFont(font).setFontSize(fontSize).add(person.getLabel()));
                    table.addCell(new Cell().setTextAlignment(TextAlignment.LEFT).setFont(font).setFontSize(fontSize).add(person.getClub()));
                    table.addCell(new Cell().setTextAlignment(TextAlignment.RIGHT).setFont(font).setFontSize(fontSize).setWidthPercent(5).add(Integer.toString(person.getYear())));
                    
                    String vyslCas = person.getTime()==null ? "......." : Tool.durationToString(Duration.ofMillis(person.getTime()));
                    
                    // Vysledny cas
                    Cell tc = new Cell().add(vyslCas);
                    tc.setFont(font).setFontSize(fontSize).setTextAlignment(TextAlignment.RIGHT).setWidthPercent(7);
                    table.addCell(tc);
                    
                    String ztrata = person.getTime()==null ? "......." : Tool.durationToString(Duration.ofMillis(person.getTime() - firstTime));
                    
                    // Ztrata
                    Cell zc = new Cell().add(ztrata);
                    zc.setFont(font).setFontSize(fontSize).setTextAlignment(TextAlignment.RIGHT).setWidthPercent(7);
                    table.addCell(zc);
                    
                    poradi++;
                }
                
                document.add(table);
                document.add(new Paragraph());
            }
        }
        
        document.close();
        
    }

}
