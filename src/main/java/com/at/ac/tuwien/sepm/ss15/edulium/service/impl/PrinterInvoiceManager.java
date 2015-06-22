package com.at.ac.tuwien.sepm.ss15.edulium.service.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.TaxRate;
import com.at.ac.tuwien.sepm.ss15.edulium.service.InvoiceManager;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import com.itextpdf.text.*;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageable;

import java.awt.*;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PrinterInvoiceManager implements InvoiceManager {
    private static final Logger LOGGER = LogManager.getLogger(PrinterInvoiceManager.class);

    private static final String templatePath = "invoice_pdf_template/template.pdf";
    private static final String outputFilePath = "target/last_invoice.pdf";

    /**
     * Generates a PDF document from the given invoice and sends it to
     * the printer
     */
    @Override
    public void manageInvoice(Invoice invoice) throws ServiceException {
        deletePDF();
        generatePDF(invoice);
        viewPDF();
//        printPDF();
    }

    private void generatePDF(Invoice invoice) throws ServiceException {
        PdfReader reader = null;
        PdfStamper stamper = null;
        try {
            reader = new PdfReader(templatePath);
            stamper = new PdfStamper(reader, new FileOutputStream(outputFilePath));
            PdfContentByte canvas = stamper.getOverContent(1);

            // fixed table width
            float tableWidth = 3f * 170f;

            Rectangle pageSize = reader.getPageSize(1);
            float xPos = (pageSize.getWidth() - tableWidth) / 2f;
            float yPos = pageSize.getHeight() - 200f;
            final float cHeight = 20f;

            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT,
                    new Phrase("Invoice ID: " + invoice.getIdentity()), xPos, yPos, 0);
            LocalDateTime invoiceTime = invoice.getTime();
            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT,
                    new Phrase("Date: " + invoiceTime.getDayOfMonth() +
                            "." + invoiceTime.getMonth().getValue() +
                            "." + invoiceTime.getYear()), xPos, yPos -= 15f, 0);
            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT,
                    new Phrase("Time: " + invoiceTime.getHour() +
                            ":" + invoiceTime.getMinute() +
                            ":" + invoiceTime.getSecond()), xPos, yPos -= 15f, 0);
            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT,
                    new Phrase("Waiter: " + invoice.getCreator().getName()), xPos, yPos -= 15f, 0);

            PdfPTable ordersTable = new PdfPTable(3);
            ordersTable.setTotalWidth(tableWidth);

            PdfPCell infoCell = new PdfPCell();
            infoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            infoCell.setFixedHeight(cHeight);
            infoCell.setPhrase(new Phrase("Item"));
            ordersTable.addCell(infoCell);
            infoCell.setPhrase(new Phrase("Quantity x Price"));
            ordersTable.addCell(infoCell);
            infoCell.setPhrase(new Phrase("Total"));
            ordersTable.addCell(infoCell);

            int index = 1;
            MenuEntry entry;
            TaxRate taxRate;
            List<TaxRate> rates = new ArrayList<>();
            for (Order order : invoice.getOrders()) {
                entry = order.getMenuEntry();
                taxRate = entry.getTaxRate();
                if (!rates.contains(taxRate)) {
                    rates.add(taxRate);
                }

                PdfPCell cell = new PdfPCell();
                cell.setFixedHeight(cHeight);
                if (index++ % 2 == 0) {
                    cell.setBackgroundColor(BaseColor.WHITE);
                } else {
                    cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                }
                cell.setPhrase(new Phrase(entry.getName()));
                ordersTable.addCell(cell);
                cell.setPhrase(new Phrase("1" + " x " + order.getBrutto() + " EUR")); // TODO: Set real amount
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                ordersTable.addCell(cell);
                cell.setPhrase(new Phrase(order.getBrutto() + " EUR [" + taxRate.getIdentity() + "]"));
                ordersTable.addCell(cell);
            }

            PdfPCell sumCell = new PdfPCell(new Phrase("Sum"));
            sumCell.setFixedHeight(cHeight);
            sumCell.setColspan(2);
            sumCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            ordersTable.addCell(sumCell);

            sumCell.setColspan(1);
            sumCell.setPhrase(new Phrase(invoice.getGross() + " EUR"));
            ordersTable.addCell(sumCell);

            // write table to page
            ordersTable.writeSelectedRows(0, -1, xPos, yPos -= 15f, canvas);

            yPos -= cHeight * ordersTable.size() + cHeight;
            for (TaxRate tr : rates) {
                ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("[" + tr.getIdentity() +
                "] incl. " + tr.getValue().floatValue() * 100f + "% Tax"), xPos, yPos, 0);
                yPos -= 15f;
            }
        } catch (DocumentException e) {
            LOGGER.error("An error occurred while creating the PDF stamper", e);
            throw new ServiceException("An error occurred while creating the PDF stamper", e);
        } catch (FileNotFoundException e) {
            LOGGER.error("An error occurred while opening the output stream", e);
            throw new ServiceException("An error occurred while opening the output stream", e);
        } catch (IOException e) {
            // TODO: Handle reader initialization error
        } finally {
            if (stamper != null) {
                try {
                    stamper.close();
                } catch (DocumentException | IOException e) {
                    // TODO: Handle exception
                }
            }

            if (reader != null) {
                reader.close();
            }
        }
    }

    // temporary method for viewing the pdf
    private void viewPDF() throws ServiceException {
        if (Desktop.isDesktopSupported()) {
            File file = new File(outputFilePath);
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                LOGGER.error("No application registered for PDFs");
            }
        }
    }

    private void printPDF() throws ServiceException {
        try {
            PDDocument document = PDDocument.load(outputFilePath);
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

    private void deletePDF() throws ServiceException {
        try {
            Files.deleteIfExists(Paths.get(outputFilePath));
        } catch (IOException e) {
            LOGGER.error("An I/O error occurred while trying to delete the PDF", e);
            throw new ServiceException("An I/O error occurred while trying to delete the PDF", e);
        }
    }
}
