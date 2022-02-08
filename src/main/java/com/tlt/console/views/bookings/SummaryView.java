package com.tlt.console.views.bookings;

import com.tlt.console.data.SummaryData;
import com.tlt.console.service.BookingService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class SummaryView extends VerticalLayout {

    private BookingService mBookingService;

    private Grid<SummaryData> mGrid;

    private Button mMenuButton;

    public SummaryView(BookingService pBookingService) {
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

        setValueToGrid(null);

        mGrid.addSelectionListener(selection -> {
            Optional<SummaryData> optionalSummaryData = selection.getFirstSelectedItem();
            if (optionalSummaryData.isPresent()) {
                // System.out.printf("Selected person: %s%n", optionalPerson.get().getFullName());
            }
        });

        Span title = new Span("Summary");
        title.getStyle().set("font-weight", "bold");
        HorizontalLayout headerLayout = new HorizontalLayout(title, mMenuButton);
        headerLayout.setAlignItems(FlexComponent.Alignment.BASELINE);
        headerLayout.setFlexGrow(1, title);
        headerLayout.setWidthFull();

        add(headerLayout, mGrid);
    }

    private void setValueToGrid(Date pFromDate) {

        try {
            List<SummaryData> summaryDataList = mBookingService.getSummaryDataForGrid(pFromDate);

            mGrid.setItems(summaryDataList);

            mGrid.addColumn(SummaryData::getName).setHeader("Name");
            mGrid.addColumn(SummaryData::getRoom).setHeader("Room");
            mGrid.addColumn(SummaryData::getUnit).setHeader("Unit");
            mGrid.addColumn(SummaryData::getCheckIn).setHeader("Check In");
            mGrid.addColumn(SummaryData::getCheckout).setHeader("Check Out");
            mGrid.addColumn(SummaryData::getTotalCashIn).setHeader("Total Cash In");
            mGrid.addColumn(SummaryData::getTotalCashOut).setHeader("Total Cash Out");

            Grid.Column<SummaryData> idColumn = mGrid.addColumn(SummaryData::getClientId).setHeader("Id");
            Grid.Column<SummaryData> bookedDateColumn = mGrid.addColumn(SummaryData::getBookedDate).setHeader("Booked Date");
            Grid.Column<SummaryData> idProofColumn = mGrid.addColumn(SummaryData::getIdProof).setHeader("Id Proof Type");
            Grid.Column<SummaryData> idProofNumberColumn = mGrid.addColumn(SummaryData::getIdProofNumber).setHeader("Id Proof number");

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
