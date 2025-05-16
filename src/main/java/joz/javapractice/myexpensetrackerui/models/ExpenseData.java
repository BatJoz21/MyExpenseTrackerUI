package joz.javapractice.myexpensetrackerui.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ExpenseData {
    private ObservableList<Expense> expenseList;

    public ExpenseData() {
        expenseList = FXCollections.observableArrayList();
    }

    public ObservableList<Expense> getExpenseList() {
        return expenseList;
    }

    public void addExpense(Expense expense){
        expenseList.add(expense);
    }
}
