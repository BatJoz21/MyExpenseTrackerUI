package joz.javapractice.myexpensetrackerui.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;
import joz.javapractice.myexpensetrackerui.models.Expense;
import joz.javapractice.myexpensetrackerui.service.ExpenseDataParser;
import joz.javapractice.myexpensetrackerui.utils.HttpClientUtil;
import joz.javapractice.myexpensetrackerui.utils.JwtStorageUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticController {
    @FXML
    public MFXComboBox<String> monthPicker;
    @FXML
    public MFXComboBox<Integer> yearPicker;
    @FXML
    public PieChart expensePieChart;
    @FXML
    public MFXButton backButton;

    @FXML
    public void initialize(){
        monthPicker.getItems().addAll("January", "February", "March", "April", "May", "June", "July",
                "August", "September", "October", "November", "December");

        Platform.runLater(
                () -> {
                    int currentMonth = LocalDate.now().getMonthValue();
                    monthPicker.setValue(monthPicker.getItems().get(currentMonth - 1));
                    int currentYear = LocalDate.now().getYear();
                    for (int year = 1950; year <= currentYear; year++){
                        yearPicker.getItems().add(year);
                    }
                    yearPicker.setValue(currentYear);

                    loadPieChartData();
                }
        );

        monthPicker.valueProperty().addListener(
                (observable, oldValue, newValue) -> {
                    loadPieChartData();
                }
        );
        yearPicker.valueProperty().addListener(
                (observable, oldvalue, newValue) -> {
                    loadPieChartData();
                }
        );
    }

    @FXML
    public void handleBack(ActionEvent actionEvent) {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

    private void loadPieChartData(){
        expensePieChart.getData().clear();
        String date = getYearAndMonth(yearPicker.getValue(), monthPicker.getValue());
        String path = "/expenses/categories/month?month=" + date;
        String token = JwtStorageUtil.getToken();
        if (token == null || token.isEmpty()){
            System.out.println("Token is not found, user is not authenticated");
            return;
        }

        try {
            String response = HttpClientUtil.sendGetRequestWithToken(path, token);
            List<Expense> expenses = ExpenseDataParser.parseExpenseList(response);
            Map<String, Integer> categoriesMap = new HashMap<>();

            for (Expense expense : expenses){
                String category = expense.getCategory();
                categoriesMap.put(category, categoriesMap.getOrDefault(category, 0) + 1);
            }

            if (categoriesMap != null && !categoriesMap.isEmpty()){
                for (Map.Entry<String, Integer> entry : categoriesMap.entrySet()){
                    PieChart.Data pieChartData = new PieChart.Data(entry.getKey(), entry.getValue());

                    expensePieChart.getData().add(pieChartData);
                }
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String getYearAndMonth(int year, String month){
        String monthValue = null;
        switch (month){
            case "January":
                monthValue = "01";
                break;
            case "February":
                monthValue = "02";
                break;
            case "March":
                monthValue = "03";
                break;
            case "April":
                monthValue = "04";
                break;
            case "May":
                monthValue = "05";
                break;
            case "June":
                monthValue = "06";
                break;
            case "July":
                monthValue = "07";
                break;
            case "August":
                monthValue = "08";
                break;
            case "September":
                monthValue = "09";
                break;
            case "October":
                monthValue = "10";
                break;
            case "November":
                monthValue = "11";
                break;
            case "December":
                monthValue = "12";
                break;
            default:
                return "Month input is invalid";
        }

        return year + "-" + monthValue;
    }
}
