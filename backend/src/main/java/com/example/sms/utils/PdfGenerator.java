package com.example.sms.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

@Component
public class PdfGenerator {
    public ByteArrayOutputStream generateMarksheet(String studentName, String rollNumber, List<Map<String, Object>> marks) throws DocumentException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, out);
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("Marksheet", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph("Student: " + studentName));
        document.add(new Paragraph("Roll Number: " + rollNumber));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.addCell("Subject");
        table.addCell("Assessment");
        table.addCell("Practical");
        table.addCell("Semester");

        for (Map<String, Object> record : marks) {
            table.addCell((String) record.get("subject"));
            table.addCell(record.get("assessment").toString());
            table.addCell(record.get("practical").toString());
            table.addCell(record.get("semester").toString());
        }

        document.add(table);
        document.close();
        return out;
    }
}
