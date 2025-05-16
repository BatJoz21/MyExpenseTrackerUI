package joz.javapractice.myexpensetrackerui.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import joz.javapractice.myexpensetrackerui.models.Expense;
import joz.javapractice.myexpensetrackerui.utils.LocalDateTypeAdapter;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;

public class ExpenseDataParser {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter()).create();

    public static List<Expense> parseExpenseList(String jsonResponse){
        Type expenseListType = new TypeToken<List<Expense>>(){}.getType();

        return gson.fromJson(jsonResponse, expenseListType);
    }
}
