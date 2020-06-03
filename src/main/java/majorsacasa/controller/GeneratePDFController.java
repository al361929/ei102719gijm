package majorsacasa.controller;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
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
    private static final BaseColor gris = new BaseColor(215, 215, 215);


    private static final Font categoryFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static final Font subcategoryFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    private static final Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, granate);
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

        // Creamos el documento e indicamos el nombre del fichero.
        try {
            Document document = new Document();
            PdfWriter writer = null;
            try {
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

            // Añadimos los metadatos del PDF
            document.addTitle("Factura-" + invoice.getInvoiceNumber());
            document.addSubject("Usando iText");
            document.addKeywords("Java, PDF, iText, Majors a Casa, HTML, CSS");
            document.addAuthor("Majors a Casa");
            document.addCreator("Majors a Casa");

            document.setMargins(70, 50, 35, 35);

            // Primera página
            // AÑADIMOS LA FRANJA GRANATE DE ARRIBA
            Chapter chapter = new Chapter(0);
            Image lineaArriba;
            try {
                lineaArriba = Image.getInstance(linea);
                lineaArriba.setAbsolutePosition(0, 822);
                chapter.add(lineaArriba);
            } catch (BadElementException ex) {
                System.out.println("Image BadElementException" + ex);
            } catch (IOException ex) {
                System.out.println("Image IOException " + ex);
            }

            // AÑADIMOS EL LOGO DE LA EMPRESA
            Image image;
            try {
                image = Image.getInstance(logo);
                image.setWidthPercentage(100F);
                image.setAbsolutePosition(70, 700);
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

            // PONEMOS LA FECHA Y EL Nº DE FACTURA
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

            // DATOS DEL CLIENTE
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

            // AÑADIMOS EL PÁRRAFO AL CUAL TIENE QUE ALMACENAR LA TABLA
            Paragraph parrafoTabla = new Paragraph("\n");
            parrafoTabla.setFont(headerFont);

            //LISTA DE LA CABECERA DE LA TABLA
            List<String> listaHeader = new ArrayList<>();
            listaHeader.add("Descripción\n");
            listaHeader.add("Unidades/Semana\t");
            listaHeader.add("Precio Unitario\t");
            listaHeader.add("Precio Total\n");

            //DATOS DE LA FACTURA QUE SE VAN A MOSTRAR MENOS LA DESCRIPCIÓN
            List<String> celdas = new ArrayList<>();
            celdas.add(request.getNumDias().toString());
            celdas.add(service.getPrice().toString());
            celdas.add(invoice.getTotalPrice().doubleValue() + " €");

            //CREAMOS LA TABLA Y AÑADIMOS LA CABECERA
            PdfPTable tabla = new PdfPTable(4);
            tabla.setWidthPercentage(100);

            PdfPCell casillas;
            for (String header : listaHeader) {
                casillas = new PdfPCell(new Phrase(header));
                casillas.setBorder(5);
                casillas.setBorderColor(granate);
                casillas.setBackgroundColor(granate);

                casillas.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(casillas);
            }
            tabla.setHeaderRows(1);

            // EL CAMPO DESCRIPCIÓN TIENE ALINEACIÓN A LA IZQUIERDA
            casillas = new PdfPCell(new Phrase(service.getDescription()));
            casillas.setBackgroundColor(gris);
            casillas.setBorder(5);
            casillas.setBorderColorRight(BaseColor.BLACK);
            casillas.setBorderColorLeft(BaseColor.BLACK);
            casillas.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(casillas);

            // SE AÑADEN LOS CAMPOS DE LA TABLA
            for (String cell : celdas) {
                casillas = new PdfPCell(new Phrase(cell));
                casillas.setBorder(5);
                casillas.setBackgroundColor(gris);
                casillas.setBorderColorRight(BaseColor.BLACK);
                casillas.setBorderColorLeft(BaseColor.BLACK);
                casillas.setHorizontalAlignment(Element.ALIGN_RIGHT);
                tabla.addCell(casillas);
            }
            parrafoTabla.add(tabla);
            chapter.add(parrafoTabla);

            Paragraph observaciones = new Paragraph("\n\n\n\n\n\n");
            observaciones.add(lineCliente);
            observaciones.add("Observaciones / Instrucciones de pago:\n");
            observaciones.setFont(paragraphFont);
            observaciones.setAlignment(Element.ALIGN_LEFT);
            observaciones.add(request.getComments());
            chapter.add(observaciones);


            // AÑADIMOS LA FRANJA GRANATE ABAJO IGUAL QUE ARRIBA
            try {
                lineaArriba = Image.getInstance(linea);
                lineaArriba.setAbsolutePosition(0, 0);
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

            document.close();
            System.out.println("Your PDF file has been generated!(¡Se ha generado tu hoja PDF!");


        } catch (DocumentException documentException) {
            System.out.println("The file not exists (Se ha producido un error al generar un documento): " + documentException);
        }

    }
}
