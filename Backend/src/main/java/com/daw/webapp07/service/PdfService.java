package com.daw.webapp07.service;

import com.daw.webapp07.model.Inversion;
import com.daw.webapp07.model.Project;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfService{

    public void generatePdf(Project project, HttpServletResponse response,long id) throws IOException {
        List<Inversion> inversions = project.getInversions();
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + project.getName() + "-estadisticas.pdf");

        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph(project.getName())
                .setTextAlignment(TextAlignment.CENTER)
                .setBold());

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = currentDate.format(formatter);
        document.add(new Paragraph(formattedDate)
                .setTextAlignment(TextAlignment.RIGHT));

        document.add(new Paragraph()
                .add(new Text("Category: ").setBold())
                .add(project.getCategory().toString()));

        document.add(new Paragraph()
                .add(new Text("Owner: ").setBold())
                .add(project.getOwner().getName()));

        document.add(new Paragraph()
                .add(new Text("Project date: ").setBold())
                .add(project.getDate().format(formatter)));

        document.add(new Paragraph()
                .add(new Text("Project url: ").setBold())
                .add(project.getUrl()));

        document.add(new Paragraph()
                .add(new Text("Description: ").setBold())
                .add(project.getDescription()));

        document.add(new Paragraph()
                .add(new Text("Goal: ").setBold())
                .add(project.getGoal() + "€"));

        document.add(new Paragraph()
                .add(new Text("Raised money: ").setBold())
                .add(project.getCurrentAmount() + "€"));

        document.add(new Paragraph("Inversions: ")
                .setTextAlignment(TextAlignment.CENTER)
                .setBold());

        if (inversions.isEmpty())
            document.add(new Paragraph()
                    .add(new Text("No inversions")));
        else {
            for (int i = 0; i < inversions.size(); i++) {
                Inversion inversion = inversions.get(i);
                document.add(new Paragraph()
                        .add(new Text("Investor: ").setBold())
                        .add(inversion.getUser().getName()));
                document.add(new Paragraph()
                        .add(new Text("Amount: ").setBold())
                        .add(String.valueOf(inversion.getAmount()))
                        .add(" €"));
                document.add(new Paragraph()
                        .add(new Text("Date: ").setBold())
                        .add(inversion.getDate().format(formatter)));
                document.add(new Paragraph()
                        .add(new Text("----------------------------------------")));
            }
        }

        document.close();
    }
}