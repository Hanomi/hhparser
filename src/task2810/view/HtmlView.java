package task2810.view;

import task2810.Controller;
import task2810.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class HtmlView implements View {
    private Controller controller;
    private final String filePath = "./4.JavaCollections/src/" + this.getClass().getPackage().getName().replace('.', '/') + "/vacancies.html";

    @Override
    public void update(List<Vacancy> vacancies) {
        File vacFile = new File(filePath);
        String data = getUpdatedFileContent(vacancies);
        updateFile(data);
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void userCitySelectEmulationMethod() {
        controller.onCitySelect("Новосибирск");
    }

    private String getUpdatedFileContent(List<Vacancy> vacancies) {
        Document document;
        try {
            document = getDocument();
            Element templateOriginal = document.getElementsByClass("template").first();
            Element copyTemplate = templateOriginal.clone();
            copyTemplate.removeAttr("style");
            copyTemplate.removeClass("template");
            document.select("tr[class=vacancy]").remove().not("tr[class=vacancy template");
            for (Vacancy vacancy : vacancies) {
                Element vacanvyTemplate = copyTemplate.clone();
                vacanvyTemplate.getElementsByClass("city").first().text(vacancy.getCity());
                vacanvyTemplate.getElementsByClass("companyName").first().text(vacancy.getCompanyName());
                vacanvyTemplate.getElementsByClass("salary").first().text(vacancy.getSalary());
                Element link = vacanvyTemplate.getElementsByTag("a").first();
                link.text(vacancy.getTitle());
                link.attr("href", vacancy.getUrl());

                templateOriginal.before(vacanvyTemplate.outerHtml());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Some exception occurred";
        }
        return document.html();
    }

    private void updateFile(String string) {
        try (FileWriter writer = new FileWriter(new File(filePath))) {
            writer.write(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected Document getDocument() throws IOException {
        return Jsoup.parse(new File(filePath), "UTF-8");
    }
}
