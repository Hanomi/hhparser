package task2810;

import task2810.model.HHStrategy;
import task2810.model.Model;
import task2810.model.MoikrugStrategy;
import task2810.model.Provider;
import task2810.view.HtmlView;

public class Aggregator {
    public static void main(String[] args) {
        Provider provider = new Provider(new HHStrategy());
        Provider mkProvider = new Provider(new MoikrugStrategy());
        HtmlView view = new HtmlView();
        Model model = new Model(view, provider, mkProvider);
        Controller controller = new Controller(model);

        view.setController(controller);

        view.userCitySelectEmulationMethod();


    }
}
