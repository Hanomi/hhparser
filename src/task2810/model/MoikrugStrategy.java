package task2810.model;

import task2810.vo.Vacancy;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MoikrugStrategy implements Strategy {
    private static final String URL_FORMAT = "https://moikrug.ru/vacancies?q=java+%s&page=%d";

    @Override
    public List<Vacancy> getVacancies(String searchString) {
        List<Vacancy> vacancyList = new ArrayList<>();
        try {
            Document doc;
            int page = 0;
            while (true) {
                doc = getDocument(searchString, page++);
                Elements elements = doc.getElementsByClass("job");
                if (elements.isEmpty())  break; // если вакансий нет на странице - выходим

                for (Element currentElement : elements) {
                    if (currentElement == null) break; // нумерация с 1, а цикл с 0... доп. проверка
                    // заголовок
                    Element titleElem = currentElement.getElementsByAttributeValue("class", "title").first();
                    String title = titleElem.text();

                    // зарплата, есть не увсех вакансий
                    Element salaryElem = currentElement.getElementsByAttributeValue("class", "salary").first();
                    String salary = "";
                    if (salaryElem != null) {
                        salary = salaryElem.text();
                    }

                    // город, актуально, если нет в фильтре
                    Element cityElem = currentElement.getElementsByAttributeValue("class", "location").first();
                    String city = "";
                    if (cityElem != null) {
                        city = cityElem.text();
                    }

                    // компания
                    Element companyNameElem = currentElement.getElementsByAttributeValue("class", "company_name").first();
                    String companyName = companyNameElem.text();

                    // сайт
                    String siteName = "https://moikrug.ru";

                    // url
                    String url = "https://moikrug.ru" + currentElement.select("a[class=job_icon]").attr("href");

                    Vacancy vacancy = new Vacancy();
                    vacancy.setTitle(title);
                    vacancy.setSalary(salary);
                    vacancy.setCity(city);
                    vacancy.setCompanyName(companyName);
                    vacancy.setSiteName(siteName);
                    vacancy.setUrl(url);

                    // добовляем в список
                    vacancyList.add(vacancy);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return vacancyList;
    }

    protected Document getDocument(String searchString, int page) throws IOException {
        String url = String.format(URL_FORMAT, searchString, page);
        Connection connection = Jsoup.connect(url);
        connection.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
        connection.referrer("none");
        return connection.get();
    }
}

