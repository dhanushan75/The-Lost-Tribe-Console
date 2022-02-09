package com.tlt.console.views.bookings;


import com.tlt.console.data.ExpenseData;
import com.tlt.console.data.SummaryData;
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

import java.util.List;

public class ExpenseDialog extends Dialog {

    private BookingService mBookingService;

    private Grid<ExpenseData> mExpenseGrid;

    private Button mSaveButton;

    private Button mCloseButton;

    private SummaryData mSummaryData;

    private HorizontalLayout headerLayout;

    private HorizontalLayout footerLayout;

    public ExpenseDialog(BookingService pBookingService, SummaryData pSummaryData) {
        mBookingService = pBookingService;
        mSummaryData = pSummaryData;
        this.open();
        createControls();
    }

    private void createControls() {
        this.setWidth("50%");
        this.setHeight("70%");
        mExpenseGrid = new Grid<>();
        mExpenseGrid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        mExpenseGrid.setSizeUndefined();

        mSaveButton = new Button("Add Expense");
        mSaveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

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
        headerLayout = new HorizontalLayout(title, mCloseButton);
        headerLayout.setAlignItems(FlexComponent.Alignment.BASELINE);
        headerLayout.setFlexGrow(1, title);
        headerLayout.setWidthFull();

        footerLayout = new HorizontalLayout(mSaveButton);
        footerLayout.setAlignItems(FlexComponent.Alignment.BASELINE);
        footerLayout.setWidthFull();
        //footerLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        this.setCloseOnOutsideClick(true);
        this.setResizable(true);
        this.setDraggable(true);
        add(headerLayout, mExpenseGrid, footerLayout);

        this.addResizeListener(resizeEvent -> {
            String gridHeight = this.getHeight();
            gridHeight = gridHeight.replace("px", "");

            mExpenseGrid.setHeight(String.valueOf(Double.valueOf(gridHeight) - 120) + "px");
        });
    }

    private void setValueToGrid() {

        try {
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

}
