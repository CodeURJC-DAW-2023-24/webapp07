package com.daw.webapp07.service;

import com.daw.webapp07.model.Inversion;
import com.daw.webapp07.model.Project;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class GraphicsService {
    private String snames;
    private String squantities;
    private String stimes;
    private String spastmoney;


    public void initializeWith(Project project) {
        HashMap<String,Integer> donors = new HashMap<>();
        int total = 0;
        for(Inversion i: project.getInversions()){
            total+=i.getAmount();
            if(donors.containsKey(i.getUser().getName())){
                donors.put(i.getUser().getName(),donors.get(i.getUser().getName())+i.getAmount());
            }else{
                donors.put(i.getUser().getName(),i.getAmount());
            }
        }

        List<String> names = new ArrayList<>(donors.keySet());
        List<Integer> quantities = new ArrayList<>(donors.values());
        names.sort((a,b)->donors.get(b).compareTo(donors.get(a)));
        quantities.sort((a,b)->b.compareTo(a));

        List<String> times = new ArrayList<>();
        List<Integer> pastmoney = new ArrayList<>();
        Calendar timeNow = Calendar.getInstance();
        Calendar oldest = Calendar.getInstance();
        oldest.set(project.getDate().getYear(), project.getDate().getMonthValue() -1, 1);
        oldest.add(Calendar.YEAR,1);
        boolean moreThanAYear = oldest.before(timeNow);
        oldest.add(Calendar.YEAR, -1);
        HashMap<String, Integer> where = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
        List<String> months = new ArrayList<>(Arrays.asList("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"));
        int i = 0;
        while (!oldest.after(timeNow)){
            System.out.println("Mesasd: "+oldest.get(Calendar.YEAR));
            if(!moreThanAYear) {
                times.add(months.get(oldest.get(Calendar.MONTH)));
            }
            else {
                times.add(months.get(oldest.get(Calendar.MONTH)) + " " + oldest.get(Calendar.YEAR));
            }
            System.out.println(times);
            where.put(sdf.format(oldest.getTime()),i);
            oldest.add(Calendar.MONTH, 1);
            i++;
        }
        for(int j = 0; j < times.size(); j++){
            pastmoney.add(0);
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/yyyy", Locale.ENGLISH);
        for(Inversion i2: project.getInversions()){
            int index = where.get(i2.getDate().format(dtf));
            pastmoney.set(index,pastmoney.get(index)+i2.getAmount());
        }
        System.out.println(pastmoney);
        for(int k = 1; k < pastmoney.size(); k++){
            pastmoney.set(k,pastmoney.get(k)+pastmoney.get(k-1));
            System.out.println(pastmoney);
        }

        snames = array_to_string_jsarray(names);
        squantities = array_to_int_jsarray(quantities);
        stimes = array_to_string_jsarray(times);
        spastmoney = array_to_int_jsarray(pastmoney);
    }


    private String array_to_string_jsarray(List<String> list){
        String ar = "[";
        for(String s: list){
            ar += "'"+s+"',";
        }
        if (ar.length() > 1) {ar = ar.substring(0,ar.length()-1);}
        ar += "]";
        System.out.println(ar);
        return ar;
    }

    private String array_to_int_jsarray(List<Integer> list){
        String ar = "[";
        for(Integer i: list){
            ar += i+",";
        }
        if (ar.length() > 1) {ar = ar.substring(0,ar.length()-1);}
        ar += "]";
        System.out.println(ar);
        return ar;
    }

    public String getNames() {
        return snames;
    }

    public String getQuantities() {
        return squantities;
    }

    public String getTimes() {
        return stimes;
    }

    public String getPastmoney() {
        return spastmoney;
    }


}
