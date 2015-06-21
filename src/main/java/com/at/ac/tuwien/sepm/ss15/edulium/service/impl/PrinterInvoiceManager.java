package com.at.ac.tuwien.sepm.ss15.edulium.service.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
import com.at.ac.tuwien.sepm.ss15.edulium.service.InvoiceManager;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageable;

import java.awt.*;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
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
        final String templatePath = "";
        final String outputFilePaht = "target/last_invoice.pdf";

        // delete the last generated PDF
        deletePDF(outputFilePaht);
        // generate new PDF and place it in the provided directory
        generatePDF(invoice, templatePath, outputFilePaht);
        // temporary opening of the PDF
        viewPDF(outputFilePaht);
        // send PDF to printer and print it out
        // printPDF(filePath);
    }

    private void generatePDF(Invoice invoice, String templatePath, String outputFilePath) throws ServiceException {
        PdfReader reader = null;
        OutputStream outputFile = null;
        Document document = new Document();
        try {
            reader = new PdfReader(templatePath);
            outputFile = new FileOutputStream(outputFilePath);
            PdfWriter.getInstance(document, outputFile);
            document.open();
            // TODO: Generate the actual content of the pdf
            document.add(new Paragraph("Hello World!"));
            document.close();
        } catch (DocumentException e) {
            LOGGER.error("An error occurred while generating the PDF", e);
            throw new ServiceException("An error occurred while generating the PDF", e);
        } catch (FileNotFoundException e) {
            LOGGER.error("An error occurred while opening the output stream", e);
            throw new ServiceException("An error occurred while opening the output stream", e);
        } catch (IOException e) {
            // TODO: Handle reader initialization error
        } finally {
            if (document.isOpen()) {
                document.close();
            }

            if (reader != null) {
                reader.close();
            }

            try {
                if (outputFile != null) {
                    outputFile.close();
                }
            } catch (IOException e) {
                LOGGER.error("An error occurred while trying to close the output stream", e);
            }
        }
    }

    // Temporary method for viewing the pdf
    private void viewPDF(String filePath) throws ServiceException {
        if (Desktop.isDesktopSupported()) {
            File file = new File(filePath);
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                LOGGER.error("No application registered for PDFs");
            }
        }
    }

    private void printPDF(String filePath) throws ServiceException {
        try {
            PDDocument document = PDDocument.load(filePath);
            PrinterJob job = PrinterJob.getPrinterJob();
            if (job == null) {
                LOGGER.error("Default printer not configured");
                throw new ServiceException("Default printer not configured");
            }
            job.setPageable(new PDPageable(document, job));
            job.setJobName("Invoice print");
            job.print();
        } catch (IOException e) {
            LOGGER.error("An error occurred while trying to load the PDF file", e);
            throw new ServiceException("An error occurred while trying to load the PDF file", e);
        } catch (PrinterException e) {
            LOGGER.error("An error occurred while trying to print the invoice", e);
            throw new ServiceException("An error occurred while trying to print the invoice", e);
        }
    }

    private void deletePDF(String filePath) throws ServiceException {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            LOGGER.error("An I/O error occurred while trying to delete the PDF", e);
            throw new ServiceException("An I/O error occurred while trying to delete the PDF", e);
        }
    }
}
