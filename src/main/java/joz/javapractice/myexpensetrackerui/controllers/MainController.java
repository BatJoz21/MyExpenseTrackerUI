package joz.javapractice.myexpensetrackerui.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import joz.javapractice.myexpensetrackerui.models.Expense;
import joz.javapractice.myexpensetrackerui.models.ExpenseData;
import joz.javapractice.myexpensetrackerui.service.ExpenseDataParser;
import joz.javapractice.myexpensetrackerui.utils.HttpClientUtil;
import joz.javapractice.myexpensetrackerui.utils.JwtStorageUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class MainController {
    @FXML
    public MFXButton addExpenseButton;
    @FXML
    public DatePicker datePicker;
    @FXML
    public MFXButton viewMonthlyStatsButton;
    @FXML
    public MFXButton logoutButton;
    @FXML
    public TableView expenseTable;
    @FXML
    public TableColumn categoryColumn;
    @FXML
    public TableColumn descriptionColumn;
    @FXML
    public TableColumn amountColumn;
    @FXML
    public TableColumn dateColumn;
    @FXML
    public TableColumn editColumn;
    @FXML
    public TableColumn deleteColumn;

    private ExpenseData expenseData;

    @FXML
    public void initialize(){
        LocalDate currentDate = LocalDate.now();
        datePicker.setValue(currentDate);
        datePicker.getEditor().setDisable(true);
        datePicker.getEditor().setOpacity(1);

        expenseTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("note"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        addEditButtonToTable();
        addDeleteButtonToTable();

        expenseTable.getColumns().add(categoryColumn);
        expenseTable.getColumns().add(descriptionColumn);
        expenseTable.getColumns().add(amountColumn);
        expenseTable.getColumns().add(dateColumn);
        expenseTable.getColumns().add(editColumn);
        expenseTable.getColumns().add(deleteColumn);

        fetchExpensesByDate(currentDate.toString());

        datePicker.valueProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null){
                        fetchExpensesByDate(newValue.toString());
                    }
                }
        );
    }

    @FXML
    public void handleAddExpense(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/joz/javapractice/myexpensetrackerui/views/ExpenseScreen.fxml"));
            VBox expensePane = loader.load();

            ExpenseController expenseController = loader.getController();
            expenseController.setMainController(this);

            Scene expenseScene = new Scene(expensePane);
            expenseScene.getStylesheets().add(
                    getClass().getResource("/joz/javapractice/myexpensetrackerui/css/expense-screen.css")
                    .toExternalForm()
            );

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(expenseScene);
            stage.setTitle("Add Expense");
            stage.setWidth(600);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void handleViewMonthlyStats(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/joz/javapractice/myexpensetrackerui/views/StatisticScreen.fxml"));
            VBox loaderBox = loader.load();

            Scene statisticScene = new Scene(loaderBox);
            statisticScene.getStylesheets().add(
                    getClass().getResource("/joz/javapractice/myexpensetrackerui/css/statistic-screen.css")
                            .toExternalForm()
            );

            Stage stage = new Stage();
            stage.setTitle("Monthly Statistics");
            stage.setScene(statisticScene);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void handleLogout(ActionEvent actionEvent) {
        JwtStorageUtil.clearToken();

        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/joz/javapractice/myexpensetrackerui/views/LoginScreen.fxml"));
            Scene loginScene = new Scene(loader.load());
            loginScene.getStylesheets().add(getClass()
                    .getResource("/joz/javapractice/myexpensetrackerui/css/style.css").toExternalForm());

            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(loginScene);
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void addEditButtonToTable(){
        Callback<TableColumn<Expense, Void>, TableCell<Expense, Void>>
                cellfactory = param -> new TableCell<Expense, Void>(){
            private final MFXButton editButton = new MFXButton("Edit");
            {
                editButton.setOnAction(
                        event -> {
                            Expense expense = getTableView().getItems().get(getIndex());
                            System.out.println("Editing: " + expense.getNote());
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass()
                                        .getResource("/joz/javapractice/myexpensetrackerui/views/EditExpenseScreen.fxml"));
                                VBox editPane = loader.load();

                                EditExpenseController editController = loader.getController();
                                editController.setMainController(MainController.this);
                                editController.initEditMode(
                                        expense.getId(), String.valueOf(expense.getExpenseType()), expense.getDate(),
                                        expense.getAmount(), expense.getCategory(), expense.getAccount(), expense.getNote()
                                );

                                Scene editScene = new Scene(editPane);
                                editScene.getStylesheets().add(
                                        getClass().getResource("/joz/javapractice/myexpensetrackerui/css/expense-screen.css")
                                                .toExternalForm()
                                );

                                Stage stage = new Stage();
                                stage.initModality(Modality.APPLICATION_MODAL);
                                stage.setScene(editScene);
                                stage.setTitle("Edit Expense");
                                stage.setWidth(600);
                                stage.setResizable(false);
                                stage.showAndWait();
                            } catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                );
                editButton.getStyleClass().add("outlined-button");
            }

            @Override
            protected void updateItem(Void item, boolean empty){
                super.updateItem(item, empty);
                if (empty){
                    setGraphic(null);
                }
                else{
                    setGraphic(editButton);
                }
            }
        };
        editColumn.setCellFactory(cellfactory);
    }

    private void addDeleteButtonToTable(){
        Callback<TableColumn<Expense, Void>, TableCell<Expense, Void>>
                cellfactory = param -> new TableCell<Expense, Void>(){
            private final MFXButton deleteButton = new MFXButton("Delete");
            {
                deleteButton.setOnAction(
                        event -> {
                            Expense expense = getTableView().getItems().get(getIndex());
                            System.out.println("Deleting: " + expense.getNote());
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass()
                                        .getResource("/joz/javapractice/myexpensetrackerui/views/DeleteExpenseScreen.fxml"));
                                VBox deletePanel = loader.load();

                                DeleteExpenseController deleteController = loader.getController();
                                deleteController.setExpenseId(expense.getId());
                                deleteController.setMainController(MainController.this);

                                Scene deleteScene = new Scene(deletePanel);
                                deleteScene.getStylesheets().add(
                                        getClass().getResource("/joz/javapractice/myexpensetrackerui/css/expense-screen.css")
                                                .toExternalForm()
                                );

                                Stage stage = new Stage();
                                stage.initModality(Modality.APPLICATION_MODAL);
                                stage.setScene(deleteScene);
                                stage.setTitle("Delete Expense");
                                stage.setWidth(600);
                                stage.setResizable(false);
                                stage.showAndWait();
                            } catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                );
                deleteButton.getStyleClass().add("outlined-button");
            }

            @Override
            protected void updateItem(Void item, boolean empty){
                super.updateItem(item, empty);
                if (empty){
                    setGraphic(null);
                }
                else{
                    setGraphic(deleteButton);
                }
            }
        };
        deleteColumn.setCellFactory(cellfactory);
    }

    public void refreshExpense(){
        LocalDate selectedDate = datePicker.getValue();

        if (selectedDate != null){
            fetchExpensesByDate(selectedDate.toString());
        }
    }

    private void fetchExpensesByDate(String date){
        String formattedDate = date;
        String token = JwtStorageUtil.getToken();

        if (token == null || token.isEmpty()){
            System.out.println("Token is not found, user is not authenticated");
            return;
        }

        String path = "/expenses/day/" + formattedDate;
        try {
            String response = HttpClientUtil.sendGetRequestWithToken(path, token);
            List<Expense> expenses = ExpenseDataParser.parseExpenseList(response);

            expenseTable.getItems().clear();
            expenseTable.getItems().addAll(expenses);
        } catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
    }

    private void handleAuthenticationFailure(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Session expired");
        alert.setHeaderText(null);
        alert.setContentText("Your session has expired. Please log in again.");

        alert.setOnHidden(event -> redirectToLogin());
        alert.showAndWait();
    }

    private void redirectToLogin(){
        JwtStorageUtil.clearToken();

        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/joz/javapractice/myexpensetrackerui/views/LoginScreen.fxml"));
            Scene loginScene = new Scene(loader.load());
            loginScene.getStylesheets().add(getClass()
                    .getResource("/joz/javapractice/myexpensetrackerui/css/style.css").toExternalForm());

            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(loginScene);
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
