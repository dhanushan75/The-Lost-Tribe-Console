package com.tlt.console.views.summary;

import com.tlt.console.data.SummaryData;
import com.tlt.console.service.BookingService;
import com.tlt.console.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@PageTitle("Summary")
@Route(value = "Summary", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class SummaryView extends VerticalLayout {

    private BookingService mBookingService;

    private Grid<SummaryData> mGrid;

    private Button mMenuButton;

    private Button mExpenseWindowButton;

    private SummaryData mSummaryData;

    public SummaryView(@Autowired BookingService pBookingService) {
        mBookingService = pBookingService;
        createControls();
    }

    private void createControls() {
        this.setSizeFull();
        mGrid = new Grid<>();
        mGrid.setSizeFull();
        mGrid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        mMenuButton = new Button("Show/Hide Columns");
        mMenuButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        mExpenseWindowButton = new Button("Show Detailed Expense");
        mExpenseWindowButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        mExpenseWindowButton.setEnabled(false);

        setValueToGrid(null);

        mGrid.addSelectionListener(selection -> {
            Optional<SummaryData> optionalSummaryData = selection.getFirstSelectedItem();
            if (optionalSummaryData.isPresent()) {
                mExpenseWindowButton.setEnabled(true);
                mSummaryData = optionalSummaryData.get();
            }
        });

        mExpenseWindowButton.addClickListener(clickEvent -> {
            ExpenseDialog expenseDialog = new ExpenseDialog(mBookingService, mSummaryData, this);
        });

        Span title = new Span("Summary");
        title.getStyle().set("font-weight", "bold");
        HorizontalLayout headerLayout = new HorizontalLayout(title, mMenuButton);
        headerLayout.setAlignItems(FlexComponent.Alignment.BASELINE);
        headerLayout.setFlexGrow(1, title);
        headerLayout.setWidthFull();

        HorizontalLayout footerLayout = new HorizontalLayout(mExpenseWindowButton);
        footerLayout.setAlignItems(Alignment.START);
        footerLayout.setWidthFull();
        Footer footer = new Footer(footerLayout);

        add(headerLayout, mGrid, footer);
    }

    public void setValueToGrid(Date pFromDate) {

        try {
            mGrid.removeAllColumns();
            List<SummaryData> summaryDataList = mBookingService.getSummaryDataForGrid(pFromDate);

            mGrid.setItems(summaryDataList);

            mGrid.addColumn(SummaryData::getName).setHeader("Name").setResizable(true);
            mGrid.addColumn(SummaryData::getRoom).setHeader("Room").setResizable(true);
            mGrid.addColumn(SummaryData::getUnit).setHeader("Unit").setResizable(true);
            mGrid.addColumn(SummaryData::getCheckIn).setHeader("Check In").setResizable(true);
            mGrid.addColumn(SummaryData::getCheckout).setHeader("Check Out").setResizable(true);
            mGrid.addColumn(SummaryData::getTotalCashIn).setHeader("Total Cash In").setResizable(true);
            mGrid.addColumn(SummaryData::getTotalCashOut).setHeader("Total Cash Out").setResizable(true);

            Grid.Column<SummaryData> idColumn = mGrid.addColumn(SummaryData::getClientId).setHeader("Id").setResizable(true);
            Grid.Column<SummaryData> bookedDateColumn = mGrid.addColumn(SummaryData::getBookedDate).setHeader("Booked Date").setResizable(true);
            Grid.Column<SummaryData> idProofColumn = mGrid.addColumn(SummaryData::getIdProof).setHeader("Id Proof Type").setResizable(true);
            Grid.Column<SummaryData> idProofNumberColumn = mGrid.addColumn(SummaryData::getIdProofNumber).setHeader("Id Proof number").setResizable(true);

            ColumnToggleContextMenu columnToggleContextMenu = new ColumnToggleContextMenu(
                    mMenuButton);
            columnToggleContextMenu.addColumnToggleItem("Id", idColumn);
            columnToggleContextMenu.addColumnToggleItem("Booked Date", bookedDateColumn);
            columnToggleContextMenu.addColumnToggleItem("Id Proof", idProofColumn);
            columnToggleContextMenu.addColumnToggleItem("Id Proof number", idProofNumberColumn);

            idColumn.setVisible(false);
            bookedDateColumn.setVisible(false);
            idProofColumn.setVisible(false);
            idProofNumberColumn.setVisible(false);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static class ColumnToggleContextMenu extends ContextMenu {
        public ColumnToggleContextMenu(Component target) {
            super(target);
            setOpenOnClick(true);
        }

        void addColumnToggleItem(String label, Grid.Column<SummaryData> column) {
            MenuItem menuItem = this.addItem(label, e -> {
                column.setVisible(e.getSource().isChecked());
            });
            menuItem.setCheckable(true);
            //menuItem.setChecked(column.isVisible());
        }
    }

}
