package com.at.ac.tuwien.sepm.ss15.edulium;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            OutputStream file = new FileOutputStream(new File("C:\\Test.pdf"));
            Document document = new Document();
            PdfWriter.getInstance(document, file);

            document.open();
            document.add(new Paragraph("Hello World, iText"));
            document.add(new Paragraph(new Date().toString()));
            document.close();

            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        primaryStage.setWidth(300);
        primaryStage.setHeight(300);
        primaryStage.setTitle("Edulium");
        primaryStage.show();
    }
}
