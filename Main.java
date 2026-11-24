import java.awt.*;
import java.awt.event.*;

public class Main extends Frame implements ActionListener {

    ExpenseManager manager = new ExpenseManager();

    TextField desc = new TextField();
    TextField amount = new TextField();
    TextField date = new TextField();
    TextField budget = new TextField();

    Choice category = new Choice();
    java.awt.List records = new java.awt.List();

    Label income = new Label();
    Label expense = new Label();
    Label balance = new Label();
    Label budgetStatus = new Label();

    Button addIncome = new Button("Add Income");
    Button addExpense = new Button("Add Expense");
    Button delete = new Button("Delete");
    Button setBudget = new Button("Set Budget");
    Button chartButton = new Button("View Expense Chart");

    double monthlyBudget = 0;

    Main() {
        setTitle("Personal Expense Tracker");
        setSize(700, 550);
        setLayout(null);
        setBackground(new Color(235,242,250));

        Label title = new Label("PERSONAL EXPENSE TRACKER");
        title.setBounds(200, 40, 300, 30);
        add(title);

        Label l1 = new Label("Description:");
        l1.setBounds(40, 100, 100, 25);
        add(l1);

        desc.setBounds(150, 100, 180, 25);
        add(desc);

        Label l2 = new Label("Amount:");
        l2.setBounds(40, 140, 100, 25);
        add(l2);

        amount.setBounds(150, 140, 180, 25);
        add(amount);

        Label l3 = new Label("Category:");
        l3.setBounds(40, 180, 100, 25);
        add(l3);

        category.add("Food");
        category.add("Travel");
        category.add("Education");
        category.add("Shopping");
        category.add("Bills");
        category.add("Health");
        category.add("Other");

        category.setBounds(150, 180, 180, 25);
        add(category);

        Label l4 = new Label("Date:");
        l4.setBounds(40, 220, 100, 25);
        add(l4);

        date.setBounds(150, 220, 180, 25);
        add(date);

        addIncome.setBounds(40, 265, 130, 30);
        addExpense.setBounds(180, 265, 130, 30);

        add(addIncome);
        add(addExpense);

        addIncome.addActionListener(this);
        addExpense.addActionListener(this);

        records.setBounds(350, 100, 300, 230);
        add(records);

        delete.setBounds(430, 340, 130, 30);
        add(delete);
        delete.addActionListener(this);

        Label b = new Label("Monthly Budget:");
        b.setBounds(40, 350, 110, 25);
        add(b);

        budget.setBounds(150, 350, 150, 25);
        add(budget);

        setBudget.setBounds(40, 385, 100, 30);
        add(setBudget);
        setBudget.addActionListener(this);

        budgetStatus.setBounds(150, 385, 300, 30);
        add(budgetStatus);

        income.setBounds(40, 440, 180, 25);
        expense.setBounds(240, 440, 180, 25);
        balance.setBounds(440, 440, 180, 25);

        add(income);
        add(expense);
        add(balance);

        manager.load();
        refresh();

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });
title.setFont(new Font("Arial", Font.BOLD, 22));
title.setForeground(new Color(20, 50, 90));

addIncome.setBackground(new Color(30, 160, 90));
addIncome.setForeground(Color.white);

addExpense.setBackground(new Color(220, 70, 70));
addExpense.setForeground(Color.white);

delete.setBackground(new Color(40, 90, 160));
delete.setForeground(Color.white);

setBudget.setBackground(new Color(30, 110, 170));
setBudget.setForeground(Color.white);

income.setFont(new Font("Arial", Font.BOLD, 14));
expense.setFont(new Font("Arial", Font.BOLD, 14));
balance.setFont(new Font("Arial", Font.BOLD, 14));

income.setForeground(new Color(20, 140, 70));
expense.setForeground(Color.red);
balance.setForeground(new Color(20, 70, 160));
chartButton.setBounds(500, 385, 150, 30);
chartButton.setBackground(new Color(100, 70, 180));
chartButton.setForeground(Color.white);

