package com.at.ac.tuwien.sepm.ss15.edulium.service.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
import com.at.ac.tuwien.sepm.ss15.edulium.service.InvoiceManager;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PrinterInvoiceManager implements InvoiceManager {
    private static final Logger LOGGER = LogManager.getLogger(PrinterInvoiceManager.class);

    /**
     * Generates a PDF document from the given invoice and sends it to
     * the printer
     */
    @Override
    public void manageInvoice(Invoice invoice) throws ServiceException {
        String filePath = "target/last_invoice.pdf";

        // delete the last generated PDF
        deletePDF(filePath);
        // generate new PDF and place it in the provided directory
        generatePDF(invoice, filePath);
        // temporary opening of the PDF
        viewPDF(filePath);
        // send PDF to printer and print it out
        printPDF(filePath);
    }

    public void generatePDF(Invoice invoice, String filePath) throws ServiceException {
        OutputStream file = null;
        Document document = new Document();
        try {
            file = new FileOutputStream(filePath);
            PdfWriter.getInstance(document, file);
            document.open();
            // TODO: Generate the actual content of the pdf
            document.add(new Paragraph("Hello World #2!"));
            document.close();
        } catch (DocumentException e) {
            LOGGER.error("An error occurred while generating the PDF", e);
            throw new ServiceException("An error occurred while generating the PDF", e);
        } catch (FileNotFoundException e) {
            LOGGER.error("An error occurred while opening the output stream", e);
            throw new ServiceException("An error occurred while opening the output stream", e);
        } finally {
            if (document.isOpen()) {
                document.close();
            }

            try {
                if (file != null) {
                    file.close();
                }
            } catch (IOException e) {
                LOGGER.error("An error occurred while trying to close the output stream", e);
            }
        }
    }

    // Temporary method for viewing the pdf
    public void viewPDF(String filePath) throws ServiceException {
        if (Desktop.isDesktopSupported()) {
            File file = new File(filePath);
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                LOGGER.error("No application registered for PDFs");
            }
        }
    }

    public void printPDF(String filePath) throws ServiceException {
        // TODO: Implement printing functionality
    }

    public void deletePDF(String filePath) throws ServiceException {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            LOGGER.error("An I/O error occurred while trying to delete the PDF", e);
            throw new ServiceException("An I/O error occurred while trying to delete the PDF", e);
        }
    }
}
