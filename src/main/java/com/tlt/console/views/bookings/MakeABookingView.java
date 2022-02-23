package com.tlt.console.views.bookings;

import com.tlt.console.entity.ClientCheckInCalendarEntity;
import com.tlt.console.entity.UnitsEntity;
import com.tlt.console.service.BookingService;
import com.tlt.console.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.FullCalendarBuilder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@PageTitle("Bookings")
@Route(value = "Bookings", layout = MainLayout.class)
//@RouteAlias(value = "", layout = MainLayout.class)
public class MakeABookingView extends VerticalLayout {

    private ComboBox<UnitsEntity> mMainUnitCombo;

    private ComboBox<UnitsEntity> mSubUnitCombo;

    private BookingService mBookingService;

    private FullCalendar calendar;

    private List<UnitsEntity> parentUnitList;

    private HashMap<Long, String> parentIdNameMap;

    private List<String> pColourList;

    public MakeABookingView(@Autowired BookingService pBookingService) {
        mBookingService = pBookingService;
        createControls();
    }

    private void createControls() {

        try {

            this.setSizeFull();

            pColourList = new ArrayList<>();
            pColourList.add("#ff3333");
            pColourList.add("#25383C");
            pColourList.add("#ADD8E6");
            pColourList.add("#800080");
            pColourList.add("#FFA500");
            pColourList.add("#646D7E");
            pColourList.add("#A52A2A");
            pColourList.add("#808080");
            pColourList.add("#7FFD4");

            ComboBox.ItemFilter<UnitsEntity> filter = (unitsEntity, filterString) -> (unitsEntity.getName().toLowerCase().startsWith(filterString.toLowerCase()));

            mSubUnitCombo = new ComboBox<>();
            mSubUnitCombo.setAllowCustomValue(false);

            mMainUnitCombo = new ComboBox<>();
            mMainUnitCombo.setAllowCustomValue(false);
            parentUnitList = mBookingService.getParentUnits();
            mMainUnitCombo.setItems(filter, parentUnitList);
            mMainUnitCombo.setItemLabelGenerator(units -> units.getName());

            parentIdNameMap = new HashMap<>();
            parentUnitList.stream().forEach(unit -> parentIdNameMap.put(unit.getUnitId(), unit.getName()));

            mMainUnitCombo.addValueChangeListener(value -> {
                setValuesToSubUnitCombobox(value.getValue());
            });

            Button mSearchButton = new Button(new Icon(VaadinIcon.CHECK));
            mSearchButton.addThemeVariants(ButtonVariant.LUMO_ICON);
            mSearchButton.getElement().setAttribute("aria-label", "Check");
            mSearchButton.addClickListener(clickEvent -> {
                getCalendarEvents();
            });

            Button mClearButton = new Button(new Icon(VaadinIcon.CLOSE_SMALL));
            mClearButton.addThemeVariants(ButtonVariant.LUMO_ICON);
            mClearButton.getElement().setAttribute("aria-label", "Clear");
            mClearButton.addClickListener(clickEvent -> {
                mSubUnitCombo.clear();
                mMainUnitCombo.clear();
            });

            HorizontalLayout buttonLayout = new HorizontalLayout();
            buttonLayout.add(mSearchButton, mClearButton);

            FormLayout formLayout = new FormLayout();
            formLayout.add(mMainUnitCombo, mSubUnitCombo, buttonLayout);
            formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 3));
            formLayout.setWidthFull();

            // Create a new calendar instance and attach it to our layout
            calendar = FullCalendarBuilder.create()
                    .withAutoBrowserTimezone()
//                    .withEntryLimit(3)
                    .build();
            calendar.setSizeFull();
            calendar.setLocale(Locale.ENGLISH);
            //        setFlexGrow(1, calendar);
//        calendar.setHeight("40%");
//        calendar.setWidth("40%");

            calendar.addTimeslotClickedListener(event -> {
                System.out.println("\n\n\n time slot \n\n\n");
            });

            Button mAddCustomer = new Button("Add new Customer");
            mAddCustomer.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            mAddCustomer.setEnabled(true);

            mAddCustomer.addClickListener(clickEvent -> {
                AddNewCustomerDialog addNewCustomerDialog = new AddNewCustomerDialog(mBookingService, this);
            });

            HorizontalLayout footerLayout = new HorizontalLayout(mAddCustomer);
            footerLayout.setAlignItems(Alignment.START);
            footerLayout.setWidthFull();
            Footer footer = new Footer(footerLayout);

            add(formLayout, calendar, mAddCustomer);

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

    public void getCalendarEvents() {

        try {

            HashMap<String, String> clientColourMap = new HashMap<>();
            Random rand = new Random(); //instance of random class
            int upperbound = pColourList.size();

            calendar.removeAllEntries();
            Long unitId = null;

            if (mSubUnitCombo.getValue() != null) {
                unitId = mSubUnitCombo.getValue().getUnitId();
            }

            List<ClientCheckInCalendarEntity> entityList = mBookingService.getCalendarEventsByUnitId(unitId);

            for (ClientCheckInCalendarEntity entity : entityList) {

                int intRandom = rand.nextInt(upperbound);

                // Create a initial sample entry
                Entry entry = new Entry();
                String title = entity.getClientId().getName() + " : " + parentIdNameMap.get(entity.getUnitId().getParentUnitId()) + " - " + entity.getUnitId().getName();
                entry.setTitle(title);

                if (clientColourMap.get(title) == null) {
                    String colour = pColourList.get(intRandom);
                    entry.setColor(colour);
                    clientColourMap.put(title, colour);
                } else {
                    entry.setColor(clientColourMap.get(title));
                }

                Calendar c = Calendar.getInstance();
                c.setTime(entity.getBookedDate());
                TimeZone tz = c.getTimeZone();
                ZoneId zid = tz == null ? ZoneId.systemDefault() : tz.toZoneId();

                entry.setAllDay(true);
                entry.setStart(LocalDateTime.ofInstant(c.toInstant(), zid));
                entry.setEnd(LocalDateTime.ofInstant(c.toInstant(), zid));

                calendar.addEntry(entry);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}