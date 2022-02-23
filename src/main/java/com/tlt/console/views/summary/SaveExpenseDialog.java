package com.tlt.console.views.summary;

import com.tlt.console.entity.ClientExpenseEntity;
import com.tlt.console.service.BookingService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;

import java.util.List;

public class SaveExpenseDialog extends Dialog {

    private ComboBox<String> mServiceTypeCombobox;

    private NumberField mCashInField;

    private NumberField mCashOutField;

    private TextArea mDescriptionText;

    private Button mSaveExpenseButton;

    private Button mCancelButton;

    private BookingService mBookingService;

    private ExpenseDialog mExpenseDialog;

    public SaveExpenseDialog(BookingService pBookingService, ExpenseDialog pExpenseDialog) {
        this.open();
        mBookingService = pBookingService;
        mExpenseDialog = pExpenseDialog;
        createControls();
    }

    private void createControls() {
        try {
            this.setWidth("30%");
            this.setHeight("80%");

            mServiceTypeCombobox = new ComboBox<>("Service Type");
            mServiceTypeCombobox.setAllowCustomValue(true);
            List<String> serviceList = mBookingService.getServiceList();
            ComboBox.ItemFilter<String> filter = (stringInBox, filterString) -> stringInBox.toLowerCase().startsWith(filterString.toLowerCase());
            mServiceTypeCombobox.setItems(filter, serviceList);
            mServiceTypeCombobox.addCustomValueSetListener(e -> {
                String customValue = e.getDetail();
                if (serviceList.contains(customValue)) return;
                serviceList.add(customValue);
                mServiceTypeCombobox.setItems(serviceList);
                mServiceTypeCombobox.setValue(customValue);
            });

            mCashInField = new NumberField();
            mCashInField.setLabel("Cash In");
            Div cashInPrefix = new Div();
            cashInPrefix.setText("Rs.");
            mCashInField.setPrefixComponent(cashInPrefix);

            mCashOutField = new NumberField();
            mCashOutField.setLabel("Cash Out");
            Div cashOutPrefix = new Div();
            cashOutPrefix.setText("Rs.");
            mCashOutField.setPrefixComponent(cashOutPrefix);

            mDescriptionText = new TextArea("Description");
            mDescriptionText.setWidthFull();
            mDescriptionText.setHeight("50%");

            FormLayout formLayout = new FormLayout();
            formLayout.add(mServiceTypeCombobox, mCashInField, mCashOutField);
            formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));

            mSaveExpenseButton = new Button("Save");
            mSaveExpenseButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            mSaveExpenseButton.addClickListener(clickEvent -> {
                try {
                    saveExpense();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            mCancelButton = new Button("Cancel");
            mCancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
            mCancelButton.addClickListener(clickEvent -> {
                this.removeAll();
                this.close();
            });

            FormLayout buttonLayout = new FormLayout();
            buttonLayout.add(mSaveExpenseButton, mCancelButton);
            buttonLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 2));

            this.setCloseOnOutsideClick(true);
            this.setResizable(true);
            this.setDraggable(true);
            add(formLayout);
            add(mDescriptionText);
            add(buttonLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveExpense() throws Exception {
        this.close();
        ClientExpenseEntity expenseEntity = new ClientExpenseEntity();
        expenseEntity.setCashIn(mCashInField.getValue());
        expenseEntity.setCashOut(mCashOutField.getValue());
        expenseEntity.setDescription(mDescriptionText.getValue());

        mExpenseDialog.saveExpenseData(expenseEntity, mServiceTypeCombobox.getValue());
    }

}
