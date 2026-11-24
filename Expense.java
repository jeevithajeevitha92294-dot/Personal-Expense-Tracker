class Expense {
    String type;
    String description;
    String category;
    String date;
    double amount;

    Expense(String type, String description,
            String category, double amount, String date) {

        this.type = type;
        this.description = description;
        this.category = category;
        this.amount = amount;
        this.date = date;
    }
}