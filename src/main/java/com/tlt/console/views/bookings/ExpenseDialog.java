package com.tlt.console.views.bookings;


import com.tlt.console.data.ExpenseData;
import com.tlt.console.data.SummaryData;
import com.tlt.console.entity.ClientExpenseEntity;
import com.tlt.console.service.BookingService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.Date;
import java.util.List;

public class ExpenseDialog extends Dialog {

    private BookingService mBookingService;

    private Grid<ExpenseData> mExpenseGrid;

    private Button mSaveButton;

    private Button mCloseButton;

    private SummaryData mSummaryData;

    private SummaryView mSummaryView;

    public ExpenseDialog(BookingService pBookingService, SummaryData pSummaryData, SummaryView pSummaryView) {
        mBookingService = pBookingService;
        mSummaryData = pSummaryData;
        mSummaryView = pSummaryView;
        this.open();
        createControls();
    }

    private void createControls() {
        try {
            this.setWidth("70%");
            this.setHeight("90%");
            mExpenseGrid = new Grid<>();
            mExpenseGrid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_ROW_STRIPES);
            mExpenseGrid.setSizeUndefined();
            mExpenseGrid.setHeight("90%");

            mSaveButton = new Button("Add Expense");
            mSaveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            mSaveButton.addClickListener(clickEvent -> {
                SaveExpenseDialog saveExpenseDialog = new SaveExpenseDialog(mBookingService, this);
            });

            mCloseButton = new Button(new Icon(VaadinIcon.CLOSE_SMALL));
            mCloseButton.addThemeVariants(ButtonVariant.LUMO_ICON);
            mCloseButton.getElement().setAttribute("aria-label", "Close");
            mCloseButton.addClickListener(clickEvent -> {
                this.removeAll();
                this.close();
            });

            setValueToGrid();

            Span title = new Span("Expense Summary");
            title.getStyle().set("font-weight", "bold");
            HorizontalLayout headerLayout = new HorizontalLayout(title, mCloseButton);
            headerLayout.setAlignItems(FlexComponent.Alignment.BASELINE);
            headerLayout.setFlexGrow(1, title);
            headerLayout.setWidthFull();

            HorizontalLayout footerLayout = new HorizontalLayout(mSaveButton);
            footerLayout.setAlignItems(FlexComponent.Alignment.BASELINE);
            footerLayout.setWidthFull();
            //footerLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

            this.setCloseOnOutsideClick(true);
            this.setResizable(true);
            this.setDraggable(true);
            add(headerLayout, mExpenseGrid, footerLayout);

            this.addResizeListener(resizeEvent -> {
                String newDialogHeight = this.getHeight();
                newDialogHeight = newDialogHeight.replace("px", "");

                mExpenseGrid.setHeight(String.valueOf(Double.valueOf(newDialogHeight) - 120) + "px");
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setValueToGrid() {

        try {
            mExpenseGrid.removeAllColumns();
            List<ExpenseData> expenseEntityList = mBookingService.getExpensesOfClient(mSummaryData.getClientId());

            mExpenseGrid.setItems(expenseEntityList);

            mExpenseGrid.addColumn(ExpenseData::getService).setHeader("Service Type").setResizable(true);
            mExpenseGrid.addColumn(ExpenseData::getUpdateDate).setHeader("Date").setResizable(true);
            mExpenseGrid.addColumn(ExpenseData::getCashIn).setHeader("Cash In").setResizable(true);
            mExpenseGrid.addColumn(ExpenseData::getCashOut).setHeader("Cash Out").setResizable(true);
            mExpenseGrid.addColumn(ExpenseData::getDescription).setHeader("Description").setResizable(true);
            // mExpenseGrid.addColumn(ExpenseData::getUpdateUser).setHeader("User").setResizable(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveExpenseData(ClientExpenseEntity expenseEntity, String serviceType) throws Exception {
        expenseEntity.setClientId(mSummaryData.getClientId());
        expenseEntity.setUpdateDate(new Date());
        expenseEntity.setUpdateUser("1");
        mBookingService.saveExpenseData(expenseEntity, serviceType);
        setValueToGrid();
    }

}
