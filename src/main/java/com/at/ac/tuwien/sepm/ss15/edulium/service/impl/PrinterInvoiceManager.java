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
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class PrinterInvoiceManager implements InvoiceManager {
    private static final Logger LOGGER = LogManager.getLogger(PrinterInvoiceManager.class);

    private static final String templatePath = "src/main/resources/invoice_templates/template.pdf";
    private static final String outputFilePath = "target/last_invoice.pdf";

    /**
     * Generates a PDF document from the given invoice and sends it to
     * the printer
     */
    @Override
    public void manageInvoice(Invoice invoice) throws ServiceException {
        deletePDF();
        generatePDF(invoice);
        printPDF();
    }

    /**
     * Manipulates the template PDF.
     * Adds all information needed for the invoice.
     */
    private void generatePDF(Invoice invoice) throws ServiceException {
        PdfReader reader = null;
        PdfStamper stamper = null;
        try {
            reader = new PdfReader(templatePath);
            stamper = new PdfStamper(reader, new FileOutputStream(outputFilePath));
            PdfContentByte canvas = stamper.getOverContent(1);

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
            infoCell.setPhrase(new Phrase("Item and info"));
            ordersTable.addCell(infoCell);
            infoCell.setPhrase(new Phrase("Quantity x Price"));
            ordersTable.addCell(infoCell);
            infoCell.setPhrase(new Phrase("Total"));
            ordersTable.addCell(infoCell);

            int index = 1;
            MenuEntry entry;
            TaxRate taxRate;
            List<Order> orders = clone(invoice.getOrders());
            Map<Order, Integer> ordersFrequency = new HashMap<>();
            Map<TaxRate, BigDecimal> ratesAndPrice = new HashMap<>();

            for (Order order : orders) {
                entry = order.getMenuEntry();
                if (!ratesAndPrice.containsKey(order.getMenuEntry().getTaxRate())) {
                    ratesAndPrice.put(entry.getTaxRate(), entry.getPrice());
                } else {
                    ratesAndPrice.put(entry.getTaxRate(), ratesAndPrice.get(entry.getTaxRate()).add(entry.getPrice()));
                }

                if (!ordersFrequency.containsKey(order)) {
                    ordersFrequency.put(order, 1);
                } else {
                    ordersFrequency.put(order, ordersFrequency.get(order) + 1);
                }
            }

            for (Map.Entry<Order, Integer> ef : ordersFrequency.entrySet()) {
                Order order = ef.getKey();
                String info = order.getAdditionalInformation();
                entry = order.getMenuEntry();
                taxRate = entry.getTaxRate();

                PdfPCell cell = new PdfPCell();
                if (info == null || info.equals("")) {
                    cell.setFixedHeight(cHeight);
                } else {
                    cell.setFixedHeight(cHeight * 2.2f);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                }

                if (index++ % 2 == 0) {
                    cell.setBackgroundColor(BaseColor.WHITE);
                } else {
                    cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                }

                cell.setPhrase(new Phrase(entry.getName() + (info == null ? "" : "\n(" + info + ")")));
                ordersTable.addCell(cell);

                cell.setPhrase(new Phrase(ef.getValue() + " x " + order.getBrutto()));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                ordersTable.addCell(cell);

                cell.setPhrase(new Phrase(order.getBrutto().multiply(new BigDecimal(ef.getValue())) +
                        " EUR [" + taxRate.getIdentity() + "]"));
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

            yPos -= ordersTable.getTotalHeight() + cHeight;
            BigDecimal net = invoice.getGross();
            for (Map.Entry<TaxRate, BigDecimal> tr : ratesAndPrice.entrySet()) {
                Phrase phrase = new Phrase("[" + tr.getKey().getIdentity() + "] incl. " +
                        String.format("%.0f", tr.getKey().getValue().multiply(new BigDecimal("100"))) + "% Tax " +
                        currencyFormat(tr.getKey().getValue().multiply(tr.getValue())));

                ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, phrase, xPos, yPos, 0);
                yPos -= 15f;

                net = net.subtract(tr.getKey().getValue().multiply(tr.getValue()));
            }

            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT,
                    new Phrase("Net sum: " + currencyFormat(net)), xPos, yPos, 0);
        } catch (DocumentException e) {
            LOGGER.error("An error occurred while creating the PDF stamper", e);
            throw new ServiceException("An error occurred while creating the PDF stamper", e);
        } catch (FileNotFoundException e) {
            LOGGER.error("An error occurred while opening the output stream", e);
            throw new ServiceException("An error occurred while opening the output stream", e);
        } catch (IOException e) {
            LOGGER.error("An error occurred while initiating the PDF reader", e);
            throw new ServiceException("An error occurred while initiating the PDF reader", e);
        } finally {
            if (stamper != null) {
                try {
                    stamper.close();
                } catch (DocumentException | IOException e) {
                    LOGGER.error("An error occurred while trying to close the PDF stamper", e);
                }
            }

            if (reader != null) {
                reader.close();
            }
        }
    }

    private List<Order> clone(List<Order> orderList) {
        List<Order> cloned = new ArrayList<>();
        for (Order o : orderList) {
            Order order = new Order();
            order.setBrutto(o.getBrutto());
            order.setAdditionalInformation(o.getAdditionalInformation());
            order.setTax(o.getTax());
            order.setMenuEntry(o.getMenuEntry());

            cloned.add(order);
        }

        return cloned;
    }

    private String currencyFormat(BigDecimal n) {
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) nf).getDecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol("");
        ((DecimalFormat) nf).setDecimalFormatSymbols(decimalFormatSymbols);
        return nf.format(n) + " EUR";
    }

    /**
     * Opens the PDF with the default application for opening PDFs
     */
    private void viewPDF() {
        if (Desktop.isDesktopSupported()) {
            File file = new File(outputFilePath);
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                LOGGER.error("No application registered for PDFs");
            }
        }
    }

    /**
     * Sends the PDF to the default set printer
     */
    private void printPDF() throws ServiceException {
        PDDocument document = null;
        try {
            document = PDDocument.load(outputFilePath);
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
        } finally {
            try {
                if (document != null) {
                    document.close();
                }
            } catch (IOException e) {
                LOGGER.error("Closing the document failed", e);
            }
        }
    }

    /**
     * Deletes the PDF
     */
    private void deletePDF() throws ServiceException {
        try {
            Files.deleteIfExists(Paths.get(outputFilePath));
        } catch (IOException e) {
            LOGGER.error("An I/O error occurred while trying to delete the PDF", e);
            throw new ServiceException("An I/O error occurred while trying to delete the PDF", e);
        }
    }
}
