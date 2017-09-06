package ua.cn.sandi.jsonview;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

/**
 * Created by mikni on 24.08.2017.
 */

class WebService {

    public String inputstr;

    private String Str = inputstr;

    int index;

    int idt;

    String toint;

    Orders orders = new Orders();

    public String getorders() {
        return orders.getorders();
    }
    public List<Integer> getordersids() {
        return orders.getordersids();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void parseStr(String Str) {
        Boolean pars_completed = false;

        while (Str.contains("orders")) {
            while (Str.contains("id")) {
                index = Str.indexOf("id");
                Str = Str.substring(index + 4);
                toint = Str.substring(0, Str.indexOf("}"));
                idt = parseInt(toint);

                orders.addorder(idt);
            }
            pars_completed = true;
        }
        while (Str.contains("order") && pars_completed == false) {
            while (Str.contains(":")) {
            // parse order

            }
        }
    }

        class Orders {

            List<Integer> id = new ArrayList<Integer>();

            public void addorder(int i) {
                id.add(i);
            }
            public void addorders(List<Integer> i) {
                this.id = i;
            }
            public String getorders(){
                return id.toString();
            }
            public List<Integer> getordersids(){
                return id;
            }
        }
}


