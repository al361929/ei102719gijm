package majorsacasa.controller;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.text.pdf.draw.LineSeparator;
import majorsacasa.model.Elderly;
import majorsacasa.model.Invoice;
import majorsacasa.model.Request;
import majorsacasa.model.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GeneratePDFController {

    private static final Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA, 26, Font.BOLD);
    private static final Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL);

    private static final BaseColor granate = new BaseColor(203, 62, 62);

    private static final Font categoryFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static final Font subcategoryFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    private static final Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
    private static final Font smallBold = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, granate);

    private static final String linea = "src/main/resources/static/img/linea-roja.png";
    private static final String logo = "src/main/resources/static/img/logo.png";


    /**
     * We create a PDF document with iText using different elements to learn
     * to use this library.
     * Creamos un documento PDF con iText usando diferentes elementos para aprender
     * a usar esta librería.
     *
     * @param pdfNewFile <code>String</code>
     *                   pdf File we are going to write.
     *                   Fichero pdf en el que vamos a escribir.
     * @param request
     * @param service
     */
    public void createPDF(File pdfNewFile, Invoice invoice, Elderly elderly, Request request, Service service) {
        // Aquí introduciremos el código para crear el PDF.
        // We create the document and set the file name.

        // Creamos el documento e indicamos el nombre del fichero.
        try {
            Document document = new Document();
            //PdfDocument pdfDocument = null;
            PdfWriter writer = null;
            try {
                //PdfWriter pdfWriter = new PdfWriter(pdfNewFile);
                //pdfDocument = new PdfDocument(pdfWriter);
                writer = com.itextpdf.text.pdf.PdfWriter.getInstance(document, new FileOutputStream(pdfNewFile));
            } catch (FileNotFoundException fileNotFoundException) {
                System.out.println("No such file was found to generate the PDF "
                        + "(No se encontró el fichero para generar el pdf)" + fileNotFoundException);
            }
            document.open();

            // Creamos el manejador de evento de pagina, el cual agregara
            // el encabezado y pie de pagina
            //EventoPagina evento = new EventoPagina(document);
            // Indicamos que el manejador se encargara del evento END_PAGE
            //pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, evento);
            // Establecemos los margenes

            // We add metadata to PDF
            // Añadimos los metadatos del PDF
            document.addTitle("Factura-" + invoice.getInvoiceNumber());
            document.addSubject("Usando iText");
            document.addKeywords("Java, PDF, iText, Majors a Casa, HTML, CSS");
            document.addAuthor("Majors a Casa");
            document.addCreator("Majors a Casa");

            document.setMargins(70, 50, 35, 35);

            // First page
            // Primera página
            // Añadimos la linea roja de arriba
            Chapter chapter = new Chapter(0);
            Image lineaArriba;
            try {
                lineaArriba = Image.getInstance(linea);
                lineaArriba.setAbsolutePosition(0, 822);
                //lineaArriba.setAlignment(4);
                chapter.add(lineaArriba);
            } catch (BadElementException ex) {
                System.out.println("Image BadElementException" + ex);
            } catch (IOException ex) {
                System.out.println("Image IOException " + ex);
            }

            //Añadimos el logo de la empresa
            Image image;
            try {
                image = Image.getInstance(logo);
                image.setWidthPercentage(100F);
                image.setAbsolutePosition(70, 700);
                //image.setAlignment(Element.ALIGN_LEFT);
                chapter.add(image);
            } catch (BadElementException ex) {
                System.out.println("Image BadElementException" + ex);
            } catch (IOException ex) {
                System.out.println("Image IOException " + ex);
            }

            Paragraph fact = new Paragraph("Factura", chapterFont);
            fact.setAlignment(Element.ALIGN_RIGHT);
            chapter.add(fact);

            LineSeparator lineSeparator = new LineSeparator();
            lineSeparator.setAlignment(Element.ALIGN_RIGHT);
            lineSeparator.setOffset(-2);
            lineSeparator.setLineWidth(1);
            lineSeparator.setLineColor(BaseColor.BLACK);
            lineSeparator.setPercentage(20);

            Paragraph fecha = new Paragraph("\nFecha", smallBold);
            fecha.setAlignment(Element.ALIGN_RIGHT);
            fecha.add(lineSeparator);
            chapter.add(fecha);

            Paragraph fechaInvoice = new Paragraph(invoice.getDateInvoice().toString());
            fechaInvoice.setAlignment(Element.ALIGN_RIGHT);
            chapter.add(fechaInvoice);

            Paragraph nFactura = new Paragraph("\nNº de factura", smallBold);
            nFactura.setAlignment(Element.ALIGN_RIGHT);
            nFactura.add(lineSeparator);
            chapter.add(nFactura);

            Paragraph factura = new Paragraph(invoice.getInvoiceNumber().toString());
            factura.setAlignment(Element.ALIGN_RIGHT);
            chapter.add(factura);

            LineSeparator lineCliente = new LineSeparator();
            lineCliente.setAlignment(0);
            lineCliente.setOffset(-2);
            lineCliente.setLineWidth(1);
            lineCliente.setLineColor(BaseColor.BLACK);
            lineCliente.setPercentage(100);

            Paragraph cliente = new Paragraph("\n\nDatos Cliente", smallBold);
            cliente.setAlignment(Element.ALIGN_LEFT);
            cliente.add(lineCliente);
            cliente.setFont(paragraphFont);
            cliente.add("\n" + elderly.getNombre() + " " + elderly.getApellidos() + "\n");
            cliente.add(elderly.getDni() + "\n");
            cliente.add(elderly.getDireccion() + "\n");
            cliente.add(elderly.getTelefono() + "\n");
            cliente.add(elderly.getEmail());
            chapter.add(cliente);

            Paragraph parrafoTabla = new Paragraph();
            PdfPTable tabla = new PdfPTable(4);
            List<String> lista = new ArrayList<>();
            lista.add("Descripción");
            lista.add("Unidades");
            lista.add("Precio Unitario");
            lista.add("Precio");
            PdfPCell headerColumn;
            for (String header : lista) {
                Phrase encabezado = new Phrase(header);
                encabezado.setFont(headerFont);
                headerColumn = new PdfPCell(encabezado);
                headerColumn.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerColumn.setBackgroundColor(granate);
                tabla.addCell(headerColumn);
            }
            tabla.setHeaderRows(1);
            for (int row = 0; row < 4; row++) {
                for (int column = 0; column < 4; column++) {
                    tabla.addCell("Row " + row + " - Col" + column);
                }
            }
            parrafoTabla.add(tabla);
            chapter.add(parrafoTabla);

            System.out.println(chapter.toString());

            try {
                lineaArriba = Image.getInstance(linea);
                lineaArriba.setAbsolutePosition(0, 0);
                //lineaArriba.setAlignment(4);
                chapter.add(lineaArriba);
            } catch (BadElementException ex) {
                System.out.println("Image BadElementException" + ex);
            } catch (IOException ex) {
                System.out.println("Image IOException " + ex);
            }

            document.add(chapter);


            //Añadimos los datos de la empresa
            PdfContentByte over = writer.getDirectContent();
            try {
                over.saveState();

                over.beginText();
                over.setFontAndSize(paragraphFont.getBaseFont(), 9);
                over.setTextMatrix(140, 740);
                over.showText("Conselleria de Asuntos Sociales");
                over.endText();

                over.beginText();
                over.setFontAndSize(paragraphFont.getBaseFont(), 9);
                over.setTextMatrix(140, 730);
                over.showText("Av. dels Germans Bou, 81");
                over.endText();

                over.beginText();
                over.setFontAndSize(paragraphFont.getBaseFont(), 9);
                over.setTextMatrix(140, 720);
                over.showText("12100 Castelló de la Plana, Castelló");
                over.endText();

                over.beginText();
                over.setFontAndSize(paragraphFont.getBaseFont(), 9);
                over.setTextMatrix(140, 710);
                over.showText("964 72 62 00");
                over.endText();

                over.beginText();
                over.setFontAndSize(paragraphFont.getBaseFont(), 9);
                over.setTextMatrix(140, 700);
                over.showText("http://www.inclusio.gva.es");
                over.endText();

                over.restoreState();
            } catch (Exception e) {
                e.printStackTrace();
            }


            // Second page - some elements
            // Segunda página - Algunos elementos
            Chapter chapSecond = new Chapter(new Paragraph(new Anchor("Some elements (Añadimos varios elementos)")), 1);
            Paragraph paragraphS = new Paragraph("Do it by Xules (Realizado por Xules)", subcategoryFont);

            // Underline a paragraph by iText (subrayando un párrafo por iText)
            Paragraph paragraphE = new Paragraph("This line will be underlined with a dotted line (Está línea será subrayada con una línea de puntos).");
            DottedLineSeparator dottedline = new DottedLineSeparator();
            dottedline.setAlignment(2);
            dottedline.setOffset(10);
            dottedline.setGap(0);
            paragraphE.add(dottedline);
            chapSecond.addSection(paragraphE);

            Section paragraphMoreS = chapSecond.addSection(paragraphS);
            // List by iText (listas por iText)
            String text = "test 1 2 3 ";
            for (int i = 0; i < 5; i++) {
                text = text + text;
            }
            /*List list = new List(List.UNORDERED);
            ListItem item = new ListItem(text);
            item.setAlignment(Element.ALIGN_JUSTIFIED);
            list.add(item);
            text = "a b c align ";
            for (int i = 0; i < 5; i++) {
                text = text + text;
            }
            item = new ListItem(text);
            item.setAlignment(Element.ALIGN_JUSTIFIED);
            list.add(item);
            text = "supercalifragilisticexpialidocious ";
            for (int i = 0; i < 3; i++) {
                text = text + text;
            }
            item = new ListItem(text);
            item.setAlignment(Element.ALIGN_JUSTIFIED);
            list.add(item);
            paragraphMoreS.add(list);
            document.add(chapSecond);
*/
            // How to use PdfPTable
            // Utilización de PdfPTable
            // We use various elements to add title and subtitle
            // Usamos varios elementos para añadir título y subtítulo
            Anchor anchor = new Anchor("Table export to PDF (Exportamos la tabla a PDF)", categoryFont);
            anchor.setName("Table export to PDF (Exportamos la tabla a PDF)");
            Chapter chapTitle = new Chapter(new Paragraph(anchor), 1);
            Paragraph paragraph = new Paragraph("Do it by Xules (Realizado por Xules)", subcategoryFont);
            Section paragraphMore = chapTitle.addSection(paragraph);
            paragraphMore.add(new Paragraph("This is a simple example (Este es un ejemplo sencillo)"));
            Integer numColumns = 6;
            Integer numRows = 120;

            // We create the table (Creamos la tabla).
            PdfPTable table = new PdfPTable(numColumns);
            // Now we fill the PDF table
            // Ahora llenamos la tabla del PDF
            PdfPCell columnHeader;
            // Fill table rows (rellenamos las filas de la tabla).
            for (int column = 0; column < numColumns; column++) {
                columnHeader = new PdfPCell(new Phrase("COL " + column));
                columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(columnHeader);
            }
            table.setHeaderRows(1);

            // Fill table rows (rellenamos las filas de la tabla).
            for (int row = 0; row < numRows; row++) {
                for (int column = 0; column < numColumns; column++) {
                    table.addCell("Row " + row + " - Col" + column);
                }
            }

            // We add the table (Añadimos la tabla)
            paragraphMore.add(table);

            // We add the paragraph with the table (Añadimos el elemento con la tabla).
            document.add(chapTitle);
            document.close();
            System.out.println("Your PDF file has been generated!(¡Se ha generado tu hoja PDF!");


        } catch (DocumentException documentException) {
            System.out.println("The file not exists (Se ha producido un error al generar un documento): " + documentException);
        }

    }
}