add(chartButton);
chartButton.addActionListener(this);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == addIncome ||
                e.getSource() == addExpense) {

                String d = desc.getText().trim();
                String dt = date.getText().trim();
                double a = Double.parseDouble(amount.getText());

                if (d.isEmpty() || dt.isEmpty() || a <= 0)
                    throw new Exception();

                String type = e.getSource() == addIncome
                    ? "Income" : "Expense";

                String cat = type.equals("Income")
                    ? "Income" : category.getSelectedItem();

                manager.add(new Expense(type, d, cat, a, dt));

                desc.setText("");
                amount.setText("");
                date.setText("");

                refresh();
            }

            else if (e.getSource() == delete) {
                int i = records.getSelectedIndex();

                if (i >= 0) {
                    manager.delete(i);
                    refresh();
                }
            }

            else if (e.getSource() == setBudget) {
                monthlyBudget =
                    Double.parseDouble(budget.getText());

                if (monthlyBudget <= 0)
                    throw new Exception();

                refresh();
            }
            else if (e.getSource() == chartButton) {
    new ExpenseChart(manager);
}

        } catch (Exception ex) {
            new DialogMessage(this);
        }
    }

    void refresh() {
        records.removeAll();

        for (Expense e : manager.expenses) {
            records.add(e.type + " | " + e.description
                + " | Rs." + e.amount + " | " + e.date);
        }

        double in = manager.total("Income");
        double out = manager.total("Expense");

        income.setText("Income: Rs." + in);
        expense.setText("Expense: Rs." + out);
        balance.setText("Balance: Rs." + (in - out));

        budgetStatus.setText("Remaining Budget: Rs."
            + (monthlyBudget - out));
    }

    public static void main(String[] args) {
        new Main();
    }
}

class DialogMessage {
    DialogMessage(Frame f) {
        Dialog d = new Dialog(f, "Error", true);
        d.setLayout(new FlowLayout());

        d.add(new Label("Enter valid details!"));

        Button b = new Button("OK");
        b.addActionListener(e -> d.dispose());

        d.add(b);
        d.setSize(250, 100);
        d.setLocationRelativeTo(f);
        d.setVisible(true);
    }
}
class ExpenseChart extends Frame {

    ExpenseChart(ExpenseManager manager) {
        setTitle("Expense Pie Chart");
        setSize(500, 400);
        setLayout(new BorderLayout());

        add(new ChartPanel(manager), BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }
}

class ChartPanel extends Canvas {

    ExpenseManager manager;

    Color[] colors = {
        Color.red, Color.blue, Color.green,
        Color.orange, Color.magenta, Color.cyan,
        Color.gray
    };

    ChartPanel(ExpenseManager manager) {
        this.manager = manager;
    }

    public void paint(Graphics g) {

        String[] names = {
            "Food", "Travel", "Education",
            "Shopping", "Bills", "Health", "Other"
        };

        double[] values = new double[names.length];
        double total = 0;

        for (Expense e : manager.expenses) {
            if (e.type.equals("Expense")) {
                for (int i = 0; i < names.length; i++) {
                    if (e.category.equals(names[i])) {
                        values[i] += e.amount;
                        total += e.amount;
                    }
                }
            }
        }

        if (total == 0) {
            g.drawString("No expense records available", 120, 150);
            return;
        }

        int start = 0;

        for (int i = 0; i < values.length; i++) {
            if (values[i] > 0) {
                int angle = (int)(values[i] * 360 / total);

                g.setColor(colors[i]);
                g.fillArc(40, 50, 220, 220, start, angle);

                start += angle;
            }
        }

        for (int i = 0; i < names.length; i++) {
            g.setColor(colors[i]);
            g.fillRect(300, 65 + i * 35, 15, 15);

            g.setColor(Color.black);
            g.drawString(names[i] + " : Rs." + values[i],
                325, 78 + i * 35);
        }

        g.setColor(Color.black);
        g.drawString("Total Expense: Rs." + total, 150, 310);
    }
}