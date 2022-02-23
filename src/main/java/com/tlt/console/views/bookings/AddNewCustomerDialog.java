package com.tlt.console.views.bookings;

import com.tlt.console.data.SummaryData;
import com.tlt.console.entity.ClientCheckInCalendarEntity;
import com.tlt.console.entity.ClientDetailEntity;
import com.tlt.console.entity.UnitsEntity;
import com.tlt.console.service.BookingService;
import com.tlt.console.views.bookings.MakeABookingView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddNewCustomerDialog extends Dialog {

    private BookingService mBookingService;

    private Button mSaveButton;

    private Button mCloseButton;

    private ComboBox<UnitsEntity> mSubUnitCombo;

    private MakeABookingView mMakeABookingView;

    public AddNewCustomerDialog(BookingService pBookingService, MakeABookingView pMakeABookingView) {
        mBookingService = pBookingService;
        mMakeABookingView = pMakeABookingView;
        this.open();
        createControls();
    }

    private void createControls() {
        try {
            this.setWidth("50%");
            this.setHeight("65%");

            TextField nameField = new TextField("Name of Customer");
            nameField.setRequired(true);
            nameField.setRequiredIndicatorVisible(true);

            NumberField numPeople = new NumberField("Number of people");

            DatePicker checkInDate = new DatePicker("Check In");

            TextArea address = new TextArea("Address");
            address.setHeight("20%");

            ComboBox<String> idProofType = new ComboBox<>("Id Proof Type");
            idProofType.setAllowCustomValue(true);
            List<String> idProofTypeList = mBookingService.getIdProofTypeList();
            com.vaadin.flow.component.combobox.ComboBox.ItemFilter<String> filter = (stringInBox, filterString) -> stringInBox.toLowerCase().startsWith(filterString.toLowerCase());
            idProofType.setItems(filter, idProofTypeList);
            idProofType.addCustomValueSetListener(e -> {
                String customValue = e.getDetail();
                if (idProofTypeList.contains(customValue)) return;
                idProofTypeList.add(customValue);
                idProofType.setItems(idProofTypeList);
                idProofType.setValue(customValue);
            });
            idProofType.setRequired(true);
            idProofType.setRequiredIndicatorVisible(true);

            TextField idProofNumberField = new TextField("Id Proof Number");
            idProofNumberField.setRequired(true);
            idProofNumberField.setRequiredIndicatorVisible(true);

            mSubUnitCombo = new ComboBox<>("Room / Dorm");
            mSubUnitCombo.setAllowCustomValue(false);
            mSubUnitCombo.setRequired(true);
            mSubUnitCombo.setRequiredIndicatorVisible(true);

            ComboBox.ItemFilter<UnitsEntity> filterUnits = (unitsEntity, filterString) -> (unitsEntity.getName().toLowerCase().startsWith(filterString.toLowerCase()));
            ComboBox<UnitsEntity> mMainUnitCombo = new ComboBox<>("Unit");
            mMainUnitCombo.setAllowCustomValue(false);
            List<UnitsEntity> parentUnitList = mBookingService.getParentUnits();
            mMainUnitCombo.setItems(filterUnits, parentUnitList);
            mMainUnitCombo.setItemLabelGenerator(units -> units.getName());

            mMainUnitCombo.addValueChangeListener(value -> {
                setValuesToSubUnitCombobox(value.getValue());
            });

            TextField additionalRequest = new TextField("Additional Request");

            FormLayout firstFormLayout = new FormLayout();
            firstFormLayout.add(nameField, numPeople, checkInDate);
            firstFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 4));
            firstFormLayout.setColspan(nameField, 2);

            FormLayout formLayout = new FormLayout();
            formLayout.add(firstFormLayout, address, idProofType, idProofNumberField, mMainUnitCombo, mSubUnitCombo, additionalRequest);
            formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 2));
            formLayout.setColspan(firstFormLayout, 2);
            formLayout.setColspan(address, 2);
            formLayout.setColspan(additionalRequest, 2);
            formLayout.setWidthFull();

            mSaveButton = new Button("Add Customer");
            mSaveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            mSaveButton.addClickListener(clickEvent -> {
                saveCustomerDetail(nameField.getValue(), numPeople.getValue(), checkInDate.getValue(), address.getValue(), idProofType.getValue(), idProofNumberField.getValue(), mSubUnitCombo.getValue(), additionalRequest.getValue());
            });

            mCloseButton = new Button(new Icon(VaadinIcon.CLOSE_SMALL));
            mCloseButton.addThemeVariants(ButtonVariant.LUMO_ICON);
            mCloseButton.getElement().setAttribute("aria-label", "Close");
            mCloseButton.addClickListener(clickEvent -> {
                this.removeAll();
                this.close();
            });

            FormLayout buttonLayout = new FormLayout();
            buttonLayout.add(mSaveButton, mCloseButton);
            buttonLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 2));

            this.setCloseOnOutsideClick(true);
            this.setResizable(true);
            this.setDraggable(true);
            add(formLayout);
            add(buttonLayout);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveCustomerDetail(String name, Double numPeople, LocalDate checkIn, String address, String idProofType, String idProofNumber, UnitsEntity units, String additionalRequest) {
        try {
            ClientDetailEntity entity = new ClientDetailEntity();
            entity.setName(name);
            entity.setNumOfPeople(numPeople.longValue());
            entity.setAddress(address);
            entity.setIdProofType(idProofType);
            entity.setIdProofUniqueNumber(idProofNumber);
            entity.setUnitId(units);
            entity.setAdditionalRequest(additionalRequest);
            entity.setBookedDate(new Date());
            entity.setUpdateDate(new Date());
            entity.setUpdateUser("1");

            entity = mBookingService.saveClientDetail(entity);

            ClientCheckInCalendarEntity checkinEntity = new ClientCheckInCalendarEntity();
            checkinEntity.setClientId(entity);
            checkinEntity.setUnitId(entity.getUnitId());
            checkinEntity.setBookedDate(Date.from(checkIn.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            checkinEntity.setUpdateDate(new Date());
            checkinEntity.setUpdateUser("1");
            mBookingService.saveClientCheckInCalendar(checkinEntity);
            this.close();
            mMakeABookingView.getCalendarEvents();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setValuesToSubUnitCombobox(UnitsEntity pUnitEntity) {
        try {
            if (pUnitEntity != null) {
                ComboBox.ItemFilter<UnitsEntity> filter = (unitsEntity, filterString) -> (unitsEntity.getName().toLowerCase().startsWith(filterString.toLowerCase()));
                List<UnitsEntity> subUnitList = mBookingService.getSubUnits(pUnitEntity.getUnitId());
                mSubUnitCombo.setItems(filter, subUnitList);
                mSubUnitCombo.setItemLabelGenerator(units -> units.getName());
            } else {
                ComboBox.ItemFilter<UnitsEntity> filter = (unitsEntity, filterString) -> (unitsEntity.getName().toLowerCase().startsWith(filterString.toLowerCase()));
                mSubUnitCombo.setItems(filter, new ArrayList<UnitsEntity>());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
