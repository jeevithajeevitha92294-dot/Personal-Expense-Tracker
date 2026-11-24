import java.util.*;
import java.io.*;

class ExpenseManager {
    ArrayList<Expense> expenses = new ArrayList<>();

    void add(Expense e) {
        expenses.add(e);
        save();
    }

    void delete(int index) {
        if (index >= 0 && index < expenses.size()) {
            expenses.remove(index);
            save();
        }
    }

    double total(String type) {
        double sum = 0;

        for (Expense e : expenses) {
            if (e.type.equals(type)) {
                sum += e.amount;
            }
        }
        return sum;
    }

    void save() {
        try {
            PrintWriter p = new PrintWriter("expenses.txt");

            for (Expense e : expenses) {
                p.println(e.type + "|" + e.description + "|"
                    + e.category + "|" + e.amount + "|" + e.date);
            }

            p.close();
        } catch (Exception e) {
            System.out.println("File Error");
        }
    }

    void load() {
        try {
            BufferedReader br = new BufferedReader(
                new FileReader("expenses.txt"));

            String line;

            while ((line = br.readLine()) != null) {
                String[] a = line.split("\\|", -1);

                if (a.length == 5) {
                    expenses.add(new Expense(
                        a[0], a[1], a[2],
                        Double.parseDouble(a[3]), a[4]));
                }
            }

            br.close();
        } catch (Exception e) {
        }
    }
}