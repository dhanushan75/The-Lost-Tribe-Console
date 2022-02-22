package com.tlt.console.views.bookings;

import com.tlt.console.entity.ClientCheckInCalendarEntity;
import com.tlt.console.entity.UnitsEntity;
import com.tlt.console.service.BookingService;
import com.tlt.console.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
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

    public MakeABookingView(@Autowired BookingService pBookingService) {
        mBookingService = pBookingService;
        createControls();
    }

    private void createControls() {

        try {

            this.setSizeFull();

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
//            formLayout.addFormItem(mMainUnitCombo, "Unit");
//            formLayout.addFormItem(mSubUnitCombo, "Room/Dorm");
//            formLayout.add(buttonLayout);
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

            add(formLayout);
            add(calendar);

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

    private void getCalendarEvents() {

        try {

            calendar.removeAllEntries();
            Long unitId = null;

            if (mSubUnitCombo.getValue() != null) {
                unitId = mSubUnitCombo.getValue().getUnitId();
            }

            List<ClientCheckInCalendarEntity> entityList = mBookingService.getCalendarEventsByUnitId(unitId);

            for (ClientCheckInCalendarEntity entity : entityList) {

                // Create a initial sample entry
                Entry entry = new Entry();
                entry.setTitle(entity.getClientId().getName() + " : " + parentIdNameMap.get(entity.getUnitId().getParentUnitId()) + " - " + entity.getUnitId().getName());
//                entry.setColor("#ff3333");
                entry.setColor("dodgerblue");
                //entry.setRenderingMode(Entry.RenderingMode.BACKGROUND);

                //the given times will be interpreted as utc based - useful when the times are fetched from your database
//        entry.setStart(LocalDate.now().withDayOfMonth(3).atTime(10, 0));
//        entry.setEnd(entry.getStart().plusHours(2));

//                Calendar c = Calendar.getInstance();
//                c.setTime(new Date());
//                c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
//                TimeZone tz = c.getTimeZone();
//                ZoneId zid = tz == null ? ZoneId.systemDefault() : tz.toZoneId();
//
//                entry.setStart(LocalDateTime.now().withDayOfMonth(1));
//                entry.setEnd(LocalDateTime.ofInstant(c.toInstant(), zid));

                Calendar c = Calendar.getInstance();
                c.setTime(entity.getBookedDate());
                //c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
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